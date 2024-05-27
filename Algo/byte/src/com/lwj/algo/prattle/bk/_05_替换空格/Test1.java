package com.lwj.algo.prattle.bk._05_替换空格;

import org.junit.Test;

/**
 * create by lwj on 2019/2/5
 */
public class Test1 {
    //将hello my world 中的空格替换为%20
    @Test
    public void test1(){
        String a="hello my world";
        char[] c = a.toCharArray();
        int count=0;
        for(int i=0;i<c.length;i++){
            if(c[i]==' '){
                count++;
            }
        }
        char [] cs = new char[c.length+count*2];
        int csCount = cs.length-1;
        for(int i=c.length-1;i>=0;i--){
            if(c[i]==' '){
                cs[csCount--]='0';
                cs[csCount--]='2';
                cs[csCount--]='%';
            }else{
                cs[csCount--]=c[i];
            }
        }
        for (char ch :cs ) {
            System.out.print(ch+"");
        }
    }
}
