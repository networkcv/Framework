- 1.线程和进程  ok！
线程和进程，多线程的优缺点

- 2.线程的生命周期  ok！
线程的创建和线程的生命周期

- Thread类的使用  
start(),run(),join(),yeild(),sleep()
suspend()resume()
interrupt(),isInterrupted(),interrupted()
wait(),notify(),notifyAll()
守护线程 线程优先级

- Java内存模型和线程安全  
竞态条件  临界区
ThreadLocal volatile synchronized

- 并发级别和无锁   
并发级别 AQS CAS 无锁类的使用 AtomicXXX类 Unsafe  乐观锁与悲观锁的比较 公平锁
AtomicIntergerArray AtomicIntegerFieldUpdater LockFreeVector


- JDK并发包  
Lock ReentrantLock Semaphore CountDownLatch  CyclicBarrier  
LockSupport ReadWriteLock 

- 锁优化  
锁粒度，锁分离，锁粗化，锁消除
偏向锁，轻量级锁，自旋锁总结

- 并发容器  
同步容器,并发容器,阻塞队列 ConcurrentModificationException
ConcurrentHashMap CopyOnWriteArrayList

- 线程池  
Executors Callable Future FutureTask Timer

- 其他
 JDK8
LongAdder  CompletableFuture StampedLock
多线程中的设计模式
单例模式 不变模式 Future模式 生产者消费者模式