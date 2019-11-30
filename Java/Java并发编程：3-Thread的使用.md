
## 1.3线程的常用操作
线程在生命周期中并不是固定处于某一个状态而是随着代码的执行在不同状态之间切换。

![6-线程状态转换图.jpg](./img/6-线程状态转换图.jpg)


1. 说说线程的生命周期和状态?   
线程创建之后它将处于 NEW（新建） 状态
调用 start() 方法后开始运行，线程这时候处于 READY（可运行） 状态。可运行状态的线程获得了 CPU 时间片（timeslice）后就处于 RUNNING（运行） 状态。Java系统将 RUNNABLE 和 RUNNING 这两个状态统称为 RUNNABLE（运行中） 状态 。
当线程执行 wait()方法之后，线程进入 WAITING（等待） 状态。进入等待状态的线程需要依靠其他线程的通知才能够返回到运行状态，而 TIME_WAITING(超时等待) 状态相当于在等待状态的基础上增加了超时限制，
比如通过 sleep（long millis）方法或 wait（long millis）方法可以将 Java 线程置于 TIMED WAITING 状态。当超时时间到达后 Java 线程将会返回到 RUNNABLE 状态。
当线程调用同步方法时，在没有获取到锁的情况下，线程将会进入到 BLOCKED（阻塞） 状态。
线程在执行 Runnable 的run()方法之后将会进入到 TERMINATED（终止） 状态。

