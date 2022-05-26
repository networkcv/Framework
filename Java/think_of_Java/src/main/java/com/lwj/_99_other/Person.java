package com.lwj._99_other;

/**
 * create by lwj on 2020/4/4
 */
public class Person {
    private static int count = 0;
    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
        count = count + 1;
    }

    private static void sound() {
        System.out.println("发声");
    }

    private void speak() {
        System.out.println(name + " 说: 我今年 " + age);
        System.out.println("当前世界人数" + count);
    }

    public static void main1(String[] args) {
        Person person = new Person(21, "张璐");
        person.age = 1;
        person.speak();
        System.out.println(person.count);
        Person person2 = new Person(24, "lwj");
        Person person3 = new Person(24, "lwj");
        Person person4 = new Person(24, "lwj");
        System.out.println(person.count);
        System.out.println(person2.count);
        System.out.println(person3.count);
    }

    public static void main2(String[] args) {
        Person.count = 100;
        Person baby = new Person(0, "baby");
        System.out.println(baby.count);
        System.out.println(Person.count);
    }

    public static void main(String[] args) {
        Person.sound();
//        Person.speak();   //error
        System.out.println("-----------");
        Person person = new Person(21, "张璐");
        person.speak();
        person.sound();
    }

}
