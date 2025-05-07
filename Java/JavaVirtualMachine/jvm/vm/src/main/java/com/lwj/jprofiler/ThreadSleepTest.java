package com.lwj.jprofiler;


import java.util.concurrent.TimeUnit;

/**
 * Date: 2022/9/2
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ThreadSleepTest {
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
//                    TimeUnit.MILLISECONDS.sleep(10000);
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = 0;
                while (i++ < 10000) {
                    System.out.println(1);
                }
            }
        }, "aaa").start();
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = 0;
                while (i++ < 10000) {
                    System.out.println(2);
                }
            }
        }, "bbb").start();
    }
}
