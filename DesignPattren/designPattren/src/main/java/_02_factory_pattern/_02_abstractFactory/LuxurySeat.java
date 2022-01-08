package _02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
class LuxurySeat implements  Seat{
    @Override
    public void massage() {
        System.out.println("可以按摩");
    }
}
