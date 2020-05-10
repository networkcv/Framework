package com.lwj.swt._02_item;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/6
 * 标签
 */
public class _02_Label {
    public static void main(String[] args) {
        Display display = new Display();//创建一个display对象。
        Shell shell = new Shell(display);//shell是程序的主窗体
        //shell.setLayout(null);         //设置shell的布局方式
        shell.setText("标签示例");  //设置主窗体的标题
        Label lb1 = new Label(shell, SWT.BORDER | SWT.RIGHT);//深陷型、文字右对齐
        lb1.setBounds(10, 10, 70, 30);
        lb1.setText("标签1");
        lb1.setFont(new Font(display, "黑体", 14, SWT.BOLD));//设置文字的字体字号
        lb1.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        Label lb2 = new Label(shell, SWT.CENTER);//文字居中的标签
        lb2.setBounds(90, 10, 70, 30);
        lb2.setText("标签2");
        lb2.setFont(new Font(display, "宋体", 14, SWT.NORMAL));//设置文字的字体字号
        Label lb3 = new Label(shell, SWT.SEPARATOR | SWT.BORDER);//竖直分栏符
        lb3.setBounds(10, 50, 70, 30);
        Label lb4 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BORDER);
        //水平分栏符
        lb4.setBounds(90, 50, 70, 30);
//        shell.pack();  //自动调整主窗体的大小
        shell.open();    //打开主窗体
        while (!shell.isDisposed()) {  //如果主窗体没有关闭则一直循环
            if (!display.readAndDispatch()) {  //如果display不忙
                display.sleep();    //休眠
            }
        }
        display.dispose();      //销毁display
    }
}
