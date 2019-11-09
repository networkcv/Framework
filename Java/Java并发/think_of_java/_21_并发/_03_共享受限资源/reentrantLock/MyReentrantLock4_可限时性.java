package think_of_java._21_并发._03_共享受限资源.reentrantLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/6
 * ReentrantLock的可限时性
 */
public class MyReentrantLock4_可限时性 extends Thread {
    public static ReentrantLock lock = new ReentrantLock();

    @Test
    //t1先启动获取锁，一直休眠不释放锁，t2无法在3秒内获取锁，打印获取锁失败
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 已启动");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            try {
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁成功");
                } else {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁失败");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    //t1先启动获取锁，随后释放锁，t2可以在3秒内获取锁，打印获取锁成功
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " 已启动");
            lock.unlock();
        }, "t1");
        Thread t2 = new Thread(() -> {
            try {
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁成功");
                } else {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁失败");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}
