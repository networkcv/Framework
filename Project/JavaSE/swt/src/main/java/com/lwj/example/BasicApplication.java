/*
 * Created on May 22, 2005
 *
 */
package com.lwj.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Base class for standalone SWT aplications.
 * @author barryf
 */
abstract public class BasicApplication extends Composite {
    protected Shell shell;
    protected Table table;

    /**
     * Indent lines
     */
    protected void indent(PrintWriter pw, int level) {
        for (int i = 0; i < level; i++) {
            pw.print("    ");
        }
    }

    /**
     * Find and invoke a zero-arg method
     */
    protected Object invoke(String name, Widget c) {
        Object res = null;
        try {
            Method m = c.getClass().getMethod(name, null);
            res = m.invoke(c, null);
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * Get the Items asociatated with a widget
     */
    protected Item[] getItems(Widget w) {
        Object o = invoke("getItems", w);
        return o instanceof Item[] ? (Item[]) o : null;
    }

    /**
     * Get the String Items asociatated with a widget
     */
    protected String[] getStringItems(Widget w) {
        Object o = invoke("getItems", w);
        return o instanceof String[] ? (String[]) o : null;
    }

    /**
     * Get the Controls asociatated with a widget
     */
    protected Control[] getChildren(Widget w) {
        return (Control[]) invoke("getChildren", w);
    }

    /**
     * Get the Shells asociatated with a widget
     */
    protected Shell[] getShells(Widget w) {
        return (Shell[]) invoke("getShells", w);
    }

    /**
     * Output a GUI component tree on System.out
     * @param w Widget to print
     */
    public void displayTree(Widget w) {
        PrintWriter pw = new PrintWriter(System.out, true);
        displayTree(pw, w);
        pw.close();
    }

    /**
     * Output a GUI component tree
     * @param pw Target to print on
     * @param w  Widget to print
     */
    public void displayTree(PrintWriter pw, Widget w) {
        displayTree(pw, w, 0);
    }

    /**
     * Output a GUI component tree
     * @param pw    Target to print on
     * @param w     Widget to print
     * @param level Indentation amount
     */
    public void displayTree(PrintWriter pw, Widget w, int level) {
        indent(pw, level);
        pw.println(w.toString());
        Item[] items = getItems(w);
        if (items != null && items.length > 0) {
            //indent(pw, level);
            //pw.println("Items:");
            for (int i = 0; i < items.length; i++) {
                displayTree(pw, items[i], level + 1);
            }
        }

        Control[] children = getChildren(w);
        if (children != null && children.length > 0) {
            //indent(pw, level);
            //pw.println("Children:");
            for (int i = 0; i < children.length; i++) {
                displayTree(pw, children[i], level + 1);
            }
        }

        Shell[] shells = getShells(w);
        if (shells != null && shells.length > 0) {
            indent(pw, level);
            pw.println("Child shells:");
            for (int i = 0; i < shells.length; i++) {
                displayTree(pw, shells[i], level + 1);
            }
        }
    }

    /**
     * Constructor.
     */
    public BasicApplication(Shell parent) {
        this(parent, SWT.NONE);  // must always supply parent
    }

    /**
     * Constructor.
     */
    public BasicApplication(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
        this.shell = shell;
    }

    protected static final Class[] args = {};

    /**
     * Register a method as an event callback routine;
     * Uses reflection to invoke
     * @param mi          MenuItem to associate callback with
     * @param handler     Class with mehto to handle request
     * @param handlerName Name of zero-arg method in handler to call
     */
    protected void registerCallback(final MenuItem mi, final Object handler, final String handlerName) {
        mi.addSelectionListener(new SelectionListener() {
            // dispatch event
            public void widgetSelected(SelectionEvent e) {
                try {
                    Method m = handler.getClass().getMethod(handlerName, args);
                    m.invoke(handler, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // *** should not happen ***
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }

    /**
     * Create a Menu
     * @param parent  Containing Menu
     * @param enabled Item's enabled state
     */
    protected Menu createMenu(Menu parent, boolean enabled) {
        Menu m = new Menu(parent);
        m.setEnabled(enabled);
        return m;
    }

    /**
     * Create a Menu
     * @param parent  Containing MenuItem
     * @param enabled Item's enabled state
     */
    protected Menu createMenu(MenuItem parent, boolean enabled) {
        Menu m = new Menu(parent);
        m.setEnabled(enabled);
        return m;
    }

    /**
     * Create a Menu
     * @param parent Containing Shell
     * @param style  Item's style
     */
    protected Menu createMenu(Shell parent, int style) {
        Menu m = new Menu(parent, style);
        return m;
    }

    /**
     * Create a Menu
     * @param parent    Containing Shell
     * @param style     Item's style
     * @param container Containing MenuItem
     * @param enabled   Item's enabled state
     */
    protected Menu createMenu(Shell parent, int style, MenuItem container, boolean enabled) {
        Menu m = createMenu(parent, style);
        m.setEnabled(enabled);
        container.setMenu(m);
        return m;
    }

    /**
     * Create a popup Menu
     * @param shell Owning Shell
     */
    protected Menu createPopupMenu(Shell shell) {
        Menu m = new Menu(shell, SWT.POP_UP);
        shell.setMenu(m);
        return m;
    }

    /**
     * Create a popup Menu
     * @param shell Owning Shell
     * @param owner Owning Control
     */
    protected Menu createPopupMenu(Shell shell, Control owner) {
        Menu m = createPopupMenu(shell);
        owner.setMenu(m);
        return m;
    }

    /**
     * Create a MenuItem
     * @param parent   Containing menu
     * @param style    Item's style
     * @param text     Item's text (optional: null)
     * @param icon     Item's icon (optional: null)
     * @param accel    Item's accelerator (optional: &lt;0)
     * @param enabled  Item's enabled state
     * @param callback Name of zero-arg method to call
     */
    protected MenuItem createMenuItem(Menu parent, int style, String text, Image icon, int accel, boolean enabled, String callback) {
        MenuItem mi = new MenuItem(parent, style);
        if (text != null) {
            mi.setText(text);
        }
        if (icon != null) {
            mi.setImage(icon);
        }
        if (accel != -1) {
            mi.setAccelerator(accel);
        }
        mi.setEnabled(enabled);
        if (callback != null) {
            registerCallback(mi, this, callback);
        }
        return mi;
    }

    /**
     * Create the GUI
     */
    protected void createGui(String[] args) {
        setLayout(new FillLayout());

//        shell.addShellListener(new ShellAdapter() {
//                public void shellClosed(ShellEvent e) {
//                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
//                    mb.setText("Confirm Exit");
//                    mb.setMessage("Are you sure you want to exit?");
//                    int rc = mb.open();
//                    e.doit = rc == SWT.OK;
//                }
//        });
        completeGui(args);
    }

    /* Make my Shell centered on the Display */
    public static void centerShell(Shell shell) {
        Display d = shell.getDisplay();
        Rectangle db = d.getBounds();
        Rectangle sb = shell.getBounds();
        int xoffset = (db.width - sb.width) / 2;
        int yoffset = (db.height - sb.height) / 2;
        shell.setLocation(xoffset, yoffset);
    }

    /**
     * Allow subclasses to complete the GUI
     */
    protected abstract void completeGui(String[] args);

    /**
     * Allow subclasses to initialize the GUI
     */
    protected abstract void initGui();

    /**
     * Run the application.
     * @param appName name of the class to construct and run
     * @param title   Shell title message
     * @param style   Content style
     * @param width   Shell width
     * @param height  Shell height
     * @param args    Any command line arguments
     */
    public static void run(String appName, String title, int style, int width, int height, String[] args) {
        // the display allows access to the host display device
        final Display display = new Display();
        try {
            // the shell is the top level window (AKA frame)
            final Shell shell = new Shell(display);
            shell.setText(title);
            shell.setLayout(new FillLayout());

            // add user component
            Class clazz = Class.forName(appName);
            Constructor ctor = clazz.getConstructor(new Class[]{Shell.class, Integer.TYPE});
            BasicApplication app = (BasicApplication) ctor.newInstance(new Object[]{shell, new Integer(style)});
            app.createGui(args);
            app.initGui();

            shell.setSize(width, height);
            centerShell(shell);
            shell.open();       // open shell for user access

            // process all user input events until the shell is disposed (i.e., closed)
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {  // process next message
                    display.sleep();              // wait for next message
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        display.dispose();  // must always clean up
    }
}
