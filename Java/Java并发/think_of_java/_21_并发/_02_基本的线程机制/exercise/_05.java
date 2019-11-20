package Java.Java并发.think_of_java._21_并发._02_基本的线程机制.exercise;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/8
 * 使用Callable接口，返回斐波数列的对应值
 */
public class _05 implements Callable<Integer> {
    int n;

    public _05(int n) {
        this.n = n;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> futures = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            FutureTask<Integer> task = new FutureTask<>(new _05(i));
//            futures.add((Future<Integer>) cachedThreadPool.submit(task));
            futures.add(cachedThreadPool.submit(new _05(i)));
        }
        cachedThreadPool.shutdown();
        for (Future<Integer> f : futures) {
            System.out.println(f.get());
        }
    }

    @Override
    public Integer call() throws Exception {
        return getRes(n);
    }

    Integer getRes(int n) {
        int res = 0;
        for (int i = 1; i <= n; i++) {
            res += getFibo(i);
        }
        return res;
    }

    private int getFibo(int n) {
        if (n == 1 || n == 2)
            return 1;
        else
            return getFibo(n - 1) + getFibo(n - 2);
    }
}
