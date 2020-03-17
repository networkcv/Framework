package _10_内部类._01_创建内部类.demo1;

/**
 * create by lwj on 2020/3/17
 */
public class Outer {
    class Inner {
    }

    public Inner inner() {
        return new Inner();
    }

    public static void main(String[] args) {
        Outer.Inner inner = new Outer().inner();
    }
}
