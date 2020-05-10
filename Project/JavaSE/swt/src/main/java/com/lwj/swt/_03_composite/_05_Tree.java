package com.lwj.swt._03_composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import java.util.Arrays;

/**
 * create by lwj on 2020/5/10
 */
public class _05_Tree {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setText("Tree");
        Tree tree = new Tree(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        TreeItem treeItem1 = new TreeItem(tree, SWT.NONE);
        treeItem1.setText("treeItem1");
        TreeItem treeItem11 = new TreeItem(treeItem1, SWT.NONE);
        treeItem11.setText("treeItem11");
        TreeItem treeItem12 = new TreeItem(treeItem1, SWT.NONE);
        treeItem12.setText("treeItem12");
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println(Arrays.toString(tree.getSelection()));
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            while (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();

    }
}
