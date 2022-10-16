package com.lwj._01_reactiveSimpleDemo;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 2021/11/22
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ReactiveTest {

    private final List<Integer> integerList = Stream.iterate(0, i -> ++i).limit(10).collect(Collectors.toList());

    @Test
    public void reactiveTest() throws InterruptedException {
        AsyncIterablePublisher<Integer> publisher = new AsyncIterablePublisher<>(integerList, Executors.newFixedThreadPool(2));
        AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<Integer>(Executors.newFixedThreadPool(1)) {
            @Override
            protected boolean whenNext(Integer element) {
                System.out.println("元 素: " + element);
                return true;
            }
        };
        publisher.subscribe(subscriber);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void customizeSubscriberTest() throws InterruptedException {
        AsyncIterablePublisher<Integer> publisher = new AsyncIterablePublisher<>(integerList, Executors.newFixedThreadPool(2));
        publisher.subscribe(new Subscriber<Integer>() {

            private Subscription subscription;
            final Integer requestNum = 3;
            final AtomicInteger count = new AtomicInteger(0);

            @Override
            public void onSubscribe(Subscription subscription) {
//                subscription.request(Long.MAX_VALUE);
                this.subscription = subscription;
                subscription.request(requestNum);
                System.out.println("call onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                count.incrementAndGet();
                System.out.println("call onNext : " + integer);
                if (count.get() == 3) {
                    count.compareAndSet(3, 0);
                    subscription.request(requestNum);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("call onError");
            }

            @Override
            public void onComplete() {
                System.out.println("call onComplete");
            }
        });
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void multiSubscriberTest() throws InterruptedException {
        AsyncIterablePublisher<Integer> publisher = new AsyncIterablePublisher<>(integerList, Executors.newFixedThreadPool(2));
        AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<Integer>(Executors.newFixedThreadPool(1)) {
            @Override
            protected boolean whenNext(Integer element) {
                System.out.println("subscriber1 元素: " + element);
                return true;
            }
        };
        AsyncSubscriber<Integer> subscriber2 = new AsyncSubscriber<Integer>(Executors.newFixedThreadPool(1)) {
            @Override
            protected boolean whenNext(Integer element) {
                System.out.println("subscriber2 元素: " + element);
                return true;
            }
        };
        publisher.subscribe(subscriber);
        publisher.subscribe(subscriber2);
        TimeUnit.SECONDS.sleep(10);

    }

}