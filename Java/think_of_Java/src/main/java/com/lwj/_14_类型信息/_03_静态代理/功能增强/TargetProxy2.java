package com.lwj._14_类型信息._03_静态代理.功能增强;

/**
 * create by lwj on 2020/4/7
 */
public class TargetProxy2 implements TargetInterface2 {
    private Target2 target;

    public TargetProxy2(Target2 target) {
        this.target = target;
    }

    @Override
    public void targetMethod() {
        System.out.println("日志处理");
        target.targetMethod();
    }


}
