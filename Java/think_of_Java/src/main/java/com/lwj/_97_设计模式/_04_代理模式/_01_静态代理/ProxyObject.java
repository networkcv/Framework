package com.lwj._97_设计模式._04_代理模式._01_静态代理;

/**
 * create by lwj on 2020/3/20
 */
public class ProxyObject implements BuyInterface {
    private RealObject realObject;
    private static int count;

    public ProxyObject(RealObject realObject) {
        this.realObject = realObject;
    }

    @Override
    public void buy() {
        System.out.println("中介挑选好了房间");
        realObject.buy();
        count++;
    }
}
