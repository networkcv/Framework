package com.lwj.minijvm;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Hotspot hotspot = new Hotspot("com.lwj.minijvm.Demo", "/Users/networkcavalry/Documents/GitHub/Framework/Java/JavaVirtualMachine/jvm/vm/target/classes");
        hotspot.start();
    }
}
