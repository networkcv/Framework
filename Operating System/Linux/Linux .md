## 大量IO也可能会造成CPU负载高

非常频繁的IO 导致了非常频繁的中断信号，cpu处理信号需要保护和恢复现场，想等于频繁上下文切换，可能会造成CPU负载高，但是使用率低。

计算机硬件上使用DMA来访问磁盘等IO，也就是请求发出后，CPU就不再管了，直到DMA处理器完成任务，再通过中断告诉CPU完成了。所以，单独的一个IO时间，对CPU的占用是很少的，阻塞了就更不会占用CPU了，因为程序都不继续运行了，CPU时间交给其它线程和进程了。虽然IO不会占用大量的CPU时间，但是非常频繁的IO还是会非常浪费CPU时间的，所以面对大量IO的任务，有时候是需要算法来合并IO，或者通过cache来缓解IO压力的。







## 理解Linux load average的误区

uptime和top等命令都可以看到load average指标，从左至右三个数字分别表示1分钟、5分钟、15分钟的load average：

```
 uptime 10:16:25 up 3 days, 19:23, 2 users, load average: 0.00, 0.01, 0.05
```

Load average的概念源自UNIX系统，虽然各家的公式不尽相同，但都是用于衡量正在使用CPU的进程数量和正在等待CPU的进程数量，一句话就是runnable processes的数量。所以load average可以作为CPU瓶颈的参考指标，如果大于CPU的数量，说明CPU可能不够用了。

但是，Linux上不是这样的！

Linux上的load average除了包括正在使用CPU的进程数量和正在等待CPU的进程数量之外，还包括uninterruptible sleep的进程数量。通常等待IO设备、等待网络的时候，进程会处于uninterruptible sleep状态。Linux设计者的逻辑是，uninterruptible sleep应该都是很短暂的，很快就会恢复运行，所以被等同于runnable。然而uninterruptible sleep即使再短暂也是sleep，何况现实世界中uninterruptible sleep未必很短暂，大量的、或长时间的uninterruptible sleep通常意味着IO设备遇到了瓶颈。众所周知，sleep状态的进程是不需要CPU的，即使所有的CPU都空闲，正在sleep的进程也是运行不了的，所以sleep进程的数量绝对不适合用作衡量CPU负载的指标，Linux把uninterruptible sleep进程算进load average的做法直接颠覆了load average的本来意义。所以在Linux系统上，load average这个指标基本失去了作用，因为你不知道它代表什么意思，当看到load average很高的时候，你不知道是runnable进程太多还是uninterruptible sleep进程太多，也就无法判断是CPU不够用还是IO设备有瓶颈。



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





## Java中线程状态和IO的关系

当我们用jstack查看Java线程状态时，会看到各种线程状态。当发生IO等待时（比如远程调用时），线程是什么状态呢，Blocked还是Waiting？

答案是Runnable状态，是不是有些出乎意料！实际上，在操作系统层面Java的Runnable状态除了包括Running状态，还包括Ready（就绪状态，等待CPU调度）和IO Wait等状态。

如上图，Runnable状态的注解明确说明了，在JVM层面执行的线程，在操作系统层面可能在等待其他资源。如果等待的资源是CPU，在操作系统层面线程就是等待被CPU调度的Ready状态；如果等待的资源是磁盘网卡等IO资源，在操作系统层面线程就是等待IO操作完成的IO Wait状态。

有人可能会问，为什么Java线程没有专门的Running状态呢？

目前绝大部分主流操作系统都是以时间分片的方式对任务进行轮询调度，时间片通常很短，大概几十毫秒，也就是说一个线程每次在cpu上只能执行几十毫秒，然后就会被CPU调度出来变成Ready状态，等待再一次被CPU执行，线程在Ready和Running两个状态间快速切换。通常情况，JVM线程状态主要为了监控使用，是给人看的。当你看到线程状态是Running的一瞬间，线程状态早已经切换N次了。所以，再给线程专门加一个Running状态也就没什么意义了。



**内核空间与用户空间**：在Linux中，应用程序稳定性远远比不上操作系统程序，为了保证操作系统的稳定性，Linux区分了内核空间和用户空间。可以这样理解，内核空间运行操作系统程序和驱动程序，用户空间运行应用程序。Linux以这种方式隔离了操作系统程序和应用程序，避免了应用程序影响到操作系统自身的稳定性。这也是Linux系统超级稳定的主要原因。所有的系统资源操作都在内核空间进行，比如读写磁盘文件，内存分配和回收，网络接口调用等。所以在一次网络IO读取过程中，数据并不是直接从网卡读取到用户空间中的应用程序缓冲区，而是先从网卡拷贝到内核空间缓冲区，然后再从内核拷贝到用户空间中的应用程序缓冲区。对于网络IO写入过程，过程则相反，先将数据从用户空间中的应用程序缓冲区拷贝到内核缓冲区，再从内核缓冲区把数据通过网卡发送出去。



## 同步阻塞IO

我们先看一下传统阻塞IO。在Linux中，默认情况下所有socket都是阻塞模式的。当用户线程调用系统函数read()，内核开始准备数据（从网络接收数据），内核准备数据完成后，数据从内核拷贝到用户空间的应用程序缓冲区，数据拷贝完成后，请求才返回。从发起read请求到最终完成内核到应用程序的拷贝，整个过程都是阻塞的。为了提高性能，可以为每个连接都分配一个线程。因此，在大量连接的场景下就需要大量的线程，会造成巨大的性能损耗，这也是传统阻塞IO的最大缺陷。

## 同步非阻塞IO

用户线程在发起Read请求后立即返回，不用等待内核准备数据的过程。如果Read请求没读取到数据，用户线程会不断轮询发起Read请求，直到数据到达（内核准备好数据）后才停止轮询。非阻塞IO模型虽然避免了由于线程阻塞问题带来的大量线程消耗，但是频繁的重复轮询大大增加了请求次数，对CPU消耗也比较明显。这种模型在实际应用中很少使用。

## 多路复用IO模型

多路复用IO模型，建立在多路事件分离函数select，poll，epoll之上。在发起read请求前，先更新select的socket监控列表，然后等待select函数返回（此过程是阻塞的，所以说多路复用IO也是阻塞IO模型）。当某个socket有数据到达时，select函数返回。此时用户线程才正式发起read请求，读取并处理数据。这种模式用一个专门的监视线程去检查多个socket，如果某个socket有数据到达就交给工作线程处理。由于等待Socket数据到达过程非常耗时，所以这种方式解决了阻塞IO模型一个Socket连接就需要一个线程的问题，也不存在非阻塞IO模型忙轮询带来的CPU性能损耗的问题。多路复用IO模型的实际应用场景很多，比如大家耳熟能详的Java NIO，Redis以及Dubbo采用的通信框架Netty都采用了这种模型



## read、write会发生阻塞

连接建立成功之后，就能开始发送和接收消息了。我们来看一下：

read 为读数据，从服务端来看就是等待客户端的请求。如果客户端不发请求，那么调用 read 会处于阻塞等待状态，没有数据可以读，这个应该很好理解。

write 为写数据。一般而言服务端接受客户端的请求之后，会进行一些逻辑处理，然后再把结果返回给客户端，这个写入也可能会被阻塞。

这里可能有人就会问 read 读不到数据阻塞等待可以理解，write 为什么还要阻塞，有数据不就直接发了吗？

因为我们用的是 TCP 协议，TCP 协议需要保证数据可靠地、有序地传输，并且给予端与端之间的流量控制。

所以说发送不是直接发出去，它有个发送缓冲区，我们需要把数据先拷贝到 TCP 的发送缓冲区，由 TCP 自行控制发送的时间和逻辑，有可能还有重传什么的。

如果我们发的过快，导致接收方处理不过来，那么接收方就会通过 TCP 协议告知：别发了！忙不过来了。发送缓存区是有大小限制的，由于无法发送，还不断调用 write 那么缓存区就满了，满了就不然你 write 了，所以 write 也会发生阻塞。

综上，read 和 write 都会发生阻塞。





# brew介绍

brew是一个软件包管理工具,类似于centos下的yum或者ubuntu下的apt-get,非常方便,免去了自己手动编译安装的不便
 　brew 安装目录 /usr/local/Cellar
 　brew 配置目录 /usr/local/etc
 　brew 命令目录 /usr/local/bin   注:homebrew在安装完成后自动在/usr/local/bin加个软连接，所以平常都是用这个路径

brew search 软件名，如brew search wget //搜索软件
 brew install 软件名，如brew install wget//安装软件
 brew remove 软件名，如brew remove wget//卸载软件



# brew换源

[为brew/git/pip设置代理&为brew正确换源终极版](https://segmentfault.com/a/1190000019758638)



# bash_profile 和 zshrc 文件的区别？

.bash_profile 中修改环境变量只对当前窗口有效，而且需要 source ~/.bash_profile才能使用
.zshrc 则相当于 windows 的开机启动的环境变量
你也可以在 .zshrc 文件中加一行 source .bash_profile 解决需要 source 才能使用的问题





## 解释与编译

编程语言没有编译型和解释型的区别，只能说某个语言常见的执行方式为*编译成新代码执行*或*解释器解释执行*
编译器的输入是A语言的源代码，而输出是B语言；比如C++，被编译成汇编语言；
解释器的输入是A语言的源代码，它直接执行A语言；一般解释器的内部实现是一个编译器加一个虚拟机，编译器把输入语言编译成中间语言，虚拟机直接执行中间语言。

## terminal

一个程序，是界面上打开的黑框框本身，比如 xterm、kvt 等。shell 运行于其中。

## shell 概念

shell 是一个命令行解释器，顾名思义就是机器外面的一层壳，用于人机交互，只要是人与电脑之间交互的接口，就可以称为 shell。表现为其作用是用户输入一条命令，shell 就立即解释执行一条。不局限于系统、语言等概念、操作方式和表现方式等。 比如我们平时在黑框框里输入命令，叫 command-line interface (CLI)；在屏幕上点点点，叫graphical user interface (GUI)

## Interactive 和 Non-interactive

Interactive，如果你打开 terminal，在里面输入 bash 代码，回车得到输出，你就是在运行一个 Interactive shell，它的特征是可以让用户输入，然后直接把输出打到界面上；如果你运行一个包含了若干行的 shell 脚本，这些 shell 代码就运行在Non-interactive shell 中。

## Login 和 Non-login

login shell 是指登录系统后所获得的顶层 shell，比如最常用的 ssh 登录，登录完后得到一个 login shell
如果已经登录了桌面电脑，打开 terminal 进入的 shell 就是 Non-login shell。

## 类型

常见的 shell 解释器有 sh、bash这两种，其他的 ksh、csh 和 zsh 等是不常见的。Mac OS 中默认安装了以上所有类型，Windows 需要自行安装，Linux 更不用说了。就像上面说的，只要一门语言有解释器，就可以作为 shell 使用。比如Java 有第三方解释器 Jshell，PHP有 PHP Shell。如果你用过 windows，那你对 cmd 这个词一定不陌生，它是 windows shell，官方名称叫做 command interpreter。

## bash

Bash 是最常见的 shell，Mac 中默认 shell 就是 bash。
[bash官网这篇文章]描述了唤起 bash shell 时加载的不同文件：login shell 加载 \~/.bash_profile ，而non-login shell 加载 \~/.bashrc 。

## zsh

很多人的 mac 中会使用 zsh 而不是 bash，一大半是因为 oh-my-zsh 这个配置集，它兼容 bash，还有自动补全等好用的功能。

zsh 的配置文件\~/.zshrc

Mac OS 中，终端默认使用的shell脚本是zsh，不是bash。会导致报错如下：

```
WARNING: this script is deprecated, please see git-completion.zsh
```

但是 git 并没有兼容zsh，所以想用git 补全 得用回 bash。

具体操作步骤  终端 --> 偏好设置 --> 通用 --> Shell的打开方式，选中【命令（完整的路径）】，设置为：/bin/bash。

## 配置 shell

如上所说，shell 在启动时都会去找配置文件，然后运行它。你安装的一些脚本，如果想让它能够全局运行，就需要在配置文件中设置路径。有过设置路径后还是不管用的经历吗？多半是因为把配置写在了错误的配置文件里。** 应该在配置shell（最常见的是配置默认命令）之前，使用 echo $SHELL，确认自己现在用的是什么shell后，再去编辑对应的配置文件 **



