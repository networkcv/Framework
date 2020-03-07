//package com.lwj._06_proxy_pattern._3_dynamic_CGlib_Proxy;
//
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
///**
// * create by lwj on 2019/8/1
// */
//public class CGLibProxyTest implements MethodInterceptor {
//    private RealStar target = new RealStar();
//
//    @Override
//    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
//        System.out.println("此处是代理类");
//        System.out.println(o.getClass());   //调用该方法的代理类对象
//        System.out.println(method.getClass());  //java.lang.reflect.Method  方法的字节码对象，调用时需要持有目标对象的引用
//        System.out.println(args);   //目标方法的参数
//        System.out.println(methodProxy.getClass()); //net.sf.cglib.proxy.MethodProxy 方法的代理字节码对象
//        //需要使用invokeSpuer调用，传入的 o 为代理对象，o 继承目标对象
//        //自然也可以反射的方式调用继承的方法
////        return method.invoke(target,args);
//        return methodProxy.invokeSuper(o, args);
//    }
//}
