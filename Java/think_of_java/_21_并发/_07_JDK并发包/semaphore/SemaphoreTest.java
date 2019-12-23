package _21_并发._07_JDK并发包.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/21
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5,true);
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
                semaphore.acquire(3);
                System.out.println("拿到了2个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(1);
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(1);
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(semaphore.availablePermits());

        new Thread(() -> {
            try {
                semaphore.acquire(4);
                System.out.println("拿到了4个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(4);
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire(3);
                System.out.println("拿到了3个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(3);
        }).start();

    }
}
