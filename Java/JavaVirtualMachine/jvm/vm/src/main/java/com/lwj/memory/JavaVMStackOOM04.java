package com.lwj.memory;

/**
* VM Args： -Xss2M （这时候不妨设大些， 请在32位系统下运行）
*/
public class JavaVMStackOOM04 {
    private void dontStop() {
        while (true) {
        }
    } 
    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    dontStop();
                }
    		});
    	thread.start();
    	}
    } 
    public static void main(String[] args) throws Throwable {
    	JavaVMStackOOM04 oom = new JavaVMStackOOM04();
    	oom.stackLeakByThread();
    }
}