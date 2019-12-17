**前言**： 

在正式开始之前学习J.U.C之前，我们先来了解一下Java中的管程模型，尤其是对管程示意图的掌握，会极大的帮助我们理解AQS中的方法逻辑。

[TOC]

## 1.管程 

管程：管理共享变量以及对共享变量的操作过程，使其支持并发。对应的英文是Monitor，Java中通常被直译为监视器，操作系统中一般翻译为“管程”。

在并发编程中，有两大核心问题：一是互斥，即同一时刻只允许一个线程访问共享资源；二是同步，即线程之间如何通信、协作。对于这两个问题，管程都可以解决。

互斥很好理解，但同步可能就不那么好理解了，同步在不同的场景也有不同的含义，关于指令执行顺序中的同步是指代码调用I/O操作时，必须等待I/O操作完成才返回的调用方式。  在并发编程中的同步则指的是线程之间的通信和协作。最简单的例子就是生产者和消费者，如果没有商品，消费者线程如何通知生产者线程进行生产，生产商品后，生产者线程如何通知消费者线程来消费。

### 2.1 如何解决互斥

将共享变量以及对共享变量的操作 统一封装起来，如下图，多个线程想要访问共享变量queue，只能通过管程提供的enq()和deq()方法实现，这两个方法保持互斥性，且只允许一个线程进入管程。管程的模型和面向对象模型的契合度很高，这也可功能是Java一开始选择管程的原因（JDK5增加了信号量），互斥锁背后的模型其实就是它。

![](D:\study\Framework\Java\img\21-管程解决互斥.jpg)

### 2.2 如何解决同步

在管程模型中，共享变量和对共享变量的操作是封装起来的，图中最外层的框代表着封装，框外边的入口等待队列，当多个线程试图进入管程内部时，只允许一个线程进入，其他线程在入口等待队列中等待，相当于多个线程同时访问临界区，只有一个线程拿到锁进入临界区，其余线程在等待区中等待，等待的时候线程状态是阻塞的。

管程中还引入了**条件变量**的概念，而且每个条件变量都有一个**等待队列**，如下图所示，管程通过引入“条件变量”和“等待队列”来解决线程同步的问题。

结合上面提到的生产者消费者的例子，商品库存为空，或者库存为满，都是条件变量，如果库存为空，那么消费者线程会调用nofity()唤醒生产者线程，并且自己调用wait()进入“库存为空”这个条件变量的等待队列中。

同理，生产者线程会唤醒消费者线程，自己调用wait()进入“库存为满”这个条件变量的等待队列中，被唤醒后会到入口等待队列中重新排队获取锁。这样就能解决线程之间的通信协作。

![](D:\study\Framework\Java\img\22-管程解决同步.jpg)



### 2.3 管程发展史上出现的三种模型

Hasen模型：将notify()放到代码最后，当前线程执行完再去唤醒另一个线程。

 Hoare模型：中断当前线程，唤醒另一个线程执行，等那个线程执行完了，再唤醒当前线程。相比Hasen模型多了一次唤醒操作。

 MESA模型：当前线程T1唤醒其他线程T2，T1继续执行，T2并不立即执行，而是从条件队列进到入口等待队列中，这样没有多余的唤醒操作，notify也不用放最后，但是会有一个问题，T2再次执行的时候，曾经满足的条件，现在已经不满足了，所以需要循环方式校验条件变量。


```java
while(条件变量){
	wait();
}
```



## 2.Lock 

### 2.1 Lock接口的由来

之前提到并发编程的两大核心问题：**互斥**，即同一时刻只允许一个线程访问共享资源；**同步**，线程之间的通信、协作。JDK5之前管程由synchronized和wait，notify来实现。JDK5之后，在J.U.C中提供了新的实现方式，使用Lock和Condition两个接口来实现管程，其中Lock用于解决互斥，Condition用于解决同步。在Lock中维护一个“入口等待队列”，每个Condition中都维护一个“条件变量等待队列”。通过将封装后的线程对象在这两种队列中来回转移，来解决互斥和同步问题。

JDK5中synchronized性能不如Lock，但是在JDK6之后，synchronized做了很多优化，将性能追上来。所以并不是因为性能才提供了Lock和Condition这种管程的实现方式。而是synchronized的会自动加锁和释放锁，无法手动控制锁的释放，在很多情况下不够灵活。比如申请不到资源时，可以通过主动释放占有的资源，来通过破坏不可抢占条件。

而Lock接口就提供了更加灵活的方式来解决这个问题：

1. 能够响应中断。synchronized的问题是，如果获取不到锁，线程就会进入阻塞，并且无法响应中断信号，Lock接口提供了可以响应中断信号的加锁方式，这样就可以主动释放占有的资源，以达到破坏不可抢占条件。
2. 支持超时。如果线程在一段时间内没有获取到锁，不是进入阻塞，而是返回一个错误，同样会释放持有的资源，也可以达到破坏不可抢占条件。
3. 非阻塞地获取锁，如果尝试获取锁失败，并不进入阻塞状态，而是直接返回，也可以达到破坏不可抢占条件。

```Java
//支持中断的加锁
void lockInterruptibly() throws InterruptedException;
//支持超时的加锁
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
//支持非阻塞获取锁
boolean tryLock();
```

总的来说，显式的Lock对象在加锁和释放锁方面，相对于内建的synchronized锁来说，赋予更细粒度的控制。



### 2.2 ReentrantLock 原理

ReentrantLock在API层面实现了和synchronized关键字类似的加锁功能，而且在使用上更加灵活。其原理仅仅是利用了volatile相关的Happens-Before规则来保证可见性和有序性，通过CAS判断或修改锁的state状态来保证原子性。

ReentrantLock的具体实现则是使用AQS框架来完成的。其静态内部类Sync继承了AbstractQueuedSynchronizer，NonfairSync和FairSync继承Sync，各自重写了尝试加锁的tryAcquire方法。使ReentrantLock可以支持公平锁和非公平锁。

AbstractQueuedSynchronizer内部持有一个volatile的成员变量state，加锁时会读写state的值；解锁时也会读写state的值 。相当于用前后两次对volatile变量的修改操作，将共享变量的修改操作给包起来了。并通过传递性规与volatile规则共同保证可见性和有序性。

简化后的代码如下面所示：

```java
class SampleLock{
	volatile int state;
    // 加锁时必须先执行修改state的操作，再执行对共享变量进行操作
	lock(){
        state=1;
    //	...

    }
	unlock(){
	//	...
		state=0;
	}
    // 解锁时必须最后执行修改state的操作，对共享变量的操作要在之前完成，这样才能保证volatile规则
}
```



### 2.3 可重入

可重入锁，同一把锁可以在持有时再进行获取(synchronized也可以)，获取几次也必须要释放几次，不然会造成死锁 。

以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。但要注意，获取多少次就要释放多么次，这样才能保证state是能回到0状态。

```java
try {
    reentrantLock.lock();
    reentrantLock.lock();
} finally {
    reentrantLock.unlock();
}
// 由于加了两次锁，但只释放了一次，所以其他线程无法成功拿到锁，会进入阻塞。
```
贴出ReentrantLock的部分源码供大家参考，以下为非公平锁的尝试获取锁方法。

```java
final boolean nonfairTryAcquire(int acquires) {	// acquires = 1
            final Thread current = Thread.currentThread();
            int c = getState();
    		//c为0，可以理解为当前锁未被使用，那么当前线程就可以去竞争一下锁
            if (c == 0) {
                //竞争的过程就是使用CAS尝试去修改state状态
                if (compareAndSetState(0, acquires)) {
                    //成功则设置 独占锁的拥有线程 为当前线程
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
    		// 程序执行到这一步，有两种可能1:c==0但是竞争失败了，2:c!=0
    		// 第一种情况：锁没人用却竞争失败了说明竞争激烈，则线程需要进入等待队列中等待，就像多个人抢着出门，挤着谁都出不去，但只要有人在门口等待一下，有序撤离，这其实才是最快的方式。
    		// 第二种情况：c!=0，说明当前锁被使用，下边判断锁在谁手里，如果自己拿着则累加state，锁在别人手里，则和情况一样，进入等待队列中。
            else if (current == getExclusiveOwnerThread()) {
                // 判断为重入锁，对state进行累加，
                int nextc = c + acquires;
                // int的MAX为2147483647 再+1的话会溢出，不会变成2147483648，反而会变成-2147483647
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                // 因为只有当前持有锁的线程才能走到这里，所以此处并不需要使用CAS，
                setState(nextc);
                return true;
            }
            return false;
        }
```
以下为非公平锁的尝试释放锁方法。
```java
protected final boolean tryRelease(int releases) {// acquires = 1
    // 解一次锁减state减一次
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    // 只有减为0时，说明锁可以被其他线程获取，返回true，同时设置 独占锁的拥有线程 为当前线程
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```


### 2.4 公平锁与非公平锁

```java
public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```

ReentrantLock有两个构造函数，一个无参构造，一个需要传入boolean类型的fair，这个参数代表的就是公平策略，如果传入true，则会构造一个公平锁，也就是谁等的时间长，谁获得锁。默认构造的是非公平锁。

在管程模型中有一个入口等待队列，如果一个线程没有获取到锁，就会进入等待队列，当有线程释放锁的时候，就需要从等待队列中唤醒一个等待的线程，如果是公平锁的话，会唤醒等待时间最长的，非公平锁则不一定，有可能刚进入等待时间最短的反而被唤醒。

```java
public class MyReentrantLock5_公平锁 extends Thread {
    //设置为公平锁
    public static ReentrantLock lock = new ReentrantLock(true);

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //线程启动后先休眠1s，尽量保证量两个线程同时抢锁，不然可能t1拿锁放锁100次了，t2还没启动
            TimeUnit.SECONDS.sleep(1);
            for (int i = 0; i < 100; i++) {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获得锁");
                lock.unlock();
            }
        });
        Thread t2 = new Thread(() -> {
            TimeUnit.SECONDS.sleep(1);
            for (int i = 0; i < 100; i++) {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获得锁");
                lock.unlock();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
//Output
// 由于lock设置的是公平锁，所以可以看到t1和t2轮流获得锁。
```



> 一个写多线程测试代码需要注意的点，上面这段程序我是使用Junit提供的@Test注解来运行的，没有放在main方法中来跑，如果上边的代码定义在main中，则可以不用写最后两行代码“ t1.join() ; t2.join() ;”。
>
> @Test运行方式是在main方法中通过反射来执行test()方法，在执行test方法执行完后会立即退出，如果没有t1.join();将无法看到t1的打印结果。main方法中执行则会等待其中线程执行完成返回后再退出。

```java
//非公平锁
final void lock() {
    // 在加锁的时候就去尝试一下
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
//公平锁
final void lock() {
    acquire(1);
}

//非公平锁/公平锁
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (!hasQueuedPredecessors() &&	//公平锁比非公平锁多这一行判断，检查等待队列的首节点（head是头节点，head后边才是阻塞队列中保存的第一个节点）是不是当前线程，如果不是的话则需要去排队
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    ...
}
```







## 3.Condition

### 3.1 简介

Condition是一个接口，这个接口是为了结合ReentrantLock实现管程模型。再次搬出Java中的管程示意图。

![](D:\Study\Framework\Java\img\22-管程解决同步.jpg)

Lock与Condition这两者之间的关系可以参考synchronized和wait()/notify()。

Condition声明了一组等待/通知的方法，AbstractQueuedSynchronizer 中的ConditionObject内部类实现了这个接口。 通过API的方式来对ReentrantLock进行类似于wait和notify的操作 。

```java
// Codition方法
void await() throws InterruptedException;
boolean await(long time, TimeUnit unit) throws InterruptedException;
void signal();
void signalAll();
```



### 3.2 Condition原理

在 Condition 中, 维护着一个队列,每当执行 await 方法,都会根据当前线程创建一个节点,并添加到尾部.

然后释放锁,并唤醒阻塞在锁的 AQS 队列中的一个线程.

然后,将自己阻塞.

在被别的线程唤醒后, 将刚刚这个节点放到 AQS 队列中.接下来就是那个节点的事情了,比如抢锁.

紧接着就会尝试抢锁.接下来的逻辑就和普通的锁一样了,抢不到就阻塞,抢到了就继续执行.

通过在条件变量上调用等待方法，使拿到锁的线程释放锁，并进入条件变量等待队列中，等待被其他线程唤醒，唤醒后会从条件变量等待等列中移除，重新竞争锁，如果拿不到的话会进入入口等待队列，

```Java
//从前向后遍历，删除waitStatus!=Node.CONDITION的节点
private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }
```



## 4.总结

### 4.1 synchronized和ReentrantLock 的区别

|                      | synchronized | ReentrantLock |
| :------------------: | :----------: | :-----------: |
|     能够响应中断     |      N       |       Y       |
|       支持超时       |      N       |       Y       |
|    非阻塞地获取锁    |      N       |       Y       |
|        可重入        |      Y       |       Y       |
|      支持公平锁      |      N       |       Y       |
|    获取锁/释放锁     |     自动     |     手动      |
|      发生异常时      |  自动释放锁  | 需手动释放锁  |
| **支持多个条件变量** |      N       |       Y       |

**synchronized 依赖于 JVM 而 ReentrantLock 依赖于 API**

synchronized 是依赖于 JVM 实现的，JDK6 为 synchronized 关键字进行了很多优化，这些优化都是在虚拟机层面实现的。ReentrantLock 是 JDK 层面实现的，也就是 API 层面，需要 lock() 和 unlock() 方法配合 try/finally 语句块来完成。

**ReentrantLock可以支持多个条件变量**

通过synchronized关键字与wait()和notify()/notifyAll()方法相结合实现的管程，其内部只能通过调用锁定对象的wait()和notify()进行线程间通信。假设有一个生产者多个消费者，消费者在消费完后需要通知生产者进行生产，但由于生产者和其他消费者都在synchronized锁定的同一个对象上wait。

调用notify随机唤醒的话，可能会唤醒的消费者，也可能唤醒生产者，如果唤醒生产者则可以进行生产，如果被唤醒的是消费者，那么该消费者还是会由于没有库存继续等待，如果消费者的数量远远多于生产者，那么会一直出现消费者唤醒其他消费者的现象，生产者不会被唤醒，则程序无法继续执行下去；

调用notifyAll方法的话，可以解决这个问题，但也带来另一个问题。唤醒全部消费者的同时也会唤醒全部生产者，会带来很大的性能开销。

因此如果有一种方式能将生产者和消费者分离开，支持区分类型的唤醒，那这个问题就迎刃而解了。

通过Lock和Condition实现的管程对这一问题进行了解决，之前开头的时候提过，Lock解决互斥，Condition解决同步，通过ReentrantLock对象的newCondition()方法，可以在锁定对象上绑定多个条件变量，也就是一个Lock对象中可以创建多个Condition实例。

线程对象可以注册在指定的Condition中，从而可以有选择性的进行线程通知，在调度线程上更加灵活。Condition实例的signalAll()方法 只会唤醒注册在该Condition实例中的所有等待线程。

```java
Lock lock = new ReentrantLock();
Condition providers = lock.newCondition();
Condition consumer = lock.newCondition();
...
// 唤醒所有生产者
providers.signalAll();	
// 唤醒所以消费者
consumer.signalAll();    
```





> 在本人学习这一部分内容时，也对AQS源码进行了阅读，大致的流程很容易走下来，但是在流程背后的一些设计细节，却不知其所以然。因此在本篇中没有对整个AQS原理进行详细的介绍，学习是一个逐渐深入的过程。有的东西需要周期反复的思考才能理解透彻。

 

## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《Java 编程思想(第4版)》  
&emsp;&emsp;https://time.geekbang.org/column/intro/159
&emsp;&emsp;https://juejin.im/post/5ae75505518825673027eddf
&emsp;&emsp;http://www.tianxiaobo.com



