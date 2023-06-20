package com.lwj.deathlock;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2023/5/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class DeathLockTest {
    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread-a").start();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread-b").start();
    }
}
