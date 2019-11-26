# 6 任务执行
## 6.1 在线程中执行任务
不建议为每个任务创建一个线程，尽量限制可创建线程的数量  
原因如下：
- 线程的生命周期的开销非常高，线程的创建和销毁
- 资源消耗，尤其是内存，可运行的线程数量多于处理器的数量，那么大量空闲的线程会占用许多内存，给垃圾回收器带来压力，而且大量线程在竞争CPU资源时也将产生其他的性能开销
- 稳定性 ，OutOfMemoryError异常

## 6.2 Executor框架
Executor的实现还提供了对生命周期的支持，以及统计信息收集、应用程序管理机制和性能监视等机制。  
### 线程池
- newFixedThreadPool(int nThreads)  创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。 
- newCachedThreadPool()：创建一个可缓存的线程池，调用execute将重用以前构造的线程(如果线程可用)，如果当前没有可用线程，则创建一个新线程并添加到池中，终止并从缓存中移除那些已有60s钟未被使用的线程；
- newSingleThreadPool() 创建只有一个线程的线程池，可以保证任务执行的顺序 且在任意给定的时间不会有多个线程是活动的 .包含一个无界队列，保证所有任务按照指定顺序（FIFO/LIFO/优先级）执行；
- newScheduledThreadPool(int corePoolSize)：创建一个支持定时及周期性任务执行的定长线程池，多数情况下可用来代替Timer类。上一个运行完之后隔2s钟
### Executor
由于Executor以异步方式来运行，因此在任何时刻，有些任务可能已经完成，有些任务可能正在运行，而其他的任务可能在队列中等待执行，当关闭应用程序时，可能采用最平缓的关闭方式（完成所有已经启动的任务，并且不再接受任务新的任务），也可以采用最粗暴的关闭形式（直接关闭机房电源）  
为解决执行服务的生命周期问题，ExecoturService扩展了Executor接口，添加了一些用于生命周期管理和任务提交的方法
```java
public interface ExecutorService extends Executor{
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
    //其他用于任务提交的方法
}
```  
ExecutorService的生命周期有3种状态：运行、关闭和已终止  
在初始创建时处于运行状态，shutdown方法将执行平缓的关闭过程，不再接受新任务，等待已提交的任务执行完成，包括那些未开始执行的任务。shutdownNow方法将执行粗暴的关闭，尝试取消所有运行中的任务，并且不再启动队列中尚未开始的任务。

### 延时任务和周期任务
使用ScheduleadThreadPoolExecutor 代替 Timer  
如果要构建自己的调度服务，那么可以使用DelayQueue，它实现类BlockingQueue，并为ScheduledThreadPoolExecutor提供调度功能    
DelayQueue 延时的无界队列，用于放置实现类Delayed接口的对象，其中的对象只能在其到期后才能从队列取走，从DelayQueue中返回的对象将根据它们的延时时间进行排序


## 6.3 找出可利用的并行性
Runnable有很大局限的抽象，无法返回一个值或者抛出一个受检查的异常  
很多任务实际上都存在延迟计算，如执行数据库查询，从网络上获取资源，或者计算某个复杂功能  
Executor执行任务有4个什么周期阶段：创建、提交、开始和完成，Executor框架中，已提交但未开始的任务可以取消，但对于那些已经开始的任务，只有它们能响应中断时，才能取消。  
只有当大量相互独立且同构的任务可以并发进行处理时，才能体现出将程序的工作负载分配到多个任务中带来的真正性能提升。例如页面渲染中分为图像渲染和文字渲染两个任务，两者的耗时可能相差巨大，在多线程跑和单线程串行执行性能差别不大，但代码却变的更加复杂了，除非是在文字渲染完成后就直接对用户进行响应，不等图片创建完成。  

### CompletionService 完成服务
CompletionService将Executor和BlockingQueue的功能融合在一起，可以使用take和poll方法来获取已完成的结果，这些结果会在完成时被封装为Futrue，ExecutorCompletionService实现类CompletionService，并将计算部分委托给Executor
内部维护了一个BlockingQueue来保存计算完成的结果，计算完成时，调用FutrueTask中的done放到方法。当提交某个人物时，该任务将首先包装一个QueueingFuture，这是FutrueTask的一个子类，重写done方法，将结果放入BlockingQueue中，这些方法在得出结果前会阻塞。
Future.get(timeStamp,TimeUnit);  超时会抛出 TimeoutException  

##　小结
通过围绕任务执行来设计应用程序，可以简化开发过程，并有助于实现开发，Executor框架将任务提交与执行策略解耦开来，同时还支持多种不同类型的执行策略。当需要创建线程来执行任务时，可以考虑使用Executor。要想在将应用程序分解为不同的任务时获得最大的好处，必须定义清晰的任务边界，某些应用程序中存在着比较明显的任务边界，而在其他一些线程中则需要进一步分析才能揭示出粒度更细的并行性。

# 7 取消与关闭
Java没有提供任何机制来安全的终止线程，但它提供了中断（Interruption），这是一种协作机制，能够使一个线程终止另一个线程的当前工作。我们很少希望某个任务、线程或服务立即停止，因为这种立即停止会使共享的数据结构处于不一致的状态。通过协作的方式，我们可以让要退出的程序清楚当前正在执行的工作，然后再结束，这提供了更好的灵活性，因为任务本身的代码比发出取消请求的代码更清楚如何清除工作。
## 7.1 任务取消
### 中断
Java没有提供抢占式中断，而是通过协作式中断，通过推迟中断请求的处理，开发人员能制定更灵活的中断策略，使程序在响应性和健壮性之间实现合理的平衡。
### 处理不可中断的阻塞
执行同步的SocketI/O或者等待获得内置锁而阻塞，无法响应中断。
InputStream和OutputStream的read和write等方法都不会响应中断，但可以通过关闭底层的套接字，使得因read或write等方法被阻塞的线程抛出一个SocketException  
Lock类中提供了lockInterruptibly方法，该方法允许在等待一个锁的同时仍能响应中断。

## 7.2 停止基于线程的服务