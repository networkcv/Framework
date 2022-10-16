package com.lwj._21_并发._02_基本的线程机制.exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * create by lwj on 2019/11/8
 * 自定义可以设置优先级的ThreadFactroy
 */
public class _09 {
    public static class EnnableSetPrioritiesThreadFacory implements ThreadFactory {

        int priority;

        public EnnableSetPrioritiesThreadFacory(int priority) {
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(priority);
            return thread;
        }

        public static void main(String[] args) {
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new EnnableSetPrioritiesThreadFacory(3));
            cachedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " priorities: " + Thread.currentThread().getPriority());
            });
            cachedThreadPool.shutdown();
        }
    }

}
