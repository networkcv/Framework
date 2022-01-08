package _06_proxy_pattern._1staticProxy;

/**
 * create by lwj on 2019/1/19
 */
public class Client {
    public static void main(String[] args){
        Star realStar =new RealStar();
        Star proxy =new ProxyStar(realStar);
        proxy.confer();
        proxy.signContract();
        proxy.bookTicket();
        proxy.sing();
        proxy.collectMoney();
    }
}
