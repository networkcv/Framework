package _21_并发._07_JDK并发包.reentrantLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 * ReentrantLock的基本使用
 */
public class MyReentrantLock1_基本使用 extends Thread {
    public static int i = 0;
    public static Object lock = new Object();
    public static ReentrantLock reentrantLock = new ReentrantLock();

    //不加锁，两个线程并发累加，结果小于预期结果 线程不安全
    @Test
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                i++;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                i++;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

    //使用synchronized加锁，结果等于预期结果 线程安全
    @Test
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                synchronized (lock) {
                    i++;
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                synchronized (lock) {
                    i++;
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

    //使用ReentrantLock加锁
    @Test
    public void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    i++;
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    reentrantLock.tryLock(1, TimeUnit.SECONDS);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }


}
