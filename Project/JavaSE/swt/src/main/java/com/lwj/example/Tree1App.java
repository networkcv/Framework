/*
 * Created on May 22, 2005
 *
 */
package com.lwj.example;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Tree Example
 *
 * @author barryf
 */
public class Tree1App extends BasicApplication {
    protected Tree tree;
    protected int mode = 0;

    /** Data Model Object */
    public class Node {
        protected java.util.List children;
        /**
         * @return Returns the children.
         */
        public java.util.List getChildren() {
            return children;
        }
        /**
         * @param children The children to set.
         */
        public void setChildren(java.util.List children) {
            this.children = children;
        }

        protected String name;
        /**
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }
        /**
         * @param name The name to set.
         */
        public void setName(String name) {
            this.name = name;
        }
        public void addChild(Node node) {
            children.add(node);
        }

        public Node() {
            this("<unknown>");
        }
        public Node(String name) {
            this(name, new ArrayList());
        }
        public Node(String name, java.util.List children) {
            setName(name);
            setChildren(children);
        }

        public String toString() {
            //return getClass().getName() + "[" + getName() + "," + getChildren() + "]";
            return "Node" + "[" + getName() + "," + getChildren() + "]";
        }
    }

    /**
     * Constructor.
     */
    public Tree1App(Shell shell, int style) {
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
        createTree(this);
    }

    /** Create the Tree */
    protected Tree createTree(Composite parent) {
        tree = new Tree(parent, mode | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        tree.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    TreeItem[] sel = tree.getSelection();
                    if (sel != null && sel.length > 0) {
                        System.out.println("Selection:");
                        for (int i = 0; i < sel.length; i++) {
                            System.out.println("   " + sel[i].getText());
                        }
                    }
                }
                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                }
         });
         return tree;
    }

    /** Set the Tree data model */
    public void setTreeContents(Node root) {
        tree.removeAll();
        TreeItem ti = new TreeItem(tree, SWT.NONE);
        setTreeItemContents(ti, root);

    }

    /** Set a Tree level */
    protected void setTreeItemContents(TreeItem ti, Node root) {
        ti.setText(root.getName());
        java.util.List children = root.getChildren();
        if (children != null && children.size() > 0) {
            for (Iterator i = children.iterator(); i.hasNext();) {
                Node n = (Node)i.next();
                TreeItem tix = new TreeItem(ti, SWT.NONE);
                setTreeItemContents(tix, n);
            }
        }
    }

    /** Add children nodes to the  Tree; Creates dummy example nodes */
    protected void addChildren(Node n, int count, int depth, String prefix) {
        if (depth > 0) {
            for (int i = 0; i < count; i++) {
                String name = prefix + '.' + i;
                Node child = new Node(name);
                n.addChild(child);
                addChildren(child, count, depth - 1, name);
            }
        }
    }

    /** Allow subclasses to initialize the GUI */
    protected void initGui() {
        Node root = new Node("<root>");
        addChildren(root, 4, 2, "Child");
        setTreeContents(root);
        displayTree(shell);
    }

    /** Main driver */
    public static void main(String[] args) {
        run(Tree1App.class.getName(), "Tree1App Example", SWT.NONE, 400, 300, args);
    }
}
