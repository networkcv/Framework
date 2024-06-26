package com.lwj.bytecode;

import java.io.IOException;

/**
 * Date: 2024/6/25
 * <p>
 * Description:
 *
 * @author 乌柏
 */
abstract class A {
    public void printMe() {
        System.out.println("I love vim");
    }

    public abstract void sayHello();
}

class B extends A {
    @Override
    public void sayHello() {
        System.out.println("hello, i am child B");
    }
}

public class MyTest extends Object {
    public static void main(String[] args) throws IOException {
        A obj = new B();
        A obj2 = new B();
        System.out.println(obj);
        System.in.read();
    }
}

