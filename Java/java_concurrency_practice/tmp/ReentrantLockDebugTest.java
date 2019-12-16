package tmp;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/12/13
 */
public class ReentrantLockDebugTest {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        Thread thread = new Thread(() -> {
            reentrantLock.lock();
            try {
                condition.await();
                System.out.println("0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }, "t1");
        Thread thread1 = new Thread(() -> {
            try {
                reentrantLock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signal();
            System.out.println("1");
            reentrantLock.unlock();
        }, "t2");
        Thread thread2 = new Thread(() -> {
            thread1.interrupt();
        }, "t3");
        thread.start();
        thread1.start();
        thread2.start();
    }
}
