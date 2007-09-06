/*
 * VertexOutputFactory.java
 *
 * Created on April 30, 2007, 11:56 AM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.util.HashMap;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 *
 * @author Samuel Sperling
 */
public class FragmentOutputFactory implements IShaderProgramFactory {
    
    /** Creates a new instance of VertexOutputFactory */
    public FragmentOutputFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new FragmentOutput();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new FragmentOutput();
    }

    public String getName() {
        return "Hadware Interface for Fragment output";
    }

    public Color getNodeBackgroundColor() {
        return HardwareInterfaces.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class FragmentOutput extends ShaderNode implements IShaderProgram {
    
    private HashMap<String, GlobalVariable> globalVariables;
    
    public FragmentOutput() {
        super("Hadware Interface for Fragment output");
        super.setShaderProgram(this);
        setBackground(HardwareInterfaces.nodeBackgroundColor);
        setUpVariables();
    }
    protected void initComponents() {
        super.initComponents();
        this.contextMenu.remove(mnuDelete);
    }
    private void setUpVariables() {
        
        globalVariables = HardwareInterfaces.setupVars(
                this,
                new String[] {"FragCoord", "FrontFacing"},
                new String[] {"Fragment Coords", "Is Front Facing"},
                new int[] {DataType.DATA_TYPE_VEC4, DataType.DATA_TYPE_BOOL},
                GlobalVariable.GLOBAL_HARDWARE_FRAGMENT_IN,
                false,
                false
                );
        
//        // Add Globals
//        globalVariables = new HashMap<String, GlobalVariable>(2);
//        GlobalVariable globalVar;
//        
//        globalVar = new GlobalVariable("gl_FragCoord", DataType.DATA_TYPE_VEC4, GlobalVariable.GLOBAL_HARDWARE_FRAGMENT_IN);
//        Editor.globalVariables.add(globalVar);
//        globalVariables.put("gl_FragCoord", globalVar);
//        
//        globalVar = new GlobalVariable("gl_FrontFacing", DataType.DATA_TYPE_BOOL, GlobalVariable.GLOBAL_HARDWARE_FRAGMENT_IN);
//        Editor.globalVariables.add(globalVar);
//        globalVariables.put("gl_FrontFacing", globalVar);
//        
//        // Add OutputVars
//        this.addOutputVariable(new ShaderProgramOutVariable(
//                "FragCoord",
//                "Fragment Coord",
//                DataType.DATA_TYPE_VEC4,
//                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
//                ));
//        this.addOutputVariable(new ShaderProgramOutVariable(
//                "FrontFacing",
//                "Is Front Facing",
//                DataType.DATA_TYPE_BOOL,
//                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
//                ));
    }
    
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
        GlobalVariable globalVar = globalVariables.get("gl_" + outputVariable.getName());
        if (globalVar == null)
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to Vertex2FragmentTransfer");
        
        return ValueAssignment.createVariableRedirectionAssignment(globalVar);
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        GlobalVariable globalVar = globalVariables.get("gl_" + outputVariable.getName());
        if (globalVar == null)
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to Vertex2FragmentTransfer");
        
        return new IExportable[] {globalVar};
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return true;
    }
    public Class getFactoryClass() {
        return FragmentOutputFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
}
