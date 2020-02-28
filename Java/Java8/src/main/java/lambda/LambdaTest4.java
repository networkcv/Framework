package lambda;

import org.junit.Test;
import util.Student;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * create by lwj on 2020/2/26
 * 构造器引用
 * 和方法引用类似，函数式接口的抽象方法的形参列表和构造器的形参列表一致，抽象方法的返回值类型即为构造器所属类的类型。
 */
public class LambdaTest4 {
    @Test
    public void test() {
        //无参 有返回值 和Supplier 中的T get()方法类似
        Supplier<Student> supplier = () -> new Student();
        Supplier<Student> supplier2 = Student::new;
        supplier.get();
        supplier2.get();
        //有一个参数 有返回值，和Function 中的T apply(S) 方法类似
        Function<String, Student> function = s -> new Student(s);
        Function<String, Student> function2 = Student::new;
        function.apply("tom");
        function2.apply("tom");
        //有两个参数 有返回值，和BiFunction 中的
        BiFunction<String, Integer, Student> biFunction = (s, i) -> new Student(s, i);
        BiFunction<String, Integer, Student> biFunction2 = Student::new;
        biFunction.apply("tom", 1);
        biFunction2.apply("tom", 1);
    }


}
