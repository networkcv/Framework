package com.lwj.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * create by lwj on 2020/2/29
 */
public class ChannelDemo {
    /**
     * 输出一个文件
     */
    @Test
    public void test() throws IOException {
        //先定义输出流
        FileOutputStream fileOutputStream = new FileOutputStream("a.txt");
        //通过该流获取对应的Channel
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入Buffer，并进行flip
        byteBuffer.put("hello".getBytes()).flip();
        //将缓冲区的内容写到Channel
        int write = channel.write(byteBuffer);
        //关闭输出流
        fileOutputStream.close();
    }

    /**
     * 读取文件
     */
    @Test
    public void test1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }

    /**
     * 复制文件
     */
    @Test
    public void test2() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
//            byteBuffer.clear(); // 1
            int write = inputStreamChannel.read(byteBuffer);
            if (write == -1) {
                break;
            } else {
                byteBuffer.flip();
                outputStreamChannel.write(byteBuffer);
                byteBuffer.flip();  //2
//				操作 1 和 2 必须保留一个，因为write操作后，position与limit重合，需要对position进行复位才能进行后续操作
            }
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     * 从resources中复制图片到项目根目录下
     */
    @Test
    public void test4() throws IOException {
        //resource在发布时会打包到classpath下
        URL resource = getClass().getClassLoader().getResource("girl.png");
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("mm.jpg");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
//        inputStreamChannel.transferTo(0,inputStreamChannel.size(),outputStreamChannel);
        outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());
        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

}
