package com.lwj._05_adapter_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class Phone implements  DualPin{
    //手机充电插口是两项插孔
    //因为三项插孔的火线和零线插孔是斜着的，想把手机充电器插进去，就需要一个转化插头，也就是适配器
    //将三孔转换为两孔 这个适配器叫做  Adapter
    public void electrify(int l, int n) {
        System.out.println("火线通电：" + l);
        System.out.println("零线通电：" + n);
    }
}
