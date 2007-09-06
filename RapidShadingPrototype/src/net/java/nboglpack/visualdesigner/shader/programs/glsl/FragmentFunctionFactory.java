/*
 * FragmentFunctionFactory.java
 *
 * Created on June 7, 2007, 12:09 PM
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
public class FragmentFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"dFdx", "dFdy", "fwidth"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public FragmentFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new FragmentFunction("dFdx");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new FragmentFunction(variant);
    }

    public String getName() {
        return "Fragment Processing Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}


/**
 *
 * @author Samuel Sperling
 */
class FragmentFunction extends ShaderNode implements IShaderProgram {
    
    public static final byte FUNCTION_FRAGMENT_DFDX = 0;
    public static final byte FUNCTION_FRAGMENT_DFDY = 1;
    public static final byte FUNCTION_FRAGMENT_FWIDTH = 2;
    
    private byte functionType;
    
    public FragmentFunction(String variant) {
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
                "output",
                getName() + " Ouput",
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < FragmentFunctionFactory.variants.length; i++) {
            if (FragmentFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getFunctionName(int variant) {
        switch(variant) {
            case FUNCTION_FRAGMENT_DFDX:
                return "Derivative in X";
            case FUNCTION_FRAGMENT_DFDY:
                return "Derivative in Y";
            case FUNCTION_FRAGMENT_FWIDTH:
                return "Fragment Width";
        }
        return "";
    }
    
    private static String getFunctionShortName(int variant) {
        return FragmentFunctionFactory.variants[variant];
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
                new ShaderProgramInVariable[] {getInVariables().get("input")},
                ValueAssignment.CONTEXT_FRAGMENT_SHADER);
    }
    public Class getFactoryClass() {
        return FragmentFunctionFactory.class;
    }
    
    public String getVariant() {
        return getFunctionShortName(functionType);
    }
    
}