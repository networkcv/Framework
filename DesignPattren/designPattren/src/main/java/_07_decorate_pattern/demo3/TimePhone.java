package _07_decorate_pattern.demo3;

/**
 * create by lwj on 2019/8/3
 */
public class TimePhone extends PhoneDecorate {
    public TimePhone(Phone phone) {
        super(phone);
    }

    void time(){
        System.out.println("显示时间");
    }

    @Override
    public void call() {
        time();
        super.call();
    }
}
