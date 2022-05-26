package com.lwj._11_持有对象._10_Map.demo20;

import java.util.HashMap;
import java.util.Map;

/**
 * create by lwj on 2020/3/18
 */
public class Test1 {
    private static HashMap<Character, Integer> map = new HashMap<>();

    static {
        map.put('A', 0);
        map.put('O', 0);
        map.put('E', 0);
        map.put('I', 0);
        map.put('U', 0);
    }

    public static Map<Character, Integer> statistics(String word) {
        for (Character character : word.toCharArray()) {
            if (map.containsKey(character)) {
                map.put(character, map.get(character) + 1);
            }
        }
        return map;
    }
    public static void main(String[] args){
       statistics("AAABCDEEO");
        System.out.println(map);
    }
}
