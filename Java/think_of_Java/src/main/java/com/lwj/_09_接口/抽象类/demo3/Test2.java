package com.lwj._09_接口.抽象类.demo3;

/**
 * create by lwj on 2020/3/13
 */
public class Test2 {
    public static void main(String[] args) {
        T2 t21 = new T21();
        t21.print();
    }

}

abstract class T2 {
    abstract void print();

    public T2() {   //public T2(this) 这里其实隐式的将this对象传了进来
        this.print();
    }
}

class T21 extends T2 {
    int i = 1;

    public T21() {
        super();
        //int i=1 对应的字节码 在<init>方法中会放在super()的后边和我们在构造器中定义代码的前边
        i=2;
    }

    @Override
    void print() {
        System.out.println(i);
    }
}