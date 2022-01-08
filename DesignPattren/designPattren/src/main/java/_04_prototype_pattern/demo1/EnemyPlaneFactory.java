package _04_prototype_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class EnemyPlaneFactory {
    private static EnemyPlane protoType = new EnemyPlane(200,"黑鹰");   //懒汉单例模式造出原型
    private static Bullet bulletProtoTypr =new Bullet();//造出子弹的原型
    public static EnemyPlane getSimpleInstance(int x,String name) throws CloneNotSupportedException {
        //浅拷贝  不会拷贝内部的引用类型
        EnemyPlane clone = protoType.clone();
        clone.setX(x);
        clone.setName(name);
        return clone;
    }
    public static EnemyPlane getDeepInstance(int x,String name) throws CloneNotSupportedException {
        //深拷贝
        EnemyPlane clone = protoType.clone();
        clone.setX(x);
        clone.setName(name);
        clone.setBullet(bulletProtoTypr.clone());//对克隆的敌机安装克隆后的子弹
        return clone;
    }
}
