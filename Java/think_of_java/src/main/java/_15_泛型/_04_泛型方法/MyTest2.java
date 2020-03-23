package _15_泛型._04_泛型方法;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2020/3/23
 * 泛型方法
 */
public class MyTest2{
    <T> T get(T i){
        return i;
    }
    <T> T get2(){
        return (T)"C";
    }

    void get3(List<Object> a){

    }
//    void get3(List<Integer> a){}

    public static void main(String[] args){
        MyTest2 myTest2 = new MyTest2();
        String b = myTest2.<String>get("B");
        String c = myTest2.<String>get2();
        List<String> strings = new ArrayList<>();
        List<Object> strings2 = new ArrayList<>();
        List strings3 = new ArrayList<>();
//        myTest2.get3(strings);    //error
        myTest2.get3(strings2);
        myTest2.get3(strings3);
    }
}
