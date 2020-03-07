package com.lwj._03_builder_pattern.demo3;

/**
 * create by lwj on 2019/1/13
 */
public class B_AriShipFactory implements AirShipFactory {
    @Override
    public Engine createEngine() {

        return new BEngine();
    }

    @Override
    public OrbitalModule createOrbitalModule() {

        return null;
    }

    @Override
    public EscapeTower createEscapeTower() {
        return null;
    }
}
