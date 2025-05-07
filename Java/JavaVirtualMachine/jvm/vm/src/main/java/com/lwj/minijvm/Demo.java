package com.lwj.minijvm;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Demo {
    public static void main(String[] args) {
//        System.out.println(1);
//        System.out.println(get());
//        System.out.println(max(1, 4));
        System.out.println(recursion(100));

    }

    public static int recursion(int i) {
        if (i == 0) {
            return i;
        }
        return recursion(i - 1) + i;
    }

    public static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    public static int get() {
        return 1;
    }
}
