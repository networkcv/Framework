package com.lwj._07_复用类;

/**
 * create by lwj on 2020/3/12
 */
public class Test1 extends Test0 {
    Test1() {
        super(1);
    //  There is no default constructor available in
        System.out.println(1);
//        super();    //Call to 'super()' must be first statement in constructor body
    }


    Test1(long i) {
        this();
        super.t();
//        this();     //Call to 'this()' must be first statement in constructor body
    }

    @Override
    void t() {
        System.out.println(this);
        super.t();
    }

    public static void main(String[] args) {
        Test1 test1 = new Test1();
        System.out.println();
    }
}
