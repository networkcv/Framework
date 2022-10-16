package com.lwj.jprofiler;

/**
 * Date: 2022/9/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class 循环打印ABC {

    static volatile int i = 0;

    public static void main(String[] args) throws InterruptedException {
//        volatileFun();
        waitFun();
    }

    static Object a = new Object();
    static Object b = new Object();
    static Object c = new Object();

    private static void waitFun() throws InterruptedException {
        new Thread(() -> {
            int i = 0;
            while (i++ < 4) {
                synchronized (a) {
                    synchronized (b) {
                        System.out.println("A");
                        b.notify();
                    }
                    try {
                        a.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            while (true) {


                synchronized (b) {
                    synchronized (c) {
                        System.out.println("B");
                        c.notify();
                    }
                    try {
                        b.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            while (true) {
                synchronized (c) {
                    synchronized (a) {
                        System.out.println("C");
                        a.notify();

                    }
                    try {
                        c.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void volatileFun() {
        new Thread(() -> {
            while (i < 10) {
                if (i % 3 == 0) {
                    System.out.println("A");
                    i++;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                if (i % 3 == 1) {
                    System.out.println("B");
                    i++;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                if (i % 3 == 2) {
                    System.out.println("C");
                    i++;
                }
            }
        }).start();
    }
}
