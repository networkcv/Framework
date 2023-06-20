package com.lwj.deathlock;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2023/5/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class DeathLockTest2 {
    private static Object lock = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (lock2) {}
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread-a").start();
        new Thread(() -> {
            synchronized (lock2) {
                try {
                    synchronized (lock) {}
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread-b").start();
    }
}
