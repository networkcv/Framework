## JDK并发包
### ReentrantLock  
可以理解为synchronized关键字的一个细化，JDK1.5后对synchronized进行了优化，现在两者差距并不是很大  
显式的Lock对象在加锁和释放锁方面，相对于内建的synchronized锁来说，还赋予了更细粒度的控制
- 可重入：同一把锁可以在持有的时候再进行获取(synchronized也可以)，同时获取几次也必须要释放几次，不然会造成死锁  

- 可中断：当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。  
也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，而线程B只有在等待，  
那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程  
synchronized对中断信号是没有响应的，而ReentrantLock可以对中断信号做出响应  


- 可限时：起到了定时锁的作用，如果在指定时间内没有获得锁，将会返回false  

- 公平锁：谁等的时间长，谁获得锁，基本上一人一次

### ReentrantLock实现
- CAS状态  
尝试去拿CAS锁

- 等待队列  
用于保存等待在锁上的队列

- park()  
等待队列中的操作进行park挂起

### Condition
Condition与ReentrantLock结合使用，这两者之间的关系可以参考synchronized和wait()/notify()的关系  
通过API的方式来对ReentrantLock进行类似于wait和notify的操作  
```java
// Codition方法
void await() throws InterruptedException;
boolean await(long time, TimeUnit unit) throws InterruptedException;
void signal();
void signalAll();
 

ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
```
### Semaphore
信号量 撒门否儿,共享锁 可以指定信号量的个数，如果个数为1，就只有一个线程可以拿到该信号量，作用就类似于锁  
如果信号量为10，就有10个线程可以拿到该信号量，进而执行下一步的操作
```java
public void acquire() throws InterruptedException   //获取信号量
public void release()   //释放信号量
```

### ReadWriteLock
读写锁，在锁的功能上进行划分，可以让多个读线程进入  
读-读不互斥，读读之间不阻塞  
读-写互斥，读阻塞写，写也阻塞读
写-写互斥，写写阻塞
```java
ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
private static Lock readLock = readWriteLock.readLock()
private static Lock writeLock = readWriteLock.writeLock()
```

### CountDownLatch
```java
CountDownLatch end = new CountDownLatch(10);
public void run(){
  //每次调用这个countDown方法，end的值减1
  end.countDown();
}
//只有当end被countdown到0的时候，主线程里的end.await()才会被唤醒
public void main(){
  end.await();
}
```

### CyclicBarrier
循环栅栏，这个计数器可以反复使用，假设计数器设置为10，那么第一批10个线程后，计数器会重置，然后接着处理第二批的10个线程  

### LockSupport
类比于 suspend/resume，推荐使用LockSupport的原因是，即使unpark在park之前调用，也不会导致线程永久被挂起  
能够响应中断，但不抛出异常，中断的响应结果是，park()函数的返回，可以从Thread.interrupted()得到中断标志
```java
LockSupport.park(); //线程挂起
LockSupport.unpark(t1); //线程继续执行
```
### BlockingQueue
在任何时刻都只允许一个任务插入或移除元素  
可以挂起和恢复消费者   
LinkedBlockingQueue   无界队列
ArrayBlockingQueue  
DelayQueue  延时的无界队列，用于放置实现类Delayed接口的对象，其中的对象只能在其到期后才能从队列取走
PriorityBlockingQueue 基础的优先级队列，可以阻塞读取操作

插入   
- add() 队满则抛异常
- offer() 队满返回false
- offer(e,time,unit) 队满则会在给定时间内阻塞
- put() 如果队列已满则会进入阻塞，直到可插入

移除  
- remove()  队空则抛异常
- poll() 队空则返回null
- poll(e,time,unit) 队空则会在给定时间内阻塞
- take()  队空则进入阻塞，直到可返回

检查  
- element()   队空则抛异常
- peek() 队空则返回null


### ConcurrentHashMap

23. synchronized与Lock的比较
    (1).Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；
    (2).synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；
    (3).Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
    (4).通过Lock可以知道有没有成功获取锁，而synchronized却无法办到；
    (5).Lock可以提高多个线程进行读操作的效率。
    在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。

    
26. CyclicBarrier与CountDownLatch的比较
    CountDownLatch：一个或多个线程等待另外N个线程完成某个事情之后才能继续执行。

    CyclicBarrier：N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。

    对于CountDownLatch来说，重点的是那个“一个线程”，是它在等待，而另外那N个线程在把“某个事情”做完之后可以继续等待，可以终止；而对于CyclicBarrier来说，重点是那“N个线程”，它们之间任何一个没有完成，所有的线程都必须等待。

    CountDownLatch是计数器，线程完成一个就计一个，就像报数一样，只不过是递减的；
    CyclicBarrier更像一个水闸，线程执行就像水流，在水闸处就会堵住，等到水满（线程到齐）了，才开始泄流。

    CountDownLatch不可重复利用，CyclicBarrier可重复利用。

    19. 对ReadWriteLock的理解
        ReadWriteLock同Lock一样也是一个接口，提供了readLock和writeLock两种锁的操作机制，一个是只读的锁，一个是写锁；
        读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的（排它的）。每次只能有一个写线程，但是可以有多个线程并发地读数据；
        所有读写锁的实现必须确保写操作对读操作的内存影响，换句话说，一个获得了读锁的线程必须能够看到前一个释放的写锁所更新的内容；
        理论上，读写锁比互斥锁允许对于共享数据更大程度的并发。与互斥锁相比，读写锁是否能够提高性能取决于读写数据的频率、读取和写入操作的持续时间以及读线程和写线程之间的竞争。
        使用场景：假如程序中涉及到一些共享资源的读写操作，并且写操作没有读操作那么频繁。例如，最初填充有数据，然后很少修改的集合，同时频繁搜索，是使用读写锁的理想候选项。
        互斥原则：
        读-读能共存，
        读-写不能共存，
        写-写不能共存。

        public interface ReadWriteLock {
        Lock readLock();
        Lock writeLock();
        }




CountDownLatch
使用Latch (门问)替代wait notify来进行通知好处是通信方式简单,
同时也可以指定等待时间使用await和countdown方法替代wait和notify 
CountDownLatch不涉及锁定, 当count的值为零时当前线程继续运行当不涉及同步,只是涉及线程通信的时候,
用synchronized + wait/notify就显得太重了
这时应该考虑countdownlatch/cyclicbarrier/semaphore


ReentrantLock
reentrantlock用于替代synchronized,可以完成同样功能
reentrantlock必须要手动释放锁，sys锁定时遇到异常会释放锁，但reentrantlock不会
使用reentrantlock可以进行"尝试锁定"tryLock,这样无法锁定,或者在指定时间内无法锁定,
线程可以决定是否继续等待使用ReentrantLock还可以调用lockInterruptibly方法,
可以对线程interrupt方法做出响应,在一个线程等待锁的过程中,可以被打断
ReentrantLock还可以指定为公平锁，synchronized为非公平锁

Lock  lock =new ReentrantLock();
try{
    lock.lock();
}finally{
    lock.unlock();
}

可以使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
可以根据tryLock的返回值来判断是否锁定
也可以根据tryLock的时间，由于tryLock(time)抛出异常，所以finally进行unlock的处理
boolean locked= lock.tryLock();
if(locked){
    ...
}
try{
    lock.tryLock(5,TimeUnit.SECONDS)
}finally{
    if(locked) lock.unlock();
}

interrupt配合lockInterruptibly()中断线程
Thread t2 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println(locked);
            } catch (Exception e) {
                System.out.println("成功中断");
            } finally {
                lock.unlock();
            }

        });
        t2.start();
t2.interrupt() //打断线程2的等待
