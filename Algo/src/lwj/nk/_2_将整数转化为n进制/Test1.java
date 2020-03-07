package lwj.nk._2_将整数转化为n进制;

/**
 * create by lwj on 2018/10/3
 */
public class Test1 {
    public static void main(String[] args){
        System.out.println(toN1(15,16));
    }
    public static String  toN1(int number,int n){//javaAPI
        return Integer.toString(number, n);

    }

}
