/*
 * GeometricFunctionFactory.java
 *
 * Created on June 6, 2007, 5:21 PM
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
public class GeometricFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"cross", "distance", "dot", "faceforeward", "length", "normalize", "reflect", "refract"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public GeometricFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new GeometricFunction("cross");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new GeometricFunction(variant);
    }

    public String getName() {
        return "Geometric Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class GeometricFunction extends ShaderNode implements IShaderProgram {
    
    // Should be indexes of CommonFunctionFactory.variants
    public static final byte FUNCTION_GEOMETRIC_CROSS = 0;
    public static final byte FUNCTION_GEOMETRIC_DISTANCE = 1;
    public static final byte FUNCTION_GEOMETRIC_DOT = 2;
    public static final byte FUNCTION_GEOMETRIC_FACEFOREWARD = 3;
    public static final byte FUNCTION_GEOMETRIC_LENGTH = 4;
    public static final byte FUNCTION_GEOMETRIC_NORMALIZE = 5;
    public static final byte FUNCTION_GEOMETRIC_REFLECT = 6;
    public static final byte FUNCTION_GEOMETRIC_REFRACT = 7;
    
    private byte functionType;
    
    public GeometricFunction(String variant) {
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
                    getInputDataType(i),
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
        }
        this.addOutputVariable(new ShaderProgramOutVariable(
                "v_" + getName(),
                getName() + " Ouput",
                getOutputDataType(),
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }

    private int getInputVarCount() {
        switch(this.functionType) {
            case FUNCTION_GEOMETRIC_LENGTH:
            case FUNCTION_GEOMETRIC_NORMALIZE:
                return 1;
            case FUNCTION_GEOMETRIC_CROSS:
            case FUNCTION_GEOMETRIC_DISTANCE:
            case FUNCTION_GEOMETRIC_DOT:
            case FUNCTION_GEOMETRIC_REFLECT:
                return 2;
            case FUNCTION_GEOMETRIC_FACEFOREWARD:
            case FUNCTION_GEOMETRIC_REFRACT:
                return 3;
        }
        throw new RuntimeException("Functiontype " + this.functionType + " of " + getFactoryClass().getName() + " doesn't exist");
    }
    private int getInputDataType(int i) {
        switch(this.functionType) {
            case FUNCTION_GEOMETRIC_CROSS:
                return DataType.DATA_TYPE_VEC3;
            case FUNCTION_GEOMETRIC_DISTANCE:
            case FUNCTION_GEOMETRIC_DOT:
            case FUNCTION_GEOMETRIC_LENGTH:
            case FUNCTION_GEOMETRIC_NORMALIZE:
            case FUNCTION_GEOMETRIC_FACEFOREWARD:
            case FUNCTION_GEOMETRIC_REFLECT:
                return DataType.DATA_TYPE_GENTYPE;
            case FUNCTION_GEOMETRIC_REFRACT:
                if (i < 2)
                    return DataType.DATA_TYPE_GENTYPE;
                else
                    return DataType.DATA_TYPE_VEC1;
        }
        return 0;
    }
    private int getOutputDataType() {
        switch(this.functionType) {
            case FUNCTION_GEOMETRIC_CROSS:
                return DataType.DATA_TYPE_VEC3;
            case FUNCTION_GEOMETRIC_DISTANCE:
            case FUNCTION_GEOMETRIC_DOT:
            case FUNCTION_GEOMETRIC_LENGTH:
                return DataType.DATA_TYPE_VEC1;
            case FUNCTION_GEOMETRIC_NORMALIZE:
            case FUNCTION_GEOMETRIC_FACEFOREWARD:
            case FUNCTION_GEOMETRIC_REFLECT:
            case FUNCTION_GEOMETRIC_REFRACT:
                return DataType.DATA_TYPE_GENTYPE;
        }
        return 0;
    }

    private String getInputVarName(int i) {
        switch(this.functionType) {
            case FUNCTION_GEOMETRIC_LENGTH:
            case FUNCTION_GEOMETRIC_NORMALIZE:
                return "input";
            case FUNCTION_GEOMETRIC_CROSS:
            case FUNCTION_GEOMETRIC_DOT:
                return "input" + (i + 1);
            case FUNCTION_GEOMETRIC_DISTANCE:
                return "point" + (i + 1);
            case FUNCTION_GEOMETRIC_REFLECT:
                switch(i) {
                    case 0: return "incident";
                    case 1: return "normal";
                }
            case FUNCTION_GEOMETRIC_FACEFOREWARD:
                switch(i) {
                    case 0: return "normal";
                    case 1: return "incident";
                    case 2: return "normalref";
                }
            case FUNCTION_GEOMETRIC_REFRACT:
                switch(i) {
                    case 0: return "incident";
                    case 1: return "normal";
                    case 2: return "ior";
                }
        }
        throw new RuntimeException(getFunctionName(this.functionType) + " doesn't have " + (i + 1) + " aruments");
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < GeometricFunctionFactory.variants.length; i++) {
            if (GeometricFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getFunctionName(int variant) {
        return GeometricFunctionFactory.variants[variant];
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
        return GeometricFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
}
