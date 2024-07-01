package com.lwj.bytecode;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class InnerClassTest {

    private static int privateStaticIntVar;

    private int privateIntVar;

    class InnerClass {
        void test() {
            System.out.println(privateIntVar);
        }

//        public static void main(String[] args) { error
//            System.out.println(InnerClassTest.privateStaticIntVar);
//        }
    }

    static class StaticInnerClass {
        void test() {
//            System.out.println(privateIntVar); error
        }

        public static void main(String[] args) {
            System.out.println(InnerClassTest.privateStaticIntVar);

        }
    }
}

class outerClass {
    public static void main(String[] args) {
//        System.out.println(InnerClassTest.staticIntVar); error
    }
}
