package _03_builder_pattern.demo3;

/**
 * create by lwj on 2019/1/13
 */
public class Client {
    public static void main(String[] args){
        AirShipDirector sxtAirShipDirector = new SxtAirShipDirector(new SxtAirShipBuilder());
        AirShip airShip=sxtAirShipDirector.directorAirShip();
        airShip.fly();

    }
}
