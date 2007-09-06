/*
 * ShaderNode.java
 *
 * Created on April 28, 2007, 6:47 PM
 *
 */

package net.java.nboglpack.visualdesigner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.PersistanceException;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariablesCollection;

/**
 * Extends the functionality of a GraphNode to a particular ShaderNode
 * = providing functions shader needs.
 *
 * @author Samuel Sperling
 */
public class ShaderNode extends GraphNode {
    
    protected VariablesCollection<ShaderProgramInVariable> inVariables;
    protected VariablesCollection<ShaderProgramOutVariable> outVariables;
    private VariableSourceEditor variableSourceEditor;
    private IShaderProgram shaderProgram;
    
    /** Creates a new instance of ShaderNode */
    public ShaderNode(String name) {
        super(name);
    }
    
    protected void initComponents() {
        super.initComponents();
        
        // PropertiesWindows
        variableSourceEditor = new VariableSourceEditor(this);
        
        // ContextMenuItems
        contextMenu.add(new JSeparator());
        
        JMenuItem mnuInAndOutputEditor = new JMenuItem("Edit In- & Ouput Vars");
        mnuInAndOutputEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openNodeInOutputVarsPanel();
            }
        });
        mnuInAndOutputEditor.setBorder(null);
        contextMenu.add(mnuInAndOutputEditor);
        
        JMenuItem mnuProperties = new JMenuItem("Properties");
        mnuProperties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openNodePropertiesPanel();
            }
        });
        mnuProperties.setBorder(null);
        contextMenu.add(mnuProperties);
        
        
        // ContextMenu on rightclick
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                if ( me.isPopupTrigger() )
                    contextMenu.show( me.getComponent(), me.getX(), me.getY() );
            }
        });
    }
    
    private void openNodeInOutputVarsPanel() {
        Editor.findMe(this).showNodeInOutputVarsPanel();
    }
    
    private void openNodePropertiesPanel() {
        Editor.findMe(this).showNodePropertiesPanel();
    }
    
    public void addInputVariable(ShaderProgramInVariable newInVar) {
        getInVariables().add(newInVar);
        newInVar.setShaderNode(this);
    }
    
    public void addOutputVariable(ShaderProgramOutVariable newOutVar) {
        getOutVariables().add(newOutVar);
        newOutVar.setShaderNode(this);
//        variableSourceEditor.variableAdded(newInVar); //updateData();
    }
    
    public void removeVariable(ShaderProgramOutVariable itemToRemove) {
        outVariables.remove(itemToRemove);
    }
    
    public void removeVariable(ShaderProgramInVariable itemToRemove) {
        inVariables.remove(itemToRemove);
    }
    
    public VariableSourceEditor getVariableSourceEditor() {
        return variableSourceEditor;
    }
    
    public JPanel getPropertiesPanel() {
        return null;
    }
    
    public int resolveOutputDataType(String name) throws ExportingExeption {
        // Called from an InputVar
        ShaderProgramOutVariable var =  outVariables.get(name);
        if (var == null)
            throw new ExportingExeption("Inconsistency in NodeConnectors <-> ShaderProgramVariable");
        return var.resolveDataType();
    }
    
    public int resolveOutputDataType(ShaderProgramOutVariable shaderProgramVariable) throws ExportingExeption {

        ShaderProgramInVariable var = inVariables.get(0);
        if (var == null)
            throw new ExportingExeption("OutputDataType unresolvable");
        return var.resolveDataType();
    }
    
    public IShaderProgram getShaderProgram() {
        return shaderProgram;
    }
    
    public void setShaderProgram(IShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public VariablesCollection<ShaderProgramInVariable> getInVariables() {
        if (inVariables == null) inVariables = new VariablesCollection<ShaderProgramInVariable>();
        return inVariables;
    }

    public VariablesCollection<ShaderProgramOutVariable> getOutVariables() {
        if (outVariables == null) outVariables = new VariablesCollection<ShaderProgramOutVariable>();
        return outVariables;
    }

    public ValueAssignment export(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
        return this.shaderProgram.exportShaderCode(outputVariable, exportVisitor);
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        IExportable[] sources = new IExportable[getInVariables().size()];
        for (int i = 0; i < inVariables.size(); i++)
            sources[i] = inVariables.get(i);
        return sources;
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return inVariables == null || getInVariables().size() > 0;
    }

    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        super.saveState(saveVisitor);
//        saveVisitor.save("invariables", this.inVariables);
//        saveVisitor.save("outvariables", this.outVariables);
        //TODO: save In-OutVars
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        super.loadState(loadVisitor);
//        loadVisitor.loadVariablesCollection("invariables", this.inVariables);
//        loadVisitor.loadVariablesCollection("outvariables", this.outVariables);
        //TODO: load in-outVars
    }

}
