package com.lwj.classLoader.unit2;

public class ParentA {
    static {
        System.out.println("1");
    }

    public ParentA() {
        System.out.println("2");
    }
}

class SonB extends ParentA {
    static {
        System.out.println("a");
    }

    public SonB() {
        super();
        System.out.println("b");
    }

    public static void main(String[] args) {
        ParentA ab = new SonB();  //1  a  2  b
        ab = new SonB();  // 2  b
    }
}