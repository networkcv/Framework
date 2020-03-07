package com.lwj._99_observer_pattern.demo1;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2019/1/22
 */
public class Subject {
    protected List<Observer> list =new ArrayList();

    public void register(Observer observer){
        list.add(observer);
    }

    public void removerObsrver(Observer observer){
        list.remove(observer);
    }

    //通知所有观察者更新状态
    public void notifyAllObserver(){
        for (Observer obs:list) {
            obs.update(this);
        }
    }
}
