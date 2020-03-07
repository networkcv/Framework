**前言：**

通过前面对Semaphore的学习，我们了解了AQS框架中共享模式下的基本流程，本篇再去看看另一种共享模式的实现——CountDownLatch，以及功能和它类似的CyclicBarrier。

[TOC]

**面试问题**

Q ：CountDownLatch 类中主要的方法？

Q ：CountDownLatch和CyclicBarrier的区别？





## 1.CountDownLatch

### 1.1 CountDownLatch介绍

**CountDownLatch** ，闭锁，门闩，也可以理解为倒计时器。

比如有这样一个场景，火箭发射前需要确保各个部件正常，如果存在异常，则火箭无法发射。

使用多线程同时对火箭的各个部分进行检查，检查完毕后，主线程才会执行火箭发射操作。

火箭发射线程等待其他检查线程执行完毕，可以抽象为一个线程等待多个线程的场景。

在没有CountDownLatch的时候，我们可以通过定义一个倒计时器搭配synchronized和wait()/notify()来实现。

```java
	int countDown = 5;
	@Test
    public void test() throws InterruptedException {
        Object lock = new Object();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + " 检查完毕!");
                    countDown--;
                    if (countDown == 0) {
                        lock.notify();
                    }
                }
            }, i + "号线程").start();
        }
        while (countDown != 0) {
            synchronized (lock) {
                lock.wait();
            }
        }
        System.out.println("火箭发射！！");
    }
```

为了保证`countDown-- `操作的原子性，在线程检查的时候使用了synchronzied关键字进行加锁，如果不加锁的话，countDown可能会因为线程切换导致原子性问题，但是加锁之后，从多线程同时做检查变成了某一时刻只能有一个线程检查，无疑大大地降低了工作效率。有没有一个两全其美的办法？还真有，使用volatile就可以实现。

```java
//线程不安全的
	volatile int state = 50000;
    @Test
    public void test1() {
        for (int i = 0; i < 50000; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " 检查完毕!");
                state--;
            }, i + "号线程").start();
        }
        while (state != 0) {}
        System.out.println("火箭发射！！");
    }
```

上边的代码有两个问题：

1. 虽然使用volatile修饰state，使其修改结果对其他线程可见，并且不会因为重排序导致导致线程安全问题，但是volatile不能保证`state--`操作的原子性，发生原子性问题时，会使countDown无法减为0，自然发射火箭的主线程也永远无法被唤醒。
2. 主线程一直在不停的检查state有没有被减为0，这样会降低程序的性能。

解决办法：

1. 使用CAS操作自旋来修改state，解决原子性问题。
2. 先让主线程尝试一次，如果失败的话就挂起，进入等待状态，由将state修改为0的线程来唤醒它。

如果你看了前边几篇文章，你会发现上边的逻辑很熟悉，AQS最根本的原理就是这样的，并且J.U.C中为了解决这类问题，已经帮我们做了实现——CountDownLatch。

### 1.2 CountDownLatch使用

先来来看看CountDownLatch中的方法。

```java
//构造初始的state值，可以执行state次countDown
public CountDownLatch(int count) 

//等待直到被唤醒
public void await() throws InterruptedException

//等待一段时间，超时自己醒
public boolean await(long timeout, TimeUnit unit)

//对state进行-1    
public void countDown() 
    
//获取当前的state    
public long getCount()
```



**使用方式**

CountDownLatch主要用来解决一个线程等待多个线程的场景。

下面使用CountDownLatch来实现火箭发射的场景。

```java
  	@Test
    public void test2() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " 检查完毕!");
                cdl.countDown();
            }, i + "号线程").start();
        }
        cdl.await();
        System.out.println("火箭发射！！");
    }
	/*	Output
		1号线程 检查完毕!
        3号线程 检查完毕!
        4号线程 检查完毕!
        0号线程 检查完毕!
        2号线程 检查完毕!
        火箭发射！！
	*/
```

`countDown() `就是用CAS进行`state--` 减到0的线程会去通知等待在CountDownLatch上的节点，只通知首节点。

`await()` 则是先尝试，尝试失败后进入等待队列，别唤醒后还会通知后边的节点，以此类推，以传播的方式唤醒等待队列中的所有节点。



**使用场景**

1. 前面提到了倒计时器的用法，本质上就是为了等其他线程执行完，可以用在启动服务时，等待其他组件的加载。
2. 可以使多个线程同时开始执行，提供很大的并行性，这里更关注的是线程到齐之后。平时在主线程启动线程时，由于代码顺序执行的缘故，线程并不是真正同时start的，中间存在时间差，如果执行的耗时特别短，那么可能很多问题不会暴露出来。通过让多个先后启动的测试线程在CountDownLatch上进行等待，等到所有测试线程都启动完毕再统一释放。这里的CountDownLatch起到了和Thread.yield类似的作用，可以增大出现并发问题的几率。因此可以写多个线程来进行死锁的检测。



### 1.3 CountDownLatch原理

CountDownLatch的原理其实并不难，不难是建立对AQS有了解之上的。

在介绍时简单提了一下原理，下面看一看源码的实现细节吧。

构造设置state。

```java
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

	Sync(int count) {
        setState(count);
    }

    protected final void setState(int newState) {
        state = newState;
    }
```

**await()**

这次我们看一个限时的尝试。

```java
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }
```

将等待的时间转换为纳秒，调用 `tryAcquireSharedNanos(int arg, long nanosTimeout)`。

```java
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
            doAcquireSharedNanos(arg, nanosTimeout);
    }
```

`tryAcquireShared()`只检查当前的state是否被减为0

```java
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }
```

重点在 `doAcquireSharedNanos(arg, nanosTimeout)。`  
这个方法和Semphore中看过的`doAcquireShared(int arg)`很相像，不同在于该方法中加入了超时退出逻辑。这次我们关注于超时退出逻辑，想更深入了解该方法的，可以移步至 [Semaphore & AQS](https://segmentfault.com/a/1190000021400650)。


```java
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        //记录在哪个时间点超时
        final long deadline = System.nanoTime() + nanosTimeout;
        //添加到等待队列
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                //获取当前节点的前驱节点
                final Node p = node.predecessor();
                if (p == head) {
                    //检查state是为0，是0返回1，不是返回-1
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        //将当前节点设置为头节点，并向后唤醒
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                //判断是否超时，超时则返回false
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                //判断是否挂起当前线程，其实就是将前驱节点的waitStatus设置为SIGNAL
                if (shouldParkAfterFailedAcquire(p, node) &&
                    //如果要等待的时间小于1000L纳秒，则线程不会被挂起，
                    nanosTimeout > spinForTimeoutThreshold)
                    //挂起当前线程
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

**countDown()**

```java
//CountDownLatch
	public void countDown() {
        sync.releaseShared(1);
    }
```

```java
//AQS   
	public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }
```

```java
//CountDownLatch	
	//这个方法就是变相的保证了state--操作的原子性
	protected boolean tryReleaseShared(int releases) {
        for (;;) {
            int c = getState();
            if (c == 0)
                return false;
            int nextc = c-1;
            if (compareAndSetState(c, nextc))
                return nextc == 0;
        }
    }
}
```

```java
//AQS 
	private void doReleaseShared() {
        for (;;) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    //将头节点的waitStatus从SIGNAL设置为0
                    //不成功则自旋，成功会执行唤醒操作
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    //唤醒后继节点
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                         !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            if (h == head)                   // loop if head changed
                break;
        }
    }
```



## 2.CyclicBarrier

### 2.1 CyclicBarrier介绍

通过前面的内容，我们大致了解了CountDownLatch的使用方法及使用场景。

现在模拟一个新的应用场景，LOL或者王者荣耀大家都玩过吧，在点了开始游戏按钮后，会进到一个等待队列中，系统根据匹配机制找到10个旗鼓相当的玩家，然后才正式开始游戏。

开始游戏的主线程需要等待计算玩家水平的10个线程完成后才执行，第一反应是使用CountDownLatch，但是CountDownLatch有个问题是只能使用一次，减到0后就没办法再修改state了，而在等待队列中的游戏玩家有很多，每10个玩家就需要创建一个CountDownLatch对象，这样可能会频繁GC，所以可以考虑复用倒计时器对象，减少对象创建销毁带来的性能损耗。

CountDownLatch还有一个问题是无法执行回调，比如之前那个火箭发射的场景，检查完毕后火箭自己就发射了，但是我们无法得知。如果完成检查后执行一个回调函数，来通知我们执行结果，那就好了。

这两点**CyclicBarrier**已经实现了。

- 可以循环使用
- 可以执行回调函数

CyclicBarrier字面意思循环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。当所有等待线程都被释放以后，CyclicBarrier可以被重用。并且最后一个到达的线程还会执行CyclicBarrier中的回调函数。

### 2.2 CyclicBarrier使用

先看构造，`parties`，是指parties个线程到达后栅栏才会打开一次。

```java
    public CyclicBarrier(int parties) 
```

`barrierAction`则是指当这些线程都到达后会执行的内容，具体是由最后一个到达的线程来执行。

```java
	public CyclicBarrier(int parties, Runnable barrierAction)
```

```java
    @Test
    public void test2() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println(Thread.currentThread().getName() + " print all is ok");
        });
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is ok!");
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, i + "号玩家").start();
        }

        TimeUnit.SECONDS.sleep(2);
    }
	/*  Output
            1号玩家 is ok!
            3号玩家 is ok!
            2号玩家 is ok!
            0号玩家 is ok!
            4号玩家 is ok!
            4号玩家 print all is ok
	 */

```

可以被中断的等待操作，用来挂起当前线程，直至所有线程都到达再同时执行后续任务；　

```java
	public int await() throws InterruptedException, BrokenBarrierException 
```

```java
 	@Test
	public void test() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(finalI + " is ok!");
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + " 加入游戏");
            }).start();
        }
        TimeUnit.SECONDS.sleep(2);
    }
	/*  Output
            2 is ok!
            1 is ok!
            3 is ok!
            4 is ok!
            0 is ok!
            4 加入游戏
            0 加入游戏
            1 加入游戏
            3 加入游戏
            2 加入游戏
	*/
```

线程等待一段的时间后，如果还有线程没有到达，就直接让到达的线程执行后续任务。

```java
	public int await(long timeout, TimeUnit unit)
```

```java
    @Test
    public void test3() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println("五个玩家到齐了，开始游戏");
        });
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is ok!");
                    barrier.await(100,TimeUnit.MILLISECONDS);
                    System.out.println(Thread.currentThread().getName() +" 开始游戏");
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    System.out.println(Thread.currentThread().getName()+" 不等了");
                }
            }, i + "号玩家").start();
        }
        TimeUnit.SECONDS.sleep(2);
    }
	/*  Output
            0号玩家 is ok!
            1号玩家 is ok!
            2号玩家 is ok!
            3号玩家 is ok!
            3号玩家 不等了
            0号玩家 不等了
            2号玩家 不等了
            1号玩家 不等了
	*/
```

主要使用的是上边这四个，下边还有四个其他方法：

返回当前在等待的线程数

```java
    public int getNumberWaiting() 
```

返回该CyclicBarrier每批阻拦的最大线程数

```java
  	public int getParties() 
```

判断当前这一轮线程的屏障状态有没有被打破

```java
	public boolean isBroken() 
```

强制重置屏障，使屏障进入新一轮的运行过程中

```java
    public void reset() 
```





### 2.3 CyclicBarrier原理

CyclicBarrier并没有直接使用AQS，而是借助ReentrantLock和Condition实现的。

```java
public class CyclicBarrier {
    private static class Generation {
        //记录屏障是否被破坏
        boolean broken = false;
    }

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition trip = lock.newCondition();
    
    //线程数，即当 parties 个线程到达屏障后，屏障才会放行。
    private final int parties;
    
    //回调方法，如果不为空的话，在将count减为0的线程中执行。
    private final Runnable barrierCommand;
    
    //这一批线程的屏障状态
    private Generation generation = new Generation();

    //计数器，当 count > 0 时，到达屏障的线程会进入等待状态。当最后一个线程到达屏障后，
    //count 自减至0。最后一个到达的线程会执行回调方法，并唤醒其他处于等待状态中的线程。
    private int count;

```



核心方法`await()`

```java
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }
```

```java
   //参数timed 表示是否有等待时间限制，参数nanos 表示最大等待的纳秒数
	private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
		//为了保证 --count 操作的原子性，此时使用Lock加锁
        lock.lock();
        try {
            final Generation g = generation;
		   //如果屏障被破坏，则抛出异常，锁也会随之释放。
            if (g.broken)
                throw new BrokenBarrierException();
			
            /*
             * 如果当前线程被中断，会释放Condition上等待的线程，重置count
             * 将屏障状态设置为被破坏，并抛出中断异常
             */
            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }
			
            /*
             * count--，如果当前线程将count减为0，则先执行回调方法，
             * 再去调用breakBarrier()来开启下一批拦截
             */
            int index = --count;
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        breakBarrier();
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            for (;;) {
                try {
                    if (!timed)
                        trip.await();
                    else if (nanos > 0L)
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    /*
                     * 若下面的条件成立，则表明本轮运行还未结束。此时调用 breakBarrier 
                     * 破坏屏障，唤醒其他线程，并抛出异常
                     */ 
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        /*
                         * 若上面的条件不成立，则有两种可能：
                         * 1. g != generation
                         *     此种情况下，表明循环屏障的第 g 轮次的运行已经结束，屏障已经
                         *     进入了新的一轮运行轮次中。当前线程在稍后返回 到达屏障 的顺序即可
                         *     
                         * 2. g = generation 但 g.broken = true
                         *     此种情况下，表明已经有线程执行过 breakBarrier 方法了，当前
                         *     线程则会在稍后抛出 BrokenBarrierException
                         */
                        Thread.currentThread().interrupt();
                    }
                }
				
                // 屏障被破坏，则抛出 BrokenBarrierException 异常
                if (g.broken)
                    throw new BrokenBarrierException();
			
                // 屏障进入新的运行轮次，此时返回线程在上一轮次到达屏障的顺序
                if (g != generation)
                    return index;
	
                // 超时判断
                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }
```





### 2.4 小结

`CyclicBarrier` 内部通过一个 count 变量作为计数器，cout 的初始值为 parties 属性的初始化值，每当一个线程到了栅栏这里了，先使用ReentrantLock加锁，再对计数器减一，随后在Lock的condition上挂起，如果 count 被减为 0 了，表示这是这一批最后一个线程到达栅栏，就在当前线程执行回调方法，并调用condition对象的`signalAll()`方法唤醒其他等待中的线程，一起执行后续的任务。



## 3.总结

### CyclicBarrier与CountDownLatch的比较

|            | CountDownLatch | CyclicBarrier |
| :--------: | :------------: | :-----------: |
| 可循环使用 |       否       |      是       |
| 可设置回调 |       否       |      是       |

CountDownLatch：一个或多个线程等待另外N个线程完成某个事情之后才能继续执行。

CyclicBarrier：N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。

CountDownLatch是计数器，线程完成一个就计一个，就像报数一样，只不过是递减的。
CyclicBarrier更像一个水闸，线程执行就像水流，在水闸处就会堵住，等到水满（线程到齐）了，才开始泄流。

**最后**

对J.U.C的学习到此暂时就结束了，后边遇到问题时，再回过头来看看，说不定有新的收获。

下面会对并发级别、无锁、同步容器、并发容器、线程池，以及多线程的设计模式等来进行学习总结。



## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《Java 编程思想(第4版)》  
&emsp;&emsp;https://time.geekbang.org/column/intro/159
&emsp;&emsp;https://snailclimb.gitee.io/javaguide/#/
&emsp;&emsp;https://www.cnblogs.com/dolphin0520/p/3920397.html

**感谢阅读**！