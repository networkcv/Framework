package com.lwj._01_singleton_pattern.test;

/**
 * create by lwj on 2019/7/31
 */
public class _3_双重检测锁式 {
    private static  _3_双重检测锁式 instance;
    private _3_双重检测锁式(){};
    public static _3_双重检测锁式 getInstance(){
        if (instance==null){
            synchronized (_3_双重检测锁式.class){
                if (instance==null)
                    instance=new _3_双重检测锁式();
            }
        }
        return instance;
    }
}
