## 1. Netty 概述

Netty 是由JBOSS提供的一个Java开源框架。

Netty 是一个 **异步** 的、**基于事件驱动** 、底层通过包装 **NIO** 来实现的  **网络** 应用框架，

![](D:\Study\Framework\Netty\img\Netty架构.jpg)

在分布式系统中，各个节点之间需要远程服务调用，高性能的RPC框架必不可少，Netty作为异步高性能的通信框架，往往作为基础通信组件被这些RPC框架使用，如阿里的分布式服务框架Dubbo的RPC框架使用的Dubbo协议进行节点间通信，Dubbo协议默认使用Netty作为基础通信组件，用于实现各进程节点之间的内部通信。

**原生NIO 存在的问题**

1. NIO 的类库和 API 繁杂，使用麻烦：需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 等。
2. 需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程和网络编程非常熟悉，才能编写出高质量的 NIO 程序。
3. 开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等等。
4. JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 版本该问题仍旧存在，没有被根本解决。

**Netty 的优点**

1. Netty 对 JDK 自带的 NIO 的 API 进行了封装，解决了上述问题。设计优雅：适用于各种传输类型的统一 API 阻塞和非阻塞 Socket；基于灵活且可扩展的事件模型，可以清晰地分离关注点；高度可定制的线程模型 - 单线程，一个或多个线程池。
2. 使用方便：详细记录的 Javadoc，用户指南和示例；没有其他依赖项，JDK 5（Netty 3.x）或 6（Netty 4.x）就足够了。
3. 高性能、吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制。
4. 安全：完整的 SSL/TLS 和 StartTLS 支持。
5. 社区活跃、不断更新：社区活跃，版本迭代周期短，发现的 Bug 可以被及时修复，同时，更多的新功能会被加入

**线程模型基本介绍**

1. 不同的线程模式，对程序的性能有很大影响，为了搞清Netty 线程模式，我们来系统的讲解下 各个线程模式， 最后看看Netty 线程模型有什么优越性。
2. 目前存在的线程模型有：
   - 传统阻塞I/O 模型
   - Reactor 模式
3. 根据 Reactor 的数量和处理资源池线程的数量不同，有 3 种典型的实现
   - 单 Reactor 单线程；
   - 单 Reactor 多线程；
   - 主从 Reactor 多线程 
4. Netty 线程模式（Netty 主要基于主从Reactor多线程模型做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor)

**传统阻塞I/O 模型**

![](D:\Study\Framework\Netty\img\传统IO模型图.jpg)

黄色的框表示对象， 蓝色的框表示线程，白色的框表示方法(API)

模型特点

1. 采用阻塞IO模式获取输入的数据
2. 每个连接都需要独立的线程完成数据的输入，业务处理, 数据返回

问题分析

1. 当并发数很大，就会创建大量的线程，占用很大系统资源
2. 连接创建后，如果当前线程暂时没有数据可读，该线程会阻塞在read 操作，造成线程资源浪费

**Reactor 模式**

![](D:\Study\Framework\Netty\img\Reactor设计理念.jpg)

1. 基于 I/O 复用模型：多个连接共用一个阻塞对象，应用程序只需要在一个阻塞对象等待，无需阻塞等待所有连接。当某个连接有新的数据可以处理时，操作系统通知应用程序，线程从阻塞状态返回，开始进行业务处理
    Reactor 对应的叫法:  反应器模式 / 分发者模式(Dispatcher) / 通知者模式(notifier)
2. 基于线程池复用线程资源：不必再为每个连接创建线程，将连接完成后的业务处理任务分配给线程进行处理，一个线程可以处理多个连接的业务。
3. Reactor 模式使用IO复用监听事件, 收到事件后，分发给某个线程(进程), 这点就是网络服务器高并发处理关键。



**Reactor模式 中核心组成：**

1. Reactor：Reactor 在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对 IO 事件做出反应。 它就像公司的电话接线员，它接听来自客户的电话并将线路转移到适当的联系人；

2. Handlers：处理程序执行 I/O 事件要完成的实际事件，类似于客户想要与之交谈的公司中的实际人员。Reactor 通过调度适当的处理程序来响应 I/O 事件，处理程序执行非阻塞操作。


### Reactor模式

#### **单Reactor 单线程**

![](D:\Study\Framework\Netty\img\单 Reactor 单线程.jpg)

**方案说明：**

1. Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求
2. Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发
3. 如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理
4. 如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应
5. Handler 会完成 Read→业务处理→Send 的完整业务流程

结合实例：服务器端用一个线程通过多路复用搞定所有的 IO 操作（包括连接，读、写等），编码简单，清晰明了，但是如果客户端连接数量较多，将无法支撑，前面的 NIO 案例就属于这种模型。

**方案优缺点分析：**

- 优点：模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成

- 缺点：性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈

- 缺点：可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障

- 使用场景：客户端的数量有限，业务处理非常快速，比如 Redis在业务处理的时间复杂度 O(1) 的情况

  

#### **单Reactor 多线程**

![](D:\Study\Framework\Netty\img\单Reactor 多线程.jpg)

**方案说明：**

1. Reactor 对象通过select 监控客户端请求 事件, 收到事件后，通过dispatch进行分发
2. 如果建立连接请求, 则右Acceptor 通过 accept 处理连接请求, 然后创建一个Handler对象处理完成连接后的各种事件
3. 如果不是连接请求，则由reactor分发调用连接对应的handler 来处理
4. handler 只负责响应事件，不做具体的业务处理, 通过read 读取数据后，会分发给后面的worker线程池的某个线程处理业务
5. worker 线程池会分配独立线程完成真正的业务，并将结果返回给handler
6. handler收到响应后，通过send 将结果返回给client

**方案优缺点分析：**

- 优点：可以充分的利用多核cpu 的处理能力
- 缺点：多线程数据共享和访问比较复杂， reactor 处理所有的事件的监听和响应，在单线程运行， 在高并发场景容易出现性能瓶颈。



#### **主从Reactor 多线程**

![](D:\study\Framework\Netty\img\主从Reactor多线程.jpg)

**方案说明：**

1. Reactor主线程 MainReactor 对象通过select 监听连接事件, 收到事件后，通过Acceptor 处理连接事件
2. 当 Acceptor 处理连接事件后，MainReactor 将连接分配给SubReactor 
3. subreactor 将连接加入到连接队列进行监听,并创建handler进行各种事件处理
4. 当有新事件发生时， subreactor 就会调用对应的handler处理
5. handler 通过read 读取数据，分发给后面的worker 线程处理
6. worker 线程池分配独立的worker 线程进行业务处理，并返回结果
7. handler 收到响应的结果后，再通过send 将结果返回给client
8. Reactor 主线程可以对应多个Reactor 子线程, 即MainRecator 可以关联多个SubReactor

**方案优缺点说明：**

- **优点：**父线程与子线程的数据交互简单职责明确，父线程只需要接收新连接，子线程完成后续的业务处理。
- **优点：**父线程与子线程的数据交互简单，Reactor 主线程只需要把新连接传给子线程，子线程无需返回数据。
- **缺点：**编程复杂度较高

**结合实例：**这种模型在许多项目中广泛使用，包括 Nginx 主从 Reactor 多进程模型，Memcached 主从多线程，Netty 主从多线程模型的支持



#### **Reactor模式小结：**

- 单Reactor单线程，前台接待员和服务员是同一个人，全程为顾客服务
- 单Reactor多线程，一个前台接待员，多个服务生，接待员只负责接待
- 主从Reactor多线程，一个门迎，多个接待员，多个服务生

**Reatcor模式具有如下优点：**

1. 响应快，不必为单个同步时间所阻塞，虽然 Reactor 本身依然是同步的

2. 可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销

3. 扩展性好，可以方便的通过增加 Reactor 实例个数来充分利用 CPU 资源

4. 复用性好，Reactor 模型本身与具体事件处理逻辑无关，具有很高的复用性

   

### Netty模型

#### 简单版

![](D:\study\Framework\Netty\img\Netty简单版.jpg)

**方案说明：**

1. BossGroup 线程维护Selector , 只关注Accecpt
2. 当接收到Accept事件，获取到对应的SocketChannel, 封装成 NIOScoketChannel并注册到Worker 线程(事件循环), 并进行维护
3. 当Worker线程监听到selector 中通道发生自己感兴趣的事件后，就进行处理(就由handler)， 注意handler 已经加入到通道



#### 进阶版

![](D:\study\Framework\Netty\img\Netty进阶版.jpg)



主要基于**主从** **Reactors** **多线程模型**（如图）做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor

#### 详尽版

![](D:\study\Framework\Netty\img\Netty详尽版.jpg)

**方案说明：**

1. Netty抽象出两组线程池 **BossGroup** 专门负责接收客户端的连接，**WorkGroup** 专门负责网络的读写。
2. **BossGroup** 和 **WorkGroup** 类型都是 **NioEventLoopGroup**。
3. NioEventLoopGroup 相当于一个事件循环组，这个组中含有多个事件循环，每个事件循环是NioEventLoop（也就是上图中的NioEventGroup）
4. NioEventLoop 表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个selector，用于监听绑定在其上的socket的网络通讯。
5. NioEventLoopGroup 可以有多个线程，即可以含有多个NioEventLoop（对应的就是图中的NioEventGroup）
6. 每个Boss NioEventLoop 执行的步骤有3步
   - 轮询 accept 事件
   - 处理 accept 事件，与client 建立连接，生成NioSocketChannel，并将其注册到某个worker NioEventLopp 上的 selector
   - 处理任务队列的任务，即 runAllTasks
7. 每个 Worker NioEventLoop 循环执行的步骤
   - 轮询read，write 事件
   - 处理IO事件，即read、write事件，在对应NioSocketChannel处理
   - 处理任务队列的任务，即 runAllTasks
8. 每个Worker NioEventLoop 处理业务时，会使用 Pipeline（管道），pipeline 与 channel 是相互引用的关系，即通过pipeline 可以获取到对应通道，也可以通过channel获取到pipeline ，同时 管道中维护里很多的处理器。

### Netty入门实例 - TCP服务

1. 实例要求：使用IDEA 创建Netty项目
2. Netty 服务器在 6668 端口监听，客户端能发送消息给服务器 "hello, 服务器~"
3. 服务器可以回复消息给客户端 "hello, 客户端~"
4. 目的：对Netty 线程模型 有一个初步认识, 便于理解Netty 模型理论

```java



```



**任务队列中的** **Task** **有** **3** **种典型使用场景**

1. 用户程序自定义的普通任务  ，存放在TaskQueue
2. 用户自定义定时任务 ，存放在ScheduleTaskQueue
3. 非当前 Reactor 线程调用 Channel 的各种方法

例如在**推送系统**的业务线程里面，根据**用户的标识**，找到对应的 **Channel** **引用**，然后调用 Write 类方法向该用户推送消息，就会进入到这种场景。最终的 Write 会提交到任务队列中后被**异步消费**



**方案再说明**：

1. Netty 抽象出两组**线程池**，BossGroup 专门负责接收客户端连接，WorkerGroup 专门负责网络读写操作。
2. NioEventLoop 表示一个不断循环执行处理任务的线程，每个 NioEventLoop 都有一个 selector，用于监听绑定在其上的 socket 网络通道。
3. NioEventLoop 内部采用串行化设计，从消息的读取->解码->处理->编码->发送，始终由 IO 线程 NioEventLoop 负责
4. NioEventLoopGroup 下包含多个 NioEventLoop
5. 每个 NioEventLoop 中包含有一个 Selector，一个 taskQueue
6. 每个 NioEventLoop 的 Selector 上可以注册监听多个 NioChannel
7. 每个 NioChannel 只会绑定在唯一的 NioEventLoop 上
8. 每个 NioChannel 都绑定有一个自己的 ChannelPipeline



**异步模型**

**基本介绍**

1. 异步的概念和同步相对。当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的组件在完成后，通过状态、通知和回调来通知调用者。
2. Netty 中的 I/O 操作是异步的，包括 Bind、Write、Connect 等操作会简单的返回一个 ChannelFuture。
3. 调用者并不能立刻获得结果，而是通过 Future-Listener 机制，用户可以方便的主动获取或者通过通知机制获得 IO 操作结果
4. Netty 的异步模型是建立在 future 和 callback 的之上的。callback 就是回调。重点说 Future，它的核心思想是：假设一个方法 fun，计算过程可能非常耗时，等待 fun返回显然不合适。那么可以在调用 fun 的时候，立马返回一个 Future，后续可以通过 Future去监控方法 fun 的处理过程(即 ： Future-Listener 机制)

**Future说明**

1. 表示异步的执行结果, 可以通过它提供的方法来检测执行是否完成，比如检索计算等等.
2. ChannelFuture 是一个接口 ： **public interface** ChannelFuture **extends** Future<Void>
   我们可以添加监听器，当监听的事件发生时，就会通知到监听器. 案例说明

**Future-Listener 机制**

- 当 Future 对象刚刚创建时，处于非完成状态，调用者可以通过返回的 ChannelFuture 来获取操作执行的状态，注册监听函数来执行完成后的操作。
- 常见有如下操作：
  - 通过 isDone 方法来判断当前操作是否完成；
  - 通过 isSuccess 方法来判断已完成的当前操作是否成功；
  - 通过 getCause 方法来获取已完成的当前操作失败的原因；
  - 通过 isCancelled 方法来判断已完成的当前操作是否被取消；
  - 通过 addListener 方法来注册监听器，当操作已完成(isDone 方法返回完成)，将会通知指定的监听器；如果 Future 对象已完成，则通知指定的监听器

Netty入门实例 - HTTP服务

1. 实例要求：使用IDEA 创建Netty项目
2. Netty 服务器在 6668 端口监听，浏览器发出请求 "http://localhost:6668/ "
3. 服务器可以回复消息给客户端 "Hello! 我是服务器 5 " , 并对特定请求资源进行过滤。
4. 目的：Netty 可以做Http服务开发，并且理解Handler实例和客户端及其请求的关系。