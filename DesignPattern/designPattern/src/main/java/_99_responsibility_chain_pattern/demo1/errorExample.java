package _99_responsibility_chain_pattern.demo1;

/**
 * Date: 2022/6/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 *
 * <p>
 * 假设现在有一个闯关游戏，进入下一关的条件是上一关的分数要高于 xx：
 * <p>
 * 游戏一共 3 个关卡
 * <p>
 * 进入第二关需要第一关的游戏得分大于等于 80
 * <p>
 * 进入第三关需要第二关的游戏得分大于等于 90
 */
//第一关
class FirstPassHandler {
    public int handler() {
        System.out.println("第一关-->FirstPassHandler");
        return 80;
    }
}

//第二关
class SecondPassHandler {
    public int handler() {
        System.out.println("第二关-->SecondPassHandler");
        return 90;
    }
}


//第三关
class ThirdPassHandler {
    public int handler() {
        System.out.println("第三关-->ThirdPassHandler，这是最后一关啦");
        return 95;
    }
}

public class errorExample {
    public static void main(String[] args) {

        FirstPassHandler firstPassHandler = new FirstPassHandler();//第一关
        SecondPassHandler secondPassHandler = new SecondPassHandler();//第二关
        ThirdPassHandler thirdPassHandler = new ThirdPassHandler();//第三关

        int firstScore = firstPassHandler.handler();
        //第一关的分数大于等于80则进入第二关
        if (firstScore >= 80) {
            int secondScore = secondPassHandler.handler();
            //第二关的分数大于等于90则进入第二关
            if (secondScore >= 90) {
                thirdPassHandler.handler();
            }
        }
        /*
         *   那么如果这个游戏有 100 关，我们的代码很可能就会写成这个样子：
         * if(第1关通过){
         *     // 第2关 游戏
         *     if(第2关通过){
         *         // 第3关 游戏
         *         if(第3关通过){
         *            // 第4关 游戏
         *             if(第4关通过){
         *                 // 第5关 游戏
         *                 if(第5关通过){
         *                     // 第6关 游戏
         *                     if(第6关通过){
         *                         //...
         *                     }
         *                 }
         *             }
         *         }
         *     }
         * }
         */
    }
}

