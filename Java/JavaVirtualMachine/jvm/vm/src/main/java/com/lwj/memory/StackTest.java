package com.lwj.memory;

/**
 * -Xss256k 1961
 */
public class StackTest {
    static long count = 0 ;
    public static void main(String[] args) {
        count++;
        System.out.println(count);  //8523
        main(args);
    }
}
