package think_of_java._21_并发._05_线程之间的协作.exercise;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/18
 * 线程A进入wait() 释放锁（自身对象）， 线程B持有线程A的对象引用，作为锁
 * 休眠2s后唤醒在这个锁上wait的线程
 */
public class _21 {
    Object lock = new Object();

    public static class ThreadA extends Thread {
        @Override
        public void run() {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ok");
            }
        }
    }

    public static class ThreadB extends Thread {
        public Object object;

        public ThreadB(Object o) {
            object = o;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (object) {
                object.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        ThreadA t1 = new ThreadA();
        t1.start();
        ThreadB t2 = new ThreadB(t1);
        t2.start();

    }

}
