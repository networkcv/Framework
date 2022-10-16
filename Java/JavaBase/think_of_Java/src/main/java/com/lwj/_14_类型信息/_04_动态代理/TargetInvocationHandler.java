package com.lwj._14_类型信息._04_动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * create by lwj on 2020/4/7
 */
public class TargetInvocationHandler implements InvocationHandler {
    private Object target;

    public TargetInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("日志处理");
        return method.invoke(target, args);
    }
}
