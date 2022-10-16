package _06_proxy_pattern._2dynamic_JDK_Proxy;

import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * create by lwj on 2019/1/19
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Star realStar =new RealStar();
        StarHandler starHandler=new StarHandler(realStar);

        Star proxy = (Star) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{Star.class}, starHandler);
        proxy.confer();
        proxy.sing();

//        byte[] proxy0s = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{Star.class});
//        FileOutputStream outputStream =new FileOutputStream("F:/$Proxy0.class");
//        outputStream.write(proxy0s);
//        outputStream.close();
    }

}
