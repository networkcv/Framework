package _99_strategy_pattern._02_factory;

/**
 * create by lwj on 2019/9/27
 */
public class OperationFactory {
    public static Operation getOperationInstance(String operate) {
        Operation operation;
        switch (operate)
        {
            case "+" :
                operation=new AddOperation();
                break;
            case "-" :
                operation=new SubOperation();
                break;
            default:
                throw new RuntimeException("错误运算符");
        }
        return operation;
    }
}
