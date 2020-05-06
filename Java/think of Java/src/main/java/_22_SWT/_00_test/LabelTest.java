package _22_SWT._00_test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * create by lwj on 2020/5/6
 */
public class LabelTest {
    public static void main(String[] args) {
        Display display = new Display ();
        Shell shell0 =new Shell(display);
        Shell shell = new Shell(shell0,SWT.DIALOG_TRIM);
        shell.setText("Snippet 1");
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }
}
