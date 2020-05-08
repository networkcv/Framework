package com.lwj.swt._99_notepad;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.io.*;

/**
 * create by lwj on 2020/5/7
 */
@Slf4j
public class Notepad {
    private static String INIT_NAME = "无标题 - 记事本";
    private static String SUFFIX = "- 记事本";

    private static class CurrentFile {
        private Shell shell;
        private Text text;
        private String fileName="无标题";
        private String filePath;
        private boolean isSave = true;

        public void updateShellText() {
            StringBuilder res = new StringBuilder();
            if (!isSave) {
                res.append("* ");
            }
            res.append(fileName).append(SUFFIX);
            shell.setText(res.toString());
        }
    }

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        Text text = new Text(shell, SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        CurrentFile currentFile = new CurrentFile();
        currentFile.shell = shell;
        currentFile.text = text;
        //初始化笔记本
        init(currentFile);
        //显示
        shell.open();
        while (!shell.isDisposed()) {
            while (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
    }

    private static void init(CurrentFile currentFile) {
        Shell shell = currentFile.shell;
        Text text = currentFile.text;
        shell.setMaximized(true);
        shell.setText(INIT_NAME);
        //初始化主菜单
        initMainMenu(currentFile);
        //初始化右键菜单
        initRightMenu(currentFile);
        //初始化布局
        shell.setLayout(new FillLayout());
        //初始化文本框
        initText(currentFile);
        shell.layout();
    }

    private static void initText(CurrentFile currentFile) {
        currentFile.text.setText("");
        currentFile.text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                currentFile.isSave = false;
                currentFile.updateShellText();
            }
        });
    }

    private static void initMainMenu(CurrentFile currentFile) {
        Shell shell = currentFile.shell;
        Text text = currentFile.text;

        Menu mainMenu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(mainMenu);
        {
            //文件项
            MenuItem fileItem = new MenuItem(mainMenu, SWT.CASCADE);
            fileItem.setText("文件&(F)");

            //文件菜单
            Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
            fileItem.setMenu(fileMenu);
            {
                //新建项
                MenuItem newFilesItem = new MenuItem(fileMenu, SWT.CASCADE);
                newFilesItem.setText("新建&(N)");

                //新建菜单
                Menu newFilesMenu = new Menu(shell, SWT.DROP_DOWN);
                newFilesItem.setMenu(newFilesMenu);
                {
                    //新建文件项
                    MenuItem newFileItem = new MenuItem(newFilesMenu, SWT.PUSH);
                    newFileItem.setText("文件    Ctrl+Shift+N");
                    newFileItem.setAccelerator(SWT.CTRL + SWT.SHIFT + 'N');
                    newFileItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            if (!currentFile.isSave) {
                                MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_INFORMATION);
                                messageBox.setText("提示");
                                String fileName = currentFile.fileName == null ? "无标题" : currentFile.filePath;
                                messageBox.setMessage("你想要将更改保存到\n" + fileName);
                                int open = messageBox.open();
                                switch (open) {
                                    case SWT.YES:
                                        saveFile(currentFile);
                                        break;
                                    case SWT.NO:
                                        init(currentFile);
                                        break;
                                    case SWT.CANCEL:
                                        break;
                                }
                            }
                        }
                    });
                    //新建类项
                    MenuItem newClassItem = new MenuItem(newFilesMenu, SWT.PUSH);
                    newClassItem.setText("类");

                }
                //打开项
                {
                    MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
                    menuItem.setText("打开");
                    menuItem.setAccelerator(SWT.CTRL + 'O');
                    menuItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
                            fileDialog.setText("文本选择");
                            fileDialog.setFilterPath("SystemRoot");
                            String selected = fileDialog.open();
                            // 用户取消打开
                            if (selected == null)
                                return;
                            log.info("文件>打开 用户选择路径为: {}", selected);
                            currentFile.filePath = selected;
                            currentFile.fileName = selected.substring(selected.lastIndexOf("\\") + 1);

                            //读取文件到记事本
                            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(selected))))) {
                                StringBuilder sb = new StringBuilder();
                                String s;
                                while ((s = br.readLine()) != null) {
                                    sb.append(s);
                                    sb.append("\n");
                                }
                                text.setText(sb.toString());
                            } catch (IOException ioException) {
                                log.error(ioException.getMessage());
                            }
                            currentFile.updateShellText();
                        }
                    });

                }
                //保存项
                {
                    MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
                    menuItem.setText("保存");
                    menuItem.setAccelerator(SWT.CTRL + 'S');
                    menuItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            saveFile(currentFile);
                        }
                    });
                }
                //另存为项
                {
                    MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
                    menuItem.setText("另存为");
                    menuItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            log.info("进行另存为操作,filePath:{}", currentFile.fileName);
                            FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
                            String fName = "新建文本文档.txt";
                            if (currentFile.fileName != null) {
                                fName = currentFile.fileName;
                            }
                            fileDialog.setFileName(fName);
                            String selected = fileDialog.open();
                            if (selected == null) {
                                return;
                            }
                            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(selected))))) {
                                bw.write(text.getText());
                            } catch (IOException ioException) {
                                log.error(ioException.getMessage());
                            }
                        }
                    });

                }

                //打印项
                {
                    MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
                    menuItem.setText("打印");
                    menuItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            MessageBox messageBox = new MessageBox(shell, SWT.OK);
                            messageBox.setMessage("打印");
                            messageBox.open();
                        }
                    });
                }

                //退出项
                {
                    MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
                    menuItem.setText("退出");
                    menuItem.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            shell.dispose();
                        }
                    });

                }

            }
        }
        {
            //编辑项
            MenuItem editItem = new MenuItem(mainMenu, SWT.CASCADE);
            editItem.setText("编辑&(E)");

        }
        {
            //帮助项
            MenuItem helpItem = new MenuItem(mainMenu, SWT.CASCADE);
            helpItem.setText("帮助&(H)");
        }
    }

    private static void initRightMenu(CurrentFile currentFile) {
        Menu rightMenu = new Menu(currentFile.shell, SWT.POP_UP);
        currentFile.shell.setMenu(rightMenu);
    }

    private static void saveFile(CurrentFile currentFile) {
        log.info("进行保存操作,filePath:{}", currentFile.filePath);
        if (currentFile.filePath == null) {
            FileDialog fileDialog = new FileDialog(currentFile.shell, SWT.SAVE);
            fileDialog.setFilterNames(new String[]{
                    "文本文档 (*.txt)",           //这样输入文件名时不用加.xls
                    "All Files (*.*)"
            });
            fileDialog.setFilterExtensions(new String[]{
                    "*.txt", "*.*"
            });
            String selected = fileDialog.open();
            if (selected == null) {
                return;
            }
            currentFile.filePath = selected;
            currentFile.fileName = currentFile.filePath.substring(currentFile.filePath.lastIndexOf("\\") + 1);
            log.info("文件>保存 用户选择路径为: {}", selected);
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(currentFile.filePath))))) {
            bw.write(currentFile.text.getText());
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
        currentFile.shell.setText(currentFile.fileName + "-" + "记事本");
        currentFile.isSave = true;
    }

}
