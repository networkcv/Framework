package netty.ch3;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Date: 2024/2/8
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class AtomicIntegerFieldUpdaterDemo {
    public volatile int a;

    public AtomicIntegerFieldUpdaterDemo(int a) {
        this.a = a;
    }

    public void test() {
        AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterDemo> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterDemo.class, "a");
        System.out.println(fieldUpdater.get(this));
        fieldUpdater.set(this, 2);
        System.out.println(fieldUpdater.get(this));

    }

    public static void main(String[] args) {
        new AtomicIntegerFieldUpdaterDemo(1).test();
    }
}
