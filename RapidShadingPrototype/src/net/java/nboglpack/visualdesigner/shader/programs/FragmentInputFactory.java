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
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 *
 * @author Samuel Sperling
 */
public class FragmentInputFactory implements IShaderProgramFactory {
    
    /** Creates a new instance of VertexOutputFactory */
    public FragmentInputFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new FragmentInputShader();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new FragmentInputShader();
    }

    public String getName() {
        return "Hadware Interface for Fragment Input";
    }

    public Color getNodeBackgroundColor() {
        return HardwareInterfaces.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class FragmentInputShader extends ShaderNode implements IShaderProgram {
    
    private HashMap<String, GlobalVariable> globalVariables;
    private ShaderProgramOutVariable modelVertex;
    
    public FragmentInputShader() {
        super("Hadware Interface for Fragment Input");
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
                new String[] {"FragColor", "FragDepth"},
                new String[] {"Fragment Color", "Fragment Depth"},
                new int[] {DataType.DATA_TYPE_VEC4, DataType.DATA_TYPE_VEC4},
                GlobalVariable.GLOBAL_HARDWARE_FRAGMENT_OUT,
                true,
                true
                );
        
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
//        if (outputVariable == modelVertex) {
//            return ValueAssignment.createVariableRedirectionAssignment(Editor.globalVariables.get("gl_Vertex"));
//        }
        ShaderProgramInVariable inVar =  this.getInVariables().get(outputVariable.getName());
        if (inVar == null) {
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to FragmentInputShader");
        }
        return ValueAssignment.createVariableRedirectionAssignment(inVar);
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
//        if (outputVariable == modelVertex) {
//            return new IExportable[] {Editor.globalVariables.get("gl_Vertex")};
//        }
        ShaderProgramInVariable inVar =  this.getInVariables().get(outputVariable.getName());
        if (inVar == null) {
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to FragmentInputShader");
        }
        return new IExportable[] {inVar};        
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return true;
    }
    
    public Class getFactoryClass() {
        return FragmentInputFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
}
