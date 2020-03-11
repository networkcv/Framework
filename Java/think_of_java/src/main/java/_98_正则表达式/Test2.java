package _98_正则表达式;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by lwj on 2020/3/11
 * 匹配IP地址
 */
public class Test2 {

    @Test
    public void test() {
        Pattern compile = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        Matcher matcher = compile.matcher("192.168.1.0001");
        System.out.println(matcher.matches());
    }

    @Test
    public void test2() {
        String ip = "192.168.1.0001";
        Pattern pattern = Pattern.compile("\\.");
        Matcher matcher = pattern.matcher(ip);
        int last = 0;
        int ipField;
        while (matcher.find()) {
            ipField = Integer.parseInt(ip.substring(last, matcher.start()));
            last = matcher.end();
            if (ipField > 255 || ipField < 0) {
                System.out.println("error");
                break;
            }
        }
        ipField = Integer.parseInt(ip.substring(last));
        if (ipField > 255 || ipField < 0) {
            System.out.println("error");
            return;
        }
        System.out.println("ok");
    }
}
