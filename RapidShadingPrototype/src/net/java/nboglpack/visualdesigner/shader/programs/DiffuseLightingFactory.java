/*
 * DiffuseLightingFactory.java
 *
 * Created on June 9, 2007, 10:59 PM
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

/**
 *
 * @author Samuel Sperling
 */
public class DiffuseLightingFactory implements IShaderProgramFactory {
    
    /**
     * Creates a new instance of DiffuseLightingFactory
     */
    public DiffuseLightingFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new DiffuseLighting();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new DiffuseLighting();
    }

    public String getName() {
        return "Diffuse Lighting";
    }

    public Color getNodeBackgroundColor() {
        return LightingShaderPrograms.nodeBackgroundColor;
    }
}

/**
 *
 * @author Samuel Sperling
 */
class DiffuseLighting extends ShaderNode implements IShaderProgram {
    
    public DiffuseLighting() {
        super("Diffuse Lighting");
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
        code += "return clamp(gl_FrontLightProduct[0].diffuse * max(dot(normal,L), 0.0), 0.0, 1.0);\n";
        return ValueAssignment.createFunctionAssignment("DiffuseComplete", code, getSources(outputVariable, exportVisitor), ValueAssignment.CONTEXT_FRAGMENT_SHADER);
    }
    public Class getFactoryClass() {
        return DiffuseLightingFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
}
