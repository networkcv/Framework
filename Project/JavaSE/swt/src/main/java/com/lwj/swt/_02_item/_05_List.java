package com.lwj.swt._02_item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;


/**
 * create by lwj on 2020/5/7
 * 列表框 （使用 ctrl 可以多选）
 */
public class _05_List {
    public static void main(String[] args) {
        Display display = new Display();//创建一个display对象。
        final Shell shell = new Shell(display);//shell是程序的主窗体
        shell.setText("列表框示例");
        final List list = new List(shell, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
//        声明一个可复选、带垂直滚动条、有边框的列表框。
        list.setBounds(10, 10, 200, 100);
        Label lb = new Label(shell, SWT.WRAP);
        lb.setBounds(220, 10, 90, 50);
        Button bt1 = new Button(shell, SWT.NONE);
        bt1.setBounds(20, 160, 100, 25);
        bt1.setText("设值");
        bt1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                list.removeAll();
                for (int i = 1; i <= 8; i++) {
                    list.add("第" + i + "项");   //将选项循环加入到列表框中
                }
                list.select(0);
            }
        });
        Button bt2 = new Button(shell, SWT.NONE);
        bt2.setBounds(200, 60, 100, 25);
        bt2.setText("取值");
        bt2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String[] selected = list.getSelection(); //声明字符串数组保存选项
                String outStr = " ";
                for (int j = 0; j < selected.length; j++) {
                    outStr = outStr + " " + selected[j]; //将数组中的选项加入到输出字符串
                }
                lb.setText("你选择的是：" + outStr);
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
