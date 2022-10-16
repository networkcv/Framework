package com.lwj._08_多态;

/**
 * create by lwj on 2020/3/12
 */
public class Test0 {
    public void fu1() {
        System.out.println("fu1()");
        fu2();
    }

    public void fu2() {
        System.out.println("fu2()");
    }

    static class Test1 extends Test0 {
        @Override
        public void fu2() {
            System.out.println("zi2()");
        }
    }

    public static void main(String[] args) {
        Test0 t = new Test1();
//        Test1 t = new Test1();
        t.fu1();
    }
    //Output
//    fu1()
//    zi2()

}
