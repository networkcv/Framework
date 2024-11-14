package com.lwj._17_集合;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
        list.add(0, 1);
        list.get(0);
        list.indexOf(1);
        ArrayList<Integer> clone = (ArrayList<Integer>) ((ArrayList<Integer>) list).clone();
        System.out.println(clone);
        char ch='a';
        System.out.println(ch*1);


    }

    @Test
    public void test() {
        List<Integer> arrayList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Iterator<Integer> iterator = arrayList.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (count++ == 1) {
                iterator.remove();
            }
        }
        System.out.println(arrayList);
    }

    void test1() {
        System.out.println(1);
    }

    ;

    class Inner {
        void test2() {
            ArrayList源码分析.this.test1();
        }
    }

    @Test
    public void test2() {
        new Inner().test2();
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        boolean b = list.retainAll(Arrays.asList(1, 2,3,44,4,4));
        System.out.println(list);
    }
}
