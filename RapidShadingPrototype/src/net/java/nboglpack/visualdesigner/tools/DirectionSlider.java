/*
 * DirectionSlider.java
 *
 * Created on April 18, 2007, 3:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import javax.swing.JSlider;

/**
 *
 * @author Samuel Sperling
 */
public class DirectionSlider extends JSlider implements IRefreshable {
    
    private boolean isDragging;
    private ArrayList<Callable> callables;
    
    /** Creates a new instance of DirectionSlider */
    public DirectionSlider() {
        super(-10000, 10000, 0);
        initComponents();
    }
    
    private void initComponents() {
        this.setBackground(new Color(1f, 1f, 1f, 0.5f));
        this.setOpaque(false);
        
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                isDragging = true;
            }
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                resetValue();
            }
        });
    }

    public void paint(Graphics g) {
        Rectangle bounds = this.getBounds();
        g.setColor(this.getBackground());
        g.fillRoundRect(0, 0, bounds.width, bounds.height, 8, 8);
        
        super.paint(g);
    }
    
    private void resetValue() {
        this.setValue(0);
    }

    public boolean needsRefresh() {
        if (!isDragging) return false;
        return (callables != null);
    }
    
    /**
     * @return Returns a value between -1.0 and 1.0 depending on the sliders
     *         relative position
     */
    public float getRelativeValue() {
//        if (this.getValue() < 0)
            return (float) this.getValue() / this.getMinimum();
//        else
//            return (float) this.getValue() / this.getMinimum();
    }
    
    /**
     * Add a Listener that will be called while the slider is beeing moved
     * in a frequency defined by the attached refreshing-thread.
     */
    public boolean addSlidingListener(Callable callable) {
        if (this.callables == null)
            this.callables = new ArrayList<Callable>();
        return this.callables.add(callable);
    }
    
    public boolean remoceSlidingListener(Callable callable) {
        if (this.callables == null) return false;
        return this.callables.remove(callable);
    }

    public void refresh() {
        for (Callable callable : callables) {
            try {callable.call(); }
            catch (Exception ex) {}
        }
    }
    
}
