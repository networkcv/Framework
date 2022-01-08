package _99_strategy_pattern._01_strategy;

/**
 * create by Administrator on 2018/7/29
 */
public class Subtraction implements Strategy {
    @Override
    public int calculate(int a, int b) {
        return a-b;
    }
}
