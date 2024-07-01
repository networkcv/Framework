package com.lwj.bytecode;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NumberAutoIncrTest {
    public static int foo() {
        int x = 1;
        try {
            return x;
        } finally {
            ++x;
        }
    }

    public static int foo2() {
        int x = 1;
        x++;
        return x;
    }


    public static int foo3() {
        int x = 1;
        x = x++;
        return x;
    }

    public static int foo4() {
        int x = 1;
        x = ++x;
        return x;
    }

    public static void main(String[] args) {
        System.out.println(foo());//1
        System.out.println(foo2());//2
        System.out.println(foo3());//1
        System.out.println(foo4());//2
    }

}
