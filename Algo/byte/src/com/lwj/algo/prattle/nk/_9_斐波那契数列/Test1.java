package com.lwj.algo.prattle.nk._9_斐波那契数列;

/**
 * create by lwj on 2018/10/2
 */
//斐波那契数列指的是这样一个数列 1, 1, 2, 3, 5, 8, 13, 21, 34...这个数列从第3项开始，每一项都等于前两项之和。
public class Test1 {
    public static void main(String[] args){
        System.out.println(fibonacci(8));
        System.out.println(fibonacci1(8));
    }

    private static int fibonacci(int n) {  //使用递归
        if(n<1){
            return -1;
        }
        if(n==1||n==2){
            return 1;
        }
        return  fibonacci(n-1)+fibonacci(n-2);
    }
    private static int fibonacci1(int n) {  //使用循环
        int a=1,b=1,c=0;
        if(n<1){
            return -1;
        }
        if(n==1||n==2){
            return 1;
        }
        for(int i =3 ;i<=n;i++){
            c=a+b;          //1  c(2)=快速排序(1)+b(1)    2   c(3)=快速排序(1)+b(2)   3   c(5)=快速排序(2)+b(3)
            a=b;            //   快速排序(1)=b(1)             快速排序(2)=b(2)            快速排序(3)=b(3)
            b=c;            //   b(2)=c(2)             b(3)=c(3)            b(5)=c(5)
        }
            return c;
    }
}
