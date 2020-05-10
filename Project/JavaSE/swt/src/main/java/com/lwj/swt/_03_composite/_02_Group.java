package com.lwj.swt._03_composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/7
 */
public class _02_Group {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("分组框示例");
        Group group = new Group(shell, SWT.NONE);
        group.setText("录入信息");
        group.setBounds(10, 20, 200, 100);
        {
            Label label1 = new Label(group, SWT.NONE);
            label1.setText("姓名");
            label1.setBounds(20, 20, 70, 20);
            Text text1 = new Text(group, SWT.BORDER);
            text1.setBounds(100, 20, 70, 20);
        }
        {
            Label label2 = new Label(group, SWT.NONE);
            label2.setText("地址");
            label2.setBounds(20, 60, 70, 20);
            Text text2 = new Text(group, SWT.BORDER);
            text2.setBounds(100, 60, 70, 20);
        }
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            while (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
    }
}
