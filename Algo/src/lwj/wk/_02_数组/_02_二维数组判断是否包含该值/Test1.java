package lwj.wk._02_数组._02_二维数组判断是否包含该值;

import org.junit.Test;

/**
 * create by lwj on 2019/4/10
 */
public class Test1 {
    @Test
    public void test1() {
        int[][] a = {{1, 4, 5, 10}, {2, 6, 8, 11,}, {3, 7, 9, 12}};
        /*
                1   4   5   10
                2   6   8   11
                3   7   9   12
         */
        System.out.println(find(a, 0));
        System.out.println(find(a, 13));
        System.out.println(find(a, 1));
        System.out.println(find(a, 12));
        System.out.println(find(a, 10));
        System.out.println(find(a, 8));
        System.out.println(find(a, 4));
        System.out.println(find(a, 6));
        System.out.println(find(a, 7));
    }

    //使用右上角缩小查找范围
    public String find(int[][] a, int target) {
        int x = 0, y = a[0].length - 1;
        for (; x < a.length; x++) {
            for (; y >= 0; ) {
                if (target == a[x][y])
                    return target + "： a[" + x + "][" + y + "]";
                else if (target < a[x][y])
                    y--;
                else break;
            }
        }
        return target + "： -1";
    }
}
