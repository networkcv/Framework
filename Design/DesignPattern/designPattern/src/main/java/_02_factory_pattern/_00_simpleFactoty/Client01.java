package _02_factory_pattern._00_simpleFactoty;

/**
 * create by lwj on 2019/1/10
 */
public class Client01 {
    public static void main(String[] args){
        Car bmw = new BWM();
        Car byd = new BYD();

        bmw.run();
        byd.run();

    }
}
