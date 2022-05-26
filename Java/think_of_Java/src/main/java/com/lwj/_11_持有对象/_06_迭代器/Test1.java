package com.lwj._11_持有对象._06_迭代器;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * create by lwj on 2020/3/18
 */
public class Test1 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        //下面代码报 ConcurrentModificationException 并发修改异常
        try {
//            list.removeIf(integer -> integer == 1);
            for (Integer i : list) {
                if (i==1){
                    list.remove(i);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(list);
        //迭代器可以
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next()==2) {
                iterator.remove();
            }
        }
        System.out.println(list);
    }
    @Test
    public void test(){
        for (Map.Entry entry: System.getenv().entrySet()) {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }
}
