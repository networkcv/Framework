package com.lwj.classLoader;

/**
 * Date: 2024/6/21
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ClassLoaderHSDB {

    private int a = 10;

    private final  static int b = 1;

    public static void main(String[] args) {

        int[] intArr = new int[1];

        ClassLoaderHSDB[] objArr = new ClassLoaderHSDB[1];

        Class<ClassLoaderHSDB> clazz = ClassLoaderHSDB.class;

        System.out.println("ClassLoaderHSDB");

//        while (true) {
//        }
    }
}
