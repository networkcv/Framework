package com.lwj.java8.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        transactions.stream().filter(t -> t.getYear() == 2011).sorted(Comparator.comparingInt(Transaction::getValue)).forEach(System.out::println);

        System.out.println("2.交易员在哪些不同的城市工作过");
        Stream.of(raoul, mario, alan, brian).map(Trader::getCity).distinct().forEach(System.out::println);

        System.out.println("3.查找所有来自剑桥的交易员，并按姓名排序");
        Arrays.stream(traders).filter(t -> "Cambridge".equals(t.getCity())).sorted(Comparator.comparing(Trader::getName)).forEach(System.out::println);

        System.out.println("4.返回所有交易员的姓名字符串，并按字母顺序排序");
        Arrays.stream(traders).map(Trader::getName).sorted(String::compareTo).forEach(System.out::println);

        System.out.println("5.有没有交易员在米兰工作的？");
        Arrays.stream(traders).filter(t -> t.getCity().equals("Milan")).forEach(System.out::println);

        System.out.println("6.打印生活在剑桥的交易员的所有交易额");
        Optional<Integer> res = transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge")).map(Transaction::getValue).reduce(Integer::sum);
        System.out.println(res.get());

        System.out.println("7.所有交易中，最高的交易额是多少");
        Optional<Integer> max = transactions.stream().map(Transaction::getValue).max(Integer::compareTo);
        System.out.println(max.get());

        System.out.println("8.找到交易额最小的交易");
//        List<Transaction> collect = transactions.stream().sorted((t1, t2) -> t1.getValue() < t2.getValue() ? 1 : 0).limit(1).collect(Collectors.toList());
//        System.out.println(collect.get(0));
        Optional<Transaction> min = transactions.stream().min(Comparator.comparingInt(Transaction::getValue));
        System.out.println(min.get());


    }
}
