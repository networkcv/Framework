package com.lwj.jvisualvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Demo7_JConsole02 {
    /**
     * 线程死循环演示
     */
    public static void createBusyThread() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true);
            }
        }, "testBusyThread");
        System.out.println("启动testBusyThread 线程完毕..");
        thread.start();
    }

    /**
     * 线程锁等待演示
     */
    public static void createLockThread(final Object lock) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "testLockThread");

        thread.start();
        System.out.println("启动testLockThread 线程完毕..");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("main 线程..");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("redLine阻塞");
        br.readLine();
        createBusyThread();
        System.out.println("redLine阻塞");
        br.readLine();
        Object obj = new Object();
        createLockThread(obj);
        System.out.println("main 线程结束..");
    }
}
