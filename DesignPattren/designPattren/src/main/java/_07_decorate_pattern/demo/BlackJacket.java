package _07_decorate_pattern.demo;

/**
 * Date: 2022/1/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BlackJacket extends FilterJacket {

    public BlackJacket(Clothes person) {
        super(person);
    }

    @Override
    public void clothes() {
        super.clothes();
        System.out.println("穿上黑色夹克");
    }
}
