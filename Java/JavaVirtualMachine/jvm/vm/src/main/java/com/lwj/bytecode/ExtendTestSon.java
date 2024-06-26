package com.lwj.bytecode;


/**
 * Date: 2024/6/25
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ExtendTestSon extends ExtendTestFather {
    private int i = test();
    private static int j = method();

    static {
        System.out.print("(6)");
    }

    ExtendTestSon() {
        System.out.print("(7)");
    }

    {
        System.out.print("(8)");
    }

    public int test() {
        System.out.print("(9)");
        return 1;
    }

    public static int method() {
        System.out.print("(10)");
        return 1;
    }

    public static void main(String[] args) {
        ExtendTestSon s1 = new ExtendTestSon();
        System.out.println();
        ExtendTestSon s2 = new ExtendTestSon();
    }
}
