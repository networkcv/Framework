package com.lwj.classLoader.unit2;

/**
 * 1.获取不同的类加载器
 */
public class Demo2_ClassLoader {
    public static void main(String[] args) {
        //1.获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        //2.获取扩展类加载器 (扩展类加载器是系统类加载器的父元素)
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println(extClassLoader);  //sun.misc.Launcher$ExtClassLoader@6d6f6e28

        //3.获取启动类加载器
        ClassLoader loaderParent = extClassLoader.getParent();
        System.out.println(loaderParent); //null

        //4.获取用户自定义类加载器
        ClassLoader classLoader = Demo2_ClassLoader.class.getClassLoader();
        System.out.println(classLoader);  //sun.misc.Launcher$AppClassLoader@18b4aac2

        //5.通过启动类加载器的对象,获取启动类加载器.
        ClassLoader loaderParent2 = String.class.getClassLoader();
        System.out.println(loaderParent2);
    }
}
