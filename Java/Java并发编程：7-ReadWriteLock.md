**前言：**

上一篇我们了解了Lock接口与Condition接口。本篇来看看J.U.C中的ReadWriteLock，再次膜拜一下Doug Lea大神的杰作。

[TOC]

**面试问题**
Q ：谈谈ReadWriteLock的好处？



## 1.ReadWriteLock简介

ReadWriteLock接口是在JDK5提供的，具体的实现类为ReentrantReadWriteLock，还有一个实现类ReadWriteLockView，是StampedLock的内部类。后边会有讲到。

```Java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```

ReadWriteLock直译为读写锁，从接口命名上就可以看出该工具类用于特定的场景下，前面讲过的ReentrantLock和synchronized基本可以用来解决一切并发问题，但在特定的场景下可能表现的效果不那么令人满意，如在读多写少的时，大部分线程都在进行读操作，很少有线程会修改共享数据。但由于加锁的特性，导致大量的读操作进行了不必要的锁竞争，如果能将读写的锁分离，有写操作的时候，进行读操作需要加锁；没有写操作的时候，可以多个线程同时进行读操作。这样势必会提升性能。

读写锁便是解决这种场景问题的。读写锁有三个基本原则：

1. 允许多个线程同时读共享变量
2. 只允许一个线程写共享变量
3. 如果一个写线程正在执行，此时禁止读线程读共享变量，如果一个线程在读，同样也禁止写共享变量。

## 2.ReentrantReadWriteLock使用

### 2.1 锁降级

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

ReentrantReadWriteLock<font color=Crimson>不支持锁的升级</font>，但是<font color=dodgerblue>支持锁的降级</font>。锁降级就是持有写锁去申请读锁；锁升级是持有读锁去申请写锁，如果出现类似锁升级的代码，则会导致线程阻塞，且无法被唤醒。这点需要注意。

锁的升级：

```java
 	ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();  
	@Test
    //无法进行锁的升级，从读变成写
    public void test2() throws InterruptedException {
        Thread thread = new Thread(() -> {
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");
            
            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");
            
            readWriteLock.writeLock().unlock();
            System.out.println("释放写锁");
            
            readWriteLock.readLock().unlock();
            System.out.println("释放读锁");
        });
        thread.start();
        thread.join();
    }
	/*Output
        获取读锁
        ----- 发生阻塞----- 必须先释放读锁才能去申请写锁，不然会阻塞
```

锁的降级：

```java
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    @Test
    //进行锁的降级，从写变成读
    public void test() throws InterruptedException {
        Thread thread = new Thread(() -> {
            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");
            
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");
            
            readWriteLock.readLock().unlock();
            System.out.println("释放写锁");
            
            readWriteLock.writeLock().unlock();
            System.out.println("释放读锁"); 
        });
        thread.start();
        thread.join();
    }
	/*Output
        获取写锁
        获取读锁
        释放写锁
        释放读锁
	*/
```



锁降级还是有很多应用场景的。比如有业务需要先查缓存，发现缓存失效需要重新去数据库查询数据并修改缓存，完成修改操作后应该尽快释放写锁，减小锁的粒度。这样能让更多的读线程尽快访问到修改后的数据。不然业务逻辑半天执行不完，这期间尽管缓存数据是最新的，但是由于写锁未释放，其他线程也无法进行读操作。极大的降低了并发性。

因此在完成修改缓存后去拿读锁，然后释放写锁，这样既能保证其他线程读取到最新的数据，又能保证当前线程的后续操作使用的数据是最新的。

```java
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    final Lock r = readWriteLock.readLock();
    final Lock w = readWriteLock.writeLock();
    volatile boolean cacheValid;

    @Test
    public void processCachedData() {
        r.lock();           //获取读锁
        if (!cacheValid){   //缓存失效
            r.unlock();     //先释放读锁，不允许锁升级
            w.lock();
            try {
                if (!cacheValid){   //再次检查缓存状态，
//                  cache= ...
                    cacheValid =true;   //完成缓存更新
                }
                r.lock();   //释放写锁前，降级为读锁
            }finally {
                w.unlock(); //释放写锁
            }
        }
        try{
//            ...     //执行业务逻辑
        }finally {
            r.unlock();
        }
    }
```

### 2.2 写锁支持条件变量

<font color=cirmson>读锁不支持条件变量</font>，如果读锁调用newCondition（）会抛出UnsupportedOperationException异常；

<font color=dodgerblue>写锁支持条件变量</font>，锁与条件变量的搭配使用可以参考上一篇 [Lock & Condition]()。



## 3.ReentrantReadWriteLock原理

ReentrantReadWriteLock内部还是使用的AQS框架，通过前面的学习我们知道，在AQS中，通过`volatile int state `来表示线程锁的状态，ReentrantReadWriteLock有两把锁：读锁和写锁，它们保护的都是同一个资源，如何用一个共享变量来区分写锁和读锁的状态呢？答案就是按位拆分。

![](D:\study\Framework\Java\img\30-读写锁的state拆分.jpg)

由于state是int类型的变量，在内存中占用4个字节，也就是32位。将其拆分为两部分：高16位和低16位，其中高16位用来表示读锁状态，低16位用来表示写锁状态。这样就可以用一个int变量来表示两种锁的状态，低16位写锁的加锁和释放锁操作不会发生变化，仍是state+1/state-1；但高16位的加锁和释放锁就变成了state +  (1<<16)/ state-(1<<16)。

同样获取读锁和写锁的状态也有所不同：

获取读锁：`state >>> 16`	无符号右移16位，空的地方补0。

获取写锁：`state  & [(1 << 16) - 1]` 相当于state & 0x0000FFFF  相当于把高16位置空，只保留低16位。

由于读锁和写锁的状态值都只占用16位，所以读锁和写锁各自可重入锁的最大数量为2^16-1。



前面说了持有锁状态表示的问题，现在来看看其具体的实现。在此之前先了解一下ReentrantReadWriteLock的类结构。

ReentrantReadWriteLock中有两个内部类，ReadLock和WriteLock，这两个类在具体实现Lock接口时，分别调用ReentrantReadWriteLock中实现AQS类的同步组件Sync的共享和独占两种加锁释放锁方式来实现各自的功能。

Sync中实现AQS中独占锁加锁tryAcquire（）和独占锁释放锁tryRelease（），以及共享锁的加锁tryAcquireShared（）和共享锁的释放锁tryReleaseShared（）。

下面的内容也是围绕这四个方法展开。

### 3.1 写锁加锁

```java
protected final boolean tryAcquire(int acquires) {
            /*
             * Walkthrough
             * 1. 如果读锁或者写锁其中有一个不为0，而且锁的持有者不是线程，
             *	  尝试获取锁失败，该方法返回false。
             * 2. 如果加锁的数量满了，返回false。
             * 3. 另外，重复获取或者入口等待队列允许的线程才有资格加锁，
             *    修改state和锁的持有者
             */
            Thread current = Thread.currentThread();
    		//返回state，高16代表读锁的数量，低16代表写锁的数量。
            int c = getState();
    		//返回独占锁(也就是写锁)的数量
            int w = exclusiveCount(c);
    		//只有读锁和写锁有一个数量不为0，state就不为0，如果state为0，
    		//那么没有一个线程当前在使用读写锁，可以直接让当前线程去拿写锁
            if (c != 0) {
                /*
                1.写锁数量为0，且独占锁不是被当前线程占用，那么一定是读锁的数量不为0，
                  说明读锁在使用，尝试获取锁失败，对应前边提到读写锁的基本原则：
                  如果一个线程在读，同样也禁止写共享变量。
               	  
                2.写锁不为0，但是独占锁的持有线程不是当前线程，说明其他线程在使用写锁
                  独占锁是互斥的，所以也会返回false。
                */
                if (w == 0 || current != getExclusiveOwnerThread())
                    return false;
                //判断一下重入锁的次数会不会超过2^16-1
                if (w + exclusiveCount(acquires) > MAX_COUNT)
                    throw new Error("Maximum lock count exceeded");
                /* 这里设置state时没有使用CAS，原因很简单，能走到这里的必然是
                 	已经持有独占锁的线程来重入锁，其他情况无法通过前边的状态判断。
                */
                setState(c + acquires);
                return true;
            }
    		/*state=0才能走到此处，writerShouldBlock()是用来判断是否需要排队
    		非公平锁：
    			直接返回false，然后使用CAS来修改state，如果修改成功则说明拿到锁了
    			那么可以继续将独占锁的持有线程设置为当前线程。
    		公平锁：
    			公平锁会调用hasQueuedPredecessors()，这个方法是判断入口等待队列
    			中是否有线程在等待锁。如果没有线程等待或者等待队列中排在最前边的是当前
    			线程，那么可以继续进行后边的CAS操作，否则返回false。
    		*/
            if (writerShouldBlock() ||
                !compareAndSetState(c, c + acquires))
                return false;	//返回false的后续操作在下边
            setExclusiveOwnerThread(current);
            return true;
        }
```



下面附上tryAcquire()的流程图：
![](D:\study\Framework\Java\img\31-写锁加锁流程.jpg)

tryAcquire返回false的后续操作。

```java
        public final void acquire(int arg) {
                //如果tryAcquire()返回false，会继续往下执行。
            if (!tryAcquire(arg) &&
                //这行代码是在AQS中实现的，具体步骤为把当前线程封装为一个独占锁节点，
                //并加入到等待队列中，然后将当前线程阻塞
                //在阻塞前还会当前线程还会再尝试一次，是否能获取到锁，
                //这次尝试时，当前线程已经在等待队列中了，这个是acquireQueued()的内容。
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
                selfInterrupt();
        }
```



### 3.2 写锁释放

```java
         protected final boolean tryRelease(int releases) {//realeases=1
            //判断当前线程是否持有锁
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
             //realease是这次调用要释放持有的锁的个数，比如之前重入了3次，
             //getState()返回3，releases=1，则本次释放1次，后边还需要再释放2次
             //只有state为0时才算彻底释放锁，独占锁的所有线程才会置为null
            int nextc = getState() - releases;
            //检查是否是彻底释放锁
            boolean free = exclusiveCount(nextc) == 0;
             //是彻底释放锁
            if (free)
                //独占锁的所有线程才会置为null
                setExclusiveOwnerThread(null);
             //同样没用CAS，因为 if (!isHeldExclusively()),只有当前线程可以通过
            setState(nextc);
            return free;
        }
```



### 3.3 读锁加锁

```java
protected final int tryAcquireShared(int unused) {
          Thread current = Thread.currentThread();
            int c = getState();
    		//如果不是当前线程占用写锁则会返回-1，表示共享锁加锁失败
    		//如果是当前线程占用写锁，再申请读锁，则是被允许的，这是锁降级的过程。
            if (exclusiveCount(c) != 0 &&
                getExclusiveOwnerThread() != current)
                return -1;
    		//获取读锁的数量
            int r = sharedCount(c);
    		//这里又是公平锁和非公平的一个区别。
            if (!readerShouldBlock() &&		//判断是否需要阻塞
                r < MAX_COUNT &&
                compareAndSetState(c, c + SHARED_UNIT)) { //读锁+1
                //r=0，代表读锁没被占用，下边的操作对应写(独占)锁的话则是
                //设置写锁的持有者为当前线程，但是读锁是共享的，所以不能这样设置
                if (r == 0) {
                    /*共享锁会为每个获取ReadLock的线程创建一个HoldCounter来记录该线程
                    的线程ID和获取ReadLock的次数(包括重入)。并将这个HoldCounter对象
                    保存在线程自己的ThreadLocal中。
                    ThreadLocalHoldCounter readHolds;
                    HoldCounter cachedHoldCounter;
                    
                    static final class ThreadLocalHoldCounter
                        extends ThreadLocal<HoldCounter> {
                        public HoldCounter initialValue() {
                            return new HoldCounter();
                        }
                    }
                    static final class HoldCounter {
                        int count = 0;
                        // Use id, not reference, to avoid garbage retention
                        final long tid = getThreadId(Thread.currentThread());
				  }
                    
                    设计者考虑到有些场景只有一个线程获取读锁，那么使用ThreadLocal
                    反而会降低性能，所以在ReentrantReadWriteLock中定义了：
                    private transient Thread firstReader = null;
                    private transient int firstReaderHoldCount;
                    来提供只有一个线程获取读锁的性能保障。
                    */
                    firstReader = current;
                    firstReaderHoldCount = 1;
                } else if (firstReader == current) {
                    //当前线程重入读锁
                    firstReaderHoldCount++;
                } else {
                    //多个线程来申请读锁时会到这一步,默认会从cachedHoldCounter中拿
                    HoldCounter rh = cachedHoldCounter;
                    //如果rh.tid == getThreadId(current)，说明这个线程连续两次来拿读锁
                    //如果不等的话,则说明缓存失效了,需要重新从ThreadLocal取出HoldCounter
                    //顺便修改缓存
                    if (rh == null || rh.tid != getThreadId(current))
                        cachedHoldCounter = rh = readHolds.get();
                    //上一次拿读锁的是别的线程，这个线程是第一次来拿读锁
                    else if (rh.count == 0)
                        readHolds.set(rh);
                    rh.count++;
                }
                //获取读锁成功
                return 1;
            }
    		//在readerShouldBlock()返回true时，或者CAS修改失败时走到这里
    		//在这个方法中会用自旋的方式一直获取读锁，中途写锁被其他线程持有会返回-1
            return fullTryAcquireShared(current);
        }
```

下面附上tryAcquireShared()的流程图：

![](D:\Study\Framework\Java\img\32-读锁加锁流程.jpg)

### 3.4 读锁释放

```java
protected final boolean tryReleaseShared(int unused) {
            Thread current = Thread.currentThread();
    		/*
    		前边介绍了firstReader、firstReaderHoldCount
    		和cachedHoldCounter、readHolds，这里就很容易理解了
    		减少线程持有读锁个数，如果彻底释放读锁，那么还会将
    		firstReader或readHolds置空。
    		*/
            if (firstReader == current) {
                // assert firstReaderHoldCount > 0;
                if (firstReaderHoldCount == 1)
                    firstReader = null;
                else
                    firstReaderHoldCount--;
            } else {
                HoldCounter rh = cachedHoldCounter;
                if (rh == null || rh.tid != getThreadId(current))
                    rh = readHolds.get();
                int count = rh.count;
                if (count <= 1) {
                    readHolds.remove();
                    if (count <= 0)
                        throw unmatchedUnlockException();
                }
                --rh.count;
            }
    		//这里是自旋CAS释放读锁的操作, 因为可能其他的线程此刻也在进行 release操作
            for (;;) {
                int c = getState();
                //读锁是高16位，所以这里通过c-(1<<16)来释放读锁
                int nextc = c - SHARED_UNIT;
                if (compareAndSetState(c, nextc))
                    /*                    
                    这里是判断读锁有没有彻底被释放，如果完全释放，后边则会以传播的方式
                    唤醒后继节点中共享类型的节点，成功获取读锁时也会执行唤醒后续共享类型节点
                    */
                    return nextc == 0;
            }
        }
```



前面提到的四个方法，是Doug Lea大神留给我们的主要的发挥空间，AQS中其他核心方法都无法重写。如果需要实现自定义的同步组件，那么对这四个方法必然要深入理解，然后根据组件特性来实现独占锁或共享锁。如ReentrantLock是独占锁，所以其内部只实现了tryAcquire（）和tryRelease（）。因此本篇着重介绍了ReentrantReadWriteLock实现AQS的具体细节，没有从AQS框架整体上展开，有些地方可能不太好理解，还希望大家多多谅解。




## 4.总结



在ReentrantReadWriteLock中实现了独占锁和共享锁两种方式，读锁是共享的，可以在没有写锁的时候被多个线程同时持有，并发地读数据；写锁是独占的，每次只能被一个线程持有，其他线程要想修改共享数据，则需要排队等待。获得了读锁的线程能够看到前一个释放的写锁所更新的内容。

理论上，读写锁比互斥锁允许对于共享数据更大程度的并发。与互斥锁相比，读写锁是否能够提高性能取决于读写数据的频率、读取和写入操作的持续时间以及读线程和写线程之间的竞争。

ReentrantReadWriteLock<font color=Crimson>不支持锁的升级</font>，但是<font color=dodgerblue>支持锁的降级</font>。

ReentrantReadWriteLock的<font color=cirmson>读锁不支持条件变量</font>，但<font color=dodgerblue>写锁支持条件变量。</font>

## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《Java 编程思想(第4版)》  
&emsp;&emsp;https://juejin.im/post/5dc22993f265da4cf77c8ded
&emsp;&emsp;http://www.tianxiaobo.com

**感谢阅读**！



