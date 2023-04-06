**前言**： 
前面大致了解了线程的创建和生命周期，线程在生命周期中并不是固定处于某一个状态而是随着代码的执行在不同状态之间切换。本篇通过对Thread类中方法的讲解来展示线程生命周期的变化，同时也会对Thread类本身进行理解。

[toc]



**面试问题**

Q ：wait和sleep方法的区别？  
Q ：为什么wait和notify/notifyAll要定义在Object中？



## 1.Thread中的属性

```java
    public class Thread implements Runnable {
        private volatile String name;
        private boolean  daemon = false;
        private Runnable target;
        private volatile Interruptible blocker;
        volatile Object parkBlocker;
        private volatile int threadStatus = 0;

        private int priority;
        public final static int MIN_PRIORITY = 1;
        public final static int NORM_PRIORITY = 5;
        public final static int MAX_PRIORITY = 10;

        private volatile UncaughtExceptionHandler uncaughtExceptionHandler;
        private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;
        ...
    }

```
&emsp;&emsp;**name** ：是表示Thread的名字，可以通过Thread类的构造器中的参数来指定线程名字，通过getName（）来获取线程的名称，建议根据任务或功能对线程进行合理命名，以便调试。    

&emsp;&emsp;**dameon** ：表示线程是否是守护线程,默认为false，守护线程是为非守护线程服务的，在后台默默的完成一些系统性的服务，比如垃圾回收线程，JIT线程，如果JVM中只剩守护线程，JVM会直接退出。守护线程的设置务必在线程启动前完成。
```java
    Thread t=new Thread();
    t.setDaemon(true);
    t.start();
```
&emsp;&emsp;**target** ：用来存放需要执行的任务。也就是构造方法中传入的Runnable实现，FutureTask也会存在这里。    

&emsp;&emsp;**blocker** ：中断标志位，用于判断是否被中断，中断可以理解为打断，如果需要停止一个正在执行任务的线程，可以通过线程的实例方法interrupt（）或者Thread.interrupted（）来设置其中断标志。

&emsp;&emsp;**parkBlocker** ：和中断标志位类似，不过修改这个状态的方法不在Threa中，而是通过JUC包下的LockSupport来操作。

&emsp;&emsp;**threadStatus** ：线程当前的状态。NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING和TERMINATED。

&emsp;&emsp;**priority** ：表示线程的优先级，优先级不是谁先谁后，而是权重，优先级高的线程更容易抢到cpu时间片,优先级分为1-10共10个等级，1表示最低优先级，5是默认级别。setPriority()用来设定线程的优先级，需要在线程start（）调用之前进行设定。  

&emsp;&emsp;**uncaughtExceptionHandler** ：未捕获异常的处理器，由于线程的本质特性，无法在当前线程捕获到从其他线程中逃逸的异常，一旦异常逃逸出run方法，它就会向外传播到控制台，而我们通常需要记录异常日志,所以就需要对线程做运行时的异常处理可以使用实例方法setUncaughtExceptionHandler() 来配置未捕获异常处理器。
```java
    public void test() {
        try {
            new Thread(() -> {
                throw new RuntimeException();
            }).start();
        } catch (Exception e) {
            //不能捕获到其他线程的异常，所以下面这行代码不会被打印
            System.out.println("出错了");
        }
    }

    public static class MyUnCatchExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("this is MyUnCatchExceptionHandler ");
            System.out.println("catch " + e + " 记录日志");
        }
    }

    //通过对线程实例设置unCaughtExceptionHandler,
    public void test2() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException();
        });
        thread.setUncaughtExceptionHandler(new MyUnCatchExceptionHandler());
        thread.start();
    }
```
&emsp;&emsp;JDK5 之后可以使用Executor的ThreadFactory来解决这个问题,通过给 Thread实例 设置一个(实现 UnCatchExceptionHandler接口)未捕获异常处理器，也可以使用 Thread.setDefaultUnCatchExceptionHandler() 来配置默认的未捕获异常处理器。
```java
    //通过在ThreadFactory中设置defaultUnCatchExceptionHandler
    public void test3() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new MyThreadFactory());
        cachedThreadPool.execute(() -> {
            throw new RuntimeException();
        });
    }


    public static class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            //thread.setUncaughtExceptionHandler(new MyUnCatchExceptionHandler());
            Thread.setDefaultUncaughtExceptionHandler(new MyUnCatchExceptionHandler());
            return thread;
        }
    }
```

&emsp;&emsp;**defaultUncaughtExceptionHandler** :Thread类提供的默认未捕获异常的处理器。


## 2.Thread中的方法
&emsp;&emsp;通过Thread中的属性，大致了解了Thread类的结构，下面我们通过线程状态转换图来学习Thread类中的方法。  

![6-线程状态转换图.jpg](img/Java并发编程：3-Thread类的使用/6-线程状态转换图.jpg)

### 2.1 start()、run()和stop()
&emsp;&emsp;在我们实例化一个Thread对象后，这个对象处于初始状态，也就是threadStatus为NEW，此时这个对象只是堆中的一个普通Java对象，虽然被称为线程对象，但其实在操作系统中并没有与之对应的线程，只有当调用该对象的start，操作系统才会创建一个新线程，我们可以通过断点进行查看。  

在执行thread.start()之前

![8-线程创建时机.jpg](img/Java并发编程：3-Thread类的使用/8-线程创建时机.jpg)

在执行thread.start()之后

![9-线程创建时机.jpg](img/Java并发编程：3-Thread类的使用/9-线程创建时机2.jpg)

&emsp;&emsp;Java的线程是不允许启动两次的，第二次调用会抛出IllegalThreadStateException，这是一种运行时异常。

&emsp;&emsp;run()不需要我们手动调用，通过start()方法启动线程之后，当线程获得CPU执行时间，便进入run()方法体去执行具体的任务。直接调用run方法会被当前线程当作一次普通的方法调用，归属于当前的线程栈。  

&emsp;&emsp;在run()方法正常执行完成后，线程会处于终止状态。但也总是会有例外情况，如果需要提前终止一个正在运行的线程，可以使用interrupt 方式或者stop()方法：  

- (1) 使用stop方法强行终止线程，**不推荐使用**，stop会立即释放掉该线程所持有的锁，可能无法正常释放自己所持有的资源，造成未知错误。如果修改了一半就被stop掉，那数据也只会被修改一半，可能产生不可预料的结果； 

- (2) 使用interrupt的方式，interrupt()方法并未真正停止线程，只不过在线程中修改了blocker标记，此时可以使用抛异常的方式使线程停止。  

&emsp;&emsp;windows的线程是抢占式的，意味着线程可以强制结束其他线程，例如通过任务管理器结束一个无响应的应用程序。   
Java的线程工作方式是协作式，这样设计是为了让线程自身能够在线程关闭前处理自己的数据。

### 2.2 suspend()和resume()
**不推荐使用**  
&emsp;&emsp;如果想让一个线程暂停执行，而不是终止这个线程，可以使用suspend()将线程挂起，需要线程继续执行时使用resume()。正常情况下是先suspend()再resume()，如果将这两个方法的调用顺序调换，那么线程将永远被挂起，并且suspend()不会释放锁，这种情况下则会发生死锁。而且被suspend挂起的线程状态显示为"RUNNABLE"状态，这给排查bug带来困难。  
&emsp;&emsp;因此在JUC中提供了LockSupport类来代替suspend()和resume()，可以看到线程状态转换图中的LockSupport.park()和LockSupport.unpark(),后面会的对LockSupport的实现进行详细的讲解。

```java
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                    System.out.println(i++);
            }
        });
        thread.start();
        TimeUnit.NANOSECONDS.sleep(1);
        //先挂起再继续执行
        thread.resume();
        thread.suspend();
    }
    //Output
    // 这种情况程序会一直执行，不停的打印i的值
```

```java
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                    System.out.println(i++);
            }
        });
        thread.start();
        TimeUnit.NANOSECONDS.sleep(1);
        //先继续执行再挂起
        thread.suspend();
        thread.resume();
    }
    //Output
    // 这种情况线程会被挂起，控制台只会显示在挂起前打印的值，
```

### 2.3 sleep()和TimeUnit
&emsp;&emsp;static sleep(long millis) 的作用是当前正在执行的线程睡眠一段时间，出 CPU 让其去执行其他的任务，睡眠结束后获取到时间片才会继续执行任务。  
&emsp;&emsp;调用sleep()，会抛出编译期异常InterruptedException，你需要捕获或者将该异常继续上抛。sleep方法不会释放锁，也就是说如果当前线程持有对某个对象的锁，其他线程无法访问这个对象。由于sleep(long millis)中控制睡眠时长的单位是毫秒级，这样可读性比较差，建议使用TimeUnit：

- TimeUnit.DAYS 日的工具类 
- TimeUnit.HOURS 时的工具类 
- TimeUnit.MINUTES 分的工具类 
- TimeUnit.SECONDS 秒的工具类 
- TimeUnit.MILLISECONDS 毫秒的工具类
```java
    public void test() throws InterruptedException {
        //休眠一天
        Thread.sleep(1000*60*60*24);
        TimeUnit.DAYS.sleep(1);
    }
```



### 2.4 interrupt()、isInterrupted()和Thread.interrupted()
&emsp;&emsp;Java没有提供任何机制来安全的终止线程，但它提供了**中断**（Interruption），这是一种协作机制，能够使一个线程终止另一个线程的当前工作。我们很少希望某个任务、线程或服务立即停止，因为这种立即停止会使共享的数据结构处于不一致的状态。通过协作的方式，我们可以让要退出的程序清理当前正在执行的工作，然后再结束，这提供了更好的灵活性，因为任务本身的代码比发出取消请求的代码更清楚如何清除工作。  
&emsp;&emsp;Java通过协作式中断，通过推迟中断请求的处理，开发人员能制定更灵活的中断策略，使程序在响应性和健壮性之间实现合理的平衡。需要使用中断的方法都要求抛出或捕获处理InterruptedException异常。

**interrupt()**  
&emsp;&emsp;设置中断状态为true，如果线程处于就绪状态则不会直接中断，而是将线程状态改为中断状态，需要手动去检测线程的中断状态，如果线程被阻塞则能抛出InterruptedException异常，当抛出InterruptedException异常或者调用Thread.interrupted()时，中状态将被复位。
```java
    //错误写法  虽然将线程设置为中断状态，但内部程序一直在执行
    public void run(){
      //线程处于就绪状态
      while(true){
        ...
      }
    }
    thred.interrupt()

    //当线程被中断后，会执行完当前的操作后，进入下一轮循环的时候停止
    public void run(){
      while(true){
        if(Thread.currentThread().isInterrupted()){
          System.out.println("Interrupted!");
          break;
        }
        ...
      }
    }
    thred.interrupt()
```

**isInterrupted()**   
判断线程是否被中断
```java
    public boolean isInterrupted() {
      //不清除中断状态
      return isInterrupted(false);
    }
```

 **Thread.interrupted()**  
判断是否被中断，并清除当前中断状态，实现Runnable接口的只能调用这个方法。
 ```java
    public static boolean Thread.interrupted(){ 
      return currentThread().isInterrupted(true);
    }
 ```

**无法响应中断的阻塞**

- 执行同步的SocketI/O无法响应中断。  
- InputStream和OutputStream的read和write等方法都不会响应中断，但可以通过关闭底层的套接字，使因read或write等方法被阻塞的线程抛出一个SocketException。  
- 等待获得内置锁（synchronized）而阻塞，无法响应中断。但使用Lock类中提供了lockInterruptibly方法，该方法允许在等待一个锁的同时仍能响应中断。

**中断方法**

- Thread.currentThread().isInterrupted()

  返回调用该方法线程的中断状态

  ```java
  public boolean isInterrupted() {
  return isInterrupted(false);
  }
  ```

- Thread.interrupted()	

  返回调用该方法线程的中断状态，然后直接把线程的中断状态清除了，设置为false

  ```java
  public static boolean interrupted() {
    return currentThread().isInterrupted(true);
  }
  ```

- Thread.currentThread().interrupt()

  中断调用该方法所在的线程，将该线程的中断状态调整为true，如果线程在阻塞时，能够感知中断信号，就会抛出InterruptedException异常，如果线程处于运行状态，发生中断时，线程本身不会立即停止或者抛出异常，因为这可能会导致业务处理了一半，因此我们需要在循环处理业务前，判断线程的中断状态。如果已经中断了，则退出线程。这样可以保证正在处理的业务能够继续处理完。

  ```java
  public void interrupt() {
    if (this != Thread.currentThread())
      checkAccess();
  
    synchronized (blockerLock) {
      Interruptible b = blocker;
      if (b != null) {
        interrupt0();           // Just to set the interrupt flag
        b.interrupt(this);
        return;
      }
    }
    interrupt0();
  }
  ```

  **能感知中断信号的方法**

  ```java
  Object.wait()/wait(long)/wait(long,int);
  Thread.sleep(long)/sleep(long,int);
  Thread.join()/join(long)/join(long,int);
  java.util.concurrent.BlockingQueue.take()/put(E)
  java.util.concurrent.locks.Lock.lockInterruptibly()
  java.util.concurrent.CountDownLatch.await()
  java.util.concurrent.CyclicBarrier.await()
  java.util.concurrent.Exchanger.exchange(V)
  java.nio.channels.InterruptibleChannel相关方法
  java.nio.channels.Selector的相关方法
  ```

### 2.5 wait()和notify()/notifyAll()
调用这三个方法的前提是调用者持有锁，不然会抛出IllegalMonitorStateException异常。
```java
    Object lock =new Object();
    synchronized(lock){
        lock.wait();
    }
    ...
    synchronized(lock){
        lock.notify();
        lock.notifyAll();
        ...
    }

```
&emsp;&emsp;**wait()** 会释放当前持有的锁，让出CPU，使线程进入等待状态。  
&emsp;&emsp;**notify()** 唤醒一个等待该锁的线程，然后继续执行，直至退出临界区（锁住notify()的区域），锁才会被释放，等待该锁的线程才能去抢锁。  
&emsp;&emsp;**notifyAll()** 唤醒所有在对象锁上等待的线程。  

之前展示的所有方法都是定义在Thread中的，但是这三个被定义在Object对象中。
```java
    public class Object {
        public final void wait() throws InterruptedException {
                wait(0);
        }
        public final native void notify();
        public final native void notifyAll();
        ...
    }
```
**为什么 wait() 和notify() / notifyAll() 方法要放在同步块中？**

&emsp;&emsp;在多线程环境下有著名问题“Lost wake-up”。线程进入等待状态后，丢失了唤醒操作，导致线程永远处于等待状态。
假如有两个线程，一个消费者线程，一个生产者线程。生产者线程的任务生产商品简化为count+1，而后唤醒消费者；消费者则是判断有无商品，有则消费商品，无则进入等待。  

![10-LostWeakUp.jpg](img/Java并发编程：3-Thread类的使用/10-LostWeakUp.jpg)

&emsp;&emsp;在消费者执行的过程中，先判断了count的状态，随后发生上下文切换，生产者执行了全部操作，由于消费者还没有进入等待状态，所以生产者的notify没有任何作用，于是在此处唤醒操作就丢失了。执行权切换到消费者，继续执行，但此时的count已经被修改为1，所以之前的判断失效，消费者没有重新判断count状态，就继续执行，进入等待后没有唤醒操作，导致无限制等待。

&emsp;&emsp;问题的根源在于，消费者在检查count到调用wait()之间，count就可能被改掉了。这就是一种最常见的竞态条件“先检查后执行(check-Then-Act)”。还有一种常见的是“读取-修改-写入”，为了确保线程安全性，这类复合操作必须以原子方式执行来确保线程的安全性，synchronized是Java提供的内置锁，它可以保证一组操作的原子性，应用在刚才的场景下就是，检查count和调用wait()之间，count不会修改。这样才能避免“Lost Weak-up”问题的发生。因此wait()必须在同步块中调用。

**为什么 wait() 和notify() / notifyAll() 方法定义在Object而不是Thread类中？** 

&emsp;&emsp;通过上一个问题我们了解到wait和notify执行的前提是需要持有锁，而Java中锁可以是任意的对象，所以这三个方法定义在Object中。

**sleep() 方法和 wait() 方法区别?**

- sleep不会释放锁，使得线程仍然可以同步控制；wait会释放锁，进入线程等待池中等待。
- sleep可以在任何地方使用；wait/notify/notifyAll只能在同步控制方法或者同步控制块中使用。
- sleep通常被用于暂停执行；wait通常被用于线程间交互。
- sleep是Thread类中的方法；wait是Object类中的方法。
- sleep会自动唤醒，如果想要提前唤醒，可以使用interrupt方法中断；调用wait()方法的线程，不会自己唤醒，需要线程调用notify/notifyAll方法唤醒。


### 2.6 yeild()和join()
**yeild()**
```java
    public static native void yield();
```
&emsp;&emsp;当一个线程拿到CPU时间片后，调用yeild()方法使得线程交出当前时间片，重新与拥有相同优先级或更高优先级的线程竞争，竞争成功后还是会执行的，yield()方法不能控制具体的交出，只能让其他线程有更多获取 CPU 执行时间的机会，yeild不会释放锁。  
&emsp;&emsp;yield()方法会增加发生上下文切换的概率，并发调试的时候更容易将问题暴露出来。

**sleep与yield的区别**

- sleep方法给其它线程运行机会时不考虑线程的优先级，因此会给低优先级的线程以运行的机会；yield方法只会给相同或更高优先级的线程以运行的机会。
- sleep方法之后转入阻塞状态；yield方法之后转入就绪状态。
- sleep方法声明抛出InterruptedException；yield方法没有声明任何异常。
- sleep方法具有更好的可移植性且sleep更容易被控制；（yield不好控制，只是瞬间放弃CPU的执行权，有可能马上又抢回接着执行）。
- sleep和yield方法将在当前正在执行的线程上运行，所以在其它处于等待状态的线程上调用这些方法是没有意义的。这就是为什么这些方法是静态的。

    

**join()**
可以理解为等待一个线程执行结束，先看一个简单的实例。
```java
        public void test() throws InterruptedException {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("线程执行结束");
                    } catch (InterruptedException ignore) {}
                }
            };
            thread.start();
            thread.join();  
            //等待thread线程执行完成或者抛出异常，在等待期间执行这行代码的线程是阻塞的
            System.out.println("...");  

            //Output
                3s后打印
            // 线程执行结束
            // ...
        }
```
Java中提供了3种join的方法。
```java
        public final void join() throws InterruptedException {
                join(0);    //调用无参join则会一直等待,直至线程执行结束
            }

        public final synchronized void join(long millis)
            throws InterruptedException {
                long base = System.currentTimeMillis();
                long now = 0;

                if (millis < 0) {
                    throw new IllegalArgumentException("timeout value is negative");
                }

                if (millis == 0) {
                    while (isAlive()) {
                        wait(0);    //join的通过调用wait()实现，因此join会释放锁
                                    //线程执行完毕后，系统会调用notifyAll()  
                                    //因此建议不要在Thread实例上使用wait()和notify()  
                    }
                } else {
                    while (isAlive()) {
                        long delay = millis - now;
                        if (delay <= 0) {
                            break;
                        }
                        wait(delay);
                        now = System.currentTimeMillis() - base;
                    }
                }
            }

        public final synchronized void join(long millis, int nanos)
                                              throws InterruptedException {...
```


## Reference
&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《Java 编程思想(第4版)》  
&emsp;&emsp;https://blog.csdn.net/justloveyou_/article/details/54347954   
&emsp;&emsp;https://blog.csdn.net/qq_35508033/article/details/89299305

**感谢阅读**！











