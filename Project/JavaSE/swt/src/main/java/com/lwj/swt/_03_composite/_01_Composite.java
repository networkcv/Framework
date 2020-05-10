package com.lwj.swt._03_composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/7
 *
 * 包含其他控件的控件（类似于 Java AWT 中的 Container和 Swing 中的 JPanel）
 *
 * 容器类
 */
public class _01_Composite {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("容器示例");
        Composite composite1 = new Composite(shell, SWT.NONE);
        composite1.setBounds(10,10,100,50);
        Composite composite2 = new Composite(shell, SWT.BORDER);
        composite2.setBounds(120,10,100,50);
        Label lb1 = new Label(composite1, SWT.NONE);
        lb1.setText("面板1");
        lb1.pack();
        Label lb2 = new Label(composite2, SWT.NONE);
        lb2.setText("面板2");
        lb2.pack();

        shell.pack();
        shell.open();
//        System.out.println(composite.getShell() == composite.getParent());
        while (!shell.isDisposed()) {
            while (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
    }
}
