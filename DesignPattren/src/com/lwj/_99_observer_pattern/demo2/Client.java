package com.lwj._99_observer_pattern.demo2;


/**
 * create by lwj on 2019/1/23
 */
public class Client {
    public static void main(String[] args){
      ConcreteSubject concreteSubject=new ConcreteSubject();
        ObserverA observerA=new ObserverA();
        concreteSubject.addObserver(observerA);
        observerA.setMyState(0);
        System.out.println(observerA.getMyState());
        concreteSubject.setState(1);
        System.out.println(observerA.getMyState());
    }
}
