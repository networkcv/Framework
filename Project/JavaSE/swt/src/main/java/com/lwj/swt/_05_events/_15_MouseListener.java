package com.lwj.swt._05_events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/7
 *
 * 每一种类型的监听器，都有一个接口来定义这种监听器，由类提供事件信息，由应
 * 用程序接口方法负责添加监听器。如果一个监听器接口中定义了多个方法，则会提供一
 * 个适配器来实现监听器接口并同时提供空方法。所有的事件、监听器和适配器都放在包
 * org.eclipse.swt.events 中。
 *
 * 例 如 ， 添 加 组 件 选 择 事 件 的 监 听 器 为 addSelectionListener ， 事 件 为
 * SelectionEvent，相应的适配器为 SelectionAdapter。添加鼠标事件的监听器为
 * addMouseListener，事件为 MouseEvent，相应的适配器为 MouseAdapter。
 * SWT中常用的事件如下：
 * 1．addMouseListener 鼠标监听器。常用方法：
 * mouseDown() 鼠标按下时触发。
 * mouseUP() 鼠标放开时触发。
 * mouseDoubleClick() 鼠标双击时触发。
 * 2．addKeyListener 按键监听器。常用方法：
 * keyPressed() 当焦点在组件上时，按下键盘任一键时触发。但对某些组件（如按钮 Button），按回车键时不能触发。
 * keyReleased() 按键弹起时触发。
 * 3．addSelectionListener 组件选择监听器。常用方法：
 * widgetSelected() 当组件被选择（单击鼠标、焦点在组件上时按回车键）时触发。
 * 4．addFocusListener 焦点监听器。常用方法：
 * focusGained() 得到焦点时触发。
 * focusLost() 失去焦点时触发。
 */
public class _15_MouseListener {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("鼠标监听器");
        RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        shell.setLayout(rowLayout);
        Label label = new Label(shell, SWT.NONE);
        Button button1 = new Button(shell, SWT.NONE);
        button1.setText("按钮1");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                label.setText("mouseDown");
                shell.layout();
            }

            @Override
            public void mouseUp(MouseEvent e) {
                label.setText("mouseUp");
                shell.layout();
            }
        });
        Button button2 = new Button(shell, SWT.NONE);
        button2.setText("按钮2");
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                label.setText("mouseDoubleClick");
                shell.layout();
                MessageBox messageBox=new MessageBox(shell,SWT.OK|SWT.CANCEL|SWT.ICON_INFORMATION);
                messageBox.setText("Double click");
                messageBox.setMessage("文本框中鼠标双击事件发生！");
                messageBox.open();
            }
        });
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
