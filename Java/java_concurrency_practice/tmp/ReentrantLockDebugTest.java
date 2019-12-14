package tmp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/12/13
 */
public class ReentrantLockDebugTest {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        new Thread(() -> {
            reentrantLock.lock();
            try {
                TimeUnit.SECONDS.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }).start();
        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("1");
            reentrantLock.unlock();
        }).start();
    }
}
