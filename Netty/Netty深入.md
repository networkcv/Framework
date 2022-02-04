Netty 是一个 **异步** 的、**基于事件驱动** 、底层通过包装 **NIO** 来实现的  **网络** 应用框架。

可扩展的事件驱动模型

通用的通信API

零拷贝以及字节缓冲区



1、首先，要有编、解码器，这应该是一个网络框架最基本的组件了，基本原理涉及到TCP协议的本质（基于流的可靠协议），它在应用层会有粘包现象发生，需要开发者设计识别的约定，比如定长，分片等，这些Netty已经做好了，只不过Netty的可就复杂多了，它提供的编、解码器不仅种类多，而且细节更完善

2、其次，就是NIO线程池，Netty中叫NioEventLoopGroup，基于异步无锁化模式设计，而且封装了Netty自己的线程NioEventLoop，NioEventLoop表面上只是一个普通的类，但是其内部绑定了一个Netty的线程，叫FastThreadLocalThread，之所以这样设计，是因为Netty重写了JDK的ThreadLocal类，叫FastThreadLocal，重写的原因简单说就是因为Netty作者觉得JDK的ThreadLocal性能不行，自己就搞了更好的。而且Netty线程池还很好的同时处理了定时任务，异步任务等的分片执行和聚合逻辑，并为它实现了负载均衡。可以认为NioEventLoopGroup+NioEventLoop是Netty的心脏，因为最核心的新连接接入（select也叫I/O多路复用器）和I/O事件处理（processSelectedKey）逻辑，就是依靠的它俩完成的

3、基于2，Netty的服务端用法是实现两个线程池，一个是接入线程池，一个是I/O处理线程池，这个模式也叫Reactor模型

4、再次，Netty重新设计和封装了JDK底层的Socket/ServerSocket和ServerSocketChannel/SocketChannel等I/O相关的类，并设计了自己的Channel类，这样的设计灵活，方便业务代码使用。

5、再次，Netty设计了handler处理器，其基于责任链设计模式组装了一套业务逻辑链，底层是一个双向链表，这样设计的目的是为了方便业务代码的灵活扩展，可以让用户自己扩展实现不同的业务逻辑单元——Netty里叫ChannelHandler，将其加入handler链里（Netty里叫pipeline）被处理即可，并且该逻辑链底层是双向链表，可以让请求来回流动，可以看做是Netty的动脉。

6、再次，Netty更是重新设计了NIO的buffer，虽然NIO的buffer已经具备了I/O的常用API，但是还不够灵活和优秀，Netty对其进行了全方面的改造，消除了晦涩的设计，封装了自己的输入、输出流。

7、最后，Netty在自己封装的buffer的基础上，实现了自己的内存管理体系，不再强依赖Java的GC。而是大胆的使用了堆外内存管理技术，并设计了很多工具类，使其尽最大可能复用内存（内存池化技术），提高GC性能。

8、还有一点就是Netty修复了JDK NIO的所有已知bug！这是非常重要的，避免了开发人员直接使用NIO API而走弯路。



类比到通信，整个 I/O 过程只在调用 select、poll 这些函数的时候才会阻塞，收发客户消息是不会阻塞的，BIO中会阻塞在accept获取连接时和read读取消息时。

I/O复用模型：Linux提供select/poll，进程通过将一个或多个fd传递给select或poll系统调用，阻塞在select操作上，而不是阻塞在真正的I/O系统调用上。这样select/poll可以帮我们侦测多个fd是否处于就绪状态，等待数据报套接字变为可读。当select 返回套接字可读这一条件时，我们调用recvfrom 把所读数据报复制到应用进程缓冲区。





