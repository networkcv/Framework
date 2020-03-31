package _10_内部类._02_链接到外部类.demo2;

/**
 * create by lwj on 2020/3/17
 */
public class Test2 {
    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        sequence.add(new Demo2("a"));
        sequence.add(new Demo2("b"));
        sequence.add(new Demo2("c"));
        sequence.add(new Demo2("d"));
        Iterator iterator = sequence.iterator();
        while (iterator.hashNext()) {
            Object next = iterator.next();
            System.out.println(next.toString());
        }
    }
}
