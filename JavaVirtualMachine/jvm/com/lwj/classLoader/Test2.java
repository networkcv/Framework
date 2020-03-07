package com.lwj.classLoader;

/**
 * create by lwj on 2020/1/7
 * 之前我们知道数组是一个对象，而这个对象是由哪个类创建的？由数组存放类型的类创建的吗？
 * 对于数组实例来说，其类型是在JVM运行期间动态生成的，如果是引用类型的数组，那么会根据其
 * 存储类型动态生成对应的Class对象，该字节码对象的名称为 [L com.lwj.xxx
 * 如果是基础类型的数组，则对应数组类型的字节码对象名称为 int[]->[I  char[]->[C ...
 */
public class Test2 {

    public static void main(String[] args) {

        MyTest2[] ts = new MyTest2[1];
        System.out.println(ts.getClass());
        byte[] bytes = new byte[1];
        System.out.println(bytes.getClass());
        int[] ints = new int[1];
        System.out.println(ints.getClass());
    }
}

class MyTest2 {
    static {
        System.out.println("T");
    }
}