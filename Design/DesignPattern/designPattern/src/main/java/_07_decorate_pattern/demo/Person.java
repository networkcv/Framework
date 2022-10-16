package _07_decorate_pattern.demo;

/**
 * Date: 2022/1/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Person implements Clothes {

    @Override
    public void clothes() {
        System.out.println("未穿衣服的人");
    }
}
