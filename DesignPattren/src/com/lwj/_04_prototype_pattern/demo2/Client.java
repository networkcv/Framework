package com.lwj._04_prototype_pattern.demo2;

import java.util.Date;

public class Client {
    public static void main(String[] args) throws CloneNotSupportedException {
        Sheep s1 =  new Sheep();
        s1.setName("原型多利");
        s1.setBirthday(new Date());
        s1.setWool(new Wool());
        Sheep s2 = (Sheep) s1.clone();
        System.out.println(s2.getName());
        System.out.println(s2.getBirthday());
        System.out.println(s1);
        System.out.println(s2);
        s1.getWool();
        s2.getWool();
    }
}
