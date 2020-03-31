package _15_泛型._07_擦除的神秘之处;

import java.util.List;

/**
 * create by lwj on 2020/3/23
 */
public class Test1 {
    <T> void test1(List<T> list) {
        list.size();
    }

    public static void main(String[] args) {
    }

    class Test2<T> {
        void disposeNumber(T t) {
            t.toString(); //由于t被转换为了Object类型，因此此时只能调用Object中的方法
        }
    }
}
