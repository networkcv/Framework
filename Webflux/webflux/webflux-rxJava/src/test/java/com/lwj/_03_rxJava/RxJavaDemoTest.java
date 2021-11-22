package com.lwj._03_rxJava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.Test;

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

}