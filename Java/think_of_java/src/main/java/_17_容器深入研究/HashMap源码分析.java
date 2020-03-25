package _17_容器深入研究;

import java.util.HashMap;

/**
 * create by lwj on 2020/3/25
 */
public class HashMap源码分析 {
    public static void main(String[] args) {
        HashMap<Object, Number> map = new HashMap<>();
        map.put(3, 1);
        Integer key=3;
        System.out.println(key.hashCode());
        int h;
        h=( h = key.hashCode()) ^ (h >>> 16);
        System.out.println(h);
    }
}
