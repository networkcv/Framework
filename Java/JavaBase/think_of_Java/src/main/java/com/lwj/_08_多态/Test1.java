package com.lwj._08_多态;

/**
 * create by lwj on 2020/3/12
 */
public class Test1 {
    static class Fu{
        private void f(){
            System.out.println("fu");
        }
    }
    static class Zi extends  Fu{
        public void f(){
            System.out.println("zi");
        }
    }
    public static void main(String[] args){
        Fu zi = new Zi();
        zi.f();
        Zi zi2 = new Zi();
        zi2.f();
    }
}
