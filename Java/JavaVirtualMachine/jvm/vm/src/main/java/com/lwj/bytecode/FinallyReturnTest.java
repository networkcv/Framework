package com.lwj.bytecode;

/**
 * Date: 2024/11/14
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class FinallyReturnTest {
    public static void main(String[] args) {
        System.out.println(test1());
    }

    public static int test1() {
        int i = 0;
        try {
            i = 2;
            return i;
        } finally {
            i = 3;
        }
    }

    public static int test2() {
        int i = 2;
        return i;
    }

    public int test3() {
        int i = 2;
        return i;
    }
}
