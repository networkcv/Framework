

## 3.Semaphore

信号量可以允许多个线程访问同一个临界区。

信号量模型可以简单概括：一个计数器、一个等待队列，两个方法。在信号量模型里，计数器和等待队列对外是透明的，只能通过信号量模型提供的方法来访问。acquire()和release()，也被称为PV操作。如果计数器为1，就只有一个线程可以拿到该信号量，作用就类似于锁 ，如果计数器为10，就有10个线程可以拿到该信号量，进而执行下一步的操作。

![](D:\study\Framework\Java\img\27-信号量模型.jpg)

Semaphore底层通过AQS实现，通过一个volatile变量间接实现同步。

下面我们再来分析一下,信号量是如何保证互斥的。假设两个线程T1和T2同时访问addOne)方法,当它们同时调用acquire)的时候,由于acquire)是一个原子操作,所以只能有一个线程(假设T1)把信号量里的计数器减为0,另外一个线程(T2)则是将计数器减为-1。对于线程T1,信号量里面的计数器的值是0,大于等于0,所以线程T1会继续执行;对于线程T2,信号量里面的计数器的值是-1,小于0,按照信号量模型里对down()操作的描述,线程T2将被阻塞。所以此时只有线程T1会进入临界区执行count+=1: .

当线程T1执行release)操作,也就是up()操作的时候,信号量里计数器的值是-1,加1之后的值是0,小于等于0,按照信号量模型里对up0)操作的描述,此时等待队列中的T2将会被唤·醒。于是T2在T1执行完临界区代码之后才获得了进入临界区执行的机会，从而保证了互斥性。

```java
public void acquire() throws InterruptedException   //获取信号量
public void release()   //释放信号量
```



## 4.ReadWriteLock & StampedLock

读写锁，在锁的功能上进行划分，可以让多个读线程进入  
读-读不互斥，读读之间不阻塞  
读-写互斥，读阻塞写，写也阻塞读
写-写互斥，写写阻塞

```java
ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
private static Lock readLock = readWriteLock.readLock()
private static Lock writeLock = readWriteLock.writeLock()
```



## 5.CountDownLatch & CyclicBarrier

再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作

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


循环栅栏，这个计数器可以反复使用，假设计数器设置为10，那么第一批10个线程后，计数器会重置，然后接着处理第二批的10个线程  



## 6.LockSupport

类比于 suspend/resume，推荐使用LockSupport的原因是，即使unpark在park之前调用，也不会导致线程永久被挂起  
能够响应中断，但不抛出异常，中断的响应结果是，park()函数的返回，可以从Thread.interrupted()得到中断标志

```java
LockSupport.park(); //线程挂起
LockSupport.unpark(t1); //线程继续执行
```

# synchronized与Lock的比较

(1).Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；
(2).synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；
(3).Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
(4).通过Lock可以知道有没有成功获取锁，而synchronized却无法办到；
(5).Lock可以提高多个线程进行读操作的效率。
在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。

# CyclicBarrier与CountDownLatch的比较

CountDownLatch：一个或多个线程等待另外N个线程完成某个事情之后才能继续执行。

CyclicBarrier：N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。

对于CountDownLatch来说，重点的是那个“一个线程”，是它在等待，而另外那N个线程在把“某个事情”做完之后可以继续等待，可以终止；而对于CyclicBarrier来说，重点是那“N个线程”，它们之间任何一个没有完成，所有的线程都必须等待。

CountDownLatch是计数器，线程完成一个就计一个，就像报数一样，只不过是递减的；
CyclicBarrier更像一个水闸，线程执行就像水流，在水闸处就会堵住，等到水满（线程到齐）了，才开始泄流。

CountDownLatch不可重复利用，CyclicBarrier可重复利用。

# 对ReadWriteLock的理解

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



# CountDownLatch

使用Latch (门问)替代wait notify来进行通知好处是通信方式简单,
同时也可以指定等待时间使用await和countdown方法替代wait和notify 
CountDownLatch不涉及锁定, 当count的值为零时当前线程继续运行当不涉及同步,只是涉及线程通信的时候,
用synchronized + wait/notify就显得太重了
这时应该考虑countdownlatch/cyclicbarrier/semaphore

# ReentrantLock

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

