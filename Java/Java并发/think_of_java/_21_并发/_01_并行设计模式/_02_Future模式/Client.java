package think_of_java._21_并发._01_并行设计模式._02_Future模式;

/**
 * create by lwj on 2019/11/12
 */
public class Client {
    public Data request(final String queryStr) {
        final FutureData futureData = new FutureData();
        new Thread(() -> {
            ReadlData readlData = new ReadlData(queryStr);
            futureData.setReadlData(readlData);
        }).start();
        return futureData;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        FutureData data = (FutureData) client.request("test");
        while (true) {
            if (data.isReady) {
                System.out.println(data.getResult());
                return;
            }
        }
    }

}
