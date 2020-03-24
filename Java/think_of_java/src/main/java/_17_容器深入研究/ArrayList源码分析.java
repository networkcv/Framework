package _17_容器深入研究;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/3/24
 */
public class ArrayList源码分析 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(11);
//        List<String> list = new ArrayList<>();
        list.add(1);
        list.addAll(Arrays.asList(1, 2, 3, 4, 5));
        list.add(0,1);
        list.get(0);
        list.indexOf(1);
        ArrayList<Integer> clone = (ArrayList<Integer>) ((ArrayList<Integer>) list).clone();
        System.out.println(clone);


    }

    @Test
    public void test() {
        int i = Integer.MAX_VALUE + 1;
        System.out.println(i);
    }
}
