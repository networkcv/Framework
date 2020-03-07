package com.lwj._04_prototype_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class EnemyPlane implements Cloneable{
    private int x;//敌机横坐标
    private int y = 0;//敌机纵坐标
    private String name;//敌机名称
    private Bullet bullet=new Bullet();

    public EnemyPlane(int x,String name) {//构造器
        this.name=name;
        this.x = x;
    }

    public void setX(int x){
        this.x=x;
    }
    public int getX() {
        return x;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public int getY() {
        return y;
    }

    public void setBullet(Bullet bullet){
        this.bullet=bullet;
    }

    public void fly() {//让敌机飞
        y++;//每调用一次，敌机飞行时纵坐标＋1
    }

    public void fire() {//让敌机开火
        System.out.print(name+"： 坐标为"+x+" ");
        if (bullet.getBulletNum()> 0) {
            bullet.fire();
        } else {
            System.out.println("没子弹了");
        }
    }

    protected EnemyPlane clone() throws CloneNotSupportedException {
        //通过这个克隆方法，jvm会直接去内存中拷贝原始数据流然后，而省去了很多复杂操作（类加载，实例化，初始化）
        //克隆的速度远远快于实例化操作
        return (EnemyPlane) super.clone();
    }
}

