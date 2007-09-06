/*
 * NodeOutputConnector.java
 *
 * Created on April 13, 2007, 4:30 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Samuel Sperling
 */
public class NodeOutputConnector extends NodeConnector {
    
    private ArrayList<NodeInputConnector> connectionInputs;
    
    public NodeOutputConnector(String name) {
        super(name);
        isInputConnector = false;
    }
    
    public void addConnectionInput(NodeInputConnector connectionInput) {
        addConnectionInput(connectionInput, false);
    }
    
    public void addConnectionInput(ArrayList<NodeInputConnector> connectionInputs) {
        for (NodeInputConnector connectionInput : connectionInputs) {
            addConnectionInput(connectionInput, false);
        }
    }
    
    protected void addConnectionInput(NodeInputConnector connectionInput, boolean isInternalCall) {
        if (this.connectionInputs == null)
            this.connectionInputs = new ArrayList<NodeInputConnector>();
        this.connectionInputs.add(connectionInput);
        
        if (!isInternalCall)
            connectionInput.setConnectionOutput(this, true);
    }
    
    protected void removeConnectionInput(ArrayList<NodeInputConnector> connectionInputs) {
        for (NodeInputConnector connectionInput : connectionInputs) {
            connectionInput.removeConnectionOutput(this);
        }
        connectionInputs.removeAll(connectionInputs);
    }
    protected boolean removeConnectionInput(NodeInputConnector connectionInput) {
        if (this.connectionInputs == null || !connectionInputs.remove(connectionInput)) return false;
        
        connectionInput.removeConnectionOutput(this);
        return true;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle b = this.getBounds();
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, b.width * 2, b.height, 8, 8);
        g.fillRoundRect(0, 1, b.width * 2, b.height - 2, 8, 8);
        g.fillRoundRect(0, 2, b.width * 2, b.height - 4, 8, 8);
    }
    
    protected void startDragging() {
        
        // First case might cause some problems, since it moves more than one
        // connection, so a compatibility-check with another ouput might be a problem.
        if (this.connectionInputs != null && this.connectionInputs.size() > 0) {
            // Drag the incoming connection 'away'
            NodeOutputConnector tmpConnector = new NodeOutputConnector("Connect to a different ouput...");
            tmpConnector.setSize(tmpConnector.getPreferredSize());
            tmpConnector.setLocation(getAbsolutePosition());

            replaceConnector(tmpConnector);
            this.draggingConnector = tmpConnector;
            graph.startConnectorDragging(tmpConnector);
        } else {
            // Drag a connection from here.
            NodeInputConnector tmpConnector = new NodeInputConnector("Create a connection to an input...");
            tmpConnector.setSize(tmpConnector.getPreferredSize());
            tmpConnector.setLocation(getAbsolutePosition());

            addConnectionInput(tmpConnector);
            this.draggingConnector = tmpConnector;
            graph.startConnectorDragging(tmpConnector);
        }
    }
    
    protected void endDragging() {
        if (graph == null) return;
        
        // if it's a dragging-end by deletion of a connection return directly
        if (draggingConnector != null) {
            // If the mouse was hovers over a node for a connection
            if (graph.getCurrentHoverPanel() == null) {

                // Revert to old situation
                if (draggingConnector instanceof NodeInputConnector) {
                    ((NodeInputConnector) this.draggingConnector).removeConnectionOutput(this);
                } else if(draggingConnector instanceof NodeOutputConnector) {
                    ((NodeOutputConnector) this.draggingConnector).replaceConnector(this);
                }
            } else {
                draggingConnector.replaceConnector((NodeConnector) graph.getCurrentHoverPanel());
            }
            graph.stopConnectorDragging(this.draggingConnector);
        } else {
            graph.stopConnectorDragging(this);
        }
    }
    
    protected boolean isValidHoverConnection() {
        if (!graph.isDraggingConnector()) return false;
        if (!(graph.getDraggingPanel() instanceof NodeOutputConnector)) return false;
        return graph.getDraggingPanel() != this;
    }
    protected boolean isCompatibleHoverConnection(NodeConnector connector) {
        return true;
    }

    public void replaceConnector(NodeConnector nodeConnector) {
        NodeOutputConnector newOutputConnector = (NodeOutputConnector) nodeConnector;
        newOutputConnector.addConnectionInput(this.connectionInputs);
        this.removeConnectionInput(this.connectionInputs);
    }

    public void delete() {
        endDragging();
        if (this.connectionInputs != null) {
            for (int i = this.connectionInputs.size() - 1; i >= 0; i--)
                this.connectionInputs.get(i).removeConnectionOutput(this);
        }
        if (node != null)
            node.removeOutputConnector(this);
    }
    
    public void removeDraggingConnector() {
        this.draggingConnector.delete();
        this.draggingConnector = null;
    }
//    protected void deleteConnection() {
//        for (int i = 0; i < this.connectionInputs.size(); i++)
//            this.connectionInputs.get(i).removeConnectionOutput(this);
//    }
}
