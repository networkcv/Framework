package _02_factory_pattern._00_simpleFactoty;

/**
 * 简单工厂模式1/静态工厂
 * create by lwj on 2019/1/10
 */
public class CarFactory {
    public static Car createCar(String type){
        if("宝马".equals(type)){
            return new BWM();
        }else if ("比亚迪".equals(type)){
            return new BYD();
        }else{
            return null;
        }
    }
}
