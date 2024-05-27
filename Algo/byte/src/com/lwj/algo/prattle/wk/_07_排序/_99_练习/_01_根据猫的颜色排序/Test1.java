package com.lwj.algo.prattle.wk._07_排序._99_练习._01_根据猫的颜色排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/13.
 */
public class Test1{
    public static void main(String[] args) {
        Cat [] cats =new Cat[5];
        for(int i=0;i<5;i++){
            cats[i]=new Cat(i,"白");
        }

        cats[0]=new Cat(0,"黑");
        cats[3]=new Cat(3,"黑");
        //黑白白黑白
//        sort1(cats);
//        System.out.println(Arrays.toString(cats));
        cats[1]=new Cat(1,"黑");
        cats[4]=new Cat(4,"灰");
        //黑灰白黑灰
//        sort2(cats);
        sort3(cats);
        System.out.println(Arrays.toString(cats));
    }

    //白猫放在黑猫前面
    //思路类似于交换排序  分排序区和未排序区  时间复杂度O(n)  空间复杂度O(1)    不稳定
    private static void sort1(Cat[] cats) {
        int n=cats.length;
        Cat tmp;
        int count=0;
        for(int i=0;i<n;i++){
            if(cats[i].getColor().equals("白")){
                tmp=cats[i];
                cats[i]=cats[count];
                cats[count++]=tmp;
            }
        }
    }

    //白猫放前边，灰猫放中间，黑猫放后边
    //思路同上，只是多加了个一个指针， 时间复杂度O(n)  空间复杂度O(1)    不稳定
    private static void sort2(Cat[] cats) {
        int n=cats.length;
        Cat tmp;
        int left=0;
        int right=n-1;
        for(int i=0,j=n-1;i<n&&j>=0;i++,j--){
            if(cats[i].getColor().equals("白")){
                tmp=cats[i];
                cats[i]=cats[left];
                cats[left++]=tmp;
            }
            if(cats[j].getColor().equals("黑")){
                tmp=cats[j];
                cats[j]=cats[right];
                cats[right--]=tmp;
            }
        }
    }
    //白猫放前边，灰猫放中间，黑猫放后边 稳定排序
    //使用桶排序 分3个桶装3种颜色的猫 时间复杂度O(n)  空间复杂度O(n)  稳定 
    private static void sort3(Cat[] cats) {
        int n=cats.length;
        Cat [] white = new Cat[n];
        Cat [] grey = new Cat[n];
        Cat [] black = new Cat[n];
        int w = 0,g = 0,b=0;
        for (Cat cat:cats){
            if(cat.getColor().equals("白")){
                white[w++]=cat;
            }else if(cat.getColor().equals("灰")){
                grey[g++]=cat;
            }else {
                black[b++]=cat;
            }
        }
        int index=0;
        for (Cat cat:white){
            if (cat==null){
                break;
            }
            cats[index++]=cat;
        }
        for (Cat cat:grey){
            if (cat==null){
                break;
            }
            cats[index++]=cat;
        }
        for (Cat cat:black){
            if (cat==null){
                break;
            }
            cats[index++]=cat;
        }

    }

}
