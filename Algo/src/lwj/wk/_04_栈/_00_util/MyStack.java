package lwj.wk._04_栈._00_util;

/**
 * create by lwj on 2019/2/21
 */
public class MyStack {
    private String [] array; //存储数据的数组
    private int size;   //栈中元素个数
    private int n=18;  //初始化栈的大小

    public MyStack(){array=new String[n];this.size=0;}

    public MyStack(int n){
        this.size=0;
        this.n=n;
        array=new String[n];
    }

    public boolean push(String s){
        if(n==size)return  false;
        array[size++]=s;
        return true;
    }

    public String pop(){
        if(size==0)return null;
        return array[size---1];
    }

    public String peek(){
        if(size==0)return null;
        return array[size-1];
    }

    public int size(){
        return size;
    }

}
