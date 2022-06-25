package _06_proxy_pattern._3_dynamic_CGlib_Proxy;

/**
 * create by lwj on 2019/8/1
 */
//这是一个没有实现接口的实体类，将通过CGLib代理，用继承的方式来实现动态代理
public class RealStar {

    public void sing() {
        System.out.println("明星唱歌");
    }

    @Override
    public String toString() {
        return "this is RealStar";
    }
}
