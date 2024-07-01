package com.lwj.bytecode;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2024/6/30 
 *
 * Description: 
 *
 * @author 乌柏
 */
public class GenericTest {

    public void test(List<String> list) {
        System.out.println(list);
    }

    public void inspect(List<Object> list) {
    }

    public void test() {
        List<String> strs = new ArrayList<String>();
//        inspect(strs); // 编译错误
    }

}
