package com.lwj.swt._04_layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * create by lwj on 2020/5/7
 * <p>
 * 行列式布局（RowLayout类）可以使组件折行显示，可以设置边界距离和间距。另外，
 * 还可以对每个组件通过 setLayoutData()方法设置 RowData 对象。RowData 用来设置组件的大
 * 小。
 */
public class _12_RowLayout {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("RowLayout 示例");
        RowLayout rowLayout = new RowLayout();
        rowLayout.pack = false; //强制组件大小相同
        rowLayout.wrap = false; //不自动折行
        rowLayout.justify=true; //则组件间的距离随容器的拉伸而变大。
        rowLayout.marginWidth = 20; //组件距容器边缘的宽度为20像素
        rowLayout.marginHeight = 20; //组件距容器边缘的高度为20像素
        rowLayout.spacing = 10; //组件之间的间距为10像素
        shell.setLayout(rowLayout); //设置容器shell的布局方式为rowLayout
        //创建一个特大按钮
        Button button = new Button(shell, SWT.PUSH);
        button.setText("button1");
        RowData rowData = new RowData(90, 90);
        button.setLayoutData(rowData);

        new Button(shell,SWT.PUSH).setText("button2");
        new Button(shell,SWT.PUSH).setText("button3");
        new Button(shell,SWT.PUSH).setText("button4");

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
