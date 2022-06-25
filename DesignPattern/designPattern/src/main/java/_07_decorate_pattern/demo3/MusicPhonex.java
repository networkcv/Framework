package _07_decorate_pattern.demo3;

/**
 * create by lwj on 2019/8/3
 */
public class MusicPhonex extends PhoneDecorate {
    public MusicPhonex(Phone phone) {
        super(phone);
    }
    public void music(){
        System.out.println("播放音乐");
    }


    @Override
    public void call() {
        music();
        super.call();
    }
}
