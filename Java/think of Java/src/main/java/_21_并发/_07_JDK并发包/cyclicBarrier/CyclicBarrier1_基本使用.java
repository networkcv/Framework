package _21_并发._07_JDK并发包.cyclicBarrier;

import java.util.Random;
import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/19
 */
public class CyclicBarrier1_基本使用 {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6, () -> {
            System.out.println("Game will begin!");
        });
        Random random = new Random();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 18; i++) {
            cachedThreadPool.execute(() -> {
                try {
                    int time = random.nextInt(250);
                    TimeUnit.MILLISECONDS.sleep(time);
                    System.out.println(Thread.currentThread().getName() + "号马 is ok!  cast"+time);
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cachedThreadPool.shutdownNow();
        System.out.println("你要赌几号马赢？");
    }
}

