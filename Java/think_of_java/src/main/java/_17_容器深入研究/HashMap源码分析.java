package _17_容器深入研究;

import java.util.HashMap;

/**
 * create by lwj on 2020/3/25
 */
public class HashMap源码分析 {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1,"1");
        map.put(17,"17");
        System.out.println(map);
    }
}
