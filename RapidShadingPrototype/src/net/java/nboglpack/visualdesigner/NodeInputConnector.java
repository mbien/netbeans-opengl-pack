/*
 * NodeConnectorInput.java
 *
 * Created on April 13, 2007, 4:25 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.RepaintManager;
import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 * Visual representative for an input connector on a GraphNode
 *
 * @author Samuel Sperling
 */
public class NodeInputConnector extends NodeConnector {
    
    private NodeOutputConnector connectionOutput;
    private Settings settings;
    private double arrowLength;
    private double angleOffset;
    
    public NodeInputConnector(String name) {
        super(name);
//        settings = Editor.mainSettings.getChildSettingsByPath("GraphNode/Connectors");
        arrowLength = /*settings.getAttributeValueDouble("Arrow", "Length",*/ 15d;//);
        angleOffset = /*settings.getAttributeValueDouble("Arrow", "AngleOffset",*/ 0.4d;//);
        isInputConnector = true;
    }
    
    public boolean setConnectionOutput(NodeOutputConnector connectionOutput) {
        return setConnectionOutput(connectionOutput, false);
    }
    
    protected boolean setConnectionOutput(NodeOutputConnector connectionOutput, boolean isInternalCall) {
        boolean wasSet = this.connectionOutput != null;
        this.connectionOutput = connectionOutput;
        
        if (!isInternalCall && connectionOutput != null)
            connectionOutput.addConnectionInput(this, true);
        return wasSet;
    }
    
    public boolean removeConnectionOutput(NodeOutputConnector connectionOutput) {
        if (this.connectionOutput == null || connectionOutput != this.connectionOutput) return false;
        this.connectionOutput = null;
        connectionOutput.removeConnectionInput(this);
        return true;
    }

    public NodeOutputConnector getConnectionOutput() {
        return connectionOutput;
    }


    public void paint(Graphics g) {
        super.paint(g);
        Rectangle b = this.getBounds();
        g.setColor(getBackground());
        g.fillRoundRect(-1 * b.width, 0, b.width * 2, b.height, 8, 8);
        g.fillRoundRect(-1 * b.width, 1, b.width * 2, b.height - 2, 8, 8);
        g.fillRoundRect(-1 * b.width, 2, b.width * 2, b.height - 4, 8, 8);
    }
    
    public void paintOnGraph(Graphics g) {
        if (connectionOutput == null)
            return;
        
        // Draw Line from here to the connectionOutput
        Point thisPoint = getAbsolutePosition();
        Point connectedPoint = connectionOutput.getAbsolutePosition();
        if (thisPoint.equals(connectedPoint)) return;
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(thisPoint.x, thisPoint.y, connectedPoint.x, connectedPoint.y);
        
        // Draw Direction arrow
        Vector2d midPoint = new Vector2d(connectedPoint.x - thisPoint.x, connectedPoint.y - thisPoint.y);
        Vector2d direction = new Vector2d(midPoint);
        Point2d thisPointD = new Point2d(thisPoint.x, thisPoint.y);
        direction.normalize();
        
        midPoint.x /= 2;
        midPoint.y /= 2;
        midPoint.add(thisPointD);
        
        Vector2d arrow = new Vector2d();
        double angle = Math.atan2(direction.x, direction.y);
        arrow.x = Math.sin(angle + angleOffset) * arrowLength;
        arrow.y = Math.cos(angle + 0.3) * arrowLength;
        arrow.add(midPoint);
        g.drawLine((int) Math.round(midPoint.x), (int) Math.round(midPoint.y), (int) Math.round(arrow.x), (int) Math.round(arrow.y));
        
        arrow.x = Math.sin(angle - angleOffset) * arrowLength;
        arrow.y = Math.cos(angle - angleOffset) * arrowLength;
        arrow.add(midPoint);
        g.drawLine((int) midPoint.x, (int) midPoint.y, (int) arrow.x, (int) arrow.y);
    }

    private Rectangle getLineBounds(Point thisPoint, Point connectedPoint) {
        Rectangle bounds = new Rectangle();
        bounds.x = Math.min(thisPoint.x, connectedPoint.x);
        bounds.y = Math.min(thisPoint.y, connectedPoint.y);
        bounds.width = Math.max(thisPoint.x, connectedPoint.x) - bounds.x;
        bounds.height = Math.max(thisPoint.y, connectedPoint.y) - bounds.y;
        return bounds;
    }
    
    protected void startDragging() {
        if (this.connectionOutput != null) {
            // Drag the incoming connection 'away'
            NodeInputConnector tmpConnector = new NodeInputConnector("Connect to a different input...");
            tmpConnector.setSize(tmpConnector.getPreferredSize());
            tmpConnector.setLocation(getAbsolutePosition());

            replaceConnector(tmpConnector);
            this.draggingConnector = tmpConnector;
            graph.startConnectorDragging(tmpConnector);
        } else {
            // Drag a connection from here.
            NodeOutputConnector tmpConnector = new NodeOutputConnector("Get connection from a different output...");
            tmpConnector.setSize(tmpConnector.getPreferredSize());
            tmpConnector.setLocation(getAbsolutePosition());

            setConnectionOutput(tmpConnector);
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
                    ((NodeInputConnector) this.draggingConnector).replaceConnector(this);
                } else if(draggingConnector instanceof NodeOutputConnector) {
                    ((NodeOutputConnector) this.draggingConnector).removeConnectionInput(this);
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
        if (this.connectionOutput != null) return false;
        if (!(graph.getDraggingPanel() instanceof NodeInputConnector)) return false;
        return graph.getDraggingPanel() != this;
    }
    protected boolean isCompatibleHoverConnection(NodeConnector connector) {
        return true;
    }

    public void replaceConnector(NodeConnector nodeConnector) {
        NodeInputConnector nodeInputConnector = (NodeInputConnector) nodeConnector;
        nodeInputConnector.setConnectionOutput(connectionOutput);
        this.removeConnectionOutput(connectionOutput);
    }

    public void delete() {
        endDragging();
        if (this.connectionOutput != null)
            this.connectionOutput.removeConnectionInput(this);
        if (node != null)
            node.removeInputConnector(this);
    }
    
    public void removeDraggingConnector() {
        this.draggingConnector.delete();
//        graph.stopConnectorDragging(this.draggingConnector);
        this.draggingConnector = null;
    }
}
