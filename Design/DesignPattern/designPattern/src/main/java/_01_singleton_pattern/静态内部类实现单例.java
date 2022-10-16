package _01_singleton_pattern;

import java.io.ObjectStreamException;

/**
 * create by lwj on 2019/1/9
 */
public class 静态内部类实现单例 {
    public static 静态内部类实现单例 getInstance(){
        return InnerClass.instance;
    }
    private  静态内部类实现单例(){
        if(InnerClass.instance!=null){
            throw  new RuntimeException("已经存在实例");
        }
    }
    private static class InnerClass{
        private static  静态内部类实现单例 instance=new 静态内部类实现单例();
    }

    //解决反序列化漏洞
    private Object readResolve() throws ObjectStreamException {
            return InnerClass.instance;
    }
}
