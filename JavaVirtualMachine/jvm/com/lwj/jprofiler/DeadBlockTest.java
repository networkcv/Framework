package com.lwj.jprofiler;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2022/9/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class DeadBlockTest {
    public static void main(String[] args) {
        Object a = new Object();
        Object b = new Object();
        new Thread(() -> {
            synchronized (a) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (b) {
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "a").start();
        new Thread(() -> {
            synchronized (b) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (a) {
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "b").start();
    }
}
