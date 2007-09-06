/*
 * FullscreenPreview.java
 *
 * Created on June 16, 2007, 3:34 PM
 */

package net.java.nboglpack.visualdesigner.preview;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author Samuel Sperling
 */
public class FullscreenPreview implements GLEventListener {
    private int width;
    private int height;
    
    /** Creates a new instance of FullscreenPreview */
    public FullscreenPreview(int width, int height) {
        this.width  = width;
        this.height = height;
    }
    
    public void init(GLAutoDrawable drawable) {
        // Find parent frame if any
        final Frame frame = getParentFrame((Component) drawable);
        if (frame != null) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setVisible(false);
                    frame.setBounds(0, 0, width, height);
                    frame.setVisible(true);
                    frame.toFront();
                }
            });
        }
    }
    
    public void display(GLAutoDrawable drawable) {}
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
    
    
    private static Frame getParentFrame(Component c) {
        while (c != null && (!(c instanceof Frame))) {
            c = c.getParent();
        }
        return (Frame) c;
    }
}
