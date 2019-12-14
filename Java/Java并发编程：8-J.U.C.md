## 1.Lock & Condition

### 1.1 Lock接口的由来

之前提到并发编程的两大核心问题：**互斥**，即同一时刻只允许一个线程访问共享资源；**同步**，线程之间的通信、协作。JDK5之前管程由synchronized和wait，notify来实现。JDK5之后，在J.U.C中提供了新的实现方式，使用Lock和Condition两个接口来实现管程，其中Lock用于解决互斥，Condition用于解决同步。

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

### 1.2 可重入

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


### 1.3 公平锁与非公平锁

```java
public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```

ReentrantLock有两个构造函数，一个无参构造，一个需要传入boolean类型的fair，这个参数代表的就是公平策略，如果传入true，则会构造一个公平锁，也就是谁等的时间长，谁获得锁。默认构造的是非公平锁。

在管程模型中有一个入口等待队列，锁都对应一个等待队列，如果一个线程没有获取到锁，就会进入等待队列，当有线程释放锁的时候，就需要从等待队列中唤醒一个等待的线程，如果是公平锁的话，会唤醒等待时间最长的，非公平锁则会随机唤醒，有可能刚进入等待时间最短的反而被唤醒。

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
   final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
  final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
```



```java
 final void lock() {
            acquire(1);
        }

protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
```



### 1.4 ReentrantLock 原理

ReentrantLock实现了Lock接口，其可见性和有序性都是借助volatile规则来保证的。原理是利用了volatile相关的Happens-Before规则。J.U.C中的 ReentrantLock，内部持有一个volatile的成员变量state,获取锁的时候,会读写state的值;解锁的时候,也会读写state的值(简化后的代码如下面所示) 。相当于用前后两次对volatile变量的修改操作，将我们要对共享变量的修改操作给包起来了。通过巧妙利用volatile变量规则及传递性规来保证可见性和有序性。

```java
class SampleLock{
	volatile int state;
	lock(){
        state=1;
    //	...
        
	}
	unlock(){
	//	...
		state=0;
	}
}
```



使用CAS来保障原子性

 

### 1.5.Condition

Condition与ReentrantLock结合使用，这两者之间的关系可以参考synchronized和wait()/notify()的关系  
通过API的方式来对ReentrantLock进行类似于wait和notify的操作  

```java
// Codition方法
void await() throws InterruptedException;
boolean await(long time, TimeUnit unit) throws InterruptedException;
void signal();
void signalAll();
```


### 1.6 synchronized和ReentrantLock 的区别

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



## 2.Semaphore

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



## 3.ReadWriteLock & StampedLock

## 4.CountDownLatch & CyclicBarrier

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

### 
循环栅栏，这个计数器可以反复使用，假设计数器设置为10，那么第一批10个线程后，计数器会重置，然后接着处理第二批的10个线程  

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



### LockSupport

类比于 suspend/resume，推荐使用LockSupport的原因是，即使unpark在park之前调用，也不会导致线程永久被挂起  
能够响应中断，但不抛出异常，中断的响应结果是，park()函数的返回，可以从Thread.interrupted()得到中断标志
```java
LockSupport.park(); //线程挂起
LockSupport.unpark(t1); //线程继续执行
```



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


