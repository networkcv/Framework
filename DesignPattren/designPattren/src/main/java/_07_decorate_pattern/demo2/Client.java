package _07_decorate_pattern.demo2;

/**
 * Date: 2022/1/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Client {
    public static void main(String[] args) {
        Person person = new Person();
        BlackJacket blackJacket = new BlackJacket(person);
        BlueJacket blueJacket = new BlueJacket(blackJacket);
        blueJacket.clothes();
    }
}
