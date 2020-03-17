package _10_内部类._03_使用this和new.demo5;

/**
 * create by lwj on 2020/3/17
 */
public class Outer {
    private class Inner{}
    public static void main(String[] args){
        Outer outer = new Outer();
        Inner inner = outer.new Inner();
    }
}

