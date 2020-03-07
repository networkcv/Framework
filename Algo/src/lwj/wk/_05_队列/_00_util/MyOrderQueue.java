package lwj.wk._05_队列._00_util;

//顺序队列
public class MyOrderQueue {
    private int n = 5;
    private String[] arr = new String[n];
    private int head = 0;
    private int tail = 0;

    public boolean enqueue(String s) {
        //head==0,tail==n 说明数组被占满了
        if(tail==n){
            if(head==0)
                return false;
            //数据搬移
            for(int i=head;i<tail;i++){
                arr[i-head]=arr[i];
            }
            //重置head好tail
            tail-=head;
            head=0;
        }
        arr[tail++] = s;
        return true;
    }

    public String dequque(){
        if(head==tail) return null;
        return arr[head++];
    }
}
