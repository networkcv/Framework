package tmp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * create by lwj on 2019/12/11
 */
public class BlockTest {
    public static void main(String[] args) {
        Object o = new Object();
        Thread thread = new Thread(() -> {
            synchronized (o) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Lock
        Thread thread2 = new Thread(() -> {
            synchronized (o) {
                System.out.println("ok");
            }
        });
        thread2.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();

    }
}
