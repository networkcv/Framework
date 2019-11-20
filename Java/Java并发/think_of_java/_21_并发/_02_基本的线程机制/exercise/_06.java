package Java.Java并发.think_of_java._21_并发._02_基本的线程机制.exercise;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/8
 */
public class _06 implements Runnable {
    int n;

    public _06(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " sleep " + n + "s");
    }

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            cachedThreadPool.execute(new Thread(new _06(random.nextInt(10)), "线程" + i));
        }
        cachedThreadPool.shutdown();
    }
}
