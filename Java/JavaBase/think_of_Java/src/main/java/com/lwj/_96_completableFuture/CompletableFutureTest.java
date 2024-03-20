package com.lwj._96_completableFuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2024/3/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> 1);
        c1.thenApply(a -> {
            return a + 1;
        });
        c1.thenAccept(a -> {
            System.out.println(a + 1);
        }).join();
        c1.thenRun(() -> {
            //over main
            System.out.println("over " + Thread.currentThread().getName());
        });
        c1.thenRunAsync(() -> {
            //over ForkJoinPool.commonPool-worker-3
            System.out.println("over " + Thread.currentThread().getName());
        });
        c1.thenRunAsync(() -> {
            //over ool-1-thread-1
            System.out.println("over " + Thread.currentThread().getName());
        }, executorService);

        CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("over2 " + Thread.currentThread().getName());
            throw new RuntimeException("-1");
        });
        c2.exceptionally(e -> {
            System.out.println(e);
            System.out.println("over3 " + Thread.currentThread().getName());
            return 1;
        }).thenAccept(System.out::println);

        c2.handle((res, e) -> {
            System.out.println(res);
            System.out.println(e);
            System.out.println("over4 " + Thread.currentThread().getName());
            return 1;
        }).thenAccept(System.out::println);


    }

    @Test
    public void test_completeExceptionally() {
        CompletableFuture<Integer> cf = new CompletableFuture<>();
        cf.completeExceptionally(new RuntimeException("-1"));
        cf.thenRun(() -> System.out.println(1));

        cf.join();

        try {
            cf.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
