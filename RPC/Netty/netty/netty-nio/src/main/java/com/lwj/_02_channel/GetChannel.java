package com.lwj._02_channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Date: 2025/4/28
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class GetChannel {
    private static final int SIZE = 1024;

    public static void main(String[] args) throws Exception {
        // 获取通道，该通道允许写操作
        FileChannel fc = new FileOutputStream("data.txt").getChannel();
        // 将字节数组包装到缓冲区中
        fc.write(ByteBuffer.wrap("SomeText ".getBytes()));
        // 关闭通道
        fc.close();

        // 随机读写文件流创建的管道
        fc = new RandomAccessFile("data.txt", "rw").getChannel();
        // fc.position()计算从文件的开始到当前位置之间的字节数
        System.out.println("此通道的文件位置：" + fc.position());
        // 设置此通道的文件位置,fc.size()此通道的文件的当前大小,该条语句执行后，通道位置处于文件的末尾
        fc.position(fc.size());
        System.out.println("此通道的文件位置：" + fc.position());
        // 在文件末尾写入字节
        fc.write(ByteBuffer.wrap("MoreText  ".getBytes()));
        System.out.println("此通道的文件位置：" + fc.position());
        fc.close();

        // 用通道读取文件
        fc = new FileInputStream("data.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);
        // 将文件内容读到指定的缓冲区中
        fc.read(buffer);
        System.out.println("写到缓冲区后");
        System.out.println("buffer读 capacity:" + buffer.capacity());
        System.out.println("buffer读 limit:" + buffer.limit());
        System.out.println("buffer读 position:" + buffer.position());
        System.out.println("buffer读 remaining:" + buffer.remaining());
        buffer.flip();//此行语句一定要有
        System.out.println("转换读模式");
        System.out.println("buffer写 capacity:" + buffer.capacity());
        System.out.println("buffer写 limit:" + buffer.limit());
        System.out.println("buffer写 position:" + buffer.position());
        System.out.println("buffer写 remaining:" + buffer.remaining());
        while (buffer.hasRemaining()) {
            byte[] dst = new byte[buffer.limit()];
            buffer.get(dst);
            System.out.println(new String(dst));
        }
        System.out.println("读模式读取后");
        System.out.println("buffer写 capacity:" + buffer.capacity());
        System.out.println("buffer写 limit:" + buffer.limit());
        System.out.println("buffer写 position:" + buffer.position());
        System.out.println("buffer写 remaining:" + buffer.remaining());
        buffer.flip();
        System.out.println("重新切换读写后");
        System.out.println("buffer写 capacity:" + buffer.capacity());
        System.out.println("buffer写 limit:" + buffer.limit());
        System.out.println("buffer写 position:" + buffer.position());
        System.out.println("buffer写 remaining:" + buffer.remaining());
        fc.close();
    }
}
