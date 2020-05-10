package com.lwj.swt._03_composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

/**
 * create by lwj on 2020/5/10
 */
public class _04_Table {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setText("table");
        Table table = new Table(shell, SWT.CHECK | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tc = null;
        tc = new TableColumn(table, SWT.LEFT);
        tc.setText("Column 1");
        tc.setResizable(true);
        tc.setWidth(100);

        tc = new TableColumn(table, SWT.CENTER);
        tc.setText("Column 2");
        tc.setResizable(true);
        tc.setWidth(100);

        tc = new TableColumn(table, SWT.RIGHT);
        tc.setText("Column 3");
        tc.setResizable(true);
        tc.setWidth(100);

        Object[] items = {
                new String[] {"A", "a", "0"},
                new String[] {"B", "b", "1"},
                new String[] {"C", "c", "2"},
                new String[] {"D", "d", "3"},
                new String[] {"E", "e", "4"},
                new String[] {"F", "f", "5"},
                new String[] {"G", "g", "6"},
                new String[] {"H", "h", "7"},
                new String[] {"I", "i", "8"},
                new String[] {"J", "j", "9"},
        };

        for (int i = 0; i < items.length; i++) {
            String[] item = (String[])items[i];
            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(item);
        }

        shell.open();
        while (!shell.isDisposed()) {
            while (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
    }

    public void addTableContents(Object[] items) {

    }
}
