package _21_并发._01_并行设计模式._03_生产者消费者模式;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/18
 */
public class 内置锁_synchronized {
    static int count = 0;

    @Test
    public void test() {
        //生产者
        Object lock = new Object();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (lock) {
                    System.out.println("生产者-生产" + ++count);
                    lock.notifyAll();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //消费者
        new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (lock) {
                    System.out.println("消费者-消费" + count);
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
