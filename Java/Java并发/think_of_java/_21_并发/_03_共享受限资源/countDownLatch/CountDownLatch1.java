package think_of_java._21_并发._03_共享受限资源.countDownLatch;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/7
 */
public class CountDownLatch1 {
    static CountDownLatch end = new CountDownLatch(10);

    @Test
    public void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" ok");
                end.countDown();
            }),i);
        }
        try {
            end.await();
            System.out.println("检查完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
