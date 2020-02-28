package lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * create by lwj on 2020/2/26
 * java内置的4种函数式接口
 */
public class LambdaTest2 {

    //消费型接口 Consumer<T>	void accept(T t)
    public void consumer(Integer integer, Consumer<Integer> consumer) {
        consumer.accept(integer);
    }

    @Test
    public void test() {
        consumer(100, s -> System.out.println("花费" + s));
    }

    //供给型接口 Supplier<T> T get()
    public Integer getTime(Supplier<Integer> suppliers) {
        return suppliers.get();
    }

    @Test
    public void test2() {
        System.out.println(getTime(()->2));

        Supplier<String> sp = () -> "1";
        System.out.println(sp.get());
    }

    //函数型接口 Function<T,R> R apply(T t)
    public List<String> filterString(List<String> list, Predicate<String> predicate) {
        List<String> res = new ArrayList<>();
        for (String s : list) {
            if (predicate.test(s)) {
                res.add(s);
            }
        }
        return res;
    }

    @Test
    public void test4() {
        List<String> list = Arrays.asList("1", "2", "3", "11");
        List<String> res = filterString(list, s -> s.startsWith("1"));
        System.out.println(res);
    }

}
