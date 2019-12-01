## 前言： 
前面大致了解了线程的创建和生命周期，线程在生命周期中并不是固定处于某一个状态而是随着代码的执行在不同状态之间切换。本篇通过对Thread类中方法的讲解来展示线程生命周期的变化，同时也会对Thread类本身进行理解。

## [Thread的使用](#Thread的使用)
- [1.1如何创建一个线程](#1.1如何创建一个线程)
  - [1.1.1实现Runnable接口](#1.1.1实现Runnable接口)
  - [1.1.2继承Thread类](#1.1.2继承Thread类)
  - [1.1.3实现Callable接口](#1.1.3实现Callable接口)
  - [1.1.4对创建线程的一些个人理解](#1.1.4对创建线程的一些个人理解)
- [1.2线程的生命周期和状态](#1.2线程的生命周期和状态)
- [Reference](#Reference)


## 面试问题
Q ：wait和sleep方法的区别？  
Q ：为什么wait和notify/notifyAll要定义在Object中？

# Thread的使用
## Thread中的属性
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
```java
    public static State toThreadState(int var0) {
          if ((var0 & 4) != 0) {
              return State.RUNNABLE;
          } else if ((var0 & 1024) != 0) {
              return State.BLOCKED;
          } else if ((var0 & 16) != 0) {
              return State.WAITING;
          } else if ((var0 & 32) != 0) {
              return State.TIMED_WAITING;
          } else if ((var0 & 2) != 0) {
              return State.TERMINATED;
          } else {
              return (var0 & 1) == 0 ? State.NEW : State.RUNNABLE;
          }
    }
```
&emsp;&emsp;**priority** ：表示线程的优先级,优先级分为1-10共10个等级，1表示最低优先级，5是默认级别。setPriority()
用来设定线程的优先级，需要在线程start（）调用之前进行设定。  

&emsp;&emsp;**uncaughtExceptionHandler** ：//TODOTODO
* 由于线程的本质特性，无法在当前线程捕获到从其他线程中逃逸的异常，一旦异常逃逸出run方法，它就会向外传播到控制台
 * 而我们通常需要记录异常日志,所以就需要对线程做运行时的异常处理
 * JDK5 之后可以使用Executor来解决这个问题,通过给 Thread实例 设置一个(实现 UnCatchExceptionHandler接口)未捕获异常处理器
 * 也可以使用 Thread.setDefaultUnCatchExceptionHandler() 来配置默认的未捕获异常处理器

## 1.1线程状态转换图
&emsp;&emsp;下图中展示了线程各个状态以及转换状态的方法。

![6-线程状态转换图.jpg](./img/6-线程状态转换图.jpg)

### 线程优先级
优先级高的线程更容易抢到cpu时间片




## 1.2Thread中的常用方法

### Synchronized
- 指定加锁对象：对定对象加锁，进入同步代码块前要获得给定对象的锁
- 直接作用于实例方法：相当于对当前实例加锁，进入同步代码块前要获得当前实例的锁
- 直接作用于静态方法：相当于对当前类加锁，进入同步代码块前要获得当前类的锁，其实锁的是字节码对象


- ### start()和stop()
- - 线程启动   
调用线程的start方法，而不是run方法
- 直接调用run方法会被当前线程当作一次普通的方法调用，归属于当前的线程栈  
通过start方法启动，则会创建一个新的线程来执行
- stop 不推荐使用，stop会立即释放掉该线程所持有的锁，能无法正常释放自己所持有的资源，造成未知错误  
如果修改了一半就被stop掉，那数据也只会被修改一半  
windows的线程是抢占式的，意味着线程可以强制结束其他线程 
java的线程工作方式是协作式，这样设计是为了让线程自身能够在线程关闭前处理自己的数据  

- ### suspend()和resume()
- - 线程挂起(suspend)和线程继续执行(resume)  
不推荐使用，suspend()不会释放锁，如果resume在suspend前调用则会发生死锁  
线程一旦被挂起，下一次被唤醒会导致上下文切换  
让步式上下文切换耗费CPU时钟非常严重，通常高达80,000个时钟周期  
主频为3GHz的处理器每秒钟可用时钟周期为3,000,000,000  
jps 查看当前的Java进程、
jstack JVM自带的堆栈跟踪工具


- ### join()和yeild()
- - 等待线程结束(join)和谦让(yeild)  

join  
```java
public final void join() throws InterruptedException
public final synchronized void join(long millis) throws  InterruptedException

//主线程会等待线程t执行完或者抛出中断异常，再进行下一步操作
public static void main(String[]args) {
  Thread t=new Thread(A)
  t.start();
  try{
    t.join()
  }catch(IntrrruptedExection e){
  }
  System.out.println("ok")
}
```
```java
join的本质
while(isAlive()){
  wait(0);  //一直等待，直到被唤醒
}
```
线程执行完毕后，系统会调用notifyAll()  
因此不要在Thread实例上使用wait()和notify()  

yeild  
使当前线程放弃当前时间片，重新参与竞争，竞争成功后还是会执行的


- ### wait()和notify()/notifyAll()
### wait和notify
为什么将这两个方法定义在Object，我个人有如下理解  
wait和notify执行的前提是需要持有锁，而Java中锁可以是任意的Object对象，例如在字段上锁的是字段的对象，所以不能对基础类型加锁，除非封装为包装类，
加在方法上，锁的是调用这个方法的实例对象，加在静态方法上，锁的其实是该类对应的字节码对象，所以将这两个方法定义在Object中，也只能定义在Object中

![7-wait_notify.png](./images/7-wait_notify.png)

Object.wait()  
线程等待在当前对象上,调用的前提是获得object的锁对象,因为调用wait()方法会释放锁，那么必须先持有锁
```java
synchronized(obj){
  obj.wait();
}
```
Object.notify()/notifyAll()  
通知在这个对象上等待的线程，进行唤醒  
调用的前提也是获得object的锁对象,唤醒在这个锁对象上的一个线程(notifyAll会唤醒全部线程)，被唤醒的线程还不会立即执行，因为要等当前线程执行完释放锁后，才能去抢锁


- ### interrupt()、isInterrupted()和Thread.interrupted()
- 
- 线程中断  
sleep类型的阻塞，是可中断阻塞  
IO类型的阻塞，无法被中断  
试图获取synchronized锁类型的阻塞，无法被中断

interrupt
```java
//中断
//设置中断状态为true，如果线程被阻塞则能抛出InterruptedException异常
//如果线程处于就绪状态则不会直接中断，而是将线程状态改为中断状态，需要手动去检测线程的中断状态
//当抛出InterruptedException异常或者调用Thread.interrupted()时，中状态将被复位
public void Thread.interrupt()  //判断是否被中断，如果线程被阻塞则抛出异常
public boolean Thread.isInterrupted() //判断是否被中断
public static boolean Thread.interrupted(){ //实现Runnable接口的只能调用这个方法
  //判断是否被中断，并清除当前中断状态
  return currentThread().isInterrupted(true);
}

//错误写法  虽然线程设置为中断状态，但内部程序一直在执行
public void run(){
  while(true){
    //线程处于就绪状态
    ...
  }
}
t1.interrupt()

//正确写法   当线程被中断后，会执行完当前的操作后，进入下一轮循环的时候停止
public void run(){
  while(true){
    if(Thread.currentThread().isInterrupted()){
      System.out.println("Interrupted!");
      break;
    }
    ...
  }
}
t1.interrupt()

//当线程在休眠中，被别的线程打断时会抛出InterruptedExecption异常，通过这种方式来提前中断线程的休眠
public static native void sleep(long millis) throws InterruptedExecption
public void run(){
  while(true){
    if(Thread.currentThread().isInterrupted()){
      System.out.println("Interrupted!");
      break;
    }
    try{
      Thread.sleep(10000);
    }catch(InterruptedException e){
      //设置中断状态，抛出异常后会清除中断标记位
      Thread.interrupted();
    }
  }
  }
}

12. 说说 sleep() 方法和 wait() 方法区别和共同点?
        两者最主要的区别在于：sleep 方法没有释放锁，而 wait 方法释放了锁 。
        两者都可以暂停线程的执行。
        Wait 通常被用于线程间交互/通信，sleep 通常被用于暂停执行。
        wait() 方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的 notify() 或者 notifyAll() 方法。sleep() 方法执行完成后，线程会自动苏醒。或者可以使用wait(long timeout)超时后线程会自动苏醒。


16. sleep与wait的比较
    (1)/wait是Object类中的方法，sleep是Thread类中的方法；
    (2).sleep是Thread类的静态方法，谁调用，谁睡觉；
    (3).sleep方法调用之后并没有释放锁，使得线程仍然可以同步控制，sleep不会让出系统资源；
    (4).wait是进入线程等待池中等待，让出系统资源；
    (5).调用wait方法的线程，不会自己唤醒，需要线程调用notify/notifyAll方法唤醒等待池中的所有线程，
    才会进入就绪队列中等待系统分配资源。sleep方法会自动唤醒，如果时间不到，想要唤醒，可以使用interrupt方法强行打断；
    (6).sleep可以在任何地方使用，而wait/notify/notifyAll只能在同步控制方法或者同步控制块中使用；
    (7).sleep和wait必须捕获异常，notify/notifyAll不需要捕获异常；
    (8).wait通常被用于线程间交互，sleep通常被用于暂停执行。


16. 如何停止一个正在运行的线程
    (1).使用stop方法强行终止线程，不推荐，因为和suspend、resume等一样，都是过期作废的方法，可能产生不可预料的结果；
    (2).使用interrupt方法，而interrupt方法并未真正停止线程，只不过在线程中打了一个标记；此时可以使用抛异常的方式

17. sleep与yield的比较
    (1).sleep方法给其它线程运行机会时不考虑线程的优先级，因此会给低优先级的线程以运行的机会；
    yield方法只会给相同或更高优先级的线程以运行的机会；
    (2).sleep方法之后转入阻塞状态，yield方法之后转入就绪状态；
    (3).sleep方法声明抛出InterruptedException，而yield方法没有声明任何异常；
    (4).sleep方法具有更好的可移植性（yield不好控制，只是瞬间放弃CPU的执行权，有可能马上又抢回
    接着执行，而sleep更容易被控制）；
    (5).另外，Thread类的sleep和yield方法将在当前正在执行的线程上运行，所以在其它处于等待状态的
    线程上调用这些方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中
    工作，并避免程序员错误地认为可以在其它非运行线程调用这些方法。


18. 为什么wait/notify/notifyAll都必须放在同步方法/同步块中
        简单地说，由于wait、notify、notifyAll都是锁级别的操作，所以把它们定义在Object类中，因为锁属于对象。





11. 为什么我们调用 start() 方法时会执行 run() 方法，为什么我们不能直接调用 run() 方法？
       
21. 对守护线程的理解
    在Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程)
    用个比较通俗的比喻，任何一个守护线程都是整个JVM中所有非守护线程的保姆：只要当前JVM实例中尚存在任何一个非守护线程没有结束，守护线程就全部工作；只有当最后一个非守护线程结束时，守护线程随着JVM一同结束工作。

    退出的先后顺序：非守护线程 > 守护线程 > jvm。
    Daemon的作用是为其它线程的运行提供便利服务，守护线程最典型的应用就是 GC (垃圾回收器)，它就是一个很称职的守护者。

    User和Daemon两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：如果 User Thread已经全部退出运行了，只剩下Daemon Thread存在了，虚拟机也就退出了。因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必要了。

    优先级：守护线程的优先级比较低，用于为系统中的其它对象和线程提供服务。  值得一提的是，守护线程并非只有虚拟机内部提供，用户在编写程序时也可以自己设置守护线程。

    这里有几点需要注意：
    (1) thread.setDaemon(true)必须在thread.start()之前设置，否则会抛出一个IllegalThreadStateException异常。你不能把正在运行的常规线程设置为守护线程。
    (2) 在Daemon线程中产生的新线程也是Daemon的。
    (3) 不要认为所有的应用都可以分配给Daemon来进行服务，比如读写操作或者计算逻辑。



        
      




19. 线程是否可以被多次调用start方法
    Java的线程是不允许启动两次的，第二次调用必然会抛出IllegalThreadStateException，这是一种运行时异常，多次调用start被认为是编程错误。














