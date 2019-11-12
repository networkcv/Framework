package think_of_java._21_并发._99_test;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/12
 * 开三个线程顺序交替打印ABC，循环10次
 */
public class 顺序交替打印ABC {
    @Test
    public void test() throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Object lock3 = new Object();
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("A");
                    lock2.notify();
                    try {
                        lock1.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        new Thread(() -> {
            synchronized (lock2) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("B");
                    try {
                        lock2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        new Thread(() -> {
            synchronized (lock3) {
                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("C");
                }
            }
        }).start();

    }

}
