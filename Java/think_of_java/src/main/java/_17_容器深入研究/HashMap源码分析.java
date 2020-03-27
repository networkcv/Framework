package _17_容器深入研究;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * create by lwj on 2020/3/25
 */
public class HashMap源码分析 {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1,"1");
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        for (int i = 0, count = 0;count<8 ; i += 16,count++) {
        map.put(i, i + "");
        }
        System.out.println(map);
        map.get(16);
    }
}
