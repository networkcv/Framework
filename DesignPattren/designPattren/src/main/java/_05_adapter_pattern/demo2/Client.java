package _05_adapter_pattern.demo2;

/**
 * create by lwj on 2019/1/15
 */

/***
 *  客户端类
 *  相当于
 */
public class Client {
    public void test1(Target t) {
        t.handleReq();
    }

    public static void main(String[] args) {
        Client client = new Client();
        NeedAdapter needAdapter = new NeedAdapter();
        Adapter adapter = new Adapter(needAdapter);
        client.test1(adapter);
    }
}
