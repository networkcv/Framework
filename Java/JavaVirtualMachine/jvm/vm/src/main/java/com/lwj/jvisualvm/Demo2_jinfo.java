package com.lwj.jvisualvm;

/**
 jinfo [option]  <pid>
    options参数解释：
 - no options 输出所有的系统属性和参数
 - -flag <name> 打印指定名称的参数
 - -flag [+|-]<name> 打开或关闭参数
 - -flag <name>=<value> 设置参数
 - -flags 打印所有参数
 - -sysprops 打印系统配置
 *
 */
public class Demo2_jinfo {

    public static void main(String[] args) {
        System.out.println("jinfo 指令");
        try {
            Thread.sleep(2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
