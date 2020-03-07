package com.lwj._01_singleton_pattern.test;

/**
 * create by lwj on 2019/7/31
 */
public class _1_饿汉式 {
    //实例会随着类加载而被创建，如果实例不被使用的话便浪费了内存空间，典型的以空间换时间
    //但如果某个实例需要在其被使用时才需要创建，那么就需要懒汉式了
    private static _1_饿汉式 instance = new _1_饿汉式();
    private _1_饿汉式(){};
    public static _1_饿汉式 getInstance(){
        return instance;
    }
}
