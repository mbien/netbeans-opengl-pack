/*
 * SpecularLightingFactory.java
 *
 * Created on June 9, 2007, 11:00 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 *
 * @author Samuel Sperling
 */
public class SpecularLightingFactory implements IShaderProgramFactory {
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public SpecularLightingFactory() {
    }
    
    public String[] getVariants() {
        return null;
    }
    
    public IShaderProgram createShaderProgram() {
        return new SpecularLighting();
    }
    
    public IShaderProgram createShaderProgram(String variant) {
        return new SpecularLighting();
    }
    
    public String getName() {
        return "Specular Lighting";
    }
    
    public Color getNodeBackgroundColor() {
        return LightingShaderPrograms.nodeBackgroundColor;
    }
    
}


/**
 *
 * @author Samuel Sperling
 */
class SpecularLighting extends ShaderNode implements IShaderProgram {
    
    private byte functionType;
    
    public SpecularLighting() {
        super("Specular Lighting");
        super.setShaderProgram(this);
        setBackground(LightingShaderPrograms.nodeBackgroundColor);
        
        setUpVariables();
    }
    
    private void setUpVariables() {
        
        this.addOutputVariable(new ShaderProgramOutVariable(
                "diffuse",
                "Diffuse Shading",
                DataType.DATA_TYPE_VEC4,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addInputVariable(new ShaderProgramInVariable(
                "vertex",
                "eye Vertex",
                DataType.DATA_TYPE_VEC3,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addInputVariable(new ShaderProgramInVariable(
                "normal",
                "eye Normal",
                DataType.DATA_TYPE_VEC3,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        ShaderProgramInVariable inVar = new ShaderProgramInVariable(
                "shininess",
                "Shininess",
                DataType.DATA_TYPE_FLOAT,
                ShaderProgramInVariable.VALUE_SOURCE_CONST
                );
        VariableValue vv = new VariableValue();
        vv.setDataType(DataType.DATA_TYPE_VEC1);
        vv.setValue(0, 16f);
        inVar.setVariableValue(vv);
        this.addInputVariable(inVar);
    }
    
    public ShaderNode getShaderNode() {
        return this;
    }
    
    public JPanel getProperiesPanel() {
        return null;
    }
    
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
    throws ExportingExeption {
        String code = "";
        code += "vec3 L = normalize(gl_LightSource[0].position.xyz - vertex);\n";
        code += "vec3 E = normalize(-vertex);\n";
        code += "vec3 R = normalize(-reflect(L,normal));\n";
        code += "return clamp(gl_FrontLightProduct[0].specular * pow(max(dot(R, E), 0.0), 0.3 * shininess), 0.0, 1.0);";
        return ValueAssignment.createFunctionAssignment("Specular", code, getSources(outputVariable, exportVisitor), ValueAssignment.CONTEXT_FRAGMENT_SHADER);
    }
    public Class getFactoryClass() {
        return SpecularLightingFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
}