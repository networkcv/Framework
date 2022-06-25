package _02_factory_pattern._00_simpleFactoty;

/**
 *
 * create by lwj on 2019/1/10
 */
public class Client02 {
    public static void main(String[] args){
        Car bwm=CarFactory.createCar("宝马");
        Car byd=CarFactory.createCar("比亚迪");
        bwm.run();
        byd.run();
    }
}
