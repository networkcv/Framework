package think_of_java._21_并发._02_基本的线程机制;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/8
 */
public class _07 {
    //通过daemon线程创建的线程都是daemon线程
    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + " : " + Thread.currentThread().isDaemon());
                }).start();
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
