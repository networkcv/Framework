package stream;

import org.junit.Test;
import util.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * create by lwj on 2020/2/27
 * Stream的中间操作
 */
public class StreamTest2 {
    ArrayList<Student> students = new ArrayList<>();

    {
        BiFunction<String, Integer, Student> biFunction = Student::new;
        students.add(biFunction.apply("tom", 18));
        students.add(biFunction.apply("jack", 19));
        students.add(biFunction.apply("ros", 20));
        students.add(biFunction.apply("tom", 20));
        students.add(biFunction.apply("jack", 19));
    }

    /**
     * 筛选与切片
     */
    @Test
    public void test() {

        // filter(Predicate p) 从流中排除某些元素
        Stream<String> stream = Stream.of("1", "2", "3", "4");
        stream.filter(s -> s.equals("1")).forEach(System.out::println);

        // limit(n) 截断流，使其元素不超过给定数量
        Stream<String> stream2 = Stream.of("1", "2", "3", "4");
        stream2.limit(3).forEach(s -> System.out.print(s + " "));
        System.out.println();

        //skip(n) 跳过元素，返回一个扔掉前n个元素的流，元素不足的话，会返回空流
        Stream<String> stream3 = Stream.of("1", "2", "3", "4");
        stream3.skip(3).forEach(System.out::println);

        //distinct() 筛选去重，通过流所产生元素的hashCode()和equals()去除重复元素
        students.stream().forEach(System.out::println);
        System.out.println("去重后");
        students.stream().distinct().forEach(System.out::println);
    }

    /**
     * 映射
     */
    @Test
    public void test2() {
        //map(Function f) 接收一个函数作为参数，作为映射规则，将元素转换成其他形式或提取信息，
        //该函数会被应用到每个元素上，并将其映射成一个新的元素。
        //筛选出名字长度大于3的姓名
        students.stream().map(Student::getName).filter(s -> s.length() > 3).forEach(System.out::println);
    }

    @Test
    public void test3() {
        //flatMap(Function f) 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把流合并
        Stream<String> stream = Stream.of("12", "34", "56", "78");
        stream.flatMap(StreamTest2::getCharacter).forEach(System.out::println);
    }

    public static Stream<Character> getCharacter(String s) {
        ArrayList<Character> characters = new ArrayList<>();
        for (Character c : s.toCharArray()) {
            characters.add(c);
        }
        return characters.stream();
    }

    /**
     * 排序
     */
    @Test
    public void test4() {
        Stream<Integer> integerStream = Stream.of(321, 54, 35, 43, 6, 546, 24, 132, 2, 21);
        integerStream.sorted().forEach(s -> System.out.print(s + " "));
        System.out.println();

        students.stream().sorted((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge())).forEach(System.out::println);
        students.stream().sorted(Comparator.comparingInt(Student::getAge)).forEach(System.out::println);
        students.stream().sorted((s1, s2) -> {
            return s1.getAge() < s2.getAge() ? -1 : 0;
        }).forEach(System.out::println);
    }
}
