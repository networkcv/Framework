package com.lwj._03_builder_pattern.demo3;

/**
 * create by lwj on 2019/1/13
 */
public interface AirShipBuilder {
    Engine builderEngine();
    OrbitalModule builderOrbitalModule();
    EscapeTower builderEscapeTower();
}
