/*
 * OperationsFactory.java
 *
 * Created on May 31, 2007, 4:12 PM
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
public class OperationsFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"add", "subtract", "multiply", "divide"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public OperationsFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new Operation("add");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new Operation(variant);
    }

    public String getName() {
        return "Operations";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

class Operation extends ShaderNode implements IShaderProgram {
    
    public static final byte OPERATION_ADD = 0;
    public static final byte OPERATION_SUBTRACT = 1;
    public static final byte OPERATION_MULTIPLY = 2;
    public static final byte OPERATION_DIVIDE = 3;
    private byte operationType;
    
    public Operation(String variant) {
        super(variant);
        super.setShaderProgram(this);
        setOperationType(getOperationType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }
    
    private String getInput1Name() {
        switch (operationType) {
            case OPERATION_SUBTRACT: return "minuend";
            case OPERATION_MULTIPLY: return "quotient";
            case OPERATION_DIVIDE: return "dividend";
            case OPERATION_ADD:
            default:
                return "input1";
        }
    }
    private String getInput2Name() {
        switch (operationType) {
            case OPERATION_SUBTRACT: return "subtrahend";
            case OPERATION_MULTIPLY: return "divisor";
            case OPERATION_DIVIDE: return "divisor";
            case OPERATION_ADD:
            default:
                return "input2";
        }
    }
    private String getOutputName() {
        switch (operationType) {
            case OPERATION_SUBTRACT: return "difference";
            case OPERATION_MULTIPLY: return "dividend";
            case OPERATION_DIVIDE: return "quotient";
            case OPERATION_ADD:
            default:
                return "output";
        }
    }

    private void setUpVariables() {
        String input1 = getInput1Name();
        String input2 = getInput2Name();
        String output = getOutputName();
        this.addInputVariable(new ShaderProgramInVariable(
                input1,
                getName() + " " + input1,
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addInputVariable(new ShaderProgramInVariable(
                input2,
                getName() + " " + input2,
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addOutputVariable(new ShaderProgramOutVariable(
                output,
                getName() + " " + output,
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption {
        return ValueAssignment.createCodeLineAssignment(
                    "<" + getInput1Name() + ">" +
                    getOperatorSymbol() +
                    "<" + getInput2Name() + ">",
                getSources(outputVariable, exportVisitor));
    }
    
    private String getOperatorSymbol() {
        switch (operationType) {
            case OPERATION_ADD:
                return "+";
            case OPERATION_SUBTRACT:
                return "-";
            case OPERATION_MULTIPLY:
                return "*";
            case OPERATION_DIVIDE:
                return "/";
        }
        return "";
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public byte getOperationType() {
        return operationType;
    }

    public void setOperationType(byte operationType) {
        this.operationType = operationType;
    }
    
    private static byte getOperationType(String variant) {
        if (variant.equals("add"))
            return OPERATION_ADD;
        if (variant.equals("subtract"))
            return OPERATION_SUBTRACT;
        if (variant.equals("multiply"))
            return OPERATION_MULTIPLY;
        if (variant.equals("divide"))
            return OPERATION_DIVIDE;
        throw new RuntimeException("OperationType '" + variant + "' is not valid");
    }
    
    public Class getFactoryClass() {
        return OperationsFactory.class;
    }
    
    public String getVariant() {
        switch (this.operationType) {
            case OPERATION_ADD: return "add";
            case OPERATION_SUBTRACT: return "subtract";
            case OPERATION_MULTIPLY: return "multiply";
            case OPERATION_DIVIDE: return "divide";
        }
        return null;
    }
}
