package com.lwj.bytecode;


import java.io.IOException;

/**
 * create by lwj on 2020/2/24
 */
public class Test1<C> {
    String[]s;
    private C c;

    public void test(){
    }
    private C getC(){
        return c;
    }
    public  final  static int b=1;
    private int a=1;
    public int getA(){
        return a;
    }
    public void setA(int a){
        this.a=a;
    }

    public int exceptionTest()throws IOException ,Exception {
        int i;
        try{
            i=1/0;
            return i;
        }catch (NullPointerException e){
            i=2;
            return i;
        }finally {
            i=3;
            return i;
        }
    }
    public class InnerClass{
        int i=1;
        private void innerFun(){
            int i=2;
            String s="3";
            System.out.println(1);
        }
    }


}
