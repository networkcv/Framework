package com.lwj._11_持有对象._09_Set.demo16;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * create by lwj on 2020/3/18
 */
public class Test1 {
    private static Set<Character> set = new HashSet<>();
    private static int count = 0;

    static {
        Collections.addAll(set, 'A', 'O', 'E', 'I', 'U', 'a', 'o', 'e', 'i', 'u');
    }

    static int statistics(String word) {
        for (Character c : word.toCharArray()) {
            if (set.contains(c)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(statistics("ABCEDF"));
    }
}
