package com.lwj._04_prototype_pattern.demo1;

import org.junit.Test;



/**
 * create by LiuWangJie on 2018/7/25
 * 原型模式
 */
public class MyTest {
    @Test
    public void fun1() throws CloneNotSupportedException {
//        List<EnemyPlane> enemyPlanes = new ArrayList<EnemyPlane>();
//        for (int i = 0; i < 50; i++) {
//            //此处随机位置产生敌机
//            EnemyPlane ep = new EnemyPlane(new Random().nextInt(200));
//            enemyPlanes.add(ep);
//        }
        //这样重复调用构造方法50次，十分消耗cpu和内存，这个问题可以通过原型设计模式，快速复制对象
        EnemyPlane ep1 = EnemyPlaneFactory.getSimpleInstance(1,"黑鹰");
        EnemyPlane ep2 = EnemyPlaneFactory.getSimpleInstance(2,"白鸽");
        ep1.fire();//        黑鹰： 坐标为1 让子弹飞一会
        ep2.fire();//        白鸽： 坐标为2 没子弹了
        //这个的原因克隆的子弹对象的引用，ep1和ep2的子弹引用是同一个，所以ep1射击后ep2没有子弹了
        //解决这个的办法是对引用对象一起克隆，方法和克隆敌机一样
        EnemyPlane ep3 = EnemyPlaneFactory.getDeepInstance(3,"黑夜");
        EnemyPlane ep4 = EnemyPlaneFactory.getDeepInstance(4,"白天");
        ep3.fire();//        黑夜： 坐标为3 让子弹飞一会
        ep4.fire();//        白天： 坐标为4 让子弹飞一会

    }
}
