package com.lwj._03_builder_pattern.demo3;

/**
 * create by lwj on 2019/1/13
 */
public class SxtAirShipBuilder implements AirShipBuilder {
    AirShipFactory airShipFactory = new A_AriShipFactory();

    @Override
    public Engine builderEngine() {
        System.out.println("成功构建发动机");
        Engine engine = airShipFactory.createEngine();
        return engine;
    }

    @Override
    public OrbitalModule builderOrbitalModule() {
        System.out.println("成功构建轨道舱");
        OrbitalModule orbitalModule = new OrbitalModule();
        orbitalModule.setName("lwj的轨道舱");
        return orbitalModule;
    }

    @Override
    public EscapeTower builderEscapeTower() {
        System.out.println("成功构建逃逸舱");
        return new EscapeTower();
    }
}
