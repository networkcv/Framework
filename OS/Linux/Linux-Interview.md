## [automake,autoconf使用详解](https://www.laruence.com/2009/11/18/1154.html)

## [Linux_top命令详解](https://www.cnblogs.com/ggjucheng/archive/2012/01/08/2316399.html)

top不是显示cpu占用情况工具，而是linux系统资源的占用情况。

CPU指标信息：

- %us：用户空间占用CPU百分比
- %sy：内核空间占用CPU百分比
- %ni：用户进程空间内改变过优先级的进程占用CPU百分比
- %wa：表示系统 **有未完成** 的 **磁盘I/O **时，CPU处于空闲状态的百分比。（不包括网络I/O）
- %id：表示系统 **没有未完成** 的 **磁盘I/O **时，CPU处于空闲状态的百分比。
- %hi：硬件CPU中断占用百分比
- %si：软中断占用百分比

## JVM 问题排查工具

jps 查看java 进程

jstack 查看方法调用栈

jcmd 替代 jmap 可以查看堆（GC.heap_info） 导出堆（GC.heap_dump），查看java调用栈（Thread.print），执行GC（ GC.run ），

```sh
$ jcmd 96004 help
96004:
The following commands are available:
VM.unlock_commercial_features
JFR.configure
JFR.stop
JFR.start
JFR.dump
JFR.check
VM.native_memory
ManagementAgent.stop
ManagementAgent.start_local
ManagementAgent.start
VM.classloader_stats
GC.rotate_log
Thread.print
GC.class_stats
GC.class_histogram
GC.heap_dump
GC.finalizer_info
GC.heap_info
GC.run_finalization
GC.run
VM.uptime
VM.dynlibs
VM.flags
VM.system_properties
VM.command_line
VM.version
help
```





VIRT：virtual memory usage 虚拟内存

1、进程“需要的”虚拟内存大小，包括进程使用的库、代码、数据等

2、假如进程申请100m的内存，但实际只使用了10m，那么它会增长100m，而不是实际的使用量

RES：resident memory usage 常驻内存

1、进程当前使用的内存大小，但不包括swap out

2、包含其他进程的共享

3、如果申请100m的内存，实际使用10m，它只增长10m，与VIRT相反

4、关于库占用内存的情况，它只统计加载的库文件所占内存大小

SHR：shared memory 共享内存

1、除了自身进程的共享内存，也包括其他进程的共享内存

2、虽然进程只使用了几个共享库的函数，但它包含了整个共享库的大小

3、计算某个进程所占的物理内存大小公式：RES – SHR

4、swap out后，它将会降下来

DATA

1、数据占用的内存。如果top没有显示，按f键可以显示出来。

2、真正的该程序要求的数据空间，是真正在运行中要使用的。



## 大量IO也可能会造成CPU负载高

非常频繁的IO 导致了非常频繁的中断信号，cpu处理信号需要保护和恢复现场，想等于频繁上下文切换，可能会造成CPU负载高，但是使用率低。

计算机硬件上使用DMA来访问磁盘等IO，也就是请求发出后，CPU就不再管了，直到DMA处理器完成任务，再通过中断告诉CPU完成了。所以，单独的一个IO时间，对CPU的占用是很少的，阻塞了就更不会占用CPU了，因为程序都不继续运行了，CPU时间交给其它线程和进程了。虽然IO不会占用大量的CPU时间，但是非常频繁的IO还是会非常浪费CPU时间的，所以面对大量IO的任务，有时候是需要算法来合并IO，或者通过cache来缓解IO压力的。







## 理解Linux load average的误区

uptime和top等命令都可以看到load average指标，从左至右三个数字分别表示1分钟、5分钟、15分钟的load average：

| 12   | $ uptime 10:16:25 up 3 days, 19:23, 2 users, load average: 0.00, 0.01, 0.05 |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Load average的概念源自UNIX系统，虽然各家的公式不尽相同，但都是用于衡量正在使用CPU的进程数量和正在等待CPU的进程数量，一句话就是runnable processes的数量。所以load average可以作为CPU瓶颈的参考指标，如果大于CPU的数量，说明CPU可能不够用了。

但是，Linux上不是这样的！

Linux上的load average除了包括正在使用CPU的进程数量和正在等待CPU的进程数量之外，还包括uninterruptible sleep的进程数量。通常等待IO设备、等待网络的时候，进程会处于uninterruptible sleep状态。Linux设计者的逻辑是，uninterruptible sleep应该都是很短暂的，很快就会恢复运行，所以被等同于runnable。然而uninterruptible sleep即使再短暂也是sleep，何况现实世界中uninterruptible sleep未必很短暂，大量的、或长时间的uninterruptible sleep通常意味着IO设备遇到了瓶颈。众所周知，sleep状态的进程是不需要CPU的，即使所有的CPU都空闲，正在sleep的进程也是运行不了的，所以sleep进程的数量绝对不适合用作衡量CPU负载的指标，Linux把uninterruptible sleep进程算进load average的做法直接颠覆了load average的本来意义。所以在Linux系统上，load average这个指标基本失去了作用，因为你不知道它代表什么意思，当看到load average很高的时候，你不知道是runnable进程太多还是uninterruptible sleep进程太多，也就无法判断是CPU不够用还是IO设备有瓶颈。



### Java中线程状态和IO的关系

当我们用jstack查看Java线程状态时，会看到各种线程状态。当发生IO等待时（比如远程调用时），线程是什么状态呢，Blocked还是Waiting？

答案是Runnable状态，是不是有些出乎意料！实际上，在操作系统层面Java的Runnable状态除了包括Running状态，还包括Ready（就绪状态，等待CPU调度）和IO Wait等状态。

如上图，Runnable状态的注解明确说明了，在JVM层面执行的线程，在操作系统层面可能在等待其他资源。如果等待的资源是CPU，在操作系统层面线程就是等待被CPU调度的Ready状态；如果等待的资源是磁盘网卡等IO资源，在操作系统层面线程就是等待IO操作完成的IO Wait状态。

有人可能会问，为什么Java线程没有专门的Running状态呢？

目前绝大部分主流操作系统都是以时间分片的方式对任务进行轮询调度，时间片通常很短，大概几十毫秒，也就是说一个线程每次在cpu上只能执行几十毫秒，然后就会被CPU调度出来变成Ready状态，等待再一次被CPU执行，线程在Ready和Running两个状态间快速切换。通常情况，JVM线程状态主要为了监控使用，是给人看的。当你看到线程状态是Running的一瞬间，线程状态早已经切换N次了。所以，再给线程专门加一个Running状态也就没什么意义了。



**内核空间与用户空间**：在Linux中，应用程序稳定性远远比不上操作系统程序，为了保证操作系统的稳定性，Linux区分了内核空间和用户空间。可以这样理解，内核空间运行操作系统程序和驱动程序，用户空间运行应用程序。Linux以这种方式隔离了操作系统程序和应用程序，避免了应用程序影响到操作系统自身的稳定性。这也是Linux系统超级稳定的主要原因。所有的系统资源操作都在内核空间进行，比如读写磁盘文件，内存分配和回收，网络接口调用等。所以在一次网络IO读取过程中，数据并不是直接从网卡读取到用户空间中的应用程序缓冲区，而是先从网卡拷贝到内核空间缓冲区，然后再从内核拷贝到用户空间中的应用程序缓冲区。对于网络IO写入过程，过程则相反，先将数据从用户空间中的应用程序缓冲区拷贝到内核缓冲区，再从内核缓冲区把数据通过网卡发送出去。



### 同步阻塞IO

我们先看一下传统阻塞IO。在Linux中，默认情况下所有socket都是阻塞模式的。当用户线程调用系统函数read()，内核开始准备数据（从网络接收数据），内核准备数据完成后，数据从内核拷贝到用户空间的应用程序缓冲区，数据拷贝完成后，请求才返回。从发起read请求到最终完成内核到应用程序的拷贝，整个过程都是阻塞的。为了提高性能，可以为每个连接都分配一个线程。因此，在大量连接的场景下就需要大量的线程，会造成巨大的性能损耗，这也是传统阻塞IO的最大缺陷。

### 同步非阻塞IO

用户线程在发起Read请求后立即返回，不用等待内核准备数据的过程。如果Read请求没读取到数据，用户线程会不断轮询发起Read请求，直到数据到达（内核准备好数据）后才停止轮询。非阻塞IO模型虽然避免了由于线程阻塞问题带来的大量线程消耗，但是频繁的重复轮询大大增加了请求次数，对CPU消耗也比较明显。这种模型在实际应用中很少使用。

### 多路复用IO模型

多路复用IO模型，建立在多路事件分离函数select，poll，epoll之上。在发起read请求前，先更新select的socket监控列表，然后等待select函数返回（此过程是阻塞的，所以说多路复用IO也是阻塞IO模型）。当某个socket有数据到达时，select函数返回。此时用户线程才正式发起read请求，读取并处理数据。这种模式用一个专门的监视线程去检查多个socket，如果某个socket有数据到达就交给工作线程处理。由于等待Socket数据到达过程非常耗时，所以这种方式解决了阻塞IO模型一个Socket连接就需要一个线程的问题，也不存在非阻塞IO模型忙轮询带来的CPU性能损耗的问题。多路复用IO模型的实际应用场景很多，比如大家耳熟能详的Java NIO，Redis以及Dubbo采用的通信框架Netty都采用了这种模型



## 7. read、write会发生阻塞

连接建立成功之后，就能开始发送和接收消息了。我们来看一下：

read 为读数据，从服务端来看就是等待客户端的请求。如果客户端不发请求，那么调用 read 会处于阻塞等待状态，没有数据可以读，这个应该很好理解。

write 为写数据。一般而言服务端接受客户端的请求之后，会进行一些逻辑处理，然后再把结果返回给客户端，这个写入也可能会被阻塞。

这里可能有人就会问 read 读不到数据阻塞等待可以理解，write 为什么还要阻塞，有数据不就直接发了吗？

因为我们用的是 TCP 协议，TCP 协议需要保证数据可靠地、有序地传输，并且给予端与端之间的流量控制。

所以说发送不是直接发出去，它有个发送缓冲区，我们需要把数据先拷贝到 TCP 的发送缓冲区，由 TCP 自行控制发送的时间和逻辑，有可能还有重传什么的。

如果我们发的过快，导致接收方处理不过来，那么接收方就会通过 TCP 协议告知：别发了！忙不过来了。发送缓存区是有大小限制的，由于无法发送，还不断调用 write 那么缓存区就满了，满了就不然你 write 了，所以 write 也会发生阻塞。

综上，read 和 write 都会发生阻塞。



## -XX:MaxDirectMemorySize堆外内存大小限制失效

上面的结论有点不完善:

如果通过反射的方式拿到Unsafe的实例,然后用Unsafe的allocateMemory方法分配堆外内存. 确实不受-XX:MaxDirectMemorySize这个JVM参数的限制 . 所以限制的内存大小为操作系统的内存.

如果使用Java自带的 ByteBuffer.allocateDirect(size) 或者直接 new DirectByteBuffer(capacity) , 这样受-XX:MaxDirectMemorySize 这个JVM参数的限制. 其实底层都是用的Unsafe#allocateMemory,区别是对大小做了限制. 如果超出限制直接OOM.

如果不设置-XX:MaxDirectMemorySize 默认的话,是跟堆内存大小保持一致. [堆内存大小如果不设置的话,默认为操作系统的 1/4, 所以 DirectMemory的大小限制JVM的Runtime.getRuntime().maxMemory()内存大小 . ], 代码入下:



## 为啥Metadata GC会触发Full GC？

## https://www.zhihu.com/question/442664600
