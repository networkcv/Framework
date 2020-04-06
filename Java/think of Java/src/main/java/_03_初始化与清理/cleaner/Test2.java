package _03_初始化与清理.cleaner;

import java.util.ArrayList;

/**
 * create by lwj on 2020/4/3
 */
public class Test2 {
    public static void main(String[] args){
        ArrayList<Object> objects = new ArrayList<>(5);
        System.out.println(objects.size());
        objects.set(2,1);
    }
}
