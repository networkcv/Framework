package lambda;

import org.junit.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * create by lwj on 2020/2/26
 * 方法引用
 * - 情况一	对象：：实例方法名
 * - 情况二	类：：静态方法名
 * - 情况三	类：：实例方法名
 */
public class LambdaTest3 {

    public void getInteger(Integer i) {
        System.out.println("i: " + i);
    }

    public static void printInteger(Integer i) {
        System.out.println("i: " + i);
    }

    @Test
    public void test() {
        Consumer<String> consumer = (s) -> System.out.println(s);
        consumer.accept("a");
        //情况一
        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("b");
        //情况一
        Consumer<Integer> consumer2 = new LambdaTest3()::getInteger;
        consumer2.accept(1);
        //情况二
        Consumer<Integer> consumer3 = LambdaTest3::printInteger;
        consumer3.accept(2);
        //情况三
        Comparator<String> comparator0 = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Comparator<String> comparator = (s1, s2) -> s1.compareTo(s2);
        Comparator<String> comparator2 = String::compareTo;
        System.out.println(comparator2.compare("1", "2"));

        Function<Object, String> function0 = new Function<Object, String>() {
            public String apply(Object o) {
                return o.toString();
            }
        };
        Function<Object, String> function = o -> o.toString();
        Function<Object, String> function2 = Objects::toString;
        System.out.println(function2.apply("123"));
    }

}
