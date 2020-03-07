package com.lwj._02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
class LowTyre implements  Tyre{
    @Override
    public void revolve() {
        System.out.println("低端轮胎慢");
    }
}
