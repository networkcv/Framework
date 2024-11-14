package com.lwj._17_集合;

import java.util.HashMap;

/**
 * create by lwj on 2020/3/25
 */
public class HashMap源码分析 {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>(64);
        for (int i = 0, count = 0; count < 9; i += 64, count++) {
            map.put(i, i + "");
        }
        System.out.println();
    }
}
