package com.lwj._02_observerPattern;

/**
 * Date: 2021/11/22
 * <p>
 * Description: 主题对象
 *
 * @author liuWangjie
 */
public interface Subject {
    /**
     * 注册观察者
     */
    void registerObserver(Observer observer);
    /**
     * 解除观察者
     */
    void unRegisterObserver(Observer observer);
    /**
     * 通知观察者
     */
    void notifyObserver(String event);
 }
