package com.lwj.springbootanalysis.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 2022/3/30
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class AQS2 {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock(true);
        Condition condition = reentrantLock.newCondition();
        Condition condition2 = reentrantLock.newCondition();
        Thread threadA = new Thread(() -> {
            reentrantLock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }, "ThreadA");

        Thread threadB = new Thread(() -> {
            reentrantLock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }, "ThreadB");
        threadA.start();
        threadB.start();
    }
}
