package JDK并发包.reentrantLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 * ReentrantLock的可重入性
 */
public class MyReentrantLock2_可重入性 extends Thread {
    public static int i = 0;
    public static ReentrantLock reentrantLock = new ReentrantLock();

    //可重入锁，加几次锁，就必须要释放几次锁,不然会导致死锁
    @Test
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    reentrantLock.lock();
                    i++;
                } finally {
                    //只释放一次
                    reentrantLock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    i++;
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

    @Test
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    reentrantLock.lock();
                    i++;
                } finally {
                    //加几次释放几次
                    reentrantLock.unlock();
                    reentrantLock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                try {
                    reentrantLock.lock();
                    i++;
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
