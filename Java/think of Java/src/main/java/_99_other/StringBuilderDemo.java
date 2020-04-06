package _99_other;


import org.junit.Test;

/**
 * create by lwj on 2020/4/6
 */
public class StringBuilderDemo {

//    public void test0() {
//        String str = "111" + "222";
//    }

    public void test1() {
        final String str1 = "111";
        final String str2 = "222";
        String str = str1 + str2;
    }
//
//    void test2() {
//        String str1 = "111";
//        String str2 = "222";
//        String str=new StringBuilder().append(str1).append(str2).toString();
//    }

    //
//    public void test() {
//        StringBuilder str = new StringBuilder("pingtouge");
//        for (int i = 0; i < 1000; i++) {
//            str = str.append(i);
//        }
//    }
//
//    public void test1(){
//        String str="";
//        for (int i = 0; i < 1000; i++) {
//            str = str+i;
//        }
//    }
//
    @Test
    public void test2() {
        String a="a"+"a";
        String a1 = new String("aa");
        String a2 = new String("aa");
        System.out.println(a1 == a2);
        String b1 = new String("b").intern();
        String b2 = new String("b").intern();
        System.out.println(b1 == b2);
        char c='a';
        byte b=97;
        System.out.println(c);
        System.out.println(b);
        char c1='中';
        System.out.println(c1);
        byte bb='a';
        System.out.println(bb);
    }



}

