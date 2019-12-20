

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

