package JDK并发包.reentrantLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 * ReentrantLock的公平锁
 */
public class MyReentrantLock5_公平锁 extends Thread {
    //设置为公平锁
    public static ReentrantLock lock = new ReentrantLock(true);

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                //线程启动后先休眠1s，尽量保证量两个线程同时抢锁
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获得锁");
                lock.unlock();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获得锁");
                lock.unlock();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
