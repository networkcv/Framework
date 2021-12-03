package com.lwj._03_rxJava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/11/22
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class RxJavaDemoTest {

    @Test
    public void rxJavaDemo() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                System.out.println("subscribe: " + emitter);
                for (int i = 0; i < 10; i++) {
                    emitter.onNext(String.valueOf(i));
                }
                emitter.onComplete();
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe: " + d);
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError: " + 3);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    @Test
    public void rxJavaDemoWithLambda() {
        Observable.create(emitter -> {
            System.out.println("subscribe: " + emitter);
            for (int i = 0; i < 10; i++) {
                emitter.onNext(String.valueOf(i));
            }
            emitter.onComplete();
        }).subscribe(
                item -> System.out.println("onSubscribe: " + item),
                error -> System.out.println("onError: " + error),
                () -> System.out.println("onComplete"));
    }

    @Test
    public void justElement() {
        Observable.just("1", "2", "3").subscribe(
                item -> System.out.println("onSubscribe: " + item),
                error -> System.out.println("onError: " + error),
                () -> System.out.println("onComplete"));
    }

    @Test
    public void concatElement() {
        Observable.concat(Observable.just(1, 2, 3),
                Observable.fromArray(4, 5, 6),
                Observable.create(subscriber ->
                {
                    for (int i = 7; i < 10; i++) {
                        subscriber.onNext(i);
                    }
                    subscriber.onComplete();
                }))
                .forEach(System.out::println);
    }

    @Test
    public void intervalTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //创建基于时间的异步响应流
        Disposable subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (subscribe.isDisposed()) {
                subscribe.dispose();
            }
            countDownLatch.countDown();
        }).start();
        System.out.println("====");
        countDownLatch.await();
        System.out.println("----");
    }

    @Test
    public void zipTest() {
        //通过指定的函数将多个Observable发送的元素组合在一起，并根据此函数的结果作为每个组合发出的单个数据项，如果凑不齐组合则会丢弃对应元素
        Observable.zip(
                Observable.just(1, 2, 3),
                Observable.just(1, 2, 3), Integer::sum
        ).forEach(System.out::println);
        /* output
            2
            4
            6
         */
    }

    @Test
    public void subscribeOnTest() {
        //将传统的同步慢查询包装起来，走异步的响应流
        Observable.fromCallable(this::doSyncSlowQuery)
                .subscribeOn(Schedulers.io())
                .subscribe(this::processResult);
    }

    private String doSyncSlowQuery() {
        return "queryResult";
    }

    private void processResult(String integer) {
    }


}