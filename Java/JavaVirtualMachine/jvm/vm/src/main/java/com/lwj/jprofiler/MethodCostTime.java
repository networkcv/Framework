package com.lwj.jprofiler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 2022/9/2
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MethodCostTime {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            for (int i = 0; i < 90000; i++) {
                ReentrantLock reentrantLock = new ReentrantLock();
                System.out.println(reentrantLock);
            }
            fun1();
            fun2();
            fun3();
            System.out.println("over");
            TimeUnit.SECONDS.sleep(5);
        }
    }

    private static void fun3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

    private static void fun2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        fun22();
    }

    private static void fun22() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }

    private static void fun1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }
}
