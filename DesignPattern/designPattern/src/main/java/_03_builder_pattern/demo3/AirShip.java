package _03_builder_pattern.demo3;


/**
 * create by lwj on 2019/1/13
 */
public class AirShip {
    private OrbitalModule orbitalModule; //轨道舱
    private Engine engine; //发动机
    private EscapeTower escapeTower; //逃生舱

    public void fly(){
        engine.start();
        System.out.println(" -- "+orbitalModule.getName()+"  起飞");
    }

    public AirShip(OrbitalModule orbitalModule, Engine engine, EscapeTower escapeTower) {
        this.orbitalModule = orbitalModule;
        this.engine = engine;
        this.escapeTower = escapeTower;
    }

    public OrbitalModule getOrbitalModule() {
        return orbitalModule;
    }

    public void setOrbitalModule(OrbitalModule orbitalModule) {
        this.orbitalModule = orbitalModule;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public EscapeTower getEscapeTower() {
        return escapeTower;
    }

    public void setEscapeTower(EscapeTower escapeTower) {
        this.escapeTower = escapeTower;
    }
}
