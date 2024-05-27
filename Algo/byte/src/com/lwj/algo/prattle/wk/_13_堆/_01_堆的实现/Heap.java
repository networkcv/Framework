package com.lwj.algo.prattle.wk._13_堆._01_堆的实现;

/**
 * create by lwj on 2019/3/29
 */
public class Heap {
    private int [] a;   //数组 下标从1开始存放数据
    private int n;  //堆可以存放的最大元素个数
    private int count;  //堆中已经存储的元素个数

    public Heap(int capacity){
        a=new int[capacity+1];
        n=capacity;
        count=0;
    }

    //自下而上的堆化
    public void insert(int data){
        if(count>=n)return; //堆满了
        a[++count]=data;
        int i=count;
        while(i/2>0&&a[i]>a[i/2]){
            int tmp=a[i/2];
            a[i/2]=a[i];
            a[i]=tmp;
            i=i/2;
        }
    }

    public void removeMax(){
        if(count==0)return;
        a[1]=a[count--];
        heapify(a,count,1);
    }

    //自上而下的堆化
    private void heapify(int[] a, int n, int i) {
        while(true){
            int maxPos=i;
            if(i*2<=n&&a[i]<a[i*2]) maxPos=i*2; //如果比左子树大的话，接下来比较左子树和右子树哪个更大一些，
            if(i*2+1<=n&&a[maxPos]<a[i*2+1]) maxPos=i*2+1;  //和更大的交换位置
            if(maxPos==i) break;
            swap(a,i,maxPos);
            i=maxPos;
        }
    }

    //打印堆
    public void print(){
        int num=1;
        while(num<=count){
            System.out.print(a[num++]+" ");
        }
    }

    private void swap(int[] a, int i, int maxPos) {
        int tmp=a[i];
        a[i]=a[maxPos];
        a[maxPos]=tmp;
    }

    public static void main(String[] args){
        Heap heap = new Heap(10);
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        heap.removeMax();
        heap.print();
    }
}
