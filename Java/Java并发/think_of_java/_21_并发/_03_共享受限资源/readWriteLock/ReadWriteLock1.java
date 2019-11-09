package think_of_java._21_并发._03_共享受限资源.readWriteLock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * create by lwj on 2019/11/7
 */
public class ReadWriteLock1 {
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Test
    //可以进行锁的降级，从写变成读
    public void test() {
        new Thread(() -> {
            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");

            System.out.println("释放写锁");
            readWriteLock.readLock().unlock();
            System.out.println("释放读锁");
            readWriteLock.writeLock().unlock();
        }).start();
        try {
            TimeUnit.SECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    //无法进行锁的升级，从读变成写
    public void test2() {
        new Thread(() -> {
            readWriteLock.readLock().lock();
            System.out.println("获取读锁");
            readWriteLock.writeLock().lock();
            System.out.println("获取写锁");

            System.out.println("释放写锁");
            readWriteLock.readLock().unlock();
            System.out.println("释放读锁");
            readWriteLock.writeLock().unlock();
        }).start();
        try {
            TimeUnit.SECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
