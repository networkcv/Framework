## 1.线程和进程  ok！
- [1进程](#1.1进程)
  - [1.1.1进程的由来](#1.1.1进程的由来)
  - [1.1.2并行和并发](#1.1.2并行和并发)
  - [1.1.3从应用层面理解进程](#1.1.3从应用层面理解进程)
- [1.2线程](#1.2线程)
  - [1.2.1线程的由来](#1.2.1线程的由来)
  - [1.2.2Java中的线程](#1.2.2Java中的线程)
- [1.3多线程的优缺点](#1.3多线程的优缺点)
  - [1.3.1多线程的优势](#1.3.1多线程的优势)
  - [1.3.2多线程带来的风险](#1.3.2多线程带来的风险)

## 2.线程的生命周期  ok！
- [1.如何创建一个线程](#1.如何创建一个线程)
  - [1.1实现Runnable接口](#1.1实现Runnable接口)
  - [1.2继承Thread类](#1.2继承Thread类)
  - [1.3实现Callable接口](#1.3实现Callable接口)
  - [1.4对创建线程的一些个人理解](#1.4对创建线程的一些个人理解)
- [2.线程的生命周期和状态](#2.线程的生命周期和状态)

## 3.Thread类的使用  ok！
- [1.Thread中的属性](#1.Thread中的属性)
- [2.Thread中的方法](#2.Thread中的方法)
  - [2.1 start()、run()和stop()](#2.1start()、run()和stop())
  - [2.2 suspend()和resume()](#2.2suspend()和resume())
  - [2.3 sleep()和TimeUnit](#2.3sleep()和TimeUnit)
  - [2.4 interrupt()、isInterrupted()和Thread.interrupted()](#2.4interrupt()、isInterrupted()和Thread.interrupted())
  - [2.5 wait()和notify()/notifyAll()](#2.5wait()和notify()/notifyAll())
  - [2.6 yeild()和join()](#2.6yeild()和join())

## 4.Java内存模型  ok！
- [1.硬件内存架构、Java内存结构和Java内存模型](#1.硬件内存架构、Java内存结构和Java内存模型)
    - [1.1 硬件内存架构](#1.1硬件内存架构)
    - [1.2 Java内存结构](#1.2Java内存结构)
    - [1.3 Java内存模型](#1.3Java内存模型)
- [2.并发编程常见问题来源](#2.并发编程常见问题来源)
    - [2.1 原子性问题](#2.1原子性问题)
    - [2.2 可见性问题](#2.2可见性问题)
    - [2.3 有序性问题](#2.3有序性问题)
- [3.Java如何解决并发问题](#3.Java如何解决并发问题)
    - [3.1 Happens-Before](#3.1Happens-Before)
    - [3.2 volatile](#3.2volatile)
    - [3.3 synchronized](#3.3synchronized)

## 5.线程安全  ok！
- [1.活跃性问题](#1.活跃性问题)
	- [1.1 死锁](#1.1 死锁)
	- [1.2 活锁](#1.2 活锁)
	- [1.3 饥饿](#1.3 饥饿)
- [2.性能问题](#2.性能问题)
	- [2.1 上下文切换](#2.1 上下文切换)
	- [2.2 内存同步](#2.2 内存同步)
    - [2.3 阻塞](#2.3 阻塞)
- [3.线程安全](#3.线程安全)
	- [3.1 加锁机制](#3.1 加锁机制)
	- [3.2 保证可见性](#3.2 保证可见性)
	- [3.3 线程封闭](#3.3 线程封闭)
	- [3.4 实例封闭](#3.4 实例封闭)

## 6.Lock&Condition  ok!
- [1.管程](#1.管程)
    - [1.1 如何解决互斥](#1.1 如何解决互斥)
    - [1.2 如何解决同步](#1.2 如何解决同步)
    - [1.3 管程发展史上出现的三种模型](#1.3 管程发展史上出现的三种模型)
- [2.Lock ](#2.Lock )
	- [2.1 Lock接口的由来](#2.1 Lock接口的由来)
	- [2.2 ReentrantLock原理](#2.2 内存同步)
    - [2.3 可重入](#2.3 可重入)
    - [2.4 公平锁与非公平锁](#2.4 公平锁与非公平锁)
- [3.Condition](#3.Condition)
	- [3.1 Condition简介](#3.1 Condition简介)
	- [3.2 Condition原理](#3.2 Condition原理)
- [4.总结](#4.总结)



## 7.ReadWriteLock

- [1.ReadWriteLock简介]()

- [2.ReentrantReadWriteLock使用]()

	- [2.1 锁降级]()

	- [2.2 写锁支持条件变量]()

- [3.ReentrantReadWriteLock原理]()

	- [3.1 写锁加锁]()

	- [3.2 写锁释放]()

	- [3.3 读锁加锁]()

	- [3.4 读锁释放]()

- [4.总结]()

## AQS

AQS介绍，AQS原理，AQS设计模式，对JUC的总结。

## 同步容器和并发容器

同步容器,并发容器,阻塞队列 ConcurrentModificationException
ConcurrentHashMap CopyOnWriteArrayList

## 线程池  
Executors Callable Future FutureTask Timer
10-创建多少线程才是合适的

## 锁优化

减时间 减粒度 锁分离 锁粗化 锁消除

偏向锁，轻量级锁，自旋锁总结

## 并发级别和无锁类

并发级别，无锁类，CAS，悲观锁和乐观锁， StampedLock



## 其他

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

