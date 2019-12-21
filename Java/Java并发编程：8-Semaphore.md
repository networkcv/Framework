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

### [3 Semaphore(信号量)-允许多个线程同时访问](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS?id=_3-semaphore信号量-允许多个线程同时访问)

**synchronized 和 ReentrantLock 都是一次只允许一个线程访问某个资源，Semaphore(信号量)可以指定多个线程同时访问某个资源。** 示例代码如下：

```java
/**
 *
 * @author Snailclimb
 * @date 2018年9月30日
 * @Description: 需要一次性拿一个许可的情况
 */
public class SemaphoreExample1 {
  // 请求的数量
  private static final int threadCount = 550;

  public static void main(String[] args) throws InterruptedException {
    // 创建一个具有固定线程数量的线程池对象（如果这里线程池的线程数量给太少的话你会发现执行的很慢）
    ExecutorService threadPool = Executors.newFixedThreadPool(300);
    // 一次只能允许执行的线程数量。
    final Semaphore semaphore = new Semaphore(20);

    for (int i = 0; i < threadCount; i++) {
      final int threadnum = i;
      threadPool.execute(() -> {// Lambda 表达式的运用
        try {
          semaphore.acquire();// 获取一个许可，所以可运行线程数量为20/1=20
          test(threadnum);
          semaphore.release();// 释放一个许可
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      });
    }
    threadPool.shutdown();
    System.out.println("finish");
  }

  public static void test(int threadnum) throws InterruptedException {
    Thread.sleep(1000);// 模拟请求的耗时操作
    System.out.println("threadnum:" + threadnum);
    Thread.sleep(1000);// 模拟请求的耗时操作
  }
}
```

执行 `acquire` 方法阻塞，直到有一个许可证可以获得然后拿走一个许可证；每个 `release` 方法增加一个许可证，这可能会释放一个阻塞的 acquire 方法。然而，其实并没有实际的许可证这个对象，Semaphore 只是维持了一个可获得许可证的数量。 Semaphore 经常用于限制获取某种资源的线程数量。

当然一次也可以一次拿取和释放多个许可，不过一般没有必要这样做：

```java
          semaphore.acquire(5);// 获取5个许可，所以可运行线程数量为20/5=4
          test(threadnum);
          semaphore.release(5);// 获取5个许可，所以可运行线程数量为20/5=4
```

除了 `acquire`方法之外，另一个比较常用的与之对应的方法是`tryAcquire`方法，该方法如果获取不到许可就立即返回 false。

Semaphore 有两种模式，公平模式和非公平模式。

- **公平模式：** 调用 acquire 的顺序就是获取许可证的顺序，遵循 FIFO；
- **非公平模式：** 抢占式的。

**Semaphore 对应的两个构造方法如下：**

```java
   public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }

    public Semaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }
```

**这两个构造方法，都必须提供许可的数量，第二个构造方法可以指定是公平模式还是非公平模式，默认非公平模式。**

- 源码解析
  Semaphore有两种模式，公平模式和非公平模式。公平模式就是调用acquire的顺序就是获取许可证的顺序，遵循FIFO；而非公平模式是抢占式的，也就是有可能一个新的获取线程恰好在一个许可证释放时得到了这个许可证，而前面还有等待的线程。

  构造方法
  Semaphore有两个构造方法，如下：

         public Semaphore(int permits) {
          sync = new NonfairSync(permits);
      }
      
      public Semaphore(int permits, boolean fair) {
          sync = fair ? new FairSync(permits) : new NonfairSync(permits);
      }
  1
  2
  3
  4
  5
  6
  7
  从上面可以看到两个构造方法，都必须提供许可的数量，第二个构造方法可以指定是公平模式还是非公平模式，默认非公平模式。
  Semaphore内部基于AQS的共享模式，所以实现都委托给了Sync类。
  这里就看一下NonfairSync的构造方法：

   NonfairSync(int permits) {
              super(permits);
          }
  1
  2
  3
  可以看到直接调用了父类的构造方法，Sync的构造方法如下：

  Sync(int permits) {
              setState(permits);
          }
  1
  2
  3
  可以看到调用了setState方法，也就是说AQS中的资源就是许可证的数量。

  获取许可
  先从获取一个许可看起，并且先看非公平模式下的实现。首先看acquire方法，acquire方法有几个重载，但主要是下面这个方法

  public void acquire(int permits) throws InterruptedException {
          if (permits < 0) throw new IllegalArgumentException();
          sync.acquireSharedInterruptibly(permits);
      }
  1
  2
  3
  4
  从上面可以看到，调用了Sync的acquireSharedInterruptibly方法，该方法在父类AQS中，如下：

  public final void acquireSharedInterruptibly(int arg)
              throws InterruptedException {
          //如果线程被中断了，抛出异常
          if (Thread.interrupted())
              throw new InterruptedException();
          //获取许可失败，将线程加入到等待队列中
          if (tryAcquireShared(arg) < 0)
              doAcquireSharedInterruptibly(arg);
      }
  1
  2
  3
  4
  5
  6
  7
  8
  9
  AQS子类如果要使用共享模式的话，需要实现tryAcquireShared方法，下面看NonfairSync的该方法实现：

   protected int tryAcquireShared(int acquires) {
              return nonfairTryAcquireShared(acquires);
          }
  1
  2
  3
  该方法调用了父类中的nonfairTyAcquireShared方法，如下：

  final int nonfairTryAcquireShared(int acquires) {
              for (;;) {
                  //获取剩余许可数量
                  int available = getState();
                  //计算给完这次许可数量后的个数
                  int remaining = available - acquires;
                  //如果许可不够或者可以将许可数量重置的话，返回
                  if (remaining < 0 ||
                      compareAndSetState(available, remaining))
                      return remaining;
              }
          }
  1
  2
  3
  4
  5
  6
  7
  8
  9
  10
  11
  12
  从上面可以看到，只有在许可不够时返回值才会小于0，其余返回的都是剩余许可数量，这也就解释了，一旦许可不够，后面的线程将会阻塞。看完了非公平的获取，再看下公平的获取，代码如下：

   protected int tryAcquireShared(int acquires) {
              for (;;) {
                  //如果前面有线程再等待，直接返回-1
                  if (hasQueuedPredecessors())
                      return -1;
                  //后面与非公平一样
                  int available = getState();
                  int remaining = available - acquires;
                  if (remaining < 0 ||
                      compareAndSetState(available, remaining))
                      return remaining;
              }
          }
  1
  2
  3
  4
  5
  6
  7
  8
  9
  10
  11
  12
  13
  从上面可以看到，FairSync与NonFairSync的区别就在于会首先判断当前队列中有没有线程在等待，如果有，就老老实实进入到等待队列；而不像NonfairSync一样首先试一把，说不定就恰好获得了一个许可，这样就可以插队了。
  看完了获取许可后，再看一下释放许可。

  释放许可
  释放许可也有几个重载方法，但都会调用下面这个带参数的方法，

  public void release(int permits) {
          if (permits < 0) throw new IllegalArgumentException();
          sync.releaseShared(permits);
      }
  1
  2
  3
  4
  releaseShared方法在AQS中，如下：

  public final boolean releaseShared(int arg) {
          //如果改变许可数量成功
          if (tryReleaseShared(arg)) {
              doReleaseShared();
              return true;
          }
          return false;
      }
  1
  2
  3
  4
  5
  6
  7
  8
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
  1
  2
  3
  4
  5
  6
  7
  8
  9
  10
  11
  12
  13
  从上面可以看到，一旦CAS改变许可数量成功，那么就会调用doReleaseShared()方法释放阻塞的线程。

  减小许可数量
  Semaphore还有减小许可数量的方法，该方法可以用于用于当资源用完不能再用时，这时就可以减小许可证。代码如下：

  protected void reducePermits(int reduction) {
          if (reduction < 0) throw new IllegalArgumentException();
          sync.reducePermits(reduction);
      }
  1
  2
  3
  4
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
  1
  2
  3
  4
  5
  6
  7
  8
  9
  10
  11
  12
  13
  从上面可以看到，就是CAS改变AQS中的state变量，因为该变量代表许可证的数量。

  获取剩余许可数量
  Semaphore还可以一次将剩余的许可数量全部取走，该方法是drain方法，如下：

  public int drainPermits() {
          return sync.drainPermits();
      }
  1
  2
  3
  Sync的实现如下：

   final int drainPermits() {
              for (;;) {
                  int current = getState();
                  if (current == 0 || compareAndSetState(current, 0))
                      return current;
              }
          }
  1
  2
  3
  4
  5
  6
  7
  可以看到，就是CAS将许可数量置为0。
  
