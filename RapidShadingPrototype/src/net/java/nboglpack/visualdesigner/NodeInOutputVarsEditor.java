/*
 * NodeInOutputVarsEditor.java
 *
 * Created on May 4, 2007, 6:02 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Editor that allows the user to edit in & output variables and gives
 * the user the choice to e.g. direct information to globals or to be edited
 * within the NodeEditor.
 *
 * @author Samuel Sperling
 */
public class NodeInOutputVarsEditor extends JPanel {
    
    private NodeGraphPanel graph;
    private JPanel currentInOutputVarsPanel;
    private JPanel defaultPanel;
    
    /**
     * Creates a new instance of NodeInOutputVarsEditor
     */
    public NodeInOutputVarsEditor(NodeGraphPanel graph) {
        this.graph = graph;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        defaultPanel = new JPanel();
        defaultPanel.add(new JLabel("No In & Output Vars available"));
        addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                refresh();
            }
        });
    }

    
    public void refresh() {
        JPanel InOutputVarsPanel = null;
        ShaderNode shaderNode = (ShaderNode) graph.getActiveNode();
        if (shaderNode != null)
            InOutputVarsPanel = shaderNode.getVariableSourceEditor();
        if (currentInOutputVarsPanel != null)
            this.remove(currentInOutputVarsPanel);
        if (InOutputVarsPanel == null)
            InOutputVarsPanel = this.defaultPanel;
        
        currentInOutputVarsPanel = InOutputVarsPanel;
        
        add(InOutputVarsPanel, BorderLayout.CENTER);
        
    }
    
}
