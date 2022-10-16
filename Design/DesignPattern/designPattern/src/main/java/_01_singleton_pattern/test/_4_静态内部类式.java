package _01_singleton_pattern.test;

import java.io.ObjectStreamException;

/**
 * create by lwj on 2019/7/31
 */
public class _4_静态内部类式 {
    private _4_静态内部类式() {
    }

//    外部类没有static属性，则不会像饿汉式那样立即加载对象
//    只有真正调用getInstance()，才会加载静态内部类，加载类时是线程安全的
    public static _4_静态内部类式 getInstance() {
        return InnerClass.INSTANCE;
    }

    private static class InnerClass {
        private static final _4_静态内部类式 INSTANCE = new _4_静态内部类式();
    }

    //解决反序列化漏洞
    //当JVM从内存中反序列化地"组装"一个新对象时，就会自动调用这个 readResolve方法来返回我们指定好的对象了，单例规则也就得到了保证。
    private Object readResolve() throws ObjectStreamException {
        return InnerClass.INSTANCE;
    }

}
