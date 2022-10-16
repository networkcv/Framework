package com.lwj._21_并发._02_基本的线程机制.exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2019/11/8
 * 打印斐波数列
 */
public class _04 implements Runnable {

    static int n;

    public _04(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        System.out.print(Thread.currentThread().getName() + " ");
        printFibo(n);
        System.out.println();
    }

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            cachedThreadPool.execute(new Thread(new _04(5), "cachedThreadPool" + i));
        }
        cachedThreadPool.shutdown();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            fixedThreadPool.execute(new Thread(new _04(5), "cachedThreadPool" + i));
        }
        fixedThreadPool.shutdown();

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 3; i++) {
            singleThreadExecutor.execute(new Thread(new _04(5), "cachedThreadPool" + i));
        }
        singleThreadExecutor.shutdown();
    }

    void printFibo(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.print(getFibo(i) + " ");
        }
    }

    private int getFibo(int n) {
        if (n == 1 || n == 2)
            return 1;
        else
            return getFibo(n - 1) + getFibo(n - 2);
    }
}
