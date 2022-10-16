package com.lwj._09_接口.适配接口;

import java.util.Random;

/**
 * create by lwj on 2020/3/16
 */
public class CharGenerate {
    Random random = new Random(31);
    char[] c = "ABCDEFGH".toCharArray();

    char next() {
        int i = random.nextInt(c.length);
        return c[i];
    }
}
