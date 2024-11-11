package com.lwj.bytecode;

/**
 * Date: 2024/10/31
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class MultipleMethodsNestedTest {

    private int fun1() {
        int a = 1;
        int i = fun2();
        return 2;
    }

    private int fun2() {
        int b = 2;
        return fun3();
    }

    private int fun3() {
        int c = 3;
        return 4;
    }

    public void fun4() {
        int a = 1;
        if (a > 22) {
            int b = 1;
        }

    }
}
