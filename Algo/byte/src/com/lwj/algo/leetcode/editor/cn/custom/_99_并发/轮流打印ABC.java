package com.lwj.algo.leetcode.editor.cn.custom._99_并发;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 2025/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class 轮流打印ABC {
    private static final Lock lock = new ReentrantLock();
    private static int count = 1;

    public static void main(String[] args) {
        Thread a = new Thread(new PrintRunnable("a", 1));
        Thread b = new Thread(new PrintRunnable("b", 2));
        Thread c = new Thread(new PrintRunnable("c", 3));

        a.start();
        b.start();
        c.start();
    }

    static class PrintRunnable implements Runnable {
        private final String threadName;
        private final int startNum;

        public PrintRunnable(String threadName, int startNum) {
            this.threadName = threadName;
            this.startNum = startNum;
        }

        @Override
        public void run() {
            int num = startNum;
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock.lock();
                try {
                    if (threadName.equals("a") && count % 3 == 1) {
                        System.out.println(num);
                        count++;
                        num += 3;
                    } else if (threadName.equals("b") && count % 3 == 2) {
                        System.out.println(num);
                        count++;
                        num += 3;
                    } else if (threadName.equals("c") && count % 3 == 0) {
                        System.out.println(num);
                        count++;
                        num += 3;
                    }
                } finally {
                    lock.unlock();
                    Thread.yield();
                }
            }
        }
    }
}
