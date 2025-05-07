package com.lwj.minijvm;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4);
        for (Integer integer : integers) {
            switch (integer) {
                case 1: {
                    System.out.println(integer);
                    break;
                }
                case 2:
                    System.out.println(integer);
                    return;
                case 3: {
                    System.out.println(integer);
                }
            }
        }
    }
}
