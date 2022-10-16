package _05_adapter_pattern.demo2;

/**
 * create by lwj on 2019/1/15
 */
public class Adapter implements Target {
    private NeedAdapter needAdapter;
    public  Adapter(NeedAdapter  needAdapter){
        this.needAdapter=needAdapter;
    }
    @Override
    public void handleReq() {
        needAdapter.request();
    }
}
