package com.lwj._03_builder_pattern.demo1;

/**
 * create by lwj on 2019/1/13
 */
public class SxtAirShipDirector implements AirShipDirector{
    private  AirShipBuilder airShipBuilder;

    public SxtAirShipDirector(AirShipBuilder airShipBuilder) {
        this.airShipBuilder = airShipBuilder;
    }

    @Override
    public AirShip directorAirShip() {
        Engine engine = airShipBuilder.builderEngine();
        EscapeTower escapeTower = airShipBuilder.builderEscapeTower();
        OrbitalModule orbitalModule = airShipBuilder.builderOrbitalModule();
        return  new AirShip(orbitalModule,engine,escapeTower);
    }
}
