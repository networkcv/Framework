package com.lwj._06_proxy_pattern._2dynamic_JDK_Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * create by lwj on 2019/1/19
 */
public class StarHandler implements InvocationHandler {

    Star realStar;

    public StarHandler(Star realStar) {
        this.realStar = realStar;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = null;
        if (method.getName().equals("sing")) {
//            System.out.println(realStar);
//            com.lwj._06_proxy_pattern._2dynamic_JDK_Proxy.RealStar@29453f44
//            System.out.println(proxy);
//            com.lwj._06_proxy_pattern._2dynamic_JDK_Proxy.RealStar@29453f44
            System.out.println("唱歌前");
            obj = method.invoke(realStar, args);
            System.out.println("唱歌后");
        }else{
            obj = method.invoke(realStar, args);
        }
        return obj;
    }
}
