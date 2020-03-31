package _03_初始化与清理._01_用构造器确保初始化;

/**
 * create by lwj on 2020/3/11
 */
public class Test1 {
    String s;
    int i;
    boolean b;
    char c;

    public static void main(String[] args) {
        Test1 test2 = new Test1();
        System.out.println(test2.s);
        System.out.println(test2.i);
        System.out.println(test2.b);
        System.out.println("-" + test2.c + "-");
    }
}
