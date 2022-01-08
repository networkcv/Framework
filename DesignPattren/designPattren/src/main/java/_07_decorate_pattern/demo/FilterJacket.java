package _07_decorate_pattern.demo;

/**
 * Date: 2022/1/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class FilterJacket implements Clothes {
    private Clothes person;

    public FilterJacket(Clothes person) {
        this.person = person;
    }

    @Override
    public void clothes() {
        person.clothes();
    }
}
