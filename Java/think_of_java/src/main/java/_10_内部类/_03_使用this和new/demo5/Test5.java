package _10_内部类._03_使用this和new.demo5;

/**
 * create by lwj on 2020/3/17
 */
public class Test5 {
    public static void main(String[] args) {
        Outer outer = new Outer();
//        Outer.Inner inner = outer.new Inner();// 因为内部类的构造器私有所以无法访问。
    }
}
