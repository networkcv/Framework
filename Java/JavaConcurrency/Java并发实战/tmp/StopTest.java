package tmp;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/9
 */
public class StopTest {
    public static void main(String[] args) {
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                lock.notify();
                Thread.currentThread().stop();
            }
        });
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ok");
            }
        });
        thread1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.start();


    }
}
