package com.lwj._02_factory_pattern._01_factoryMethod;

public class Client {
    Car bmw = new BMWFactory().createCar();
    Car byd = new BYDFactory().createCar();
    Car audi= new AUDIFactory().createCar();
}
