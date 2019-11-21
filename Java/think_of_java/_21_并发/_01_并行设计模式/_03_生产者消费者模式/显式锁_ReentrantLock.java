package think_of_java._21_并发._01_并行设计模式._03_生产者消费者模式;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/18
 */
public class 显式锁_ReentrantLock {
    volatile int count;

    @Test
    public void test() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    lock.lock();
                    System.out.println("生产者-生产" + ++count);
                    condition.signalAll();
                    TimeUnit.MILLISECONDS.sleep(500);
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    lock.lock();
                    System.out.println("消费者-消费" + count);
                    condition.signalAll();
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
