package com.lwj.classLoader.unit2;

public class MyClassLoaderTest {
    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader("d:/");
        try {
            Class<?> clazz = classLoader.loadClass("TestMain");
            System.out.println("TestMain字节码是由"+clazz.getClassLoader().getClass().getName()+"加载的..");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
