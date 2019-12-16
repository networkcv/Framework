
### Executor-shutdownNow
在JDK5后，新的concurrent类库尽可能的在避免直接对Thread类的操作，尽量使用Executor  
如果执行Executor上的shutdownNow，将发送一个interrupt()调用给它启动的所有线程  
如果想中断某一个线程，则需要用使用submit()来启动任务，通过Future<?>的cancel(true)来中断线程
```java
public void 中断Executors的某个线程() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Future<?> submit = cachedThreadPool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("over");
            }

        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        submit.cancel(true);
    }
```


20. 线程池的工作原理
    Executor
        Executor框架是在Java5中引入的，其内部使用了线程池机制，在java.util.concurrent包下，通过该框架来控制线程的启动、执行和关闭，可以简化并发编程的操作。因此，在Java5之后，通过Executor来启动线程比使用Thread的start方法更好，
    除了更易管理，效率更好外，还有关键的一点：有助于避免this逃逸问题：在构造器构造还未彻底完成之前，将自身this引用向外抛出并被其它线程访问，可能会被访问到还未被初始化到的变量，甚至可能会造成更严重的问题。
    
    ExecutorService接口继承自Executor接口，它提供了更丰富的实现多线程的方法，比如，可以调用ExecutorService的shutdown方法来平滑地关闭ExecutorService，调用该方法后，
    将导致ExecutorService停止接收任何新的任务且等待已经提交的任务执行完成（已经提交的任务会分为两类，一类是已经在执行的，另一类是还没有开始执行的），当所有已经提交的任务执行完毕后将会关闭ExecutorService，
    因此我们一般用该接口来实现和管理多线程。

    Executor接口定义了execute方法用来接收一个Runnable接口的对象， 而ExecutorService接口中的submit方法可以接收Runnable和Callable接口的对象。

    ExecutorService的生命周期包括三种状态：运行、关闭、终止。创建后便进入运行状态，当调用了shutdown方法时，便进入关闭状态，此时意味着ExecutorService不再接收新的任务，但它还在执行已经提交了的任务，
    当所有已经提交了的任务执行完成后，便达到终止状态。如果不调用shutdown方法，ExecutorService会一直处于运行状态，不断接收新的任务，服务器一般不需要关闭它，保持一直运行即可。



    (1) newFixedThreadPool(int nThreads)  创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。 

    (2) newCachedThreadPool()：创建一个可缓存的线程池，调用execute将重用以前构造的线程(如果线程可用)，如果当前没有可用线程，则创建一个新线程并添加到池中，终止并从缓存中移除那些已有60s钟未被使用的线程；

    (3) newSingleThreadPool() 创建只有一个线程的线程池，可以保证任务执行的顺序 且在任意给定的时间不会有多个线程是活动的 .包含一个无界队列，保证所有任务按照指定顺序（FIFO/LIFO/优先级）执行；

    (4) newScheduledThreadPool(int corePoolSize)：创建一个支持定时及周期性任务执行的定长线程池，多数情况下可用来代替Timer类。上一个运行完之后隔2s钟

    (5) newWorkStealingPool()  根据CPU核数，产生相同数目的线程，线程执行完成后会执行其他队列的任务，底层是ForkJoinPool

    (6) newForkJoinPool()      其所有的线程数demon线程(守护线程，精灵线程) JVM不退出，则会在后台一直运行，主线程阻塞才能看到其执行结果
    
    以上方法返回的类型为ExecutorService，调用的构造方法为ThreadPoolExecutor(......)
    核心构造方法ThreadPoolExecutor(......)参数讲解：
    corePoolSize：核心线程池大小；
    maximumPoolSize：最大线程池大小；
    keepAliveTime：线程池中超过corePoolSize数目的空闲线程最大存活时间；
    TimeUnit：keepAliveTime的时间单位；
    workQueue：阻塞任务队列；
    threadFactory（可选）：新建线程工厂；
    RejectedExecutionHandler（可选）：当提交任务数量超过maximumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler处理。

    其中比较容易让人误解的是：corePoolSize、maximumPoolSize、workQueue之间的关系：
    (1).当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程；
    (2).当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行；
    (3).当workQueue已满，且maximumPoolSize>CorePoolSize时，新提交的任务会创建新线程执行任务；
    (4).当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理；
    (5).当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程；
    (6).当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭。


22. 如何终止线程池
    shutdown：当线程池调用该方法时，线程池的状态立刻变为SHUTDOWN状态。此时，不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException。但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。

    shutdownNow：执行该方法，拒绝接收新提交的任务，（1）线程池的状态立即变为STOP，（2）并试图阻止所有正在执行的线程，（3）不再处理还在线程池队列中等待的任务，当然，它会返回那些未执行的任务。

    它试图阻止线程的方法是通过调用Thread.interrupt方法来实现的，但是这种方法的作用有限，如果线程中没有sleep、wait、Condition、定时锁等应用，interrupt是无法中断当前线程的。所以，shutdownNow并不代表线程池一定会立刻退出，它可能需要等待所有正在执行的任务都执行完毕才会退出。
