package com.lwj.algo.prattle.nk._49_把字符串转换成整数;

/**
 * create by lwj on 2019/3/13.
 */
public class Test1 {
    //把字符串转换成整数  不使用javaAPI
    public static void main(String[] args) throws Exception {
        String str1 = "";
        String str2 = null;
        String str3 = "1a33";
        String str4 = "21474836";
        String str5 = "+21474836";
        String str6 = "-21474836";
        String str7 = 2147483647+"";
        String str8 = 2147483647+1+"";
//        System.out.println(str1+" : "+transform(str1));
//        System.out.println(str2+" : "+transform(str2));
//        System.out.println(str3+" : "+transform(str3));
        System.out.println(str4+" : "+transform(str4));
        System.out.println(str5+" : "+transform(str5));
        System.out.println(str6+" : "+transform(str6));
        System.out.println("Integer.MAX_VALUE : "+Integer.MAX_VALUE);
        System.out.println(str7+" : "+transform(str7));
        System.out.println(str8+" : "+transform(str8));
    }

    private static int transform(String str) throws Exception {
        if(str==null||"".equals(str)) throw new Exception("输入不能为空");
        char[] cs = str.toCharArray();
        int res = 0;
        int i ;
        if(cs[0]=='+'||cs[0]=='-')  i=1;
        else i=0;
        for (; i < cs.length; i++) {
            if(cs[i]<48||cs[i]>57){
                throw new Exception("输入异常");
            }
            res += (cs[i] - 48) * pow(10, cs.length - i-1);
        }
        if(cs[0]=='+'||cs[0]=='-'){ res*=cs[0]=='+'?1:-1;}
        return res;
    }

    private static int pow(int num, int n) {
        int res = 1;
        while (n-- > 0) {
            res *= num;
        }
        return res;
    }
}
