package com.lwj._14_类型信息._03_静态代理.功能增强;

/**
 * create by lwj on 2020/4/8
 */
public class Client {
    public static void main(String[] args) {
        TargetProxy targetProxy = new TargetProxy(new Target());
        targetProxy.targetMethod();
        TargetProxy2 targetProxy2 = new TargetProxy2(new Target2());
        targetProxy2.targetMethod();
    }
}
