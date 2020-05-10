package com.lwj.swt._01_start;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * create by lwj on 2020/5/6
 * Hello SWT
 */
public class HelloSWT {
    public static void main(String[] args) {
        Display display = new Display();//创建一个display对象。
        Shell shell = new Shell(display);//shell是程序的主窗体
        shell.setLayout(null);         //设置shell的布局方式
        Text hello = new Text(shell, SWT.MULTI);//声明一个可以显示多行信息的文本框
        shell.setText("Java应用程序");  //设置主窗体的标题
        shell.setSize(500, 500);        //设置主窗体的大小
        Color color = new Color(Display.getCurrent(), 255, 255, 255);//声明颜色对象
        shell.setBackground(color);   //设置窗体的背景颜色
        hello.setText("Hello, SWT World!\n\n你好，SWT世界！");//设置文本框信息
        hello.pack();    //自动调整文本框的大小
        shell.pack();  //自动调整主窗体的大小
        shell.open();    //打开主窗体
        while (!shell.isDisposed()) {  //如果主窗体没有关闭则一直循环
            if (!display.readAndDispatch()) {  //如果display不忙
                display.sleep();    //休眠
            }
        }
        display.dispose();      //销毁display
    }
}
