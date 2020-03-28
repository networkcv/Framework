### 线程的五大状态

​    新建：new Thread()后处于新建状态
​    就绪：start方法启动线程，等待cpu分给时间片来调用run方法
​    运行：获得时间片后，run方法被调用
​    阻塞：可能因为I/O，线程调用sleep方法 或 访问加锁的公共资源 时，进入阻塞状态，将时间片让给其他就绪线程使用
​        1.join 合并线程
​        2.yield 暂停自己的线程 static 但不是绝对意义上的暂停，当cpu再次请求调度该线程的时候，状态从阻塞转为就绪，获取时间片后执行线程
​        3.sleep 休眠 暂停当前线程 不会释放锁， 排他锁 抱着对象休眠 ，可以用来倒计时，或者模拟网络延时
​    死亡：run方法调用完成自然死亡，一个未捕获的异常终止了run方法导致线程提前死亡，可以使用isAlive方法，判断线程是否在可运行状态（就绪运行阻塞），另外stop方法可以提前结束线程

    isAlive()   判断线程是否还活着，即线程是否还未终止
    getPriority()   获取线程优先级数值  优先级是获取cpu时间片的概率
    setPriority()   设置线程优先级数值  10最大 1最小 5正常
    getName()   获取线程名称
    setName()   设置线程名称
    currentThread() 获取当前正在运行的线程对象，取得自己本身
    
    线程同步与锁定 synchronized
    同步：多线程对同一份资源的共同访问，造成资源的不安全性，为了资源的准确和安全所以需要加入同步
        一、同步块
            synchronized(引用类型|this|类.class){}
        二、同步方法
            synchronized
        三、死锁：过多的同步容易造成死锁
            解决办法：生产者消费者模式 信号灯法(建立标志位) 管程法(使用容器)
            wait()  等待，释放锁/资源  sleep() 等待 不释放资源/锁
            notify() notifyall()   唤醒
            wait 和 notify 在同步下使用notify
    参考：
    https://blog.csdn.net/peter_teng/article/details/10197785

### 