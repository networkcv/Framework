package _21_并发._99_test;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/12
 * 开三个线程顺序交替打印ABC，循环10次
 */
public class 顺序交替打印ABC {
    @Test
    //交替打印AB Bywait()和notify()
    public void test() {
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                synchronized (lock) {
                    System.out.println("A");
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                synchronized (lock) {
                    System.out.println("BBBBBB");
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    volatile int state = 0;
    private Object lock = new Object();

    @Test
    //交替打印ABC volatile synchronized
    public void test1() {
        T1 t1 = new T1();
        T2 t2 = new T2();
        T3 t3 = new T3();
        t1.start();
        t2.start();
        t3.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class T1 extends Thread {
        int count = 0;

        @Override
        public void run() {
            while (true) {
                if (state == 0) {
                    synchronized (lock) {
                        System.out.print(" A");
                        state = 1;
                        if (count++ == 3)
                            break;
                    }

                }
            }
        }
    }

    private class T2 extends Thread {
        int count = 0;

        @Override
        public void run() {
            while (true) {
                if (state == 1) {
                    synchronized (lock) {
                        System.out.print(" B");
                        state = 2;
                        if (count++ == 3)
                            break;
                    }
                }
            }
        }
    }

    private class T3 extends Thread {
        int count = 0;

        @Override
        public void run() {
            while (true) {
                if (state == 2) {
                    synchronized (lock) {
                        System.out.print(" C");
                        state = 0;
                        if (count++ == 3)
                            break;
                    }
                }
            }
        }
    }

    @Test
    public void test4() throws InterruptedException {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        Object poll = linkedBlockingQueue.take();
        System.out.println(poll);
    }


}
