package _99_responsibility_chain_pattern.demo2;

/**
 * Date: 2022/6/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */

class FirstPassHandler {
    /**
     * 第一关的下一关是 第二关
     */
    private SecondPassHandler secondPassHandler;

    public void setSecondPassHandler(SecondPassHandler secondPassHandler) {
        this.secondPassHandler = secondPassHandler;
    }

    //本关卡游戏得分
    private int play() {
        return 80;
    }

    public int handler() {
        System.out.println("第一关-->FirstPassHandler");
        if (play() >= 80) {
            //分数>=80 并且存在下一关才进入下一关
            if (this.secondPassHandler != null) {
                return this.secondPassHandler.handler();
            }
        }

        return 80;
    }
}

class SecondPassHandler {

    /**
     * 第二关的下一关是 第三关
     */
    private ThirdPassHandler thirdPassHandler;

    public void setThirdPassHandler(ThirdPassHandler thirdPassHandler) {
        this.thirdPassHandler = thirdPassHandler;
    }

    //本关卡游戏得分
    private int play() {
        return 90;
    }

    public int handler() {
        System.out.println("第二关-->SecondPassHandler");

        if (play() >= 90) {
            //分数>=90 并且存在下一关才进入下一关
            if (this.thirdPassHandler != null) {
                return this.thirdPassHandler.handler();
            }
        }

        return 90;
    }
}

class ThirdPassHandler {

    //本关卡游戏得分
    private int play() {
        return 95;
    }

    /**
     * 这是最后一关，因此没有下一关
     */
    public int handler() {
        System.out.println("第三关-->ThirdPassHandler，这是最后一关啦");
        return play();
    }
}

public class ResponsibilityChainImprove {
    public static void main(String[] args) {

        FirstPassHandler firstPassHandler = new FirstPassHandler();//第一关
        SecondPassHandler secondPassHandler = new SecondPassHandler();//第二关
        ThirdPassHandler thirdPassHandler = new ThirdPassHandler();//第三关

        firstPassHandler.setSecondPassHandler(secondPassHandler);//第一关的下一关是第二关
        secondPassHandler.setThirdPassHandler(thirdPassHandler);//第二关的下一关是第三关

        //说明：因为第三关是最后一关，因此没有下一关
        //开始调用第一关 每一个关卡是否进入下一关卡 在每个关卡中判断
        firstPassHandler.handler();

        /*
        这种写法的缺点：
            代码的扩展性非常不好
            耦合比较严重，每个关卡都强依赖下一个具体的关卡，应该依赖抽象而非具体
         */
    }
}

