package _21_并发._02_基本的线程机制.exercise;

import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/9
 */
public class _10 implements Callable<Integer> {
    int n;

    public _10(int n) {
        this.n = n;
    }

    @Override
    public Integer call() throws Exception {
        return getRes(n);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Future<Integer> future = cachedThreadPool.submit(new _10(5));
        while (true) {
            if (future.isDone()) {
                System.out.println(future.get());
                break;
            }
        }
        cachedThreadPool.shutdown();
    }

    int runTask(int n) {
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
