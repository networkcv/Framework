package com.lwj.bytecode;

/**
 * Date: 2024/6/26
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class FinalMethodTest {

    private final void final_private_fuc() {
        System.out.println(1);
    }

    private void non_final_private_fuc() {
        System.out.println(1);
    }

    public void public_fun() {
        System.out.println(1);
    }


    public static void main(String[] args) {
        new FinalMethodTest().final_private_fuc();
        new FinalMethodTest().non_final_private_fuc();
        new FinalMethodTest().public_fun();
    }
}
