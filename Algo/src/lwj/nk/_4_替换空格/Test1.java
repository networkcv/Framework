package lwj.nk._4_替换空格;

/**
 * create by lwj on 2018/9/27
 */
//题目：请实现一个函数，把字符串中的每个空格替换成"%20"，例如“We are happy.”，则输出“We%20are%20happy.”
public class Test1 {
    public static void main(String[] args) {
    test1();
    test2();
    }

    public static void test1() {
        String str = "We are happy.";
        String[] s = str.split(" ");
        for (int i = 0; i < s.length; i++) {
            if (i > 0) {
                System.out.print("%20");
            }
            System.out.print(s[i]);
        }
    }

    public static void test2() {
        StringBuilder sb = new StringBuilder();
        String str = "We are happy.";
        String[] s = str.split(" ");
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);
            if (i > 0) {
                sb.append("%20");
            }
        }

        System.out.println(sb);
    }
}
