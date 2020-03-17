package _97_设计模式._02_适配器模式;

/**
 * create by lwj on 2020/3/16
 * 适配器模式其实用的代理思想，有一个已有的类作为参数传入到它没有实现的接口中，
 * 创建一个实现该接口的类，同时，该类中要么继承已有类，要么进行组合（持有已有类的引用）
 */
public class Test1 {
    public static int process(Operator operator, int i1, int i2) {
        return operator.process(i1, i2);
    }

    public static void main(String[] args) {
//        process(new Multiply(),1,2);  //Multiply没有实现Operator接口，所以无法作为参数传入。
        Operator multiplyAdapter1 = new MultiplyAdapter1();
        System.out.println(process(multiplyAdapter1, 2, 3));
        Operator multiplyAdapter2 = new MultiplyAdapter2(new Multiply());
        System.out.println(process(multiplyAdapter2, 2, 3));
    }
}
