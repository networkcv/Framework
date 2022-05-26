package com.lwj._12_异常处理;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * create by lwj on 2020/3/31
 * 处理异常的基本原则
 */
public class Test2 {
    public void throwEarly(String fileName) throws FileNotFoundException {
        Objects.requireNonNull(fileName);
    }

    @Test
    public void test(){
        String fileName=null;
        try {
            throwEarly(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
