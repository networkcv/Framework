package com.lwj.swt._04_layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.Test;

/**
 * create by lwj on 2020/5/7
 *
 * 网格式布局（GridLayout 类）是实用而且功能强大的标准布局，也是较为复杂的一种布
 * 局。这种布局把容器分成网格，把组件放置在网格中。GridLayout 有很多可配置的属性，和
 * RowLayout 一样，也有专用的布局数据类 GridData， GridLayout 的强大之处在于它可以通
 * 过 GridData 来设置每一个组件的外观形状。GridLayout 的构造方法无参数，但可以通过
 * GridData 和设置 GridLayout 的属性来设置组件的排列及组件的形状和位置。
 */
public class _03_GridLayout {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("GridLayout");
        shell.setBounds(30, 30, 500, 500);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.horizontalSpacing = 30;//设置列与列之间的间隔，默认值为 5。
        gridLayout.makeColumnsEqualWidth = true;//强制使列都具有相同（按最长的算）的宽度，默认值为 false。
        shell.setLayout(gridLayout);
        new Button(shell, SWT.PUSH).setText("B1");
        new Button(shell, SWT.PUSH).setText("超宽按钮 2");
        new Button(shell, SWT.PUSH).setText("按钮 3");
        new Button(shell, SWT.PUSH).setText("B4");
        new Button(shell, SWT.PUSH).setText("按钮 5");
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    @Test
    public void test() {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("GridData示例");
        shell.setSize(500,500);
        GridLayout gridLayout = new GridLayout();//创建网格布局对象
        gridLayout.numColumns = 3; //设置网格布局列数为3
        gridLayout.makeColumnsEqualWidth = true; //强制列宽相等
        shell.setLayout(gridLayout); //将shell设置为指定的网格布局式样
        GridData gridData = new GridData(GridData.FILL); //创建网格布局数据对象
        gridData.horizontalSpan = 2; //水平方向跨2列
        gridData.verticalSpan = 2; //垂直方向跨2行
        gridData.horizontalAlignment = GridData.CENTER; //水平方向居中
        gridData.verticalAlignment = GridData.FILL; //垂直方向充满
        Button b1 = new Button(shell, SWT.PUSH); //创建按钮对象b1
        b1.setText("B1");
        b1.setLayoutData(gridData); //将设定的网格布局数据用于按钮对象b1
        new Button(shell, SWT.PUSH).setText("超宽按钮 2");
        new Button(shell, SWT.PUSH).setText("按钮 3");
        Button b4 = new Button(shell, SWT.PUSH);
        b4.setText("B4");
        //用带参数的构造方法创建gridData对象
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        b4.setLayoutData(gridData); //将gridData用于b4，水平方向充满
        Button b5 = new Button(shell, SWT.PUSH);
        b5.setText("按钮 5");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;//设置b5为水平方向充满
        b5.setLayoutData(gridData);
        new Button(shell, SWT.PUSH).setText("按钮 6");
        Text t1 = new Text(shell, SWT.BORDER);
        t1.setText("文本框 1");
        gridData = new GridData();
        gridData.verticalSpan = 2; //跨两行
        gridData.horizontalSpan = 2; //跨两列
        gridData.verticalAlignment = GridData.FILL; //垂直方向充满
        gridData.grabExcessVerticalSpace = true; //抢占垂直方向额外空间
        gridData.horizontalAlignment = GridData.FILL;//水平方向充满
        gridData.grabExcessHorizontalSpace = true;//抢占水平方向额外空间
        t1.setLayoutData(gridData); //gridData用于文本框t1
        new Button(shell, SWT.PUSH).setText("按钮 7");
        Button button = new Button(shell, SWT.PUSH);
        gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        button.setLayoutData(gridData);
        button.setText("按钮 8");
//        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
