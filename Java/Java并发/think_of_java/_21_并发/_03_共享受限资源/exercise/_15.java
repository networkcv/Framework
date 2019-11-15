package think_of_java._21_并发._03_共享受限资源.exercise;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/11
 * 对临界区的同步在一个对象上 改为 每个方法都在不同的对象上同步，多个方法可以共同访问临界区
 */
public class _15 {
    public static void main(String[] args) {
        T1 t1 = new T1();
        new Thread(() -> {
            t1.m1();
        }).start();
        try {
            //让主线程先休眠100毫秒，确保t1线程启动，并先拿到锁然后进入睡眠
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.m2();

        T2 t2 = new T2();
        new Thread(() -> {
            t2.m1();
        }).start();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.m2();
    }

    //同步在一个对象上（锁同一个对象），m2的调用会发生阻塞
    public static class T1 extends Thread {
        private int num = 0;

        public void m1() {
            synchronized (this) {
                System.out.println("T1-m1");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void m2() {
            synchronized (this) {
                System.out.println("T1-m2");
            }
        }

    }

    //锁在不同对象上，m2的调用不会发生阻塞
    public static class T2 extends Thread {
        private Object o2 = new Object();
        private Object o1 = new Object();
        private int num = 0;

        public void m1() {
            synchronized (o1) {
                System.out.println("T2-m1");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void m2() {
            synchronized (o2) {
                System.out.println("T2-m2");
            }
        }
    }


}
