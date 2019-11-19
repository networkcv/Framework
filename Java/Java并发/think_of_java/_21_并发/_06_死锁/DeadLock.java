package think_of_java._21_并发._06_死锁;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/19
 */
public class DeadLock {
    @Test
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                }
                System.out.println("thread1 over");
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                }
                System.out.println("thread2 over");
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
