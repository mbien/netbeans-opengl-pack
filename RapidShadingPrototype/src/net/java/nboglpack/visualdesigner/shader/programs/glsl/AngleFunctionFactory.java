/*
 * AngleFunctionFactory.java
 *
 * Created on June 6, 2007, 11:16 AM
 */

package net.java.nboglpack.visualdesigner.shader.programs.glsl;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgramFactory;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class AngleFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"radians", "degrees"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public AngleFunctionFactory() {
    }

    @Override
    public String[] getVariants() {
        return variants;
    }

    @Override
    public IShaderProgram createShaderProgram() {
        return new AngleFunction("radians");
    }

    @Override
    public IShaderProgram createShaderProgram(String variant) {
        return new AngleFunction(variant);
    }

    @Override
    public String getName() {
        return "Angle Functions";
    }

    @Override
    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class AngleFunction extends ShaderNode implements IShaderProgram {
    
    public static final byte FUNCTION_ANGLE_RADIANS = 0;
    public static final byte FUNCTION_ANGLE_DEGREES = 2;
    
    private byte functionType;
    
    public AngleFunction(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        this.addInputVariable(new ShaderProgramInVariable(
                "input",
                getName() + " Input",
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addOutputVariable(new ShaderProgramOutVariable(
                "v_" + getName(),
                getName() + " Ouput",
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < AngleFunctionFactory.variants.length; i++) {
            if (AngleFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    
    private static String getFunctionName(int variant) {
        return AngleFunctionFactory.variants[variant];
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                throws ExportingExeption {
        String functionName = getFunctionName(this.functionType);
        return ValueAssignment.createCodeLineAssignment(functionName + "(<input>)",
                new ShaderProgramInVariable[] {getInVariables().get("input")});
    }
    public Class getFactoryClass() {
        return AngleFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}