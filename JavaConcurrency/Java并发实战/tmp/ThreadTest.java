package tmp;

import org.junit.Test;

/**
 * create by lwj on 2019/12/1
 */
public class ThreadTest {
    Object o = new Object();

    public synchronized void m1() { // 重量级的访问操作。
        System.out.println("public synchronized void m1() start");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("public synchronized void m1() end");
    }

    public void m3() {
        synchronized (o) {
            System.out.println("public void m3() start");
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("public void m3() end");
        }
    }

    public synchronized void m2() {
        System.out.println("public void m2() start");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("public void m2() end");
    }

    public static class MyThread01 implements Runnable {
        public MyThread01(int i, ThreadTest t) {
            this.i = i;
            this.t = t;
        }

        int i;
        ThreadTest t;

        public void run() {
            if (i == 0) {
                t.m1();
            } else if (i > 0) {
                t.m2();
            } else {
                t.m3();
            }
        }
    }
}