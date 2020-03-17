package _10_内部类._02_链接到外部类.demo3;

/**
 * create by lwj on 2020/3/17
 */
public class Outer {
    private String s;

    public Outer(String s) {
        this.s = s;
    }

    class Inner {
        private Inner() {
        }
        public String toString() {
            return s;
        }
    }

    public Inner inner() {
        return new Inner();
    }

    public static void main(String[] args) {
        Outer.Inner inner = new Outer("hello").inner();
        System.out.println(inner.toString());

    }
}
