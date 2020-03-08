package _21_并发._06_死锁;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/2
 */
public class SuspendAndResumeDeadLock {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                    System.out.println(i++);
            }
        });
        thread.start();
        TimeUnit.NANOSECONDS.sleep(1);
        thread.resume();
        thread.suspend();
        TimeUnit.NANOSECONDS.sleep(1);
        System.out.println(thread.getState());
    }


}
