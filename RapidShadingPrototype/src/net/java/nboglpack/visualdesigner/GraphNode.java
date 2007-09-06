/*
 * NodeBase.java
 *
 * Created on April 12, 2007, 4:20 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 *
 * @author Samuel Sperling
 */
public class GraphNode extends JPanel implements IPersistable {
    
    private NodeGraphPanel graph;
    private TreeMap<String, NodeInputConnector> inConnectors;
    private TreeMap<String, NodeOutputConnector> outConnectors;
    protected Color backgroundColor = new Color(100, 100, 100, 255);
    protected Color shadowColor = new Color(0, 0, 0, 50);
    protected Color foregroundColor = new Color(200, 200, 200, 255);
    protected Color lighterColor = new Color(255, 255, 255, 50);
    protected Color darkerColor = new Color(0, 0, 0, 50);
    protected Color hightlightColor = new Color(255, 51, 51, 204);
    protected Color hightlightColorInner = new Color(255, 51, 51, 102);
    private Settings styleSettings;
    /** Defines whether this Node is a one of the main In-/Out-nodes of the GraphEditor */
    private boolean isMainNode = false;
    private JLabel lblNodeName;
    private Dimension lastDimension;
    protected JPopupMenu contextMenu;
    private boolean hasFocus = false;
    protected JMenuItem mnuDelete;
    protected JPanel nodeContent;

    
    /** Creates a new instance of NodeBase */
    public GraphNode(String nodeName) {
        setName(nodeName);
        loadSettings();
        initComponents();
    }

    protected void initComponents() {
        setBackground(backgroundColor);
        setLayout(null);
        setOpaque(false);
        
        addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                activateNode();
            }
        });
        
        // Create ContextMenu
        contextMenu = new JPopupMenu();
        setComponentPopupMenu(contextMenu);
        
        // ContextMenuItems
        mnuDelete = new JMenuItem("Delete Node");
        mnuDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.removeNode(getName());
            }
        });
        mnuDelete.setBorder(null);
        contextMenu.add(mnuDelete);
        
        createTitle();
        
        adjustPreferedSize();
    }

    public JPanel getNodeContent() {
        if (nodeContent == null) {
            // createContentPanel
            nodeContent = new JPanel(null);
            nodeContent.setOpaque(false);
            nodeContent.setSize(1, 1);
            add(nodeContent);
            adjustPreferedSize();
        }
        return nodeContent;
    }
    
    private void activateNode() {
        graph.setNewActiveNode(this);
    }
    
    public void delete() {
        if (this.inConnectors != null) {
            Object[] inputs = inConnectors.values().toArray();
            for (int i = 0; i < inputs.length; i++) {
                ((NodeInputConnector) inputs[i]).delete();
            }
        }
        if (this.outConnectors != null) {
            Object[] outputs = outConnectors.values().toArray();
            for (int i = 0; i < outputs.length; i++) {
                ((NodeOutputConnector) outputs[i]).delete();
            }
        }
    }
    
    private void createTitle() {
        lblNodeName = new JLabel();
        lblNodeName.setText(getName());
//        lblNodeName.setForeground(styleSettings.getValueColor("ForegroundColor", foregroundColor));
        lblNodeName.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNodeName);
    }
    
    private void applyPreferedSize() {
        this.setSize(this.getPreferredSize());
    }
    
    protected void adjustPreferedSize() {
        int connectors = Math.max(
                            inConnectors == null ? 0 : inConnectors.size(), 
                            outConnectors == null ? 0 : outConnectors.size());
        
        Dimension preferedSize = isMainNode ? new Dimension(0, 0) : lblNodeName.getPreferredSize();
        
        // ContentPanel
        if (nodeContent != null) {
            Dimension contentSize = nodeContent.getPreferredSize();

            preferedSize.height += Math.max(
                    connectors * NodeConnector.PREFERED_SIZE.height * 2,
                    contentSize.height + 3);
            preferedSize.width = Math.max(
                    preferedSize.width,
                    contentSize.width);
        } else {
            preferedSize.height += connectors * NodeConnector.PREFERED_SIZE.height * 2;
        }
        preferedSize.width += 2 * NodeConnector.PREFERED_SIZE.width;

        this.setPreferredSize(preferedSize);
        
        if (!isMainNode)
            applyPreferedSize();
    }

    public void setName(String name) {
        super.setName(name);
        if (lblNodeName != null) {
            lblNodeName.setText(getName());
            adjustPreferedSize();
        }
    }

    private void loadSettings() {
//        styleSettings = Editor.mainSettings.getChildSettingsByPath("Nodes/Style");
//        
//        backgroundColor = styleSettings.getValueColor("BackgroundColor", backgroundColor);
//        shadowColor = styleSettings.getValueColor("ShadowColor", shadowColor);
    }
    
    /**
     * Adds a new output-connector to this Node
     * @param newConnector Connector to add to this node it is accessable through it's name.
     *                     Only one Connector can be attached to one name.
     * @return Returns the previous node attached to the same name.
     */
    public NodeConnector addOutputConnector(NodeOutputConnector newOutputConnector) {
        if (outConnectors == null)
            this.outConnectors = new TreeMap<String, NodeOutputConnector>();
        
        NodeOutputConnector oldOutputConnector = outConnectors.put(newOutputConnector.name, newOutputConnector);
        newOutputConnector.setParentNode(this);
        
        // Add as component to this panel
        newOutputConnector.setSize(newOutputConnector.getPreferredSize());
        newOutputConnector.setLocation(this.getWidth() - newOutputConnector.getWidth(), 0);
        this.add(newOutputConnector);
        adjustPreferedSize();
        return oldOutputConnector;
    }
    
    /**
     * removes an ouput-connector from this Node
     * @param outputConnector connector that has to be removed
     * @return true if connector was used in this node and could be removed successfully
     * false if connector wasn't used in this node
     */
    public boolean removeOutputConnector(NodeOutputConnector outputConnector) {
        if (outConnectors == null)
            return false;
        
        NodeOutputConnector oldOutputConnector = outConnectors.remove(outputConnector.name);
        this.remove(outputConnector);
        outputConnector.setParentNode(null);
        adjustPreferedSize();
        return oldOutputConnector == null;
    }
    
    /**
     * Adds a new input-connector to this Node
     * @param newConnector Connector to add to this node it is accessable through it's name.
     *                     Only one Connector can be attached to one name.
     * @return Returns the previous node attached to the same name.
     */
    public NodeConnector addInputConnector(NodeInputConnector newInputConnector) {
        if (inConnectors == null)
            this.inConnectors = new TreeMap<String, NodeInputConnector>();
        
        NodeInputConnector oldInputConnector = inConnectors.put(newInputConnector.name, newInputConnector);
        newInputConnector.setParentNode(this);
        
        // Add as component to this panel
        newInputConnector.setSize(newInputConnector.getPreferredSize());
        newInputConnector.setLocation(0, 0);
        this.add(newInputConnector);
        adjustPreferedSize();
        return oldInputConnector;
    }
    
    /**
     * removes an input-connector from this Node
     * @param inputConnector connector that has to be removed
     * @return true if connector was used in this node and could be removed successfully
     * false if connector wasn't used in this node
     */
    public boolean removeInputConnector(NodeInputConnector inputConnector) {
        if (inConnectors == null)
            return false;
        
        NodeInputConnector oldInputConnector = inConnectors.remove(inputConnector.name);
        this.remove(inputConnector);
        adjustPreferedSize();
        return oldInputConnector == null;
    }
    
    /**
     * Refreshed the position of all ouput nodes. 
     * So they are eqally distributed over the node.
     */
    private void refreshOuputConnectors() {
        if (outConnectors == null) return;
        
        int titleHeight = isMainNode ? 0 : lblNodeName.getHeight();
        float distance = this.getHeight() - titleHeight;
        distance = distance / outConnectors.size() / 2f;
        float connectorHalfSize = (float) NodeConnector.PREFERED_SIZE.height / 2f;
        
        int i = 1;
        for (NodeOutputConnector connector : outConnectors.values()) {
            connector.setLocation(
                        this.getWidth() - connector.getWidth(),
                        Math.round(i * distance - connectorHalfSize) + titleHeight);
            i += 2;
        }
    }

    /**
     * Refreshed the position of all input nodes. 
     * So they are eqally distributed over the node.
     */
    private void refreshInputConnectors() {
        if (inConnectors == null) return;
        
        int titleHeight = isMainNode ? 0 : lblNodeName.getHeight();
        float distance = this.getHeight() - titleHeight;
        distance = distance / inConnectors.size() / 2f;
        float connectorHalfSize = (float) NodeConnector.PREFERED_SIZE.height / 2f;
        
        int i = 1;
        for (NodeInputConnector connector : inConnectors.values()) {
            connector.setLocation(connector.getX(), Math.round(i * distance - connectorHalfSize) + titleHeight);
            i += 2;
        }
    }
    
    /**
     * Sets the parent of this Node. The GraphPanel
     * @param graph  paret of this GraphNode
     */
    public void setNodeGraphPanel(NodeGraphPanel graph) {
        this.graph = graph;
        if (outConnectors != null)
            for (NodeOutputConnector connector : outConnectors.values())
                connector.setParentNode(this);
        if (inConnectors != null)
            for (NodeInputConnector connector : inConnectors.values())
                connector.setParentNode(this);
    }
    
    /**
     * Retrieves the inputconnector attached to this node registered und the
     * given name.
     * @param name Name of the inputConnector
     * @return inputconnector attached to this node registered und the
     *              given name.
     */
    public NodeInputConnector getInputConnector(String name) {
        return inConnectors.get(name);
    }
    
    /**
     * Retrieves the outputconnector attached to this node registered und the
     * given name.
     * @param name Name of the outputConnector
     * @return outputconnector attached to this node registered und the
     *              given name.
     */
    public NodeOutputConnector getOutputConnector(String name) {
        return outConnectors.get(name);
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resizeComponents();
    }

    public void setSize(Dimension d) {
        super.setSize(d);
        resizeComponents();
    }
    
    public void paint(Graphics g) {
        Rectangle bounds = this.getBounds();
        int headerHeight = lblNodeName.getHeight();

        g.setColor(this.getBackground());
        g.fillRoundRect(0, 0, bounds.width, bounds.height, 8, 8);
        g.setColor(this.lighterColor);
        
        // Draw Header only if it's not one of the main nodes
        if (!isMainNode) {
            g.clipRect(0, 0, bounds.width, headerHeight);
            g.fillRoundRect(1, 1, bounds.width - 2, bounds.height - 2, 8, 8);
            g.drawLine(1, headerHeight - 1, bounds.width - 2, headerHeight -1);

            // reset clipRect
            g.setClip(null);// clipRect(0, 0, bounds.width, bounds.height);
            
            g.setColor(this.darkerColor);
            g.drawLine(1, headerHeight, bounds.width - 2, headerHeight);
        }
        super.paint(g);
        
        if (hasFocus) {
            g.setColor(hightlightColor);
            g.drawRoundRect(0, 0, bounds.width - 1, bounds.height - 1, 8, 8);
            g.setColor(hightlightColorInner);
            g.drawRoundRect(1, 1, bounds.width - 2, bounds.height - 2, 8, 8);
        }
    }

    public void paintOnGraph(Graphics g) {
        Rectangle bounds = this.getBounds();
        bounds.x += 2;
        bounds.y += 2;

        g.setColor(shadowColor);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 8, 8);
        
        if (inConnectors != null) {
            for (NodeInputConnector connection : this.inConnectors.values()) {
                connection.paintOnGraph(g);
            }
        }
        if (outConnectors != null) {
            for (NodeOutputConnector connection : this.outConnectors.values()) {
                connection.paintOnGraph(g);
            }
        }
    }
    
    private void resizeComponents() {
        refreshInputConnectors();
        refreshOuputConnectors();
        refreshContent();
        
        if (lastDimension != null && lastDimension.equals(this.getPreferredSize())) return;
        
        if (!this.isMainNode)
            this.lblNodeName.setSize(getWidth(), lblNodeName.getPreferredSize().height);

        lastDimension = this.getPreferredSize();
        
        // Since the size has changed the shadow of this node needs to be repainted
        if (this.graph != null)
            this.graph.repaint();
    }
    
    protected void refreshContent() {
        if (nodeContent == null) return;
        Dimension newSize = new Dimension();
        newSize.height = this.getHeight() - this.lblNodeName.getHeight();
        newSize.width = this.getWidth() - (2 * NodeConnector.PREFERED_SIZE.width);
        nodeContent.setSize(newSize);
        nodeContent.setLocation(NodeConnector.PREFERED_SIZE.width, lblNodeName.getHeight() + 1);
    }

    /**
     * 
     * @return 
     */
    public NodeGraphPanel getGraph() {
        return graph;
    }

    /**
     * 
     * @param graph 
     */
    public void setGraph(NodeGraphPanel graph) {
        this.graph = graph;
    }

    public void setIsMainNode(boolean isMainNode) {
        if (isMainNode) {
            remove(lblNodeName);
        } else {
            add(lblNodeName);
        }
        this.isMainNode = isMainNode;
        adjustPreferedSize();
    }

    public boolean isIsMainNode() {
        return isMainNode;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
        repaint();
    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        if (!isMainNode)
//            saveVisitor.save("location", this.getLocation());
//        saveVisitor.save("name", this.getName());
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        if (!isMainNode)
//            this.setLocation(loadVisitor.loadPoint("location"));
//        this.setName(loadVisitor.loadString("name"));
    }

}
