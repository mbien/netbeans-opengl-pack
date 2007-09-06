/*
 * ModelViewVarsFactory.java
 *
 * Created on June 9, 2007, 8:29 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.util.HashMap;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class ModelViewVarsFactory implements IShaderProgramFactory {
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public ModelViewVarsFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new ModelViewVars();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new ModelViewVars();
    }

    public String getName() {
        return "Model View Variables";
    }

    public Color getNodeBackgroundColor() {
        return UtilShaderPrograms.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class ModelViewVars extends ShaderNode implements IShaderProgram {
    
    private HashMap<String, GlobalVariable> globalVariables;
    private ShaderProgramOutVariable modelVertex;
    private ShaderProgramOutVariable modelNormal;
    
    public ModelViewVars() {
        super("ModelView Variables");
        super.setShaderProgram(this);
        setBackground(UtilShaderPrograms.nodeBackgroundColor);
        setUpVariables();
    }
    protected void initComponents() {
        super.initComponents();
    }
    
    private void setUpVariables() {

        modelVertex = new ShaderProgramOutVariable(
                "modelview_vertex",
                "Modelview Vertex",
                DataType.DATA_TYPE_VEC4,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
            );
        addOutputVariable(modelVertex);
        modelNormal = new ShaderProgramOutVariable(
                "modelview_normal",
                "Modelview Normal",
                DataType.DATA_TYPE_VEC3,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
            );
        addOutputVariable(modelNormal);
        
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
        if (outputVariable == modelVertex) {
            return ValueAssignment.createCodeLineAssignment("gl_ModelViewMatrix * gl_Vertex", null, ValueAssignment.CONTEXT_VERTEX_SHADER);
        } else if (outputVariable == modelNormal) {
            return ValueAssignment.createCodeLineAssignment("normalize(gl_NormalMatrix * gl_Normal)", null, ValueAssignment.CONTEXT_VERTEX_SHADER);
        }
        throw new ExportingExeption("The given output var " + outputVariable.getName() + " should not be attached to this node " + getName());
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return null;
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return true;
    }
    
    public Class getFactoryClass() {
        return ModelViewVarsFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
}
