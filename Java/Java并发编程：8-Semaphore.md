**前言：**

在[Lock & Condition]()中我们学习了管程这种并发编程模型，在管程模型提出之前，信号量模型一直是并发编程领域的终结者，几乎所有支持并发编程的语言都支持信号量机制，今天就来看看Java中的信号量实现--Semaphore。

[toc]

**面试问题**
Q ：谈谈ReadWriteLock的好处？



## 1.信号量模型

在正式开始前，我们先简单回顾一下管程模型，管程模型中对共享变量互斥访问，只能有一个线程成功进入临界区，其他尝试失败的线程会在临界区外等待区的入口等待队列中等待，进入临界区中的线程，如果需要等待某个条件变量，则会释放锁，唤醒入口等待队列中等待的线程，同时在该条件变量对应的等待队列中等待，线程也会进入等待状态，直到被其他线程唤醒，才会从条件变量的等待队列中移除，加入到入口等待队列中重新尝试进入临界区。                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 

信号量与管程最大的不同：信号量可以允许多个线程访问同一个临界区，而管程只允许一个线程访问临界区。

信号量模型可以简单概括：一个计数器、一个等待队列，三个方法。在信号量模型里，计数器和等待队列对外是透明的，只能通过信号量模型提供的方法来访问。

**init**(int permits)：设置计数器的初始值，最多允许多少个线程同时访问临界区。

**down**(int permits)：计数器的值减去permits个许可，如果计数器中暂时没有足够的许可，则将当前线程阻塞，并加入到等待队列。

**up**(int permits)：计数器的值加上permits个许可，并根据归还后许可的个数，唤醒等待队列中一个线程，



![](D:\study\Framework\Java\img\27-信号量模型.jpg)

举个简单的例子，在哲学家用餐问题中，我们可以通过**破坏请求与保持条件**，一次性申请所有的资源来解决死锁问题，具体的操作就是把筷子都放桌子中央，需要吃饭的人一次拿两根。筷子不够两根的话，进行等待。

桌子中央的5根筷子就相当于信号量中的5个许可，在Init()时设置，当某个哲学家打算用餐时，则通过down(2)，拿走两根筷子(许可)，用餐完毕后up(2)归还筷子，如果中间只剩一根筷子，那么再执行down(2)并不会拿走剩下的这一根筷子，而是会进入等待队列，当有筷子被归还时，//TODO检查到可用筷子数满足等待队列，在实际使用的务必注意，申请了几个许可

## 2.Semaphore使用

申请多少信号量，记得释放多少信号量。

acquire()和release()，也被称为PV操作。如果计数器为1，就只有一个线程可以拿到该信号量，作用就类似于锁 ，如果计数器为10，就有10个线程可以拿到该信号量，进而执行下一步的操作。

下面我们再来分析一下,信号量是如何保证互斥的。假设两个线程T1和T2同时访问addOne)方法,当它们同时调用acquire)的时候,由于acquire)是一个原子操作,所以只能有一个线程(假设T1)把信号量里的计数器减为0,另外一个线程(T2)则是将计数器减为-1。对于线程T1,信号量里面的计数器的值是0,大于等于0,所以线程T1会继续执行;对于线程T2,信号量里面的计数器的值是-1,小于0,按照信号量模型里对down()操作的描述,线程T2将被阻塞。所以此时只有线程T1会进入临界区执行count+=1: .

当线程T1执行release)操作,也就是up()操作的时候,信号量里计数器的值是-1,加1之后的值是0,小于等于0,按照信号量模型里对up0)操作的描述,此时等待队列中的T2将会被唤·醒。于是T2在T1执行完临界区代码之后才获得了进入临界区执行的机会，从而保证了互斥性。

```java
public void acquire() throws InterruptedException   //获取信号量
public void release()   //释放信号量
```





## 3.Semaphore解析

Semaphorey底层依旧是通过AQS实现，其静态内部抽象类Sync实现类AQS中共享模式的主要方法，FairSync与NonfairSync继承自Sync，各自通过重写tryAcquireShared分别实现了公平模式与非公平模式。

**公平模式**：在申请**相同数量许可**的前提下，调用acquire的顺序就是获取许可的顺序；如果申请许可数量不同，那么信号量会根据等待队列中的顺序，优先满足申请数量小于等于空闲数量的线程。

**非公平模式**：在进入等待队列前尝试去获取许可，恰好此时有一个许可释放，并被该线程申请到，那么就不用进入等待队列了。

Semaphore 对应的两个构造方法如下：

```java
   public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }

    public Semaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }
```

在构造的时候必填permits，作为信号量中许可的初始化个数。fair选填，默认非公平模式。



### 3.1 申请许可

在之前学习中，我们更多关注的是工具类实现AQS的部分，没有从整体的角度来学习它，在后边的内容中，我们试着去了解AQS是如何设计的，是怎样通过简单的实现就可以自定义同步组件。

在我们调用 `semaphore.acquire()`后，Semaphore调用的是内部的 `sync.acquireSharedInterruptibly(1)` 如果调用的是 `acquire(int permits)` 则会调用 `sync.acquireSharedInterruptibly(permits)` 。

acquireSharedInterruptibly（）定义在AQS中，获取可响应中断的共享，代码如下：

```java
//AbstractQueuedSynchronizer
public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
    	//如果线程被中断了，抛出中断异常
        if (Thread.interrupted())
            throw new InterruptedException();
    	/*
    	下面这个方法名是不是很眼熟，在上一篇讲ReadLock部分时主要介绍的就是这个方法，
    	但两者的内容是完全不同的，各个工具类通过实现各自的tryAcquireShared来提供不同的功能。
    	tryAcquireShared方法表示尝试去获取，能成功是运气好，失败才是常态。
    	如果返回值<0，则尝试获取许可失败，执行doAcquireSharedInterruptibly(arg);
    	*/
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }
```



tryAcquireShared()在Semaphore中有公平模式和非公平模式两种实现。

```java
//非公平模式        
        final int nonfairTryAcquireShared(int acquires) {//要申请的许可个数
            for (;;) {
                //目前信号量中空闲的许可个数。
                int available = getState();
                //remaining表示经过当前操作后剩下空闲许可的个数，
                //remaining >= 0，表示可以满足当前线程申请的许可数，申请成功
                //remaining < 0，无法满足当前线程申请的许可数，申请失败
                int remaining = available - acquires;
                if (remaining < 0 ||
                    //AQS中的volatile变量state在不同的工具类中有不同的含义，
                    //在Semaphore中表示剩余的信号量。
                    compareAndSetState(available, remaining))
                    //返回剩余的许可
                    return remaining;
            }
        }
```



```java
//公平模式        
		protected int tryAcquireShared(int acquires) {
            for (;;) 
                /*
                公平模式与非公平模式一区别在于下边这行代码。
                公平就是前边有线程在等待的话，当前线程需要排队。
                hasQueuedPredecessors()用来判断是否需要排队
                */
                if (hasQueuedPredecessors())
                //如果需要排队，为了保证公平性，不进行尝试获取，直接返回
                    return -1;
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }
```

```java
//AbstractQueuedSynchronizer    
	public final boolean hasQueuedPredecessors() {
        /*
        检查信号量模型图中的等待队列，首节点是否是当前线程。
         ____          ____          ____
        | \\ |  -->   | t1 |  -->   | t2 |
        |___ |        |___ |        |___ |
         头节点	     首节点
        */
        Node t = tail; 
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }
```



至此**acquireSharedInterruptibly( arg)**中的**tryAcquireShared(arg)** 方法执行完成，返回值大于等于0，可表示尝试成功；小于0则尝试失败，会进入**doAcquireSharedInterruptibly(arg)**方法。

```java
//AbstractQueuedSynchronizer   
	/*
	该方法是AQS中尝试获取共享锁失败后的处理方法，上一篇中的ReadLock尝试
	获取读锁失败后，也会执行该方法中定义的逻辑，而且还会做额外的中间检查。
	*/
	private void doAcquireSharedInterruptibly(int arg)
        throws InterruptedException {
        //把当前线程封装为共享类型的节点添加至等待队列，
        //可以先看后边的代码块，了解如何添加节点后再回来看当前代码。
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```



```java
 //AbstractQueuedSynchronizer 
	//向等待队列中添加节点。
	private Node addWaiter(Node mode) {//前边传过来的节点类型 Node.SHARED
        //将当前线程封装为共享类型的节点。
        Node node = new Node(Thread.currentThread(), mode);
        //pred表示前驱节点，指向当前队列的尾节点。
        Node pred = tail;
        //当前队列尾节点不为空，表示当前队列不为空。
        if (pred != null) {
            //将当前线程节点的前驱节点指向尾节点。
            //高并发时，多个线程节点的前驱指向同一个尾节点，但最后CAS只能成功一个。
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                //CAS成功设置尾节点后，才将pred节点的后继指向确定后的尾节点。
                pred.next = node;
                return node;
            }
        }
        //当前队列尾节点为空，表示当前队列为空，此时则需要初始化等待队列。
        enq(node);
        return node;
    }
```



```java
 //AbstractQueuedSynchronizer 
	//初始化等待队列。
	private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
```



### 3.2 释放许可

释放许可也有几个重载方法，但都会调用下面这个带参数的方法，

public void release(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.releaseShared(permits);
    }

releaseShared方法在AQS中，如下：

public final boolean releaseShared(int arg) {
        //如果改变许可数量成功
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

AQS子类实现共享模式的类需要实现tryReleaseShared类来判断是否释放成功，实现如下：

protected final boolean tryReleaseShared(int releases) {
            for (;;) {
                //获取当前许可数量
                int current = getState();
                //计算回收后的数量
                int next = current + releases;
                if (next < current) // overflow
                    throw new Error("Maximum permit count exceeded");
                //CAS改变许可数量成功，返回true
                if (compareAndSetState(current, next))
                    return true;
            }
        }

从上面可以看到，一旦CAS改变许可数量成功，那么就会调用doReleaseShared()方法释放阻塞的线程。

减小许可数量
Semaphore还有减小许可数量的方法，该方法可以用于用于当资源用完不能再用时，这时就可以减小许可证。代码如下：

protected void reducePermits(int reduction) {
        if (reduction < 0) throw new IllegalArgumentException();
        sync.reducePermits(reduction);
    }

可以看到，委托给了Sync，Sync的reducePermits方法如下：

  final void reducePermits(int reductions) {
            for (;;) {
                //得到当前剩余许可数量
                int current = getState();
                //得到减完之后的许可数量
                int next = current - reductions;
                if (next > current) // underflow
                    throw new Error("Permit count underflow");
                //如果CAS改变成功
                if (compareAndSetState(current, next))
                    return;
            }
        }

从上面可以看到，就是CAS改变AQS中的state变量，因为该变量代表许可证的数量。

获取剩余许可数量
Semaphore还可以一次将剩余的许可数量全部取走，该方法是drain方法，如下：

public int drainPermits() {
        return sync.drainPermits();
    }

Sync的实现如下：

 final int drainPermits() {
            for (;;) {
                int current = getState();
                if (current == 0 || compareAndSetState(current, 0))
                    return current;
            }
        }

可以看到，就是CAS将许可数量置为0。