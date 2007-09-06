/*
 * NodeConnector.java
 *
 * Created on April 13, 2007, 11:21 AM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 * Visual representative for a connector on a GraphNode
 *
 * @author Samuel Sperling
 */
public abstract class NodeConnector extends JPanel {
    
    public static final Dimension PREFERED_SIZE = new Dimension(8, 8);
    protected GraphNode node;
    protected NodeGraphPanel graph;
    public String name;
    /** Connector that is the dragging representative of this connector */
    public NodeConnector draggingConnector;
    public boolean isInputConnector;
    protected Color inactiveColor = new Color(255, 153, 0, 153);
    protected Color validActiveColor = new Color(51, 255, 0, 153);
    protected Color invalidActiveColor = new Color(255, 0, 0, 153);
    
    /** Creates a new instance of NodeConnector */
    public NodeConnector(String name) {
        this.name = name;
        this.setPreferredSize(PREFERED_SIZE);
        this.setToolTipText(name);
        this.setBackground(inactiveColor);
        this.setOpaque(false);
        setLayout(null);
        
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
                startHover(e);
            }
            public void mouseExited(MouseEvent e) {
                endHover(e);
            }
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    // Left Click
                    startDragging();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    // Left Release
                    endDragging();
                } else if (e.getButton() == e.BUTTON3) {
                    // RightClick
                    if (graph.isDraggingConnector()) {
                        removeDraggingConnector();
                    }
                }
            }
        });
    }
    
    public abstract void removeDraggingConnector();
    public abstract void delete();
    
    protected void startHover(MouseEvent e) {
        if (isValidHoverConnection()) {
            if (isCompatibleHoverConnection((NodeConnector) graph.getDraggingPanel())) {
                graph.setCurrentHoverPanel(this);
                setBackground(validActiveColor);
            } else {
                setBackground(invalidActiveColor);
            }
        }
    }
    
    protected void endHover(MouseEvent e) {
        graph.unsetCurrentHoverPanel(this);
        setBackground(inactiveColor);
    }
    
    protected abstract boolean isValidHoverConnection();
    protected abstract boolean isCompatibleHoverConnection(NodeConnector connector);
    protected abstract void startDragging();
    protected abstract void endDragging();
    public abstract void replaceConnector(NodeConnector newConnector);
    
    public Point getAbsolutePosition() {
        Point absolutePosition = this.getLocation();
        absolutePosition.x += Math.round(this.getWidth() / 2);
        absolutePosition.y += Math.round(this.getHeight() / 2);
        
        if (node != null) {
            Point parentLoc = node.getLocation();
            absolutePosition.x += parentLoc.x;
            absolutePosition.y += parentLoc.y;
        }
        return absolutePosition;
    }

    public GraphNode getParentNode() {
        return node;
    }

    public void setParentNode(GraphNode parentNode) {
        this.node = parentNode;
        if (parentNode != null)
            this.graph = parentNode.getGraph();
    }

    public int compareTo(NodeConnector nodeConnector) {
        return name.compareTo(nodeConnector.name);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void paintOnGraph(Graphics g) {
    }    
}
