package com.lwj.bytecode;

/**
 * Date: 2024/6/25
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class SwitchEnumTest {
    public void test1(SwitchEnum e) {
        switch (e) {
            case A:
                System.out.println("A");
            case C:
                System.out.println("C");
            case B:
                System.out.println("B");
            default:
                System.out.println("default");
        }
    }
}
