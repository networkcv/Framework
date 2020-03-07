package com.lwj._02_factory_pattern._02_abstractFactory;

/**
 * create by lwj on 2019/1/12
 */
public class  LowEngine implements Engine {

    @Override
    public void run() {
        System.out.println("转得慢");
    }

    @Override
    public void start() {
        System.out.println("启动慢");
    }
}
