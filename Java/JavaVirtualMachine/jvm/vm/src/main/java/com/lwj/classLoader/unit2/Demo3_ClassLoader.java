//package com.lwj.classLoader.unit2;
//
////import sun.misc.Launcher;
//
//import java.net.URL;
//
///**
// * 目的查看各个类加载器加载的内从
// * 1.启动引导类加载信息
// * 2.扩展类加载器
// */
//public class Demo3_ClassLoader {
//    public static void main(String[] args) {
//        System.out.println("*********启动引导类加载器**********");
////        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
//        for (URL urL : urLs) {
//            System.out.println(urL.toExternalForm());
//        }
//        /*System.out.println("*********扩展类加载器**********");
//        String property = System.getProperty("java.ext.dirs");
//        String[] extArr = property.split(";");
//        for (String extPath : extArr) {
//            System.out.println(extPath);
//        }*/
//    }
//}
