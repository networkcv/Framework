package com.lwj._21_并发._07_JDK并发包.semaphore;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/21
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5, false);
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire(3);
                System.out.println("拿到了3个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(1);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(2);
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(semaphore.availablePermits());

        new Thread(() -> {
            try {
                semaphore.acquire(4);
                System.out.println("拿到了4个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(4);
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                semaphore.acquire(3);
                System.out.println("拿到了3个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(3);
        }).start();
    }

    @Test
    public void test() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        thread.start();

        Thread thread1 = new Thread(() -> {
            try {
                semaphore.release();
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        thread1.start();

        thread.join();
        thread1.join();

    }
}
