package _07_decorate_pattern.demo2;

/**
 * Date: 2022/1/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BlackJacket extends Person {

    private Clothes person;

    public BlackJacket(Clothes person) {
        this.person = person;
    }

    @Override
    public void clothes() {
        person.clothes();
        System.out.println("穿上黑色夹克");
    }
}
