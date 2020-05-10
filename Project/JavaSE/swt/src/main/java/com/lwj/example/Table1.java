/*
 * Created on May 22, 2005
 *
 */
package com.lwj.example;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author barryf
 *
 */
public class Table1 extends Composite {
    protected Shell shell;
    protected Table table;

    /**
     *
     */
    public Table1(Shell parent) {
        this(parent, SWT.NONE);  // must always supply parent
    }
    /**
     * Constructor.
     */
    public Table1(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
        this.shell = shell;
        createGui();
    }

    protected void createGui() {
        setLayout(new FillLayout());

        shell.addShellListener(new ShellAdapter() {
                public void shellClosed(ShellEvent e) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                    mb.setText("Confirm Exit");
                    mb.setMessage("Are you sure you want to exit?");
                    int rc = mb.open();
                    e.doit = rc == SWT.OK;
                }
        });

        // all children split space equally
        //Composite body = new Composite(shell, SWT.BORDER);
        //createTable(body);
        //createTable(shell);
        createTable(this);
    }

    protected void createTable(Composite parent) {
        table = new Table(parent, SWT.CHECK | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        //GridData gd = new GridData(GridData.FILL_BOTH);
        //table.setLayoutData(gd);

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
    }

    public void addTableContents(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            String[] item = (String[])items[i];
            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(item);
        }
    }

    public static void centerShell(Shell shell) {
        Display d = shell.getDisplay();
        Rectangle db = d.getBounds();
        Rectangle sb = shell.getBounds();
        int xoffset = (db.width - sb.width) / 2;
        int yoffset = (db.height - sb.height) / 2;
        shell.setLocation(xoffset, yoffset);
    }


    /**
     * Main driver program.
     */
    public static void main(String[] args) {
        // the display allows access to the host display device
        final Display display = new Display();

        // the shell is the top level window (AKA frame)
        final Shell shell = new Shell(display);
        shell.setText("Example Table 1");   // Give me a title
        shell.setLayout(new FillLayout());

        Table1 basic = new Table1(shell);
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
        basic.addTableContents(items);

        //shell.pack();       // make shell take minimum size
        shell.setSize (500, 300);
        centerShell(shell);
        shell.open();       // open shell for user access

        // process all user input events until the shell is disposed (i.e., closed)
        while ( !shell.isDisposed()) {
            if (!display.readAndDispatch()) {  // process next message
                display.sleep();              // wait for next message
            }
        }
        display.dispose();  // must always clean up
    }
}
