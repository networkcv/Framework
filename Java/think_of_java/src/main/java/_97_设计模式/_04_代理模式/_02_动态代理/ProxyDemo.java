package _97_设计模式._04_代理模式._02_动态代理;

import java.lang.reflect.Proxy;

/**
 * create by lwj on 2020/3/20
 */
public class ProxyDemo {

    public static void main(String[] args) {
        BuyHandler buyHandler = new BuyHandler(new RealObject());
        BuyInterface proxy = (BuyInterface) Proxy.newProxyInstance(ProxyDemo.class.getClassLoader(), new Class[]{BuyInterface.class}, buyHandler);
        for (int i = 0; i < 10; i++) {
            proxy.buy();
        }
        System.out.println(BuyHandler.getCount());

    }
}
