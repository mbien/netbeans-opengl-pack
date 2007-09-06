/*
 * SubSpacesFactory.java
 *
 * Created on 6. August 2007, 20:10
 *
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
public class SubSpacesFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"0,1", "-1,0", "-1,1", "0,pi", "-pi,0", "-pi/2,pi/2", "sawtooth", "brick"};
    
    /**
     * Creates a new instance of SubSpacesFactory
     */
    public SubSpacesFactory() {
    }
    
    public String[] getVariants() {
        return variants;
    }
    
    public IShaderProgram createShaderProgram() {
        return new SubSpaceGenerator("0,1");
    }
    
    public IShaderProgram createShaderProgram(String variant) {
        return new SubSpaceGenerator(variant);
    }
    
    public String getName() {
        return "Sub-Spaces";
    }
    
    public Color getNodeBackgroundColor() {
        return ProceduralTexturingShaderPrograms.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class SubSpaceGenerator extends ShaderNode implements IShaderProgram {
    
    public static final byte FUNCTION_SUBSPACE_0_1 = 0;
    public static final byte FUNCTION_SUBSPACE_1_0 = 1;
    public static final byte FUNCTION_SUBSPACE_1_1 = 2;
    public static final byte FUNCTION_SUBSPACE_0_PI = 3;
    public static final byte FUNCTION_SUBSPACE_PI_0 = 4;
    public static final byte FUNCTION_SUBSPACE_PIH_PIH = 5;
    public static final byte FUNCTION_SUBSPACE_SAWTOOTH = 6;
    public static final byte FUNCTION_SUBSPACE_BRICK = 7;
    
    
    private byte functionType;
    
    public SubSpaceGenerator(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(ProceduralTexturingShaderPrograms.nodeBackgroundColor);
        
        setUpVariables();
    }
    private static String getFunctionName(int variant) {
        return SubSpacesFactory.variants[variant];
    }
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }
    private static byte getFunctionType(String variant) {
        for (int i = 0; i < SubSpacesFactory.variants.length; i++) {
            if (SubSpacesFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    
    private void setUpVariables() {
        
        this.addInputVariable(new ShaderProgramInVariable(
                "SourceSpace",
                "Source Space",
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addOutputVariable(new ShaderProgramOutVariable(
                "SubSpace",
                "Sub Space",
                DataType.DATA_TYPE_GENTYPE,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
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
        String code = "fract(<SourceSpace>)";
        int dataType = outputVariable.resolveDataType();
        int dimensions = DataType.getDimensions(dataType);
        
        float lowerLimit = 0;
        float upperLimit = 1;
        switch(this.functionType) {
            case FUNCTION_SUBSPACE_1_0:
                lowerLimit = 1;
                upperLimit = 0;
                break;
            case FUNCTION_SUBSPACE_SAWTOOTH:
            case FUNCTION_SUBSPACE_1_1:
                lowerLimit = 1;
                upperLimit = 1;
                break;
            case FUNCTION_SUBSPACE_0_PI:
                lowerLimit = 0;
                upperLimit = (float) Math.PI;
                break;
            case FUNCTION_SUBSPACE_PI_0:
                lowerLimit = (float) Math.PI;
                upperLimit = 0;
                break;
            case FUNCTION_SUBSPACE_PIH_PIH:
                lowerLimit = (float) Math.PI / 2;
                upperLimit = lowerLimit;
                break;
        }
        float multiplier = (lowerLimit + upperLimit);
        if (multiplier != 1)
            code += " * " + multiplier;
        if (lowerLimit > 0)
            code += " - " + lowerLimit;
        
        if (this.functionType == FUNCTION_SUBSPACE_SAWTOOTH)
            code = "abs(" + code + ")";
        if (this.functionType == FUNCTION_SUBSPACE_BRICK) {
            code = "fract(<SourceSpace> + vec" + dimensions + "(0.5";
            for (int i = 1; i < dimensions; i++)
                code += ", 0.0";
            code += ") * floor(<SourceSpace>.y))";
        }
        return ValueAssignment.createCodeLineAssignment(code, getSources(outputVariable, exportVisitor));
    }
    
    public Class getFactoryClass() {
        return SubSpacesFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}