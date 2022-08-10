package com.lwj.classLoader.unit2;

/**
 * 父类先执行clinit---->子类clinit
 */
class TestClinit02 {
    static class Parent {
        public static int A = 1;

        static {
            A = 2;
        }
    }

    static class Sub extends Parent {
        public static int B = A; //2
    }


    public static void main(String[] args) {
        System.out.println(Sub.B); //2
    }
}