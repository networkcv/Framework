package _97_设计模式._04_代理模式._02_动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * create by lwj on 2020/3/20
 */
public class BuyHandler implements InvocationHandler {
    private Object realObject;
    public static int count;

    public BuyHandler(RealObject realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        count++;
        Object invoke = method.invoke(realObject, args);
        return invoke;
    }

    public static int getCount() {
        return count;
    }
}
