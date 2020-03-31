package _09_接口.抽象类.demo4;

/**
 * create by lwj on 2020/3/13
 */
public class Test {
    public static void testPrint(T t) {
        ((T1) t).print();
    }

    public static void testPrint(D d) {
        d.print();
    }

    public static void main(String[] args) {
        T1 t1 = new T1();
        D1 d1 = new D1();
        testPrint(t1);
        testPrint(d1);
    }
}

abstract class T {
}

class T1 extends T {
    void print() {
        System.out.println("t1");
    }
}

abstract class D {
    abstract void print();
}

class D1 extends D {
    @Override
    void print() {
        System.out.println("d1");
    }
}