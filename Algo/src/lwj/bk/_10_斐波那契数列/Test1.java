package lwj.bk._10_斐波那契数列;

/**
 * create by lwj on 2019/2/10
 */
public class Test1 {
    public static void main(String[] args){
      System.out.println(febo2(2));
    }
    //教科书写法(最简洁 但不是最实用) 比如计算f(9)的时候会去计算f(8)和f(7),计算f(8)时会计算f(7)和f(6)，其中f(7)计算了两次，浪费资源
    public static  int febo1(int num) {
        if (num <= 0) {
            return 0;
        }
        if (num == 1) {
            return 1;
        }
        return febo1(num-1)+febo1(num-2);
    }

    //实用解法
    public static int febo2(int n){
        int [] arr ={0,1};
        if(n<2){
            return arr[n];
        }
        int num1=0;
        int num2=1;
        int num3=0;
        for(int i=2;i<=n;i++){
            num3=num1+num2;
            num1=num2;
            num2=num3;
        }
        return num3;
    }
}
