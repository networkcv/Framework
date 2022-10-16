package _01_singleton_pattern.test;

/**
 * create by lwj on 2019/7/31
 */
public class _2_懒汉式 {
    private static _2_懒汉式 instance;

    private _2_懒汉式() {
    }

//  实例在需要使用时才会被创建，以时间换空间
    //线程不安全
//    public static _2_懒汉式 getInstance() {
//        if (instance == null)
//            instance = new _2_懒汉式();
//        return instance;
//    }

    //线程安全
    //synchronized关键字偏重量级锁，虽然JDK6之后对该关键字进行了优化，降低了获得锁和释放锁带来的性能消耗
    //但仍然有很大的时间消耗，因此就有了双重检测锁式
    public static synchronized  _2_懒汉式 getInstance(){
        if (instance == null)
            instance = new _2_懒汉式();
        return instance;
    }

}
