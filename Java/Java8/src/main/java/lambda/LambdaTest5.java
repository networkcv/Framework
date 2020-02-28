package lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

/**
 * create by lwj on 2020/2/26
 * 数组引用：可以把数组类型看作一个特殊的类，写法与构造器引用类似
 */
public class LambdaTest5 {

    @Test
    public void test() {
        //由于构建数组时，必须指定数组长度
        Function<Integer, String[]> function = integer -> new String[integer];
        Function<Integer, String[]> function2 = String[]::new;
        System.out.println(Arrays.asList(function.apply(1)));
        System.out.println(Arrays.asList(function2.apply(2)));
    }


}
