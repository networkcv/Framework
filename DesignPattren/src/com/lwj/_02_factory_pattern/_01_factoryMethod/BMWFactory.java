package com.lwj._02_factory_pattern._01_factoryMethod;

public class BMWFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new BWM();
    }
}
