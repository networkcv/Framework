package _99_other;

import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/3/27
 */
public class 手动分页 {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> integers1 = subPage(integers, 1, 5);
        System.out.println(integers1);
    }

    public static <T> List<T> subPage(List<T> list, Integer pageNum, Integer pageSize) {
        int size = list.size();
        if (size <= pageSize)
            return list;
        int fromIndex = (pageNum - 1) * pageSize;
        fromIndex=Integer.min(fromIndex,(size/pageSize-1)*pageSize);
        int toIndex = +pageSize;
        toIndex = Integer.min(size, toIndex);
        return list.subList(fromIndex, toIndex);
    }
}
