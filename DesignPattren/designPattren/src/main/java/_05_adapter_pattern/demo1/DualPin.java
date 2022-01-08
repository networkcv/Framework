package _05_adapter_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public interface DualPin {  //两项插孔接口
    public void electrify(int l, int n);// 通电方法，这里没有地线
}
