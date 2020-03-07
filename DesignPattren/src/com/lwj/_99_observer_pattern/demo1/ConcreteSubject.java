package com.lwj._99_observer_pattern.demo1;

/**
 * create by lwj on 2019/1/22
 */
public class ConcreteSubject  extends  Subject{
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        //主题(目标)对象变化，通知所有观察者更新
        notifyAllObserver();
    }
}
