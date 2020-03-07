package com.lwj._02_factory_pattern._00_simpleFactoty;

/**
 * 简单工厂模式2/静态工厂
 * create by lwj on 2019/1/10
 */
public class CarFactory2 {
    public static Car createBMW(){
        return new BWM();
    }
    public static Car createBYD(){
        return new BYD();
    }
}
