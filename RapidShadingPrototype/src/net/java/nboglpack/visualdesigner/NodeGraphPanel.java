/*
 * NodeGraphPanel.java
 *
 * Created on April 12, 2007, 4:23 PM
 *
 */

package net.java.nboglpack.visualdesigner;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.java.nboglpack.visualdesigner.PersistanceException;
import net.java.nboglpack.visualdesigner.tools.DirectionSlider;
import net.java.nboglpack.visualdesigner.tools.IRefreshable;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 *
 * @author Samuel Sperling
 */
public class NodeGraphPanel extends JPanel implements IRefreshable, IPersistable {
    
    private boolean DO_SMOOTH_MOVE = true;
    
    private Point lastPosition;
    private Thread refreshThread;
    private HashMap<String, GraphNode> nodes;
    private ArrayList<GraphNode> mainInputNodes;
    private ArrayList<GraphNode> mainOutputNodes;
    private boolean draggingConnector = false;
//    private Settings settings;
    private Point slideSpeed;
    private float nodeSlideFactor;
    private int maxSliderSize;
    
    /** Position of the Nodes in the ComponentCollection */
    private int nodesComponentPosition = 0;
    
    /** Creates a new instance of NodeGraphPanel */
    public NodeGraphPanel() {
        nodes = new HashMap<String, GraphNode>();
        loadSettings();
        initComponents();
        nodesComponentPosition = this.getComponentCount();
        
        // Refresh Thread
        Editor.constantRefresher.attachConsumer(this);
        Editor.constantRefresher.attachConsumer(scrollSliderHorizontal);
        Editor.constantRefresher.attachConsumer(scrollSliderVertical);
        
    }
    
    private void loadSettings() {
//        settings = Editor.mainSettings.getChildSettingsByPath("GraphNode/Style");
        slideSpeed = new Point(
                    /*settings.getAttributeValueInt("SlideSpeed", "x", */50,//),
                    /*settings.getAttributeValueInt("SlideSpeed", "y",*/ 50//)
                );
        nodeSlideFactor = /*settings.getValueFloat("NodeSlideFactor",*/ 0.51f;//);
        maxSliderSize = /*settings.getValueInt("MaxSliderSize",*/ 300;//);
        
    }
    
    private JPanel currentDraggingPanel;
    private JPanel currentHoverPanel;
    private DirectionSlider scrollSliderHorizontal;
    private DirectionSlider scrollSliderVertical;
    
    private void initComponents() {
        this.setLayout(null);
        
        scrollSliderHorizontal = new DirectionSlider();
        scrollSliderHorizontal.addSlidingListener(new Callable() {
            public Object call() throws Exception {
                ScrollHorizontal(scrollSliderHorizontal.getRelativeValue());
                repaint();
                return null;
            }
        });
        this.add(scrollSliderHorizontal);
        
        scrollSliderVertical = new DirectionSlider();
        scrollSliderVertical.setOrientation(DirectionSlider.VERTICAL);
        scrollSliderVertical.addSlidingListener(new Callable() {
            public Object call() throws Exception {
                ScrollVertical(-1f * scrollSliderVertical.getRelativeValue());
                repaint();
                return null;
            }
        });
        this.add(scrollSliderVertical);
    }
    
    private void ScrollHorizontal(float value) {
        if (!nodes.isEmpty()) {
            Point loc;
            for (GraphNode node : nodes.values()) {
                loc = node.getLocation();
                loc.x += Math.round(slideSpeed.x * value);
                node.setLocation(loc);
            }
        }
    }
    
    private void ScrollVertical(float value) {
        if (!nodes.isEmpty()) {
            Point loc;
            for (GraphNode node : nodes.values()) {
                loc = node.getLocation();
                loc.y += Math.round(slideSpeed.y * value);
                node.setLocation(loc);
            }
        }
    }
    
    /**
     * Refreshes the panels
     */
    public void refresh() {
        refreshDragingPosition(DO_SMOOTH_MOVE);
    }

    public boolean needsRefresh() {
        return currentDraggingPanel != null;
    }
    
    private void refreshDragingPosition(boolean smoothMove) {
        if (currentDraggingPanel == null) return;
        
        Rectangle absolutePos = currentDraggingPanel.getBounds();
        Point newPosition = this.getMousePosition();
        if (newPosition == null || absolutePos == null) return;
        
        float moveX = (newPosition.x - lastPosition.x);
        float moveY = (newPosition.y - lastPosition.y);
        
        if (Math.abs(moveX) < 0.5 && Math.abs(moveY) < 0.5)
            return;
        
        if (smoothMove) {
            
            // Problem if SLIDE_FACTOR < 0.5 because absolutePos.x is an int
            // movements smaller than (1 / SLIDE_FACTOR) are not recognized
            absolutePos.x += moveX * nodeSlideFactor;
            absolutePos.y += moveY * nodeSlideFactor;
            currentDraggingPanel.setBounds(absolutePos);

            lastPosition.x += moveX * nodeSlideFactor;
            lastPosition.y += moveY * nodeSlideFactor;
        } else {
            absolutePos.x += moveX;
            absolutePos.y += moveY;
            currentDraggingPanel.setBounds(absolutePos);

            lastPosition = newPosition;
        }
        repaint();
    }
    
    public void addNode(GraphNode newNode) {
        addNode(newNode, false);
    }
    
    /**
     * Confirms the uniqueness of this nodes name.
     * Changes it if it's already in use.
     */
    private void confirmName(GraphNode newNode) {
        String baseName = newNode.getName();
        if (!this.nodes.containsKey(baseName)) return;
        
        // Name is already in use. Find a new one.
        int i = 2;
        while(this.nodes.containsKey(baseName + " " + i))
            i++;
        
        newNode.setName(baseName + " " + i);
    }
    
    /**
     * Adds a Node to this NodeGraphPanel.
     */
    public void addNode(GraphNode newNode, boolean dragging) {
        confirmName(newNode);
        this.nodes.put(newNode.getName(), newNode);

        newNode.setNodeGraphPanel(this);
        this.add(newNode, nodesComponentPosition);
        newNode.setVisible(true);
        newNode.setBounds(0, 0, newNode.getPreferredSize().width, newNode.getPreferredSize().height);

        newNode.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    startDragging((GraphNode) e.getSource());
            }
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    stopDragging((GraphNode) e.getSource());
            }
        });
        if (dragging)
            prepareDragging(newNode);
    }
    
    /**
     * The Node was inserted for dragging purpose
     * now the mouse needs to be placed at the node position
     * and a mouseclick needs to be simulated
     */
    private void prepareDragging(GraphNode newNode) {
        try {
            Robot rob = new Robot();
            Point newMousePos = newNode.getLocation();
            newMousePos.x += newNode.getWidth() / 2;
            newMousePos.y += newNode.getHeight() / 2;
            SwingUtilities.convertPointToScreen(newMousePos, this);
            rob.mouseMove(newMousePos.x, newMousePos.y);
            rob.mousePress(InputEvent.BUTTON1_MASK);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeNode(String nodeName) {
        GraphNode removeNode = this.nodes.remove(nodeName);
        this.remove(removeNode);
        removeNode.setNodeGraphPanel(null);
        removeNode.delete();
        if (lastActiveNode == removeNode)
            lastActiveNode = null;
        repaint();
    }
    
    private void startDragging(JPanel panel) {
        currentDraggingPanel = panel;
        lastPosition = this.getMousePosition();
    }
    
    private boolean stopDragging(JPanel panel) {
        if (this.currentDraggingPanel != panel) return false;
        refreshDragingPosition(false);
        currentDraggingPanel = null;
        repaint();
        return true;
    }
    
    private GraphNode lastActiveNode;
            
    public void setNewActiveNode(GraphNode newActiveNode) {
        if (lastActiveNode != null) {
            if (lastActiveNode == newActiveNode) return;
            lastActiveNode.setHasFocus(false);
        }
        newActiveNode.setHasFocus(true);
        lastActiveNode = newActiveNode;
    }
    
    /**
     * Activates draggging functionality
     *
     * @param draggableConnector  connector that should be dragged to another
     *  connector to establish a connection. The connector will be attached to
     *  the this components collection during dragging time. So it should not be
     *  component-collection of any other component.
     */
    public void startConnectorDragging(NodeConnector draggableConnector) {
        draggableConnector.setVisible(true);
        this.add(draggableConnector);
        draggableConnector.graph = this;
        draggingConnector = true;
        startDragging(draggableConnector);
    }
    
    public void stopConnectorDragging(NodeConnector draggableConnector) {
        this.remove(draggableConnector);
        stopDragging(draggableConnector);
        draggingConnector = false;
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resizeComponents();
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        resizeComponents();
    }

    private void resizeComponents() {
        
        // Arrange Components
        
        // Sliders
        Rectangle bounds = this.getBounds();
        Rectangle sliderBounds = new Rectangle(bounds);
        
        // Horizontal Slider
        sliderBounds.width = bounds.width - 10;
        if (sliderBounds.width > maxSliderSize) sliderBounds.width = maxSliderSize;
        sliderBounds.height = (int) scrollSliderHorizontal.getPreferredSize().getHeight();
        sliderBounds.x = (bounds.width / 2) - (sliderBounds.width / 2);
        sliderBounds.y = bounds.height - sliderBounds.height - 5;
        
        scrollSliderHorizontal.setBounds(sliderBounds);
        
        // Vertical Slider
        sliderBounds.height = bounds.height - 10;
        if (sliderBounds.height > maxSliderSize) sliderBounds.height = maxSliderSize;
        sliderBounds.width = 23;
        sliderBounds.y = (bounds.height / 2) - (sliderBounds.height / 2);
        sliderBounds.x = bounds.width - sliderBounds.width - NodeConnector.PREFERED_SIZE.width - 5;
        
        scrollSliderVertical.setBounds(sliderBounds);
        
        // MainNodes
        resizeMainInputNode();
        resizeMainOutputNode();
    }
    
    private void resizeMainInputNode() {
        if (mainInputNodes != null && mainInputNodes.size() > 0) {
            float nodeSize = (float) getSize().height / (float) mainInputNodes.size();
            int i = 0;
            for (GraphNode mainInputNode : mainInputNodes) {
                mainInputNode.setBounds(mainInputNode.getPreferredSize().width / -2 + this.getSize().width, (int) (nodeSize * i), mainInputNode.getPreferredSize().width, (int) nodeSize);
                i++;
            }
        }
    }
    private void resizeMainOutputNode() {
        if (mainOutputNodes != null && mainOutputNodes.size() > 0) {
            float nodeSize = (float) getSize().height / (float) mainOutputNodes.size();
            int i = 0;
            for (GraphNode mainOutputNode : mainOutputNodes) {
                mainOutputNode.setBounds(mainOutputNode.getPreferredSize().width / -2, (int) (nodeSize * i), mainOutputNode.getPreferredSize().width, (int) nodeSize);
                i++;
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (draggingConnector && this.currentDraggingPanel instanceof NodeInputConnector) {
            NodeInputConnector connector = (NodeInputConnector) this.currentDraggingPanel;
            connector.paintOnGraph(g);
        }
        
        if (!nodes.isEmpty()) {
            for (GraphNode node : nodes.values()) {
                node.paintOnGraph(g);
            }
        }
        if (mainInputNodes != null && mainInputNodes.size() > 0)
            for (GraphNode mainInputNode : mainInputNodes)
                mainInputNode.paintOnGraph(g);
        if (mainOutputNodes != null && mainOutputNodes.size() > 0)
            for (GraphNode mainOutputNode : mainOutputNodes)
                mainOutputNode.paintOnGraph(g);
    }
    
    
    public JPanel getDraggingPanel() {
        return this.currentDraggingPanel;
    }

    public boolean isDraggingConnector() {
        return draggingConnector;
    }
    
    public void setCurrentHoverPanel(JPanel currentHoverPanel) {
        this.currentHoverPanel = currentHoverPanel;
    }
    
    public JPanel getCurrentHoverPanel() {
        return this.currentHoverPanel;
    }

    /**
     * Unsets the currentHoverPanel in case it's still the same as the
     *  submitted value;
     */
    public void unsetCurrentHoverPanel(NodeConnector nodeConnector) {
        if (this.currentHoverPanel == currentHoverPanel)
            this.currentHoverPanel = null;
    }

    public ArrayList<GraphNode> addMainInputNodes() {
        return mainInputNodes;
    }

    public void addMainInputNode(GraphNode mainInputNode) {
        if (this.mainInputNodes == null) {
            mainInputNodes = new ArrayList<GraphNode>();
        }
        add(mainInputNode, nodesComponentPosition);
        mainInputNodes.add(mainInputNode);
        mainInputNode.setNodeGraphPanel(this);
        mainInputNode.setVisible(true);
        mainInputNode.setIsMainNode(true);
        resizeMainInputNode();
//        mainInputNode.setBounds(mainInputNode.getPreferredSize().width / -2, 0, mainInputNode.getPreferredSize().width, this.getSize().height);
    }

    public ArrayList<GraphNode> getMainOutputNode() {
        return mainOutputNodes;
    }

    public void addMainOutputNode(GraphNode mainOutputNode) {
        if (this.mainOutputNodes == null) {
            mainOutputNodes = new ArrayList<GraphNode>();
        }
        add(mainOutputNode, nodesComponentPosition);
        mainOutputNodes.add(mainOutputNode);
        mainOutputNode.setNodeGraphPanel(this);
        mainOutputNode.setVisible(true);
        mainOutputNode.setIsMainNode(true);
        resizeMainOutputNode();
//        mainOutputNode.setBounds(mainOutputNode.getPreferredSize().width / -2 + this.getSize().width, 0, mainOutputNode.getPreferredSize().width, this.getSize().height);
    }

    public GraphNode getNode(String nodeName) {
        GraphNode graphNode = this.nodes.get(nodeName);
        if (graphNode != null) return graphNode;
        
        for (GraphNode node : this.mainInputNodes)
            if (node.getName().equals(nodeName))
                return node;

        for (GraphNode node : this.mainOutputNodes)
            if (node.getName().equals(nodeName))
                return node;

        return null;
    }

    public GraphNode getActiveNode() {
        return this.lastActiveNode;
    }

    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        saveVisitor.save("nodes", this.nodes);
//        saveVisitor.save("maininputnodes", this.mainInputNodes);
//        saveVisitor.save("mainoutputnodes", this.mainOutputNodes);
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
        // delete existing nodes
        clear();
        
        // load nodes
//        loadVisitor.loadGraphNodes("nodes", this);
//        loadVisitor.loadArrayList("maininputnodes", this.mainInputNodes);
//        loadVisitor.loadArrayList("mainoutputnodes", this.mainOutputNodes);
        this.repaint();
    }
    
    public void clear() {

        Object[] nodeNames = nodes.keySet().toArray();
        for (int i = 0; i < nodeNames.length; i++) {
            removeNode((String) nodeNames[i]);
        }
    }
}
