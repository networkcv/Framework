package com.lwj.语法;

/**
 * Date: 2022/5/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Goto语法 {
    public static void main(String[] args) {
        boolean flag = true;
        retry:
        for (int i = 0; i < 5; i++) {
                System.out.println(i);
            if (i == 2 && flag) {
                flag = false;
                i=0;
                continue retry;
            }
            if (i==3){
                break retry;
            }
        }
        System.out.println("ok");
    }
}
