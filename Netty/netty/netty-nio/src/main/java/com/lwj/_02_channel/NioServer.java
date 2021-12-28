package com.lwj._02_channel;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/12/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        //1.开启通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2.绑定端口
        ssc.bind(new InetSocketAddress(9999));
        //3.设置非阻塞
        ssc.configureBlocking(false);
        //4.获取连接
        while (true) {
            SocketChannel accept;
            //5.处理连接
            if ((accept = ssc.accept()) == null) {
                System.out.println("等待连接");
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println("成功获取连接");
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                //返回值
                // 数字 代表读到有效的个数
                // 0 代表没有读到有效数字
                //-1 代表读到末尾了
                int read = accept.read(byteBuffer);
                System.out.println(new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
                accept.write(ByteBuffer.wrap("服务端收到".getBytes()));
                //6.释放连接
                accept.close();
            }
        }
    }
}