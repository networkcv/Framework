package com.lwj._21_并发._07_JDK并发包.condition;


import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 */
public class Condition_基本使用 {
    private ReentrantLock lock = new ReentrantLock();
    //通过lock来绑定condition,好比wait和notify必须在持有锁的情况下使用
    private Condition condition = lock.newCondition();

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //t1拿到锁
            lock.lock();
            try {
                //释放锁，进行等待
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //被唤醒后打印
            System.out.println("t1 被唤醒");
        });
        Thread t2 = new Thread(() -> {
            try {
                //t2启动后先进行休眠，确保t1先拿到锁
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            System.out.println("获取到了t1，await() 释放的锁");
            //唤醒在wait中的t1
            condition.signal();
            //这行代码很重要，t2将t1唤醒后，t1不会立即执行，而是会进入Runnable状态，重新争夺锁
            lock.unlock();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }
}
