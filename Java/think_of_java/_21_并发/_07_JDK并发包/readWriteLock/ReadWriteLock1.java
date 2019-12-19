package _21_并发._07_JDK并发包.readWriteLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * create by lwj on 2019/11/7
 * 基本使用
 */
public class ReadWriteLock1 {
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Test
    public void test3() throws InterruptedException {
        Thread thread = new Thread(() -> {
            readWriteLock.writeLock().lock();
            Condition condition = readWriteLock.writeLock().newCondition();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(1);
        });
        Thread thread2 = new Thread(() -> {
            readWriteLock.writeLock().lock();
            System.out.println(2);
        });
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
    }

    @Test
    //可以进行锁的降级，从写变成读
    public void test() throws InterruptedException {
        Thread thread = new Thread(() -> {
            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");

            readWriteLock.readLock().unlock();
            System.out.println("释放写锁");
            readWriteLock.writeLock().unlock();
            System.out.println("释放读锁");
        });
        thread.start();
        thread.join();
    }

    @Test
    //无法进行锁的升级，从读变成写
    public void test2() throws InterruptedException {
        Thread thread = new Thread(() -> {
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");

            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");

            readWriteLock.writeLock().unlock();
            System.out.println("释放写锁");

            readWriteLock.readLock().unlock();
            System.out.println("释放读锁");

        });
        thread.start();
        thread.join();
    }
}
