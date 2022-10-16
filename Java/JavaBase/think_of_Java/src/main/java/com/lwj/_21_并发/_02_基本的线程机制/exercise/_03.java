package com.lwj._21_并发._02_基本的线程机制.exercise;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/8
 * 常见线程池的使用
 */
public class _03 implements Runnable {

    @Test
    public void test() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //固定容量的线程池
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);
        //单例线程池
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 3; i++) {
            cachedThreadPool.execute(new Thread(new _03(), "cachedThreadPool " + i));
        }
        cachedThreadPool.shutdown();

        for (int i = 0; i < 3; i++) {
            newFixedThreadPool.execute(new Thread(new _03(), "newFixedThreadPool " + i));
        }
        newFixedThreadPool.shutdown();

        for (int i = 0; i < 3; i++) {
            singleThreadExecutor.execute(new Thread(new _03(), "singleThreadExecutor " + i));
        }
        singleThreadExecutor.shutdown();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " -> " + i);
            Thread.yield();
        }
    }
}
