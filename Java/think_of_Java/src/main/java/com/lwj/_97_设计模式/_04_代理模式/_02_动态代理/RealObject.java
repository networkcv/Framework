package com.lwj._97_设计模式._04_代理模式._02_动态代理;

/**
 * create by lwj on 2020/3/20
 */
public class RealObject implements BuyInterface {

    @Override
    public void buy() {
        System.out.println("买房");
    }
}
