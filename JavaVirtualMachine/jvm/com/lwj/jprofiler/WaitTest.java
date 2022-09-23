package com.lwj.jprofiler;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2022/9/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class WaitTest {
    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        synchronized (o) {
            new Thread(() -> {
                synchronized (o) {
                    try {
                        System.out.println(3);
                        TimeUnit.SECONDS.sleep(5);
                        o.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "bThread").start();
            System.out.println(1);
            o.wait();
            System.out.println(2);
        }
    }
}
