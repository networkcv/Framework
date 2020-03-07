package tmp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * create by lwj on 2019/12/19
 * 一千万个数字求和
 */
public class SumForMillionsNumber {
    private static int nums = 1000 * 10000;
    private static int num = 100 * 10000;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Random random = new Random();
        List<Future<Long>> list = new ArrayList<>(10);
        long[] arr = new long[nums];
        for (int i = 0; i < nums; i++) {
            arr[i] = random.nextInt(10);
//            arr[i] = 10;
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int j = 0; j < 10; j++) {
            int[] ints = {j};
            Future<Long> future = pool.submit(() -> {
                long res = 0;
                for (int i = ints[0] * num; i < (ints[0] + 1) * num; i++) {
                    res += arr[i];
                }
                System.out.println(res);
                return res;
            });
            list.add(future);
        }
        Long res = 0L;

        for (Future<Long> future : list) {
            res += future.get();
        }
        System.out.println("res=" + res);

    }
}
