package com.lwj.swt._04_layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * create by lwj on 2020/5/7
 * <p>
 * 在 Java 中， GUI 程序开发的目标之一是跨平台，而每种类型操作系统对屏幕的定义
 * 不一样，所以 Swing 中引入了布局的概念，对子组件的位置和大小等信息进行定义。 SWT
 * 中也采用了布局方式，用户可使用布局来控制组件中元素的位置和大小等信息。
 * <p>
 * 绝对定位：setBounds(int x,int y,int width,int height)
 * 托管定位：每个控件的坐标x，y，宽度，高度都是通过LayoutManager设置的
 * <p>
 * 常用的布局管理器：
 * 1.FillLayout 充满式布局，在容器中以相同的大小以单行或单列排列组件
 * 2.RowLayout 行列式布局，以单行或多行的方式定制组件的排列方式
 * 3.GridLayout 网格式布局，以网格的方式进行布局，组件可以占用指定的一个或几个网格
 * 4.FormLayout 表格式布局，通过定义组件四个边的距离来排列组件，被引用的相对的组件可以是父组件，也可以是同一容器中的其它组件
 */
public class _11_FillLayout {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
//        SWT.HORIZONTAL 按一行充满容器,默认。
//        SWT.VERTICAL 按一列充满容器
        FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
//        FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
        shell.setLayout(fillLayout);
        new Button(shell,SWT.PUSH).setText("button1");
        new Button(shell,SWT.PUSH).setText("button2");
        new Button(shell,SWT.PUSH).setText("button3");
        new Button(shell,SWT.PUSH).setText("button4");
        shell.open();
        while(!shell.isDisposed()){
            while (!display.readAndDispatch()){
                display.sleep();
            }
        }
        shell.dispose();

    }

}
