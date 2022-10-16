package _07_decorate_pattern.demo3;

/**
 * create by lwj on 2019/8/3
 */
public class Client {
    public static void main(String[] args) {
        Phone IPhone = new IPhone();
        Phone musicPhone = new TimePhone(new MusicPhonex(IPhone));
        musicPhone.call();
    }
}
