package com.lwj._99_adapter_pattern.demo1;

import org.junit.Test;

/**
 * create by LiuWangJie on 2018/7/25
 * 适配器模式
 */
public class MyTest {
    @Test
    public void fun1(){
        Phone phone=new Phone();    //一个手机
        Adapter adapter= new Adapter();    //一个转化插头
        adapter.setDualPinDriver(phone);    //把手机的充电器插口插入转化插口
        adapter.electrify(1,2,3);   //转化插头插入三项插孔插座
//        火线通电：1
//        零线通电：3
    }
}
