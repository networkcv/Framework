package com.lwj._21_并发.Condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 2022/5/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ConditionTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            try {
                lock.lock();
                condition.await();
                System.out.println("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
        Thread.sleep(100);
        lock.lock();
        condition.signalAll();
        lock.unlock();
    }
}
