package com.lwj._02_observerPattern;

import org.junit.Test;

/**
 * Date: 2021/11/22
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ObserverTest {

    @Test
    public void observerTest1() {
        MySubject mySubject = new MySubject();
        Observer observer = new Observer() {
            @Override
            public void observer(String event) {
                System.out.println("this is observer1: " + event);
            }
        };
        mySubject.registerObserver(event -> observer.observer(event));
        mySubject.registerObserver(event -> System.out.println("this is observer2: " + event));
        mySubject.notifyObserver("event 1");
    }
}