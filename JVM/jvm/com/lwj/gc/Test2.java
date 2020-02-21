package com.lwj.gc;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * create by lwj on 2020/2/17
 */
public class Test2 extends HashSet {
    public int size = 0;


    @Override
    public boolean addAll(Collection c) {
        return super.addAll(c);
    }

    public static void main(String[] args){
        Test2 test2 = new Test2();
        test2.addAll(Arrays.asList(1,2));
        System.out.println(test2.size);
    }
}

