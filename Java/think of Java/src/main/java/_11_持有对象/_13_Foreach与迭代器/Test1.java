package _11_持有对象._13_Foreach与迭代器;

import java.util.Iterator;

/**
 * create by lwj on 2020/3/18
 */
public class Test1 implements Iterable<String> {

    private String[] arr = "abcdef".split("");

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int index=0;
            @Override
            public boolean hasNext() {
                return index<arr.length;
            }

            @Override
            public String next() {
                return arr[index++];
            }
        };
    }

    public static void main(String[] args){
        for (String  s:new Test1()){
            System.out.println(s);
        }
    }
}
