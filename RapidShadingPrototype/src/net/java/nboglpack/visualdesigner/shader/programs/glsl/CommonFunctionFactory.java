/*
 * CommonFunctionFactory.java
 *
 * Created on June 6, 2007, 11:58 AM
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
public class CommonFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"abs", "ceil", "clamp", "floor", "fract", "max", "min", "mix", "mod", "sign", "smoothstep", "step"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public CommonFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new CommonFunction("abs");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new CommonFunction(variant);
    }

    public String getName() {
        return "Common Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class CommonFunction extends ShaderNode implements IShaderProgram {
    
    // Should be indexes of CommonFunctionFactory.variants
    public static final byte FUNCTION_COMMON_ABS = 0;
    public static final byte FUNCTION_COMMON_CEIL = 1;
    public static final byte FUNCTION_COMMON_CLAMP = 2;
    public static final byte FUNCTION_COMMON_FLOOR = 3;
    public static final byte FUNCTION_COMMON_FRACT = 4;
    public static final byte FUNCTION_COMMON_MAX = 5;
    public static final byte FUNCTION_COMMON_MIN = 6;
    public static final byte FUNCTION_COMMON_MIX = 7;
    public static final byte FUNCTION_COMMON_MOD = 8;
    public static final byte FUNCTION_COMMON_SIGN = 9;
    public static final byte FUNCTION_COMMON_SMOOTHSTEP = 10;
    public static final byte FUNCTION_COMMON_STEP = 11;
    
    private byte functionType;
    
    public CommonFunction(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        for (int i = 0; i < getInputVarCount(); i++) {
            this.addInputVariable(new ShaderProgramInVariable(
                    getInputVarName(i),
                    getName() + " " + getInputVarName(i),
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

    private int getInputVarCount() {
        switch(this.functionType) {
            case FUNCTION_COMMON_ABS:
            case FUNCTION_COMMON_SIGN:
            case FUNCTION_COMMON_FLOOR:
            case FUNCTION_COMMON_CEIL:
            case FUNCTION_COMMON_FRACT:
                return 1;
            case FUNCTION_COMMON_MOD:
            case FUNCTION_COMMON_MIN:
            case FUNCTION_COMMON_MAX:
            case FUNCTION_COMMON_STEP:
                return 2;
            case FUNCTION_COMMON_CLAMP:
            case FUNCTION_COMMON_MIX:
            case FUNCTION_COMMON_SMOOTHSTEP:
                return 3;
        }
        return 0;
    }

    private String getInputVarName(int i) {
        switch(this.functionType) {
            case FUNCTION_COMMON_ABS:
            case FUNCTION_COMMON_SIGN:
            case FUNCTION_COMMON_FLOOR:
            case FUNCTION_COMMON_CEIL:
            case FUNCTION_COMMON_FRACT:
                return "input";
            case FUNCTION_COMMON_MOD:
            case FUNCTION_COMMON_MIN:
            case FUNCTION_COMMON_MAX:
                return "input" + (i + 1);
            case FUNCTION_COMMON_STEP:
                switch(i) {
                    case 0: return "edge";
                    case 1: return "input";
                }
            case FUNCTION_COMMON_CLAMP:
                switch(i) {
                    case 0: return "input";
                    case 1: return "minimum";
                    case 2: return "maximum";
                }
            case FUNCTION_COMMON_MIX:
                switch(i) {
                    case 0: return "input1";
                    case 1: return "input2";
                    case 2: return "alpha";
                }
            case FUNCTION_COMMON_SMOOTHSTEP:
                switch(i) {
                    case 0: return "edge0";
                    case 1: return "edge1";
                    case 2: return "input";
                }
        }
        throw new RuntimeException(getFunctionName(this.functionType) + " doesn't have " + (i + 1) + " aruments");
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < CommonFunctionFactory.variants.length; i++) {
            if (CommonFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getFunctionName(int variant) {
        return CommonFunctionFactory.variants[variant];
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
        String functionBody = "";
        for (int i = 0; i < getInputVarCount(); i++)
            functionBody += "<" + getInputVarName(i) + ">, ";
        return ValueAssignment.createCodeLineAssignment(functionName + "(" + functionBody.substring(0, functionBody.length() - 2) + ")",
                getSources(outputVariable, exportVisitor));
    }
    public Class getFactoryClass() {
        return CommonFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}