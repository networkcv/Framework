package com.lwj._04_prototype_pattern.demo2;

public class TestClient {
    public static void main(String[] args) throws InterruptedException, CloneNotSupportedException {
        Laptop laptop=null;
        long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            laptop= new Laptop();
        }
        long end = System.currentTimeMillis();
        System.out.println("new对象消耗时间:"+(end-start));
        long start1 = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            laptop = (Laptop)laptop.clone();
        }
        long end1 = System.currentTimeMillis();
        System.out.println("克隆对象消耗时间:"+(end1-start1));
    }
}
