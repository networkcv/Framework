package com.lwj.springbootanalysis.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 2022/3/30
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class AQS {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock(true);
        Thread threadA = new Thread(() -> {
            reentrantLock.lock();
            reentrantLock.unlock();
        }, "ThreadA");
        Thread threadB = new Thread(() -> {
            reentrantLock.lock();
            reentrantLock.unlock();
        }, "ThreadB");
        threadA.start();
        threadB.start();
    }
}
