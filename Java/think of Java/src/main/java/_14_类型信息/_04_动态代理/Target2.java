package _14_类型信息._04_动态代理;

/**
 * create by lwj on 2020/4/7
 */
public class Target2 implements TargetInterface2 {
    @Override
    public void targetMethod() {
        System.out.println("targetMethod2 is called");
    }
}
