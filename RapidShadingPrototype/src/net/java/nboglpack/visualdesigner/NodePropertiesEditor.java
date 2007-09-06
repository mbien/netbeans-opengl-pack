/*
 * NodePropertiesEditor.java
 *
 * Created on May 4, 2007, 4:06 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Samuel Sperling
 */
public class NodePropertiesEditor extends JPanel {
    
    private NodeGraphPanel graph;
    private JPanel currentPropertiesPanel;
    private JPanel defaultPanel;
    
    /**
     * Creates a new instance of NodePropertiesEditor
     */
    public NodePropertiesEditor(NodeGraphPanel graph) {
        this.graph = graph;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        defaultPanel = new JPanel();
        defaultPanel.add(new JLabel("No Properties available"));
        defaultPanel.setVisible(true);
    }
    
    public void refresh() {
        JPanel propertiesPanel = null;
        ShaderNode shaderNode = (ShaderNode) graph.getActiveNode();
        if (shaderNode != null)
            propertiesPanel = shaderNode.getPropertiesPanel();
        if (currentPropertiesPanel != null)
            this.remove(currentPropertiesPanel);
        if (propertiesPanel == null)
            propertiesPanel = this.defaultPanel;
        currentPropertiesPanel = propertiesPanel;
        
        add(propertiesPanel, BorderLayout.CENTER);
        
    }
    
}
