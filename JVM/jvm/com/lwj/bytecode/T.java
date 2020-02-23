package com.lwj.bytecode;

/**
 * create by lwj on 2020/2/3
 */
public class T {

    int test1(String a) {

        int b = 1;
        {
            int c = 2;
        }
        return 0;
    }
}
