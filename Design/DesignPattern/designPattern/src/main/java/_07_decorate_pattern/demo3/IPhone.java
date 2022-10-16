package _07_decorate_pattern.demo3;

/**
 * create by lwj on 2019/8/3
 */
public class IPhone implements Phone {
    @Override
    public void call() {
        System.out.println("打电话");
    }

}
