package com.lwj.swt._02_item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/7
 * 菜单
 */
public class _07_Menu {
    public static void main(String[] args) {
        Display display = new Display();//创建一个display对象。
        final Shell shell = new Shell(display);//shell是程序的主窗体
        shell.setText("菜单示例");
        Menu mainMenu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(mainMenu);
//        Menu mainMenu=new Menu(shell,SWT.POP_UP); //创建右键弹出式菜单
//        shell.setMenu(mainMenu);   //创建弹出式菜单
        {
            //"文件"项
            MenuItem fileItem = new MenuItem(mainMenu, SWT.CASCADE);
            fileItem.setText("文件&(F)");
            //"文件"菜单
            Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
            fileItem.setMenu(fileMenu);
            {
                //"新建"项
                MenuItem newFileItem = new MenuItem(fileMenu, SWT.CASCADE);
                newFileItem.setText("新建&(N)");
                //"新建"菜单
                Menu newFileMenu = new Menu(shell, SWT.DROP_DOWN);
                newFileItem.setMenu(newFileMenu);
                {
                    //"新建项目"项
                    MenuItem newProjectItem = new MenuItem(newFileMenu, SWT.PUSH);
                    newProjectItem.setText("项目\tCtrl+Shift+N");
                    //设置快捷键
                    newProjectItem.setAccelerator(SWT.CTRL + SWT.SHIFT + 'N');
                    // 添加事件监听
                    newProjectItem.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                            Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP);
                            text.setBounds(10, 10, 100, 30);
                            text.setText("你选择了“新建项目”");
                        }
                    });
                    new MenuItem(newFileMenu, SWT.SEPARATOR);
                    new MenuItem(newFileMenu, SWT.PUSH).setText("包");
                    new MenuItem(newFileMenu, SWT.PUSH).setText("类");
                }
                MenuItem openFileItem = new MenuItem(fileMenu, SWT.CASCADE);
                openFileItem.setText("打开&O");
                MenuItem exitItem = new MenuItem(fileMenu, SWT.CASCADE);
                exitItem.setText("退出&E");
            }
            MenuItem helpItem = new MenuItem(mainMenu, SWT.CASCADE);
            helpItem.setText("帮助&H");
        }
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
