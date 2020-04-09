package _14_类型信息._04_动态代理;

import java.lang.reflect.Proxy;

/**
 * create by lwj on 2020/4/7
 * InvocationHandler代码进行了复用
 */
public class Client {
    public static void main(String[] args){
        Target target = new Target();
        TargetInvocationHandler invocationHandler = new TargetInvocationHandler(target);

        TargetInterface tar = (TargetInterface) Proxy.newProxyInstance(
                Client.class.getClassLoader(), new Class[]{TargetInterface.class}, invocationHandler);
        tar.targetMethod();

        Target2 target2 = new Target2();
        TargetInvocationHandler invocationHandler2 = new TargetInvocationHandler(target2);
        TargetInterface2 tar2 = (TargetInterface2) Proxy.newProxyInstance(
                Client.class.getClassLoader(), new Class[]{TargetInterface2.class}, invocationHandler2);
        tar2.targetMethod();
    }
}
