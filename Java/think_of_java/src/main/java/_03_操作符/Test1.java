package _03_操作符;

import org.junit.Test;

import java.util.Random;

/**
 * create by lwj on 2020/3/9
 */
public class Test1 {
    public static void main(String[] args) {
        Random random = new Random(1);
        System.out.println(random.nextInt(10));
        System.out.println(random.nextInt(10));
    }

    @Test
    public void test() {
        int a = 0b11;
        int b = 011;
        int c = 11;
        int d = 0x1f;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
    }

    @Test
    public void test2() {
        long l = 10L;
        float f = 1.1f;
        double d = 1.1d;
    }

    @Test
    public void test3() {
        System.out.println((byte) 127);
        System.out.println((byte) 128);
        System.out.println((byte) 129);
        System.out.println((byte) 255);
        System.out.println((byte) 256);
        int i = 1;
        long l = i;
        int i2 = (int) l;
        System.out.println(Integer.toBinaryString(255));
        System.out.println(Short.MAX_VALUE);
        byte res = (byte) ((byte) 127 + (byte) 1);
        System.out.println(res);
    }

    @Test
    public void test4() {
        System.out.println(~(byte)-128);
    }
}
