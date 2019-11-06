package JDK并发包.reentrantLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 * ReentrantLock的可中断性
 */
public class MyReentrantLock3_可中断性 extends Thread {
    public static ReentrantLock lock = new ReentrantLock();

    @Test
//    ReentrantLock 对中断信号有响应
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println("t1已拿到锁，开始休眠");
            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            System.out.println("t2启动，准备获取锁");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                System.out.println("t2已中断");
            }
        }, "t2");
        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t3休眠结束，中断t2线程");
            t2.interrupt();
        });
        t1.start();
        t2.start();
        t3.start();
        t2.join();
    }

    @Test
    //synchronized 对中断信号没有响应
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //没有调用lock内部方法，此时的lock是当作一个锁对象来使用的，可以理解为new Object()
            synchronized (lock) {
                System.out.println("t1已拿到锁，开始休眠");
                try {
                    TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            System.out.println("t2启动，准备获取锁");
            synchronized (lock) {
            }
        }, "t2");
        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t3休眠结束，中断t2线程");
            t2.interrupt();
        });
        t1.start();
        t2.start();

        t3.start();
        t2.join();
    }

}
