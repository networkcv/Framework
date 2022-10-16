package com.lwj._21_并发._04_终结任务;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/14
 * 中断线程
 */
public class InterruptDemo {

    @Test
    public void 中断Executors的某个线程() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Future<?> submit = cachedThreadPool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("over");
            }

        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        submit.cancel(true);
    }

    public static class SynchronizedBlocked implements Runnable {

        public synchronized void f() {
            while (true) {
            }
        }

        public SynchronizedBlocked() {
            new Thread(() -> {
                f();
            }).start();
        }

        @Override
        public void run() {
            System.out.println("Trying to call");
            f();
            System.out.println("Exiting SynchrnoizedBlocked.run()");
        }
    }


    static ExecutorService exec = Executors.newCachedThreadPool();

    static void test(Runnable runnable) throws InterruptedException {
        Future<?> future = exec.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Interrupting  " + runnable.getClass().getName());
        future.cancel(true);
        System.out.println("Interrupt send to " + runnable.getClass().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        //sleep类型的阻塞，是可中断阻塞
        Thread sleepBlocked = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
            System.out.println("Exiting SleepBlocked.run()");
        });

        //IO类型的阻塞，无法被中断
        Thread iOBlocked = new Thread(() -> {
            InputStream is = System.in;
            try {
                System.out.println("Waiting for read()");
                is.read();
            } catch (IOException e) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted from blocked I/O");
                } else {
                    throw new RuntimeException();
                }
            }
            System.out.println("Exiting IOBlocked.run()");
        });

//        test(sleepBlocked);
//        test(iOBlocked);
        //试图获取synchronized锁类型的阻塞，无法被中断
        test(new SynchronizedBlocked());

    }

}
