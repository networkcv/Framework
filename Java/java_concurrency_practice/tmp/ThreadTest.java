package tmp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/1
 */
public class ThreadTest {
    volatile static List list = new ArrayList();

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        list.add("1");
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (list.size() != 0) {
                        System.out.println("1:" + Thread.currentThread().getName() + " " + Thread.currentThread().getState() + " " + list.size());
                        TimeUnit.SECONDS.sleep(2);
                        lock.wait(1);
                        System.out.println("4:" + Thread.currentThread().getName() + " " + Thread.currentThread().getState() + " " + list.size());
                        System.out.println("true");
                        return;
                    }
                    System.out.println("false");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("2:" + thread.getName() + " " + thread.getState() + " " + list.size());

        synchronized (lock) {
            list.clear();
            lock.notify();
            System.out.println("3:" + thread.getName() + " " + thread.getState() + " " + list.size());
        }

    }

    @Test
    public void test() throws InterruptedException {
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
