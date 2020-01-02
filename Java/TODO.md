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



## 7.ReadWriteLock  ok!

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

## 8.Semaphore  ok!

- 1.[信号量模型]()
- [2.Semaphore使用]()
- [3.Semaphore解析]()
  - [3.1 申请许可]()
  - [3.2 释放许可]()
  - [3.3 小结]()
- [4.总结]()

## 9.CountDownLatch & CyclicBarrier  ok!
- 1.[CountDownLatch]()
  - [1.1 CountDownLatch介绍]()
  - [1.1 CountDownLatch使用]()
  - [1.1 CountDownLatch原理]()
- [2.CyclicBarrier]()
  - [2.1 CyclicBarrier介绍]()
  - [2.2 CyclicBarrier使用]()
  - [2.3 CyclicBarrier原理]()
  - [2.4小结]()
- [3.总结]()


## 10.AQS  TODO!

- AQS介绍
- AQS原理
- AQS设计模式
- JUC的总结 

## 11.并发级别和无锁类

- 1.[并发级别]()
  - [1.1 阻塞]()
  - [1.1 无饥饿]()
  - [1.1 无障碍]()
  - [1.1 无锁]()
  - [1.1 无等待]()
- [2.无锁类2]()
  - [2.1 无锁类的介绍]()
  - [2.2 AtomicInteger]()
  - [2.3 AtomicIntegerArray]()
  - [2.4AtomicReference]()
  - [2.5AtomicIntegerFieldUpdater]()
  - [2.6LongAdder]()
  - [2.7Unsafe类]()
- [3.总结]()


## 同步容器和并发容器

同步容器,并发容器,阻塞队列 ConcurrentModificationException
ConcurrentHashMap CopyOnWriteArrayList

## 线程池  
Executors Callable Future FutureTask Timer
10-创建多少线程才是合适的

## 锁优化

减时间 减粒度 锁分离 锁粗化 锁消除

偏向锁，轻量级锁，自旋锁总结



 StampedLock



