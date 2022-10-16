package com.lwj.classLoader.unit2;

public class TestClinit {

	static {
		i = 1; // 给变量复制可以正常编译通过
		//System.out.print(i); // 这句编译器会提示“非法向前引用”
	}
	static int i = 2;

	public static void main(String[] args) {
		System.out.println(i);
	}
}