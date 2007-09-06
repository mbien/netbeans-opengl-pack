/*
 * ExponentialFunctionFactory.java
 *
 * Created on June 6, 2007, 11:24 AM
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
public class ExponentialFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"pow", "exp", "log", "exp2", "log2", "sqrt", "inversesqrt"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public ExponentialFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new ExponentialFunction("pow");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new ExponentialFunction(variant);
    }

    public String getName() {
        return "Exponential Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class ExponentialFunction extends ShaderNode implements IShaderProgram {
    
    public static final byte FUNCTION_EXPOENTIAL_POW = 0;
    public static final byte FUNCTION_EXPOENTIAL_EXP = 1;
    public static final byte FUNCTION_EXPOENTIAL_LOG = 2;
    public static final byte FUNCTION_EXPOENTIAL_EXP2 = 3;
    public static final byte FUNCTION_EXPOENTIAL_LOG2 = 4;
    public static final byte FUNCTION_EXPOENTIAL_SQRT = 5;
    public static final byte FUNCTION_EXPOENTIAL_INVERSESQRT = 6;
    
    private byte functionType;
    
    public ExponentialFunction(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        if (functionType == FUNCTION_EXPOENTIAL_POW) {
            this.addInputVariable(new ShaderProgramInVariable(
                    "raised",
                    "X Raised",
                    DataType.DATA_TYPE_GENTYPE,
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
            this.addInputVariable(new ShaderProgramInVariable(
                    "power",
                    "Y Power",
                    DataType.DATA_TYPE_GENTYPE,
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
        } else {
            this.addInputVariable(new ShaderProgramInVariable(
                    "input",
                    getName() + " Input",
                    DataType.DATA_TYPE_GENTYPE,
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
        }
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
        for (int i = 0; i < ExponentialFunctionFactory.variants.length; i++) {
            if (ExponentialFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getFunctionName(int variant) {
        return ExponentialFunctionFactory.variants[variant];
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
        if (functionType == FUNCTION_EXPOENTIAL_POW) {
            return ValueAssignment.createCodeLineAssignment(functionName + "(<raised>, <power>)",
                    getSources(outputVariable, exportVisitor));
        } else {
            return ValueAssignment.createCodeLineAssignment(functionName + "(<input>)",
                    getSources(outputVariable, exportVisitor));
        }
    }
    public Class getFactoryClass() {
        return ExponentialFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}