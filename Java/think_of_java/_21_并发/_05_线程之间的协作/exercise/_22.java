package _21_并发._05_线程之间的协作.exercise;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/18
 * 忙等待和wait()
 */
public class _22 {
    public static boolean flag = false;

    @Test
    public void test() {
        new Thread(() -> {
            long start = System.nanoTime();
            //忙等待
            while (!Thread.interrupted()) {
                if (flag) {
                    System.out.println("flag: " + flag);
                    flag = false;
                    long end = System.nanoTime();
                    System.out.println("耗时:" + (end - start));
                }
            }
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
        }).start();


        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long start = System.nanoTime();
            while (!Thread.interrupted()) {
                if (flag) {
                    System.out.println("flag: " + flag);
                    flag = false;
                    long end = System.nanoTime();
                    System.out.println("耗时:" + (end - start));
                }
            }
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            synchronized (lock) {
                lock.notify();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
