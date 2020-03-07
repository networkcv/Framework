package lwj.nk._13_二进制中1的个数;

/**
 * create by lwj on 2018/10/3
 */
public class Test1 {
    public static void main(String[] args) {
        long l1 = System.nanoTime();
        System.out.println(NumberOf1(-5));
        long l2 = System.nanoTime();
        System.out.println(NumberOf2(-5));
        long l3 = System.nanoTime();
        System.out.println(l2 - l1 + "java方式");
        System.out.println(l3 - l2 + "位运算");
    }

    private static int NumberOf1(int n) {   //用java的方式
        String string = Integer.toBinaryString(n);
        System.out.println(string);// 5 101
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    public static int NumberOf2(int n) {    //与运算
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }
    //分析一下代码：
    //如果一个整数不为0，那么这个整数至少有一位是1。如果我们把这个整数减1，
    // 那么原来处在整数最右边的1就会变为0，原来在1后面的所有的0都会变成1
    // (如果最右边的1后面还有0的话)。其余所有位将不会受到影响。
    //举个例子：一个二进制数1100，从右边数起第三位是处于最右边的一个1。减去1后，第三位变成0，
    // 它后面的两位0变成了1，而前面的1保持不变，因此得到的结果是1011.我们发现减1的结果是把最右边的一个1开始的所有位都取反了。
    // 这个时候如果我们再把原来的整数和减去1之后的结果做与运算，从原来整数最右边一个1那一位开始所有位都会变成0。
    // 如1100&1011=1000.也就是说，把一个整数减去1，再和原整数做与运算
    // 会把该整数最右边一个1变成0.那么一个整数的二进制有多少个1，就可以进行多少次这样的操作。


}
