package _10_内部类._8_为什么需要内部类.demo24;

/**
 * create by lwj on 2020/3/17
 */
public class B {
    U[] us;
    private int count = 0;

    public B(int size) {
        us = new U[size];
    }

    public void add(U u) {
        if (count++ != us.length) {
            us[count-1] = u;
        }
    }

    public void reset() {
        us = null;
    }

    public void print() {
        for (U u : us) {
            u.method1();
            u.method2();
            u.method3();
        }
    }

    public static void main(String[] args) {
        B b = new B(3);
        for (int i = 0; i < 3; i++) {
            b.add(new A().creatU());
        }
        b.print();
    }
}
