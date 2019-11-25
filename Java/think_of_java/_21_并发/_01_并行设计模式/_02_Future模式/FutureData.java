package _21_并发._01_并行设计模式._02_Future模式;

/**
 * create by lwj on 2019/11/12
 */
public class FutureData implements Data {
    protected volatile boolean isReady = false;
    protected ReadlData readlData = null;

    @Override
    public synchronized String getResult() {
        while (!isReady) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return readlData.getResult();
    }

    public synchronized void setReadlData(ReadlData readlData) {
        if (isReady) {
            return;
        }
        this.readlData = readlData;
        isReady = true;
        notifyAll();
    }
}
