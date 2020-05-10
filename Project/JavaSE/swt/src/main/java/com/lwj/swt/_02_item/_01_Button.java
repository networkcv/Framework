package com.lwj.swt._02_item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * create by lwj on 2020/5/6
 * 其他控件包含的窗口小部件（该控件可能不是复合控件），比如列表和表。
 * 注意，包含一些项的控件很少包含其他控件，反之亦然。Item 是一个抽象类。
 *
 * 按钮
 */
public class _01_Button {
    public static void main(String[] args){
        Display display=new Display();//创建一个display对象。
        Shell shell=new Shell(display);//shell是程序的主窗体
        //shell.setLayout(null);         //设置shell的布局方式
        shell.setText("按钮示例");  //设置主窗体的标题
        shell.setSize(500,500);
        Button bt1=new Button(shell,SWT.NULL);  //创建默认按钮
        bt1.setText("SWT.NULL");          //设置按钮上的文字
        bt1.setBounds(10,10,90,30);       //设置按钮显示位置及宽度、高度
        Button bt2 = new Button(shell, SWT.PUSH | SWT.BORDER);//创建深陷型按钮
        bt2.setText("SWT.PUSH");
        bt2.setBounds(120,10,90,30);
        Button check1=new Button(shell,SWT.CHECK);//创建复选按钮
        check1.setText("SWT.CHECK");
        check1.setBounds(10,50,90,30);
        Button check2=new Button(shell,SWT.CHECK|SWT.BORDER);//创建深陷型复选按钮
        check2.setText("SWT.CHECK");
        check2.setBounds(120,50,90,30);
        Button radio1=new Button(shell,SWT.RADIO);//创建单选按钮
        radio1.setText("SWT.RADIO");
        radio1.setBounds(10,90,90,30);
        Button radio2=new Button(shell,SWT.RADIO|SWT.BORDER);//创建深陷型单选按钮
        radio2.setText("SWT.RADIO");
        radio2.setBounds(120,90,90,30);
        Button arrowLeft=new Button(shell,SWT.ARROW|SWT.LEFT);//创建箭头按钮（向左）
        arrowLeft.setBounds(10,130,90,20);
        Button arrowRight=new Button(shell,SWT.ARROW|SWT.RIGHT|SWT.BORDER);
        arrowRight.setBounds(120,130,90,20);
        // 同时只能有一个radio被选中
        Button radio3 = new Button(shell, SWT.RADIO);
        radio3.setText("radio3");
        radio3.setBounds(10,170,90,20);
        Button radio4 = new Button(shell, SWT.RADIO);
        radio4.setText("radio4");
        radio4.setBounds(120,170,90,20);

//        shell.pack();  //自动调整主窗体的大小
        shell.open();    //打开主窗体
        while(!shell.isDisposed()){  //如果主窗体没有关闭
            if(!display.readAndDispatch()){  //如果display不忙
                display.sleep();    //休眠
            }
        }
        display.dispose();      //销毁display
    }
}
