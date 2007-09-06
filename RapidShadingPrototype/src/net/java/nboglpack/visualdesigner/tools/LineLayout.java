/*
 * LineLayout.java
 *
 * Created on April 30, 2007, 12:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 *
 * @author Samuel Sperling
 */
public class LineLayout implements LayoutManager {
    
    private Container parent;
    private int vGap = 2;
    private int currentPosition = 0;
//    private Dimension minSize = new Dimension();
//    private Dimension prefSize = new Dimension();
    
    /** Creates a new instance of LineLayout */
    public LineLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
        //TODO
    }
    
    private void addMinSize(Component comp, Dimension minSize) {
        minSize.height += comp.getMinimumSize().getHeight() + vGap;
        minSize.width = Math.max(minSize.width, (int) comp.getMinimumSize().getWidth());
    }

    private void addPrefSize(Component comp, Dimension prefSize) {
        prefSize.height += comp.getPreferredSize().getHeight() + vGap;
        prefSize.width = Math.max(prefSize.width, (int) comp.getPreferredSize().getWidth());
    }

    public void removeLayoutComponent(Component comp) {
        //TODO
    }

    public Dimension preferredLayoutSize(Container parent) {
//        return this.prefSize;
	Insets insets = parent.getInsets();
        Component[] components = parent.getComponents();
        if (components == null) return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        
        currentPosition = insets.top;
        Dimension prefSize = new Dimension();
        for (Component component : components) {
            addPrefSize(component, prefSize);
        }
        return new Dimension(prefSize.width + insets.left + insets.right,
                             prefSize.height + insets.top + insets.bottom);
    }

    public Dimension minimumLayoutSize(Container parent) {
//        return this.minSize;
	Insets insets = parent.getInsets();
        Component[] components = parent.getComponents();
        if (components == null) return new Dimension();
        
        currentPosition = 0;
        Dimension minSize = new Dimension();
        for (Component component : components) {
            addMinSize(component, minSize);
        }
        return new Dimension(minSize.width + insets.left + insets.right,
                             minSize.height + insets.top + insets.bottom);
    }

    public void layoutContainer(Container parent) {
        this.parent = parent;
        Component[] components = this.parent.getComponents();
        if (components == null) return;
        
        int currentPosition = parent.getInsets().top;
        for (Component component : components) {
            component.setBounds(
                    parent.getInsets().left,
                    currentPosition,
                    parent.getPreferredSize().width - parent.getInsets().left - parent.getInsets().right,
                    component.getPreferredSize().height);
            if (component.isVisible())
                currentPosition += component.getPreferredSize().getHeight() + vGap;
//            this.addLayoutComponent(component.getName(), component);
        }
    }
    
}
