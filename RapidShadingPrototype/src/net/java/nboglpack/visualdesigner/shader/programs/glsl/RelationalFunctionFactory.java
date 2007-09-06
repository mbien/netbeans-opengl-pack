/*
 * RelationalFunctionFactory.java
 *
 * Created on June 7, 2007, 12:50 PM
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
public class RelationalFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"all", "any", "equal", "greaterThan", "greaterThanEqual", "lessThan", "lessThanEqual", "not", "notEqual"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public RelationalFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new RelationalFunction("all");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new RelationalFunction(variant);
    }

    public String getName() {
        return "Relational Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class RelationalFunction extends ShaderNode implements IShaderProgram {
    
    // Should be indexes of Relational.variants
    public static final byte FUNCTION_RELATIONAL_ALL = 0;
    public static final byte FUNCTION_RELATIONAL_ANY = 1;
    public static final byte FUNCTION_RELATIONAL_EQUAL = 2;
    public static final byte FUNCTION_RELATIONAL_GREATERTHAN = 3;
    public static final byte FUNCTION_RELATIONAL_GREATERTHANEQUAL = 4;
    public static final byte FUNCTION_RELATIONAL_LESSTHAN = 5;
    public static final byte FUNCTION_RELATIONAL_LESSTHANEQUAL = 6;
    public static final byte FUNCTION_RELATIONAL_NOT = 7;
    public static final byte FUNCTION_RELATIONAL_NOTEQUAL = 8;
    
    private byte functionType;
    
    public RelationalFunction(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        for (int i = 0; i < getInputVarCount(); i++) {
            this.addInputVariable(new ShaderProgramInVariable(
                    "input" + i,
                    getName() + " input " + i,
                    getInputDataType(),
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
        }
        this.addOutputVariable(new ShaderProgramOutVariable(
                "is_" + getName(),
                getName() + " Ouput",
                getOutputDataType(),
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }

    private int getInputDataType() {
        switch(this.functionType) {
            case FUNCTION_RELATIONAL_ALL:
            case FUNCTION_RELATIONAL_ANY:
            case FUNCTION_RELATIONAL_NOT:
                return DataType.DATA_TYPE_BVEC;
            case FUNCTION_RELATIONAL_EQUAL:
            case FUNCTION_RELATIONAL_NOTEQUAL:
                return DataType.DATA_TYPE_BVEC | DataType.DATA_TYPE_IVEC | DataType.DATA_TYPE_VEC;
            case FUNCTION_RELATIONAL_GREATERTHAN:
            case FUNCTION_RELATIONAL_GREATERTHANEQUAL:
            case FUNCTION_RELATIONAL_LESSTHAN:
            case FUNCTION_RELATIONAL_LESSTHANEQUAL:
                return DataType.DATA_TYPE_IVEC | DataType.DATA_TYPE_VEC;
        }
        return 0;
    }
    private int getOutputDataType() {
        switch(this.functionType) {
            case FUNCTION_RELATIONAL_ALL:
            case FUNCTION_RELATIONAL_ANY:
                return DataType.DATA_TYPE_BVEC1;
            case FUNCTION_RELATIONAL_NOT:
            case FUNCTION_RELATIONAL_EQUAL:
            case FUNCTION_RELATIONAL_NOTEQUAL:
            case FUNCTION_RELATIONAL_GREATERTHAN:
            case FUNCTION_RELATIONAL_GREATERTHANEQUAL:
            case FUNCTION_RELATIONAL_LESSTHAN:
            case FUNCTION_RELATIONAL_LESSTHANEQUAL:
                return DataType.DATA_TYPE_BVEC;
        }
        return 0;
    }

    private int getInputVarCount() {
        switch(this.functionType) {
            case FUNCTION_RELATIONAL_ALL:
            case FUNCTION_RELATIONAL_ANY:
            case FUNCTION_RELATIONAL_NOT:
                return 1;
            case FUNCTION_RELATIONAL_EQUAL:
            case FUNCTION_RELATIONAL_GREATERTHAN:
            case FUNCTION_RELATIONAL_GREATERTHANEQUAL:
            case FUNCTION_RELATIONAL_LESSTHAN:
            case FUNCTION_RELATIONAL_LESSTHANEQUAL:
            case FUNCTION_RELATIONAL_NOTEQUAL:
                return 2;
        }
        return 0;
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < RelationalFunctionFactory.variants.length; i++) {
            if (RelationalFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getFunctionName(int variant) {
        return RelationalFunctionFactory.variants[variant];
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
            functionBody += "<input" + i + ">, ";
        return ValueAssignment.createCodeLineAssignment(functionName + "(" + functionBody.substring(0, functionBody.length() - 2) + ")",
                getSources(outputVariable, exportVisitor));
    }
    public Class getFactoryClass() {
        return RelationalFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}