package _01_singleton_pattern;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class God {
    //private  static final God god= new God(); //Eager loads 饿汉模式
    private static God god; //Lazy load 懒汉模式

    private God() {
    }//构造私有化 只能自己创建自己

    //      public static synchronized  God getGod(){ //此处加入同步，防止多线程下创建多个实例,加在方法上的锁导致cpu的
//                                                //并发资源没有充分利用，有了单例后多个线程同时访问方法时也需要排队
//             if(god==null){
//                 god=new God();
//             }
//            return god;
//        }
    public static God getGod() {//这样就都可以同时进入方法
        if (god == null) {  //同时调用方法的线程，判断有没有god实例
            synchronized (God.class) {  //没有的话加锁同步去实例god，只有一个进程可以实例成功，但这些进程都要进锁
                //判断实例是否存在，存在的话，以后多个进程就可以直接获取god实例，而不用排队
                if (god == null) {
                    god = new God();
                }
            }
        }
        return god;
    }

}
