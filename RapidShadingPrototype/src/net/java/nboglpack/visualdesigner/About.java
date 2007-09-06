/*
 * About.java
 *
 * Created on August 20, 2007, 12:10 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

/**
 *
 * @author Samuel Sperling
 */
public class About extends JDialog {
    
    /** Creates a new instance of About */
    public About() {
        initComponents();
    }
    
    public About(Frame owner) {
        super(owner, true);
        initComponents();
    }
    
    private void initComponents() {
        this.setSize(250, 350);
        this.setTitle("About Rapid Shading");
        GridLayout layout = new GridLayout(0, 1);
        this.setLayout(layout);
        
        JLabel title = new JLabel("Rapid Shading Version 0.4");
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        this.add(title);
        
        JEditorPane content = new JEditorPane();
        content.setEditable(false);
        content.setContentType("text/html");
        content.setText("<html><body style='font-family: Arial; font-size: 9px;'>Please visit our website, to check for update, send feedback or contribute to the project:<br>www.procedural-texturing.com</body></html>");
        this.add(content);
        
        JEditorPane license = new JEditorPane();
        license.setContentType("text/html");
        license.setText("<html><body style='font-family: Arial; font-size: 9px; font-style: italic;'>This application can only be used under the terms of the GNU General Public License 3</body></html>");
        license.setBackground(Color.LIGHT_GRAY);
        license.setSize(new Dimension(250, 20));
        this.add(license);
    }
        
    public void showDialog() {
        this.setVisible(true);
    }
}
