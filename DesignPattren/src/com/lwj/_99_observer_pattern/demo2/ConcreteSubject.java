package com.lwj._99_observer_pattern.demo2;

import java.util.Observable;

/**
 * create by lwj on 2019/1/23
 */
public class ConcreteSubject extends Observable {
    private int state;

    public void setState(int s){
        state=s;    //目标对象发生改变

        setChanged();   //继承自父类，表示目标对象已经做了改变
        notifyObservers(state);     //通知所有的观察者
    }

    public int getState() {
        return state;
    }
}
