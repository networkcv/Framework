package com.lwj.classLoader.unit2;

import java.io.*;

public class MyClassLoader extends ClassLoader {

    //1.定义字节码文件的路径
    private String codePath;

    //2.定义构造方法
    public MyClassLoader(ClassLoader parent, String codePath) {
        super(parent);
        this.codePath = codePath;
    }

    public MyClassLoader(String codePath) {
        this.codePath = codePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //声明输入流
        BufferedInputStream bis = null;
        //声明输出流
        ByteArrayOutputStream baos = null;
        //字节码路径
        try {
            String file =  codePath+name+".class";
            //初始化输入流
            bis = new BufferedInputStream(new FileInputStream(file));
            //初始化输出流
            baos = new ByteArrayOutputStream();
            //io读写操作
            int len;
            byte[] data = new byte[1024];
            while ((len = bis.read(data)) != -1){
                baos.write(data,0,len);
            }
            //获取内存中的字节数组
            byte[] bytes = baos.toByteArray();
            //调用defineClass 将字节数组转成Class实例
            Class<?> clazz = defineClass(null, bytes, 0, bytes.length);
            //返回class对象
            return  clazz;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
