package com.lwj._02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
class LowSeat implements  Seat{
    @Override
    public void massage() {
        System.out.println("不可以按摩");
    }
}
