package com.lwj._02_factory_pattern._01_factoryMethod;

public class BYDFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new BYD();
    }
}
