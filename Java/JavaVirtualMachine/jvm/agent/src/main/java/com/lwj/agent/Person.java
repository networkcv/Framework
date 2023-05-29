package com.lwj.agent;

/**
 * Date: 2023/5/26
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void sayHello() {
        System.out.println("Hello, I'm " + name);
    }
}
