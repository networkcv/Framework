package Java.think_of_java._21_并发._03_共享受限资源;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2019/11/9
 */
public class EvenChecker {

    public static void test(Java.Java并发.think_of_java._21_并发._03_共享受限资源.IntGenerator intGenerator) {
        test(intGenerator, 10);
    }

    public static void test(Java.Java并发.think_of_java._21_并发._03_共享受限资源.IntGenerator intGenerator, int count) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            cachedThreadPool.execute(() -> {
                while (!intGenerator.isCanceld()) {
                    int val = intGenerator.next();
                    if (val % 2 != 0) {
                        System.out.println(val + " not even");
                        intGenerator.cancel();
                    } else {
                        System.out.println(val + " is even");
                    }
                }
            });
        }
        cachedThreadPool.shutdown();

    }
}
