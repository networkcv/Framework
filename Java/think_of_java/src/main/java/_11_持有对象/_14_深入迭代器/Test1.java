package _11_持有对象._14_深入迭代器;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * create by lwj on 2020/3/19
 */
public class Test1 {
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
        }
        for (Object obj :list ) {
            System.out.println(obj.toString());
        }
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()){
            Object obj = iterator.next();
        }
    }



}
