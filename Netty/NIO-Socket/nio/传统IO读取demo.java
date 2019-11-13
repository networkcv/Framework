package nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * create by lwj on 2019/11/13
 */
public class 传统IO读取demo {
    @Test
    public void test() throws IOException {
        FileInputStream fis = new FileInputStream(new File("D:\\study\\Framework\\Netty\\NIO-Socket\\a.txt"));
        FileOutputStream fos = new FileOutputStream(new File("D:\\study\\Framework\\Netty\\NIO-Socket\\b.txt"));
        byte[] bytes = new byte[1024];
        while (fis.read(bytes) != -1) {
            fos.write(bytes);
        }
        fis.close();
        fos.close();
    }
}
