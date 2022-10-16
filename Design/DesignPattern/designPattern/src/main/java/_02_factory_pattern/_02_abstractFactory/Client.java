package _02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
public abstract class Client {
    public static void main(String[] args){
        CarFactory luxuryCarFactory=new LuxuryCarFactory();
        Engine luxuryEngine = luxuryCarFactory.createEnine();
        luxuryEngine.run();
        luxuryEngine.start();
        Seat luxurySeat = luxuryCarFactory.createSeat();
        luxurySeat.massage();
        CarFactory lowCarFactory=new LowCarFactory();
        Engine low = lowCarFactory.createEnine();
        low.run();
        low.start();


    }
}
