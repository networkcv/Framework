package _03_初始化与清理._02_方法重载;

import org.junit.Test;

/**
 * create by lwj on 2020/3/11
 * 实参向上转型
 */
public class Test1 {
    void  f(byte b){
        System.out.println("f(byte b)");
    }
    void  f(short b){
        System.out.println("f(short b)");
    }
    void  f(int b){
        System.out.println("f(int b)");
    }

    void c(int i){
        System.out.println("c(int i)");
    }
    void c(long i){
        System.out.println("c(long i)");
    }

    @Test
    public void test(){
        c('1');
    }
}
