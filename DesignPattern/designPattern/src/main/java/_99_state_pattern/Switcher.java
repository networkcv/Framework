package _99_state_pattern;

/**
 * create by Administrator on 2018/8/21
 */
public class Switcher {
    //false代表关  true代表开
    private boolean state=false;//初始默认关
    public void switchOn(){
        if(state==false){//当前是关状态
            state=true;
        System.out.println("灯亮了");
        }else {//当前是开状态
            System.out.println("警告，灯已经开了");
        }
    }
    public void switchOff(){
        if(state==true){//当前是开状态
            state=false;
            System.out.println("灯灭了");
        }else {//当前是开状态
            System.out.println("警告，灯已经关了");
        }
    }
}
