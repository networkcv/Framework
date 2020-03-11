package _98_正则表达式;

import org.junit.Test;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by lwj on 2020/3/10
 */
public class Test1 {
    public static void main(String[] args) {
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

        Pattern pattern = Pattern.compile("(.*)-([a-z]{4})");
        Matcher matcher = pattern.matcher("hello-java");
        if (matcher.matches()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }

    @Test
    public void test() {
        Pattern pattern = Pattern.compile("(\\d*?)(\\d*)");
        Matcher matcher = pattern.matcher("12345");
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1));
            System.out.println("group2=" + matcher.group(2));
        }
    }

    @Test
    public void test3(){
        Pattern pattern = Pattern.compile("(\\d??)(9*)");
        Matcher matcher = pattern.matcher("9999");
        System.out.println(matcher.matches());
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1));
            System.out.println("group2=" + matcher.group(2));
        }
    }

    @Test
    public void test4(){
        String s = "the quick brown fox jumps over the lazy dog.";
        Pattern p = Pattern.compile("\\wo\\w");
        Matcher m = p.matcher(s);
        while (m.find()) {
            String sub = s.substring(m.start(), m.end());
            System.out.println(sub);
        }
    }

    @Test
    public void test5(){
        HashMap<String,String> map =new HashMap<>();
        map.put("field","name");
        map.put("table","user");
        String sql="select #{field} from #{table} order by id ";
        Pattern templateEngine = Pattern.compile("\\#\\{(\\w+)}");
        Matcher matcher = templateEngine.matcher(sql);
        StringBuffer res = new StringBuffer();
        while(matcher.find()){
            //用于截取获得 "#{field}" 中的 field
            String key=sql.substring(matcher.start() + 2, matcher.end() - 1);   //field
            //这里第一次find到的内容是 "select #{field}"，appendReplacement方法将 #{field} 替换为map.get(key)的内容
            //然后将结果 "select name" 添加到res中
            matcher.appendReplacement(res,map.get(key));
        }
        matcher.appendTail(res);
        System.out.println(res.toString());
    }
}
