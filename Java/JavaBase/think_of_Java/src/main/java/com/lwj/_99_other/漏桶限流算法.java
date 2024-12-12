package com.lwj._99_other;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

// 漏桶 限流
@Slf4j
public class 漏桶限流算法 {

    // 计算的起始时间
    private static long lastOutTime = System.currentTimeMillis();
    // 流出速率 每秒2次 不能表示每秒只能2个请求通过
    private static int leakRate = 2;
    // 桶的容量
    private static int capacity = 6;
    //剩余的水量
    private static AtomicInteger water = new AtomicInteger(0);
    //返回值说明：
    // false 没有被限制到
    // true 被限流
    public static synchronized boolean isLimit(long taskId, int turn) {
        // 如果是空桶，就当前时间作为漏出的时间
        if (water.get() == 0) {
            lastOutTime = System.currentTimeMillis();
            water.addAndGet(1);
            return false;
        }
        // 执行漏水
        long timeDiff = System.currentTimeMillis() - lastOutTime;
        int waterLeaked = ((int) (timeDiff / 1000)) * leakRate;
        // 计算剩余水量
        int waterLeft = water.get() - waterLeaked;
        water.set(Math.max(0, waterLeft));
        // 重新更新leakTimeStamp
        lastOutTime = System.currentTimeMillis();
        // 尝试加水,并且水还未满 ，放行
        if ((water.get()) < capacity) {
            water.addAndGet(1);
            return false;
        } else {
            // 水满，拒绝加水， 限流
            return true;
        }
    }


    //线程池，用于多线程模拟测试
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    public static void sleep(int mills) throws InterruptedException {
        Thread.sleep(mills);
    }

    public static void main(String[] args) throws InterruptedException {

        // 被限制的次数
        AtomicInteger limited = new AtomicInteger(0);
        // 线程数
        final int threads = 3;
        // 每条线程的执行轮数
        final int turns = 5;
        // 线程同步器
        long start = System.currentTimeMillis();
        for (int i = 1; i <= turns; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(threads);
            for (int j = 0; j < threads; j++) {
                int finalI = i;
                pool.submit(() -> {
                    countDownLatch.countDown();
                    long taskId = Thread.currentThread().getId();
                    boolean intercepted = isLimit(taskId, finalI);
                    if (intercepted) {
                        System.out.println();
                        // 被限制的次数累积
                        limited.getAndIncrement();
                        log.info("被限制 taskId:{} turn:{}", taskId, finalI);
                    }
                });
                countDownLatch.countDown();
            }
            countDownLatch.await();
            sleep(1000);
        }
        pool.shutdown();
        float time = (System.currentTimeMillis() - start) / 1000F;
        //输出统计结果
        log.info("限制的次数为：{},通过的次数为：{}", limited.get(), (threads * turns - limited.get()));
        log.info("限制的比例为：{}", (float) limited.get() / (float) (threads * turns));
        log.info("运行的时长为：{}", time);
    }
}

