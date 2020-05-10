/*
 * Created on May 22, 2005
 *
 */
package com.lwj.example;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Table Example
 *
 * @author barryf
 */
public class Table1App extends BasicApplication {
    protected Table table;
    protected int mode = 0;

    /**
     * Constructor.
     */
    public Table1App(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
    }

    /** Parse any arguments */
    protected void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                String s = arg.substring(1);
                if      (s.equalsIgnoreCase("checked")) {
                    mode |= SWT.CHECK;
                }
                else if (s.equalsIgnoreCase("plain")) {
                    mode |= SWT.NONE;
                }
                else {
                    throw new IllegalArgumentException("Invalid switch: " + s);
                }
            }
            else {
                throw new IllegalArgumentException("No argument allowed");
            }
        }
    }

    /** Allow subclasses to complete the GUI */
    protected void completeGui(String[] args) {
        parseArgs(args);
        createTable(this);
    }

    protected TableColumn createTableColumn(Table table, int style, String title, int width) {
        TableColumn tc = new TableColumn(table, style);
        tc.setText(title);
        tc.setResizable(true);
        tc.setWidth(width);
        return tc;
    }

    /** Create the Table and TableColumns */
    protected Table createTable(Composite parent) {
        table = new Table(parent, mode | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        createTableColumn(table, SWT.LEFT,   "Column 1", 100);
        createTableColumn(table, SWT.CENTER, "Column 2", 100);
        createTableColumn(table, SWT.RIGHT,  "Column 3", 100);

        return table;
    }

    /** Add items to the table */
    public void addTableContents(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            String[] item = (String[])items[i];
            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(item);
        }
    }

    /** Allow subclasses to initialize the GUI; Supply some dummy example data */
    protected void initGui() {
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
        addTableContents(items);
    }

    /** Main driver */
    public static void main(String[] args) {
        run(Table1App.class.getName(), "Table1App Example", SWT.NONE, 400, 300, args);
    }
}
