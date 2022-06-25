package _99_adapter_pattern.demo1;

/**
 * create by LiuWangJie on 2018/7/25
 */
public interface TriplePin { //墙上的三项插孔接口

    public void electrify(int l, int n, int e); //通电方法 参数分别为火线live，零线null，地线earth
}