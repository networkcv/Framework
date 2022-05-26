package com.lwj._21_并发._00_util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2019/11/9
 */
public class BaseUtil {

    public static void exec(Runnable runnable) {
        exec(runnable, 3);
    }

    public static void exec(Runnable runnable, int count) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            cachedThreadPool.execute(runnable);
        }
        cachedThreadPool.shutdown();
        while (true) {
            if (cachedThreadPool.isTerminated()) {
                System.out.println("task ok");
                break;
            }

        }
    }
}
