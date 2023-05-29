package com.lwj.classLoader;

/**
 * Date: 2022/8/2
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Test6 {
    public static void main(String[] args) {
        ZI zi = new ZI();
        zi.name();
        zi.name();
    }


    public void interfaceFun(){
        InterfaceImpl anInterface = new InterfaceImpl();
        anInterface.fun();
}

}


interface Fun1 {
    void fun();
}

class InterfaceImpl implements  Fun1{

    @Override
    public void fun() {

}
}

class FU {
    final void name() {
        System.out.println("fu");
    }
}

class ZI extends FU {

}

