package think_of_java;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/8
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
