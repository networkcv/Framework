package com.lwj._01_buffer;

import java.nio.ByteBuffer;

/**
 * Date: 2025/4/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class MappedByteBufferTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.put("Hello".getBytes());
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
    }
}
