package com.lwj._02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
class LuxuryTyre implements  Tyre{
    @Override
    public void revolve() {
        System.out.println("高端轮胎快");
    }
}
