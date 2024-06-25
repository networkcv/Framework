package com.lwj.bytecode;


/**
 * Date: 2024/6/24
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class MyConstantTest {
    public final boolean bool = true; //  1(0x01)
    public final char c = 'A';        // 65(0x41)
    public final byte b = 127;         // 66(0x42)
    public final short s = 67;        // 67(0x43)
    public final int i = 68;
    public final long l = 69;
    public final double d = 1.1d;// 68(0x44)
    public final float f = 1.1f;// 68(0x44)
    public final String str = "hello";
    public String str2 = "hello";
    public String str3 = "hello2";
}

