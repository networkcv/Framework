package com.lwj.bytecode;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * create by lwj on 2020/3/30
 */
public class Test3 {
    public void test() {
        try {
            InputStream is = new FileInputStream("test.txt");
        } catch (FileNotFoundException e) {
            int i = 0;
        } catch (IOException e) {
            int i = 1;

        } catch (Exception e) {
            int i = 2;

        } finally {
            System.out.println("finally");
        }
    }

    public void test0() throws FileNotFoundException {
    }

    public void test1() {
        Test3 test3 = new Test3();
    }

    public void test2() {
        new Test3();
    }

    public int test3() {
        int i;
        try {
            i = 1;
            return i;
        } finally {
            i = 2;
            return i;
        }
    }
}