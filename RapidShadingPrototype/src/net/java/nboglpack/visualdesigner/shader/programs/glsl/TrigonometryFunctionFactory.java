/*
 * TrigonometryFunctions.java
 *
 * Created on April 24, 2007, 12:45 PM
 *
 */

package net.java.nboglpack.visualdesigner.shader.programs.glsl;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgramFactory;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;

/**
 *
 * @author Samuel Sperling
 */
public class TrigonometryFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"sin", "cos", "tan", "asin", "acos", "atan"};
    
    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new TrigonometryFunction("sin");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new TrigonometryFunction(variant);
    }
    
    public String getName() {
        return "Trigonometry Function";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class TrigonometryFunction extends ShaderNode implements IShaderProgram {
    
    public static final byte FUNCTION_TRIGONOMETRY_SIN = 1;
    public static final byte FUNCTION_TRIGONOMETRY_COS = 2;
    public static final byte FUNCTION_TRIGONOMETRY_TAN = 3;
    public static final byte FUNCTION_TRIGONOMETRY_ASIN = 4;
    public static final byte FUNCTION_TRIGONOMETRY_ACOS = 5;
    public static final byte FUNCTION_TRIGONOMETRY_ATAN = 6;
    
    private byte functionType;
    
    public TrigonometryFunction(String variant) {
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
        if (variant.equals("sin"))
            return FUNCTION_TRIGONOMETRY_SIN;
        if (variant.equals("cos"))
            return FUNCTION_TRIGONOMETRY_COS;
        if (variant.equals("tan"))
            return FUNCTION_TRIGONOMETRY_TAN;
        if (variant.equals("asin"))
            return FUNCTION_TRIGONOMETRY_ASIN;
        if (variant.equals("acos"))
            return FUNCTION_TRIGONOMETRY_ACOS;
        if (variant.equals("atan"))
            return FUNCTION_TRIGONOMETRY_ATAN;
        return 0;
    }
    private static String getFunctionName(int variant) {
        switch(variant) {
            case FUNCTION_TRIGONOMETRY_SIN:
                return "Sine";
            case FUNCTION_TRIGONOMETRY_COS:
                return "Cosine";
            case FUNCTION_TRIGONOMETRY_TAN:
                return "Tangent";
            case FUNCTION_TRIGONOMETRY_ASIN:
                return "Arc Sine";
            case FUNCTION_TRIGONOMETRY_ACOS:
                return "Arc Cosine";
            case FUNCTION_TRIGONOMETRY_ATAN:
                return "Arc Tangent";
        }
        return "";
    }
    
    private static String getFunctionShortName(int variant) {
        switch(variant) {
            case FUNCTION_TRIGONOMETRY_SIN:
                return "sin";
            case FUNCTION_TRIGONOMETRY_COS:
                return "cos";
            case FUNCTION_TRIGONOMETRY_TAN:
                return "tan";
            case FUNCTION_TRIGONOMETRY_ASIN:
                return "asin";
            case FUNCTION_TRIGONOMETRY_ACOS:
                return "acos";
            case FUNCTION_TRIGONOMETRY_ATAN:
                return "atan";
        }
        return null;
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                throws ExportingExeption {
        String functionName = getFunctionShortName(this.functionType);
        return ValueAssignment.createCodeLineAssignment(functionName + "(<input>)",
                new ShaderProgramInVariable[] {getInVariables().get("input")});
    }
    public Class getFactoryClass() {
        return TrigonometryFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionShortName(functionType);
    }
    
}