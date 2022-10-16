package _99_strategy_pattern._01_strategy;

/**
 * create by Administrator on 2018/7/29
 */
public class Calculator {

    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }



    public int getResult(int a, int b) {
        return strategy.calculate(a, b);
    }
}
