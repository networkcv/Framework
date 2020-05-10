/*
 *  Examples on using SWT Menus.
 *
 * Created on Mar 28, 2005
 *
 */
package com.lwj.example;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Basic SWT Menus example.
 * Shows use of menus.
 *
 * @author barryf
 */
public class Menus1 extends Composite {
    protected Shell shell;

    /**
     * Constructor.
     */
    public Menus1(Shell parent) {
        this(parent, SWT.NONE);  // must always supply parent
    }
    /**
     * Constructor.
     */
    public Menus1(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
        this.shell = shell;
        createGui();
    }

    /** Center the supplied Shell */
    public static void centerShell(Shell shell) {
        Display d = shell.getDisplay();
        Rectangle db = d.getBounds();
        Rectangle sb = shell.getBounds();
        int xoffset = (db.width - sb.width) / 2;
        int yoffset = (db.height - sb.height) / 2;
        shell.setLocation(xoffset, yoffset);
    }

    protected static final Class [] args = {};

    /**
     * Register a method as an event callback routine;
     * Uses reflection to invoke
     *
     * @param mi Menu items to associate callback with
     * @param handler Class with mehto to handle request
     * @param handlerName Name of zero-arg method in handler to call
     */
    protected void registerCallback(final MenuItem mi, final Object handler, final String handlerName) {
        mi.addSelectionListener(new SelectionListener() {
                                   // dispatch event
                                   public void widgetSelected(SelectionEvent e) {
                                       try {
                                           Method m = handler.getClass().getMethod(handlerName, args);
                                           m.invoke(handler, null);
                                       }
                                       catch (Exception ex) {
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
     *
     * @param parent Containing Menu
     * @param enabled Item's enabled state
     */
    protected Menu createMenu(Menu parent, boolean enabled) {
        Menu m = new Menu(parent);
        m.setEnabled(enabled);
        return m;
    }

    /**
     * Create a Menu
     *
     * @param parent Containing MenuItem
     * @param enabled Item's enabled state
     */
    protected Menu createMenu(MenuItem parent, boolean enabled) {
        Menu m = new Menu(parent);
        m.setEnabled(enabled);
        return m;
    }

    /**
     * Create a Menu
     *
     * @param parent Containing Shell
     * @param style Item's style
     */
    protected Menu createMenu(Shell parent, int style) {
        Menu m = new Menu(parent, style);
        return m;
    }
    /**
     * Create a Menu
     *
     * @param parent Containing Shell
     * @param style Item's style
     * @param container Containing MenuItem
     * @param enabled Item's enabled state
     */
    protected Menu createMenu(Shell parent, int style, MenuItem container, boolean enabled) {
        Menu m = createMenu(parent, style);
        m.setEnabled(enabled);
        container.setMenu(m);
        return m;
    }

    /**
     * Create a popup Menu
     *
     * @param shell Owning Shell
     */
    protected Menu createPopupMenu(Shell shell) {
        Menu m = new Menu(shell, SWT.POP_UP);
        shell.setMenu(m);
        return m;
    }

    /**
     * Create a popup Menu
     *
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
     *
     * @param parent Containing menu
     * @param style Item's style
     * @param text Item's text (optional: null)
     * @param icon Item's icon (optional: null)
     * @param accel Item's accelerator (optional: &lt;0)
     * @param enabled Item's enabled state
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

    /** Create the GUI */
    protected void createGui() {
        setLayout(new FillLayout());

        // recognize the app is ending and confirm
        shell.addShellListener(new ShellAdapter() {
                public void shellClosed(ShellEvent e) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                    mb.setText("Confirm Exit");
                    mb.setMessage("Are you sure you want to exit?");
                    int rc = mb.open();
                    e.doit = rc == SWT.OK;
                }
        });

        // Layout the (dummy) contents
        Label body = new Label(this, SWT.CENTER);
        body.setText("Sample body");

        // Create the menu system
        Menu main = createMenu(shell, SWT.BAR | SWT.LEFT_TO_RIGHT);
        shell.setMenuBar(main);

        MenuItem fileMenuItem = createMenuItem(main, SWT.CASCADE, "&File", null, -1, true, null);
        Menu fileMenu = createMenu(shell, SWT.DROP_DOWN, fileMenuItem, true);
        MenuItem exitMenuItem = createMenuItem(fileMenu, SWT.PUSH, "E&xit\tCtrl+X", null, SWT.CTRL + 'X', true, "doExit");

        MenuItem helpMenuItem = createMenuItem(main, SWT.CASCADE, "&Help", null, -1, true, null);
        Menu helpMenu = createMenu(shell, SWT.DROP_DOWN, helpMenuItem, true);
        MenuItem aboutMenuItem = createMenuItem(helpMenu, SWT.PUSH, "&About\tCtrl+A", null, SWT.CTRL+ 'A', true, "doAbout");

        // add popup menu
        Menu popup = createPopupMenu(shell, body);
        MenuItem popupMenuItem1 = createMenuItem(popup, SWT.PUSH, "&About", null, -1, true, "doAbout");
        MenuItem popupMenuItem2 = createMenuItem(popup, SWT.PUSH, "&Noop", null, -1, true, "doNothing");
    }

    // Event Handlers

    public void doNothing() {
    }

    public void doExit() {
        shell.close();
    }

    public void doAbout() {
        MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
        mb.setText("About Menu1");
        mb.setMessage("Menu example by Barry Feigenbaum.");
        int rc = mb.open();
    }

    /**
     * Main driver program.
     */
    public static void main(String[] args) {
        // the display allows access to the host display device
        final Display display = new Display();

        // the shell is the top level window (AKA frame)
        final Shell shell = new Shell(display);
        shell.setText("Example Menus1");   // Give me a title
        shell.setLayout(new FillLayout());

        Menus1 basic = new Menus1(shell);

        //shell.pack();       // make shell take minimum size
        shell.setSize (300, 200);
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