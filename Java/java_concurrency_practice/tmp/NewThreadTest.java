package tmp;

import org.junit.Test;

/**
 * create by lwj on 2019/11/30
 */
public class NewThreadTest {
    @Test
    public void test() throws InterruptedException {
        Object lock = new Object();
        Thread thread = new Thread("MyThread") {
            @Override
            public void run() {
                synchronized (lock) {
                    while (true) {
                    }
                }
            }
        };

        Thread thread2 = new Thread("MyThread2") {
            @Override
            public void run() {
                synchronized (lock) {
                    while (true) {
                    }
                }
            }
        };
        thread.start();
        thread2.start();
        Thread.sleep(100);
        System.out.println(thread.getName() + "当前状态：" + thread.getState());
        System.out.println(thread2.getName() + "当前状态：" + thread2.getState());
    }

    @Test
    public void test2() throws InterruptedException {
        Thread thread = new Thread("MyThread") {
            public void run() {
                System.out.println((Thread.currentThread().getName() + " 执行完毕"));
            }
        };
        Thread thread2 = new Thread("MyThread2") {
            public void run() {
                throw new RuntimeException("程序执行出错了");
            }
        };
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        System.out.println(thread.getName() + "当前状态：" + thread.getState());
        System.out.println(thread2.getName() + "当前状态：" + thread2.getState());
    }

    @Test
    public void test3() throws InterruptedException {
        Thread thread = new Thread("MyThread") {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        Thread.sleep(100);
        System.out.println(thread.getName() + "当前状态：" + thread.getState());


    }
}
