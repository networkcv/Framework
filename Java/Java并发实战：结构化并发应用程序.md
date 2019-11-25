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
为解决执行服务的生命周期问题，Execotur扩展了ExecutorServic接口，添加了一些用于生命周期管理和任务提交的方法
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

