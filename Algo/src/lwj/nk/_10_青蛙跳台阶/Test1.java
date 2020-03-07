package lwj.nk._10_青蛙跳台阶;

/**
 * create by lwj on 2018/10/2
 */
public class Test1 {
    public static void main(String[] args) {
        // 青蛙跳n阶的时候 有两种选择从n-1阶跳一阶 从n-2阶跳两阶  f(n)=f(n-1)+f(n-2)
        //  1 2 3 4 5
        //  1 2 3 5 8
        long l1 = System.nanoTime();
        System.out.println(JumpFloor(5));
        long l2 = System.nanoTime();
        System.out.println(JumpFloor1(5));
        long l3 = System.nanoTime();
        System.out.println("递归法耗时"+(l2-l1)+"ns");
        System.out.println("迭代法耗时"+(l3-l2)+"ns");
    }

    public static int JumpFloor(int target) {   //递归法
        if(target<1){
            return 0;
        }
        if(target==1){
            return 1;
        }
        if(target==2){
            return 2;
        }
        return JumpFloor(target-1)+JumpFloor(target-2);

    }

    public static int JumpFloor1(int target) {  //迭代法
        if(target<1){
            return 0;
        }
        if(target==1){
            return 1;
        }
        if(target==2){
            return 2;
        }
        int a=1; int b=2; int c=0;
        for(int i=3;i<=target;i++){
            c=a+b;
            a=b;
            b=c;
        }
        return c;
    }

}
