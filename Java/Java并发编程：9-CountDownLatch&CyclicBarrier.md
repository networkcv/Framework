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

下面来看看CountDownLatch中的方法。

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



**使用场景**

1. 前面提到了倒计时器的用法，本质上就是为了等其他线程执行完，可以用在启动服务时，等待其他组件的加载。
2. 可以使多个线程同时开始执行，提供很大的并行性，这里更关注的是线程到齐之后。平时在主线程启动线程时，由于代码顺序执行的缘故，线程并不是真正同时start的，中间存在时间差，如果执行的耗时特别短，那么可能很多问题不会暴露出来。所以在这里CountDownLatch起到了和Thread.yield类似的作用，可以增大出现并发问题的几率，因此可以写多个线程来进行死锁的检测。



### 1.3 CountDownLatch原理

CountDownLatch的原理其实并不难，不过这里的不难是建立对AQS有了解之上的，有兴趣的可以看一看[Semaphore & AQS](https://segmentfault.com/a/1190000021400650)。

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



```java
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }
```



### 1.4 小结

 **CountDownLatch 的不足**

CountDownLatch 是一次性的，计数器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，当 CountDownLatch 使用完毕后，它不能再次被使用。

## 2.CyclicBarrier

### 2.1 CyclicBarrier使用

1.回调总要关心执行线程是谁

### 2.2 CyclicBarrier原理

字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。我们暂且把这个状态就叫做barrier，当调用await()方法之后，线程就处于barrier了。

　　CyclicBarrier类位于java.util.concurrent包下，CyclicBarrier提供2个构造器：

```
public` `CyclicBarrier(``int` `parties, Runnable barrierAction) {``}` `public` `CyclicBarrier(``int` `parties) {``}
```

　　参数parties指让多少个线程或者任务等待至barrier状态；参数barrierAction为当这些线程都达到barrier状态时会执行的内容。

　　然后CyclicBarrier中最重要的方法就是await方法，它有2个重载版本：

```
public` `int` `await() ``throws` `InterruptedException, BrokenBarrierException { };``public` `int` `await(``long` `timeout, TimeUnit unit)``throws` `InterruptedException,BrokenBarrierException,TimeoutException { };
```

 　第一个版本比较常用，用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务；

　　第二个版本是让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。

　　下面举几个例子就明白了：

　　假若有若干个线程都要进行写数据操作，并且只有所有线程都完成写数据操作之后，这些线程才能继续做后面的事情，此时就可以利用CyclicBarrier了：

```java
public class Test {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
 
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
```

 　执行结果：

![img](https://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif) View Code

　　从上面输出结果可以看出，每个写入线程执行完写数据操作之后，就在等待其他线程写入操作完毕。

　　当所有线程线程写入操作完毕之后，所有线程就继续进行后续的操作了。

　　如果说想在所有线程写入操作完之后，进行额外的其他操作可以为CyclicBarrier提供Runnable参数：

```java
public class Test {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N,new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程"+Thread.currentThread().getName());   
            }
        });
         
        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
 
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
```

 　运行结果：

![img](https://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif) View Code

　　从结果可以看出，当四个线程都到达barrier状态后，会从四个线程中选择一个线程去执行Runnable。

 　下面看一下为await指定时间的效果：

```java
public class Test {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
         
        for(int i=0;i<N;i++) {
            if(i<N-1)
                new Writer(barrier).start();
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Writer(barrier).start();
            }
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
 
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                try {
                    cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}
```

 　执行结果：

![img](https://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif) View Code

　　上面的代码在main方法的for循环中，故意让最后一个线程启动延迟，因为在前面三个线程都达到barrier之后，等待了指定的时间发现第四个线程还没有达到barrier，就抛出异常并继续执行后面的任务。

　　另外CyclicBarrier是可以重用的，看下面这个例子：

```java
public class Test {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
         
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
         
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         
        System.out.println("CyclicBarrier重用");
         
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
 
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
             
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}

```

 　执行结果：

![img](https://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif) View Code

　　从执行结果可以看出，在初次的4个线程越过barrier状态后，又可以用来进行新一轮的使用。而CountDownLatch无法进行重复使用



1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：

　　　　CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；

　　　　而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；

　　　　另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。



### [5 CyclicBarrier(循环栅栏)](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS?id=_5-cyclicbarrier循环栅栏)

CyclicBarrier 和 CountDownLatch 非常类似，它也可以实现线程间的技术等待，但是它的功能比 CountDownLatch 更加复杂和强大。主要应用场景和 CountDownLatch 类似。

CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。CyclicBarrier 默认的构造方法是 `CyclicBarrier(int parties)`，其参数表示屏障拦截的线程数量，每个线程调用`await`方法告诉 CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。

再来看一下它的构造函数：

```java
public CyclicBarrier(int parties) {
    this(parties, null);
}

public CyclicBarrier(int parties, Runnable barrierAction) {
    if (parties <= 0) throw new IllegalArgumentException();
    this.parties = parties;
    this.count = parties;
    this.barrierCommand = barrierAction;
}
```

其中，parties 就代表了有拦截的线程的数量，当拦截的线程数量达到这个值的时候就打开栅栏，让所有线程通过。

#### [5.1 CyclicBarrier 的应用场景](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS?id=_51-cyclicbarrier-的应用场景)

CyclicBarrier 可以用于多线程计算数据，最后合并计算结果的应用场景。比如我们用一个 Excel 保存了用户所有银行流水，每个 Sheet 保存一个帐户近一年的每笔银行流水，现在需要统计用户的日均银行流水，先用多线程处理每个 sheet 里的银行流水，都执行完之后，得到每个 sheet 的日均银行流水，最后，再用 barrierAction 用这些线程的计算结果，计算出整个 Excel 的日均银行流水。

#### [5.2 CyclicBarrier 的使用示例](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS?id=_52-cyclicbarrier-的使用示例)

示例 1：

```java
/**
 *
 * @author Snailclimb
 * @date 2018年10月1日
 * @Description: 测试 CyclicBarrier 类中带参数的 await() 方法
 */
public class CyclicBarrierExample2 {
  // 请求的数量
  private static final int threadCount = 550;
  // 需要同步的线程数量
  private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

  public static void main(String[] args) throws InterruptedException {
    // 创建线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    for (int i = 0; i < threadCount; i++) {
      final int threadNum = i;
      Thread.sleep(1000);
      threadPool.execute(() -> {
        try {
          test(threadNum);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (BrokenBarrierException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });
    }
    threadPool.shutdown();
  }

  public static void test(int threadnum) throws InterruptedException, BrokenBarrierException {
    System.out.println("threadnum:" + threadnum + "is ready");
    try {
      /**等待60秒，保证子线程完全执行结束*/
      cyclicBarrier.await(60, TimeUnit.SECONDS);
    } catch (Exception e) {
      System.out.println("-----CyclicBarrierException------");
    }
    System.out.println("threadnum:" + threadnum + "is finish");
  }

}
```

运行结果，如下：

```
threadnum:0is ready
threadnum:1is ready
threadnum:2is ready
threadnum:3is ready
threadnum:4is ready
threadnum:4is finish
threadnum:0is finish
threadnum:1is finish
threadnum:2is finish
threadnum:3is finish
threadnum:5is ready
threadnum:6is ready
threadnum:7is ready
threadnum:8is ready
threadnum:9is ready
threadnum:9is finish
threadnum:5is finish
threadnum:8is finish
threadnum:7is finish
threadnum:6is finish
......
```

可以看到当线程数量也就是请求数量达到我们定义的 5 个的时候， `await`方法之后的方法才被执行。

另外，CyclicBarrier 还提供一个更高级的构造函数`CyclicBarrier(int parties, Runnable barrierAction)`，用于在线程到达屏障时，优先执行`barrierAction`，方便处理更复杂的业务场景。示例代码如下：

```java
/**
 *
 * @author SnailClimb
 * @date 2018年10月1日
 * @Description: 新建 CyclicBarrier 的时候指定一个 Runnable
 */
public class CyclicBarrierExample3 {
  // 请求的数量
  private static final int threadCount = 550;
  // 需要同步的线程数量
  private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
    System.out.println("------当线程数达到之后，优先执行------");
  });

  public static void main(String[] args) throws InterruptedException {
    // 创建线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    for (int i = 0; i < threadCount; i++) {
      final int threadNum = i;
      Thread.sleep(1000);
      threadPool.execute(() -> {
        try {
          test(threadNum);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (BrokenBarrierException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });
    }
    threadPool.shutdown();
  }

  public static void test(int threadnum) throws InterruptedException, BrokenBarrierException {
    System.out.println("threadnum:" + threadnum + "is ready");
    cyclicBarrier.await();
    System.out.println("threadnum:" + threadnum + "is finish");
  }

}
```

运行结果，如下：

```
threadnum:0is ready
threadnum:1is ready
threadnum:2is ready
threadnum:3is ready
threadnum:4is ready
------当线程数达到之后，优先执行------
threadnum:4is finish
threadnum:0is finish
threadnum:2is finish
threadnum:1is finish
threadnum:3is finish
threadnum:5is ready
threadnum:6is ready
threadnum:7is ready
threadnum:8is ready
threadnum:9is ready
------当线程数达到之后，优先执行------
threadnum:9is finish
threadnum:5is finish
threadnum:6is finish
threadnum:8is finish
threadnum:7is finish
......
```

#### [5.3 `CyclicBarrier`源码分析](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS?id=_53-cyclicbarrier源码分析)

当调用 `CyclicBarrier` 对象调用 `await()` 方法时，实际上调用的是`dowait(false, 0L)`方法。 `await()` 方法就像树立起一个栅栏的行为一样，将线程挡住了，当拦住的线程数量达到 parties 的值时，栅栏才会打开，线程才得以通过执行。

```java
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }
```

`dowait(false, 0L)`：

```java
    // 当线程数量或者请求数量达到 count 时 await 之后的方法才会被执行。上面的示例中 count 的值就为 5。
    private int count;
    /**
     * Main barrier code, covering the various policies.
     */
    private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
        // 锁住
        lock.lock();
        try {
            final Generation g = generation;

            if (g.broken)
                throw new BrokenBarrierException();

            // 如果线程中断了，抛出异常
            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }
            // cout减1
            int index = --count;
            // 当 count 数量减为 0 之后说明最后一个线程已经到达栅栏了，也就是达到了可以执行await 方法之后的条件
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    // 将 count 重置为 parties 属性的初始化值
                    // 唤醒之前等待的线程
                    // 下一波执行开始
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
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;

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

总结：`CyclicBarrier` 内部通过一个 count 变量作为计数器，cout 的初始值为 parties 属性的初始化值，每当一个线程到了栅栏这里了，那么就将计数器减一。如果 count 值为 0 了，表示这是这一代最后一个线程到达栅栏，就尝试执行我们构造方法中输入的任务。





## 3.总结

### CyclicBarrier与CountDownLatch的比较

CountDownLatch：一个或多个线程等待另外N个线程完成某个事情之后才能继续执行。

CyclicBarrier：N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。

对于CountDownLatch来说，重点的是那个“一个线程”，是它在等待，而另外那N个线程在把“某个事情”做完之后可以继续等待，可以终止；而对于CyclicBarrier来说，重点是那“N个线程”，它们之间任何一个没有完成，所有的线程都必须等待。

CountDownLatch是计数器，线程完成一个就计一个，就像报数一样，只不过是递减的；
CyclicBarrier更像一个水闸，线程执行就像水流，在水闸处就会堵住，等到水满（线程到齐）了，才开始泄流。

CountDownLatch不可重复利用，CyclicBarrier可重复利用。

### **3.1 CyclicBarrier 和 CountDownLatch 的区别**

**下面这个是国外一个大佬的回答：**

CountDownLatch 是计数器，只能使用一次，而 CyclicBarrier 的计数器提供 reset 功能，可以多次使用。但是我不那么认为它们之间的区别仅仅就是这么简单的一点。我们来从 jdk 作者设计的目的来看，javadoc 是这么描述它们的：

> CountDownLatch: A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.(CountDownLatch: 一个或者多个线程，等待其他多个线程完成某件事情之后才能执行；) CyclicBarrier : A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point.(CyclicBarrier : 多个线程互相等待，直到到达同一个同步点，再继续一起执行。)

对于 CountDownLatch 来说，重点是“一个线程（多个线程）等待”，而其他的 N 个线程在完成“某件事情”之后，可以终止，也可以等待。而对于 CyclicBarrier，重点是多个线程，在任意一个线程没有完成，所有的线程都必须等待。

CountDownLatch 是计数器，线程完成一个记录一个，只不过计数不是递增而是递减，而 CyclicBarrier 更像是一个阀门，需要所有线程都到达，阀门才能打开，然后继续执行。



## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《Java 编程思想(第4版)》  
&emsp;&emsp;https://time.geekbang.org/column/intro/159
&emsp;&emsp;https://snailclimb.gitee.io/javaguide/#/
&emsp;&emsp;https://www.cnblogs.com/dolphin0520/p/3920397.html

**感谢阅读**！