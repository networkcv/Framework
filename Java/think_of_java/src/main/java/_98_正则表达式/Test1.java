package _98_正则表达式;

/**
 * create by lwj on 2020/3/10
 */
public class Test1 {
    public static void main(String[] args){
        String phone = "15229265350";
        boolean res = phone.matches("152\\d{8}");
        System.out.println(res);

        System.out.println("1&3".matches("1&3"));
        System.out.println("1-3".matches("1&3"));
        System.out.println("1&&3".matches("1&3"));
        System.out.println("1%3".matches("1&3"));

        System.out.println("abc".matches("a.c"));
        System.out.println("abbc".matches("a.c"));
        System.out.println("ac".matches("a.c"));


        System.out.println("A1C".matches("^A.*C$"));
        System.out.println("AA".matches("^A$"));

        System.out.println("A0001".matches("^A.\\d{3}$"));
        System.out.println("A0001".matches("^A\\d{4}$"));
    }
}
