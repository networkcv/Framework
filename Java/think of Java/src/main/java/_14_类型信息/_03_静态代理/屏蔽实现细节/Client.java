package _14_类型信息._03_静态代理.屏蔽实现细节;

/**
 * create by lwj on 2020/4/7
 */
public class Client {
    public static void main(String[] args) {
//        DepthImpl depth = new DepthImpl();    //无法直接操作目标类
        DepthImplProxy depthImplProxy = new DepthImplProxy();
        depthImplProxy.exec();
    }
}
