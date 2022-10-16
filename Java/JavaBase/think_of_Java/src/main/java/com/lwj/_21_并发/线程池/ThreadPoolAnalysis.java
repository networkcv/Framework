package com.lwj._21_并发.线程池;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2022/5/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ThreadPoolAnalysis {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> System.out.println("ok"));
        executorService.shutdown();
    }
}
