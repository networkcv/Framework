package com.lwj.bytecode;

/**
 * Date: 2024/6/29
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class InvokeDynamicTest {
    public static void main(String[] args) {
        Runnable r = () -> {
            System.out.println("hello, lambda");
        };
        r.run();
        System.out.println(r.getClass().getName());
    }
}

