package com.lwj.swt._02_item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/6
 * 下拉框
 */
public class _04_Combo {
    public static void main(String[] args) {
        Display display = new Display();//创建一个display对象。
        final Shell shell = new Shell(display);//shell是程序的主窗体
        shell.setText("下拉框示例");
        final Combo combo = new Combo(shell, SWT.NONE);
        combo.setBounds(10, 10, 100, 25);
        combo.setItems("1","2","3");
        combo.select(1);
        Label lb = new Label(shell, SWT.WRAP);   //创建标签，可自动换行
        lb.setBounds(120, 10, 100, 35);
        Button bt1 = new Button(shell, SWT.NONE);
        bt1.setBounds(20, 60, 100, 25);
        bt1.setText("设值");
        bt1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) { //按钮的单击事件
                combo.removeAll();       //清空combo
                for (int i = 1; i <= 3; i++) {
                    combo.add("第" + i + "项");  //循环添加选项
                }
                combo.select(0);    //设置默认选项
            }
        });
        Button bt2 = new Button(shell, SWT.NONE);
        bt2.setBounds(130, 60, 100, 25);
        bt2.setText("取值");
        bt2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) { //按钮的单击事件
                lb.setText("你选择的是：" + combo.getText());
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {  //如果主窗体没有关闭则一直循环
            if (!display.readAndDispatch()) {  //如果display不忙
                display.sleep();    //休眠
            }
        }
        display.dispose();      //销毁display
    }
}
