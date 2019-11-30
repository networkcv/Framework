## 前言： 
上一篇中我们了解到进程和线程的区别，以及使用多线程的优缺点，本篇主要讲在Java中是如何去创建一个线程，以及线程相关的操作。

线程的生命周期  
start(),run(),join(),yeild(),sleep()  
suspend()resume()  
interrupt(),isInterrupted(),interrupted()  
wait(),notify(),notifyAll()  
守护线程  

## 面试问题
Q : 线程的实现方式?  
Q : 线程的生命周期和状态?  
Q ：start()和run()的区别?


# Thread类的使用
## 1.1如何创建一个线程
## 1.2线程的生命周期和状态
以下为Java中线程的各个状态，是虚拟机层面上暴露给我们的状态，这些状态是由枚举类Thread.State中明确定义的，和操作系统中的状态有所出入。

![5-Java线程的状态.png](./img/5-Java线程的状态.png)
- 初始状态：当线程被创建且还没有调用start（）方法时,它会处于这种状态。此时它已经分配了必需的系统资源,并执行了初始化。线程当前还没有资格获取CPU的时间片。
```java
        Thread thread = new Thread("MyThread");
        System.out.println(thread.getName()+"当前状态："+thread.getState());
        
        //Output
        // MyThread当前状态：NEW
```

- 运行状态：在初始状态下调用start（）方法会进入当前状态，线程已经有资格获得CPU时间片了，在这种状态下，只要调度器把时间片分配给线程，线程就可以运行，处于运行中状态；如果没有拿到，只能等待获取时间片，此时线程处于就绪状态，统称为运行状态。
```java
        Thread thread = new Thread("MyThread");
        thread.start();
        System.out.println(thread.getName()+"当前状态："+thread.getState());
                
        //Output
        // MyThread当前状态：RUNNABLE

```
- 阻塞状态：线程本来能够运行，但由于等待获取某个锁而阻止它的运行。当线程处于阻塞状态时，调度器将忽略线程，不会分配给线程任何CPU时间。直到线程重新进入了就绪状态,它才有可能执行操作。
```java
        Object lock = new Object();
        Thread thread = new Thread("MyThread") {
            public void run() {
                synchronized (lock) {
                    while (true) {}
                }
            }
        };

        Thread thread2 = new Thread("MyThread2") {
            public void run() {
                synchronized (lock) {
                    while (true) {}
                }
            }
        };
        thread.start();
        thread2.start();
        Thread.sleep(100);
        System.out.println(thread.getName() + "当前状态：" + thread.getState());
        System.out.println(thread2.getName() + "当前状态：" + thread2.getState());

        //Output
        // MyThread当前状态：RUNNABLE
        // MyThread2当前状态：BLOCKED

```
- 等待状态：线程进入等待状态后不会被分到时间片，无法继续执行，而且必须要其他线程进行操作才能被唤醒。
```java
        Thread thread = new Thread("MyThread") {
            public void run() {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        Thread.sleep(100);
        System.out.println(thread.getName() + "当前状态：" + thread.getState());

        //Output
        // MyThread当前状态：WAITING
```
- 超时等待状态：和等待状态类似，区别在于这个状态会待一段时间，时间到了线程可以将自己唤醒。
```java
        Thread thread = new Thread("MyThread") {
            public void run() {
                synchronized (this) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        Thread.sleep(100);
        System.out.println(thread.getName() + "当前状态：" + thread.getState());

        //Output
        // MyThread当前状态：TIMED_WAITING
```

- 终止状态：终止状态的线程将不再是可调度的,再也不会得到CPU时间,它的任务已结束。任务终止通常是run()方法执行完成，或者程序抛出异常。
```java
        Thread thread = new Thread("MyThread") {
            public void run() {
                System.out.println((Thread.currentThread().getName() + " 执行完毕"));
            }
        };
        Thread thread2 = new Thread("MyThread2") {
            public void run() {
                throw new RuntimeException("程序执行出错了");
            }
        };
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        System.out.println(thread.getName() + "当前状态：" + thread.getState());
        System.out.println(thread2.getName() + "当前状态：" + thread2.getState());

        //Output
        // MyThread 执行完毕
        // Exception in thread "MyThread2" java.lang.RuntimeException: 程序执行出错了
        // 	 at tmp.NewThreadTest$4.run(NewThreadTest.java:47)
        // MyThread当前状态：TERMINATED
        // MyThread2当前状态：TERMINATED
```

### 临界区
临界区用来表示一种公共资源或者说是共享数据，可以被多个线程使用，但是每次只能一个线程使用它，一旦临界区资源被占用，其他线程想要使用这个资源，就必须等待。

![7-临界区.jpg](./img/7-临界区.jpg)

### 阻塞(Blocking)和非阻塞(Non-Blocking)
https://blog.csdn.net/historyasamirror/article/details/5778378  

阻塞非阻塞是关于线程与进程的。   
阻塞是指调用线程或者进程被操作系统挂起。非阻塞是指调用线程或者进程不会被操作系统挂起。

阻塞和非阻塞通常用来形容多线程间的相互影响，比如一个线程占用了临界区资源，那么其他所有需要这个资源的线程就必须在这个临界区外进行等待，等待会导致线程挂起。这种情况就是阻塞。此时，如果占用资源的线程一直不愿意释放资源，那么其他所有阻塞在这个临界区上的线程都不能工作  

## 1.3线程的常用操作
线程在生命周期中并不是固定处于某一个状态而是随着代码的执行在不同状态之间切换。

![6-线程状态转换图.jpg](./img/6-线程状态转换图.jpg)


8. 说说线程的生命周期和状态?   
线程创建之后它将处于 NEW（新建） 状态
调用 start() 方法后开始运行，线程这时候处于 READY（可运行） 状态。可运行状态的线程获得了 CPU 时间片（timeslice）后就处于 RUNNING（运行） 状态。Java系统将 RUNNABLE 和 RUNNING 这两个状态统称为 RUNNABLE（运行中） 状态 。
当线程执行 wait()方法之后，线程进入 WAITING（等待） 状态。进入等待状态的线程需要依靠其他线程的通知才能够返回到运行状态，而 TIME_WAITING(超时等待) 状态相当于在等待状态的基础上增加了超时限制，
比如通过 sleep（long millis）方法或 wait（long millis）方法可以将 Java 线程置于 TIMED WAITING 状态。当超时时间到达后 Java 线程将会返回到 RUNNABLE 状态。
当线程调用同步方法时，在没有获取到锁的情况下，线程将会进入到 BLOCKED（阻塞） 状态。
线程在执行 Runnable 的run()方法之后将会进入到 TERMINATED（终止） 状态。

