/*
 * PreviewTest.java
 *
 * Created on May 24, 2007, 11:55 AM
 */

package net.java.nboglpack.visualdesigner.tests;

import javax.swing.JFrame;
import javax.swing.UIManager;
import net.java.nboglpack.visualdesigner.preview.PreviewPanel;

/**
 *
 * @author Samuel Sperling
 */
public class PreviewTest extends JFrame {
    
    /** Creates a new instance of PreviewTest */
    public PreviewTest() {
        PreviewPanel preview = new PreviewPanel();
        this.add(preview);
    }
    public static void main(String[] argv) throws Exception {
        String systemLFClassName = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(systemLFClassName);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreviewTest().setVisible(true);
            }
        });
    }
    
}
