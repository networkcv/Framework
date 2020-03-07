package com.lwj._04_prototype_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class Bullet implements Cloneable {
    private int bulletNum = 1;

    public int getBulletNum() {
        return bulletNum;
    }

    public void fire() {
        System.out.println("让子弹飞一会");
        bulletNum--;
    }

    protected Bullet clone() throws CloneNotSupportedException {
        return (Bullet) super.clone();
    }
}
