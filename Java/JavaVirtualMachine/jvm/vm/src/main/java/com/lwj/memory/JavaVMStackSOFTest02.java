package com.lwj.memory;

/**
* VM Args： -Xss128k
*/
public class JavaVMStackSOFTest02 {
    private int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    } 
    public static void main(String[] args) throws Throwable {
        JavaVMStackSOFTest02 oom = new JavaVMStackSOFTest02();
        try {
       	 	oom.stackLeak();
        } catch (Throwable e) {
        	System.out.println("stack length:" + oom.stackLength);
        throw e;
        }
    }
}