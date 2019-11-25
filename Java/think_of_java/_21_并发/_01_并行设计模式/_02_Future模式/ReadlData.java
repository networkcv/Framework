package _21_并发._01_并行设计模式._02_Future模式;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/12
 */
public class ReadlData implements Data {
    protected final String result;

    public ReadlData(String str){
        try {
            //假设获取真实数据很慢，模拟用户等待
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result=str;
    }
    @Override
    public String getResult() {
        return result;
    }

}
