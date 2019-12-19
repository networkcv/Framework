**前言：**上一篇我们了解了Lock接口与Condition接口。本篇来看看J.U.C中提供的其他工具类，再次膜拜一下Doug Lea大神的杰作。

## 1.ReadWriteLock

### 1.1 ReadWriteLock简介

ReadWriteLock接口是在JDK5提供的，具体的实现类为ReentrantReadWriteLock，还有一个实现类ReadWriteLockView，是StampedLock的内部类。后边会有讲到。

```Java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```

ReadWriteLock直译为读写锁，从接口命名上就可以看出该工具类用于特定的场景下，前面讲过的ReentrantLock和synchronized基本可以用来解决一切并发问题，但在特定的场景下可能表现的效果不那么令人满意，如在读多写少的场景下，大部分的线程都在进行读操作，很少的线程才会修改共享数据。但由于加锁的特性，导致大量的读操作进行了不必要的锁竞争，如果能将读写的锁分离，有写操作的时候，进行读操作需要加锁；没有写操作的时候，可以多个线程同时进行读操作。这样势必会提升性能。

读写锁便是解决这种场景问题的。读写锁有三个基本原则：

1. 允许多个线程同时读共享变量
2. 只允许一个线程写共享变量
3. 如果一个写线程正在执行，此时禁止读线程读共享变量。

### 1.2 ReentrantReadWriteLock使用

```java
    ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    Lock readLock = readWriteLock.readLock()
    Lock writeLock = readWriteLock.writeLock()
```

可以看出无论是读锁还是写锁都是Lock接口的实现类，那么上一篇中提到Lock接口的三种加锁方式都可以使用。

```Java
//支持中断的加锁
void lockInterruptibly() throws InterruptedException;
//支持超时的加锁
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
//支持非阻塞获取锁
boolean tryLock();
```

Reentrant代表可重入的，ReentrantReadWriteLock支持重入锁，而且也支持公平锁和非公平锁。

****

前面简单的介绍的读写锁的使用，这里有一个需要注意的点，就是读写锁的升级和降级。

**ReentrantReadWriteLock不支持锁的升级**，但是支持锁的降级。锁降级就是持有写锁去申请读锁；锁升级是持有读锁去申请写锁，如果出现类似锁升级的代码，则会导致线程阻塞，且无法被唤醒。这点需要注意。

锁降级还是有很多应用场景的。比如在查缓存的时候，缓存失效需要重新去数据库查并修改缓存。

### 1.3 ReentrantReadWriteLock原理

ReentrantReadWriteLock内部还是使用的AQS框架，

### 对ReadWriteLock的理解

ReadWriteLock同Lock一样也是一个接口，提供了readLock和writeLock两种锁的操作机制，一个是只读的锁，一个是写锁；
读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的（排它的）。每次只能有一个写线程，但是可以有多个线程并发地读数据；
所有读写锁的实现必须确保写操作对读操作的内存影响，换句话说，一个获得了读锁的线程必须能够看到前一个释放的写锁所更新的内容；
理论上，读写锁比互斥锁允许对于共享数据更大程度的并发。与互斥锁相比，读写锁是否能够提高性能取决于读写数据的频率、读取和写入操作的持续时间以及读线程和写线程之间的竞争。



## 2.StampedLock



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

### CyclicBarrier与CountDownLatch的比较

CountDownLatch：一个或多个线程等待另外N个线程完成某个事情之后才能继续执行。

CyclicBarrier：N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。

对于CountDownLatch来说，重点的是那个“一个线程”，是它在等待，而另外那N个线程在把“某个事情”做完之后可以继续等待，可以终止；而对于CyclicBarrier来说，重点是那“N个线程”，它们之间任何一个没有完成，所有的线程都必须等待。

CountDownLatch是计数器，线程完成一个就计一个，就像报数一样，只不过是递减的；
CyclicBarrier更像一个水闸，线程执行就像水流，在水闸处就会堵住，等到水满（线程到齐）了，才开始泄流。

CountDownLatch不可重复利用，CyclicBarrier可重复利用。





### CountDownLatch

使用Latch (门问)替代wait notify来进行通知好处是通信方式简单,
同时也可以指定等待时间使用await和countdown方法替代wait和notify 
CountDownLatch不涉及锁定, 当count的值为零时当前线程继续运行当不涉及同步,只是涉及线程通信的时候,
用synchronized + wait/notify就显得太重了
这时应该考虑countdownlatch/cyclicbarrier/semaphore

## 6.LockSupport

类比于 suspend/resume，推荐使用LockSupport的原因是，即使unpark在park之前调用，也不会导致线程永久被挂起  
能够响应中断，但不抛出异常，中断的响应结果是，park()函数的返回，可以从Thread.interrupted()得到中断标志

```java
LockSupport.park(); //线程挂起
LockSupport.unpark(t1); //线程继续执行
```







