package com.lwj.bytecode;

/**
 * Date: 2024/6/25
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class InitBlockTest {
    public InitBlockTest() {
        int d = 6;
    }

    {
        String a = "ABCD";
        System.out.println("AAA");
    }

    public String a = "AA";
    public int b = 4;

    {
        int c = 5;
        System.out.println("BBB");
    }


    public InitBlockTest(int tmpA) {
        this.b = tmpA;
    }

    public static void main(String[] args) {
        new InitBlockTest();
        new InitBlockTest();
    }

}
