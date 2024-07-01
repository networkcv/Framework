package com.lwj.bytecode;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class AnonymousInnerClassTest {
    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello, inner class");
            }
        };
        r1.run();
    }
}
