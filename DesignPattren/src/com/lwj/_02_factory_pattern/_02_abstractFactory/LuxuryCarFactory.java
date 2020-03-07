package com.lwj._02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
public class LuxuryCarFactory implements  CarFactory{
    @Override
    public Engine createEnine() {

        return new LuxuryEngine();
    }

    @Override
    public Seat createSeat() {

        return new LuxurySeat();
    }

    @Override
    public Tyre createTyre() {

        return new LuxuryTyre();
    }
}
