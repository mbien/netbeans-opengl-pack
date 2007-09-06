/*
 * LogOutputPanel.java
 *
 * Created on 19. Juni 2007, 16:04
 */

package net.java.nboglpack.visualdesigner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This Class holds a stream and displays it in a textarea
 *
 * @author Samuel Sperling
 */
public class LogOutputPanel extends JScrollPane {
    
    PrintStream printStream;
    JTextArea textarea;
    
    /** Creates a new instance of LogOutputPanel */
    public LogOutputPanel() {
        textarea = new JTextArea();
        textarea.setEditable(false);
        printStream = new PrintStream(new OutputStream() {
            public void write(int b) throws IOException {
                textarea.append(String.valueOf((char) b));
            }
        });
        setViewportView(textarea);
        
        JPopupMenu contextmenu = new JPopupMenu();
        textarea.setComponentPopupMenu(contextmenu);
        JMenuItem mnuClear = new JMenuItem("Clear");
        mnuClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textarea.setText("");
            }
        });
        contextmenu.add(mnuClear);
    }
    
    public PrintStream getPrintStream() {
        return printStream;
    }
    
}

//class TextAreaStream extends OutputStream {
//    
//    private JTextArea textarea;
////    private StringWriter cachedContent;
//    
//    public TextAreaStream(JTextArea textarea) {
//        this.textarea = textarea;
////        this.cachedContent = new StringWriter();
//    }
//    
//    public void flushToTextArea() {
//        textarea.append(cachedContent.toString());
//        cachedContent.getBuffer().setLength(0);
//    }
//    
//    public boolean hasUnflushedContent() {
//        return cachedContent.getBuffer().length() > 0;
//    }
//    
//    public void write(int b) throws IOException {
//        cachedContent.write(b);
//        textarea.append(String.valueOf((char) b));
//    }
//    
//}