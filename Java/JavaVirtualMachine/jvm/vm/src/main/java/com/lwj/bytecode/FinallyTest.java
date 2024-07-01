package com.lwj.bytecode;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class FinallyTest {
    public static int func1() {
        try {
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            return 2;
        }
    }

    public static int func2() {
        try {
            int a = 1 / 0;
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            return 2;
        }
    }

    public static int func3() {
        try {
            int a = 1 / 0;
            return 0;
        } catch (Exception e) {
            int b = 1 / 0;
        } finally {
            return 2;
        }
    }

    public static int func4() {
        try {
            throw new RuntimeException("1");
        } catch (Exception e) {
            RuntimeException runtimeException = new RuntimeException("2");
            runtimeException.addSuppressed(e);
            throw runtimeException;
        } finally {
//            throw new RuntimeException("3");
        }
    }

    public static void main(String[] args) {
        System.out.println(func1());
        System.out.println(func2());
        System.out.println(func3());
        System.out.println(func4());
    }

}
