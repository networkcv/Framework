package com.lwj._02_observerPattern;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Date: 2021/11/22
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MySubject implements Subject {

    private final Set<Observer> observes = Sets.newCopyOnWriteArraySet();

    @Override
    public void registerObserver(Observer observer) {
        observes.add(observer);
    }

    @Override
    public void unRegisterObserver(Observer observer) {
        observes.remove(observer);
    }

    @Override
    public void notifyObserver(String event) {
        observes.forEach(o -> o.observer(event));
    }
}
