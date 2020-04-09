package _14_类型信息._03_静态代理.功能增强;

/**
 * create by lwj on 2020/4/7
 */
public class TargetProxy implements TargetInterface {
    private Target target;

    public TargetProxy(Target target) {
        this.target = target;
    }

    @Override
    public void targetMethod() {
        System.out.println("日志处理");
        target.targetMethod();
    }


}
