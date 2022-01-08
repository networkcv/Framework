package _05_adapter_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class Adapter implements TriplePin {
    //Adapter 相当于一个转化插头，因为它要插到三项插孔插座里，所以要先实现TriplePin接口
    //又因为它本身内置了两项插孔，所以这个类中必须要有一个成员变量是两项插孔
    private DualPin dualPinDriver;  //两项插孔

    public void setDualPinDriver(DualPin dualPinDriver) { //插入两项插孔的东西必须要实现两项插孔的接口
        this.dualPinDriver = dualPinDriver;
    }

    public void electrify(int l, int n, int e) { //通电了
        dualPinDriver.electrify(l, e);
    }
}
