package com.lwj.bytecode;

import java.io.Serializable;

/**
 * Date: 2024/6/24
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class MethodTest implements Cloneable, Serializable {

    private static final long serialVersionUID = -2188079057139084419L;

    public static void main(String[] args) {
        new MethodTest().test1();
        new MethodTest().test2(1L, 1);
        new MethodTest().test3(1L, 1, "");
    }

    public void test0(int a, int b, String s) {
        int c = b - a;
        test3((long) a, b, s);
    }

    public int test1() {
        int a = 1;
        int b = 2;
        System.out.println(a);
        return a;
    }

    public void test2(Long l, int i) {
    }

    public boolean test3(Long l, Integer i, String s) {
        System.out.println(l);
        {
            int ll = 2;
        }
        System.out.println(i);
        System.out.println(s);
        return true;
    }

    @Override
    public MethodTest clone() {
        try {
            MethodTest clone = (MethodTest) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
