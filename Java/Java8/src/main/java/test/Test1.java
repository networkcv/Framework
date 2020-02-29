package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * create by lwj on 2020/2/29
 */
public class Test1 {
    @Test
    public void test1() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        Trader[] traders = new Trader[]{raoul, mario, alan, brian};

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        System.out.println("1.找出2011年发生的所有交易，并按交易额排序");
        transactions.stream().filter(t -> t.getYear() == 2011).sorted((o1, o2) -> o1.getValue() < o2.getValue() ? -1 : 0).forEach(System.out::println);

        System.out.println("2.交易员在哪些不同的城市工作过");
        Stream.of(raoul, mario, alan, brian).map(Trader::getCity).distinct().forEach(System.out::println);

        System.out.println("3.查找所有来自剑桥的交易员，并按姓名排序");
        Arrays.stream(traders).filter(t->"Cambridge".equals(t.getCity())).sorted(Comparator.comparing(Trader::getName)).forEach(System.out::println);

        System.out.println("4.返回所有交易员的姓名字符串，并按字母顺序排序");
        Arrays.stream(traders).map(Trader::getName).sorted(Comparator.comparing(Trader::getName));

        System.out.println("5.有没有交易员在米兰工作的？");
        System.out.println("6.打印生活在剑桥的交易员的所有交易额");
        System.out.println("7.所有交易中，最高的交易额是多少");
        System.out.println("8.找到交易额最小的交易");


    }
}
