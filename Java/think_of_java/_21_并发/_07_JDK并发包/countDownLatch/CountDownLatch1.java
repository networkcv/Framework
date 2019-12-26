package _21_并发._07_JDK并发包.countDownLatch;


import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * create by lwj on 2019/11/7
 */
public class CountDownLatch1 {
    //    int countDown = 5;
    Object lock = new Object();

    @Test
    public void test() throws InterruptedException {
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

    volatile int countDown = 50000;

    @Test
    public void test1() {
        for (int i = 0; i < 50000; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " 检查完毕!");
                countDown--;
            }, i + "号线程").start();
        }
        while (countDown != 0) {}
        System.out.println("火箭发射！！");
    }

    @Test
    public void test2() throws InterruptedException {
        CountDownLatch end = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {

                System.out.println(Thread.currentThread().getName() + " 检查完毕!");
                end.countDown();
            }, i + "号线程").start();
        }
        end.await();
        System.out.println("火箭发射！！");
    }

}
