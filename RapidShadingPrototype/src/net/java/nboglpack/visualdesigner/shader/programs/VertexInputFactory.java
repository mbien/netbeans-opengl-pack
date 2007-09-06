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
import net.java.nboglpack.visualdesigner.shader.exporter.UnsupportedShaderExporterException;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 * Creates node to simulate the hardware-interface.
 *
 * @author Samuel Sperling
 */
public class VertexInputFactory implements IShaderProgramFactory {
    
    /** Creates a new instance of VertexOutputFactory */
    public VertexInputFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new VertexInputShader();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new VertexInputShader();
    }

    public String getName() {
        return "Hadware Interface for Vertex Input";
    }

    public Color getNodeBackgroundColor() {
        return HardwareInterfaces.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class VertexInputShader extends ShaderNode implements IShaderProgram {
    
    private HashMap<String, GlobalVariable> globalVariables;
    
    public VertexInputShader() {
        super("Hadware Interface for Vertex Input");
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
                new String[] {"Position"},
                new String[] {"Vertex Position"},
                new int[] {DataType.DATA_TYPE_VEC4},
                GlobalVariable.GLOBAL_HARDWARE_VERTEX_OUT,
                true,
                false
            );
    }
    
    public void exportShaderCode(IShaderCodeExportVisitor shaderCodeExporter)
        throws UnsupportedShaderExporterException {
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption {
        if (outputVariable.getName().equals("Position"))
            return ValueAssignment.createCodeLineAssignment("ftransform()", null);
        else
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to VertexInputShader");
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return null;
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return false;
    }
    public Class getFactoryClass() {
        return VertexInputFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
}
