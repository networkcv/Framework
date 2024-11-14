package com.lwj._01_基础数据类型._01_数值溢出;

/**
 * Date: 2024/11/14
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NumberOver {
    public static void main(String[] args) {
        byte a = -128;
        byte b = 1;
        byte res = (byte) (a - b);
        System.out.println(res);
    }
}
