package com.lwj._21_并发._07_JDK并发包.cyclicBarrier;

import org.junit.Test;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.concurrent.*;

/**
 * create by lwj on 2019/12/27
 */
public class CyclicBarrier2_基本使用 {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(10, () -> {
            System.out.println(Thread.currentThread().getName() + " --A Game Begin--");
        });
        for (int i = 0; i < 33; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(finalI + "号玩家ok");
                    barrier.await(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    System.out.println("不等了");
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(finalI + " is ok!");
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + " 加入游戏");
            }).start();
        }
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void test2() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println(Thread.currentThread().getName() + " print all is ok");
        });
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is ok!");
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, i + "号玩家").start();
        }

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void test3() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println(Thread.currentThread().getName() + " print all is ok");
        });
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is ok!");
                    barrier.await(100,TimeUnit.MILLISECONDS);
                    System.out.println(Thread.currentThread().getName() +" 开始游戏");
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
//                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName()+" 不等了");
                }
            }, i + "号玩家").start();
        }
        TimeUnit.SECONDS.sleep(2);
    }
}
