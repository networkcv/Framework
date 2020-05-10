/*
 * Created on May 22, 2005
 *
 */
package com.lwj.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

/**
 * List Example
 * @author barryf
 */
public class List1App extends BasicApplication {
    protected List list;
    protected Combo combo;

    /**
     * Constructor.
     */
    public List1App(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
    }

    /**
     * Configure the form of the container
     */
    protected static void configureLayout(Control c, FormAttachment left, FormAttachment top, FormAttachment right, FormAttachment bottom) {
        FormData fd = new FormData();
        if (left != null) {
            fd.left = left;
        }
        if (top != null) {
            fd.top = top;
        }
        if (right != null) {
            fd.right = right;
        }
        if (bottom != null) {
            fd.bottom = bottom;
        }
        c.setLayoutData(fd);
    }

    /**
     * Allow subclasses to complete the GUI
     */
    protected void completeGui(String[] args) {
        setLayout(new FormLayout());

        Combo c = createCombo(this);
        configureLayout(c, new FormAttachment(0, 5), new FormAttachment(0, 5), new FormAttachment(100, -5), null);

        List l = createList(this);
        configureLayout(l, new FormAttachment(0, 5), new FormAttachment(c, 5), new FormAttachment(100, -5), new FormAttachment(100, -5));
    }

    /**
     * Create the Combo
     */
    protected Combo createCombo(Composite parent) {
        combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        combo.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                String text = combo.getText();
                assert text != null;
                System.out.println("Selection:");
                System.out.println("   " + text);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        return combo;
    }

    /**
     * Create the List
     */
    protected List createList(Composite parent) {
        list = new List(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        list.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                String[] sel = list.getSelection();
                if (sel != null && sel.length > 0) {
                    System.out.println("Selection:");
                    for (int i = 0; i < sel.length; i++) {
                        System.out.println("   " + sel[i]);
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        return list;
    }

    /**
     * Set the Combo data model
     */
    public void setComboContents(String[] data) {
        combo.removeAll();
        for (int i = 0; i < data.length; i++) {
            combo.add(data[i]);
        }
    }

    /**
     * Set the List data model
     */
    public void setListContents(String[] data) {
        list.removeAll();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }
    }

    /**
     * Allow subclasses to initialize the GUI
     */
    protected void initGui() {
        String[] data = {
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9",
                "Item 10"
        };
        setListContents(data);
        setComboContents(data);
//        displayTree(shell);
    }

    /**
     * Main driver
     */
    public static void main(String[] args) {
        run(List1App.class.getName(), "List1App Example", SWT.NONE, 400, 300, args);
    }

    @Override
    public String toString() {
        return "List1App{" +
                "list=" + list +
                ", combo=" + combo +
                '}';
    }
}
