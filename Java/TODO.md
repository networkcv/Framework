- 1.线程和进程  ok！
线程和进程理解，多线程的优缺点

- 2.Thread类的使用  
线程的生命周期
start(),run(),join(),yeild(),sleep()
suspend()resume()
interrupt(),isInterrupted(),interrupted()
wait(),notify(),notifyAll()
守护线程

- 3.Java内存模型和线程安全  
竞态条件  临界区
ThreadLocal volatile synchronized

- 4.并发容器  
同步容器,并发容器,阻塞队列 ConcurrentModificationException
ConcurrentHashMap CopyOnWriteArrayList

- 5.线程池  
Executors Callable Future FutureTask Timer

- 6.并发级别  
并发级别 AQS CAS 无锁类的使用 AtomicXXX类 Unsafe 
AtomicIntergerArray AtomicIntegerFieldUpdater LockFreeVector

- 7.并发包  
Lock ReentrantLock Semaphore CountDownLatch  CyclicBarrier  
LockSupport ReadWriteLock 

- 8.锁优化  
锁粒度，锁分离，锁粗化，锁消除
偏向锁，轻量级锁，自旋锁总结

- 9.JDK8  
LongAdder  CompletableFuture StampedLock