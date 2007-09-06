/*
 * TextureAccessFunctionFactory.java
 *
 * Created on June 7, 2007, 2:25 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs.glsl;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
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
public class TextureAccessFunctionFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"texture", "texture projective"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public TextureAccessFunctionFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new TextureAccessFunction("texture");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new TextureAccessFunction(variant);
    }

    public String getName() {
        return "Texture Access Functions";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class TextureAccessFunction extends ShaderNode implements IShaderProgram {
    
    // Should be indexes of CommonFunctionFactory.variants
    public static final byte FUNCTION_TEXTURE_ACCESS_NORMAL = 0;
    public static final byte FUNCTION_TEXTURE_ACCESS_PROJECTIVE = 1;
    
    private byte functionType;
    
    public TextureAccessFunction(String variant) {
        super(getVariantName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        for (int i = 0; i < 3; i++) {
            this.addInputVariable(new ShaderProgramInVariable(
                    getInputVarName(i),
                    getName() + " " + getInputVarName(i),
                    getInputDataType(i),
                    ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
        }
        this.addOutputVariable(new ShaderProgramOutVariable(
                "output",
                getName() + " Ouput",
                DataType.DATA_TYPE_VEC4,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }
    private String getInputVarName(int i) {
        switch(i) {
            case 0: return "sampler";
            case 1: return "coords";
            case 2: return "lod";
        }
        
        throw new RuntimeException(getVariantName(this.functionType) + " doesn't have " + (i + 1) + " aruments");
    }
    private int getInputDataType(int i) {
        switch(i) {
            case 0: return DataType.DATA_TYPE_SAMPLER |
                           DataType.DATA_TYPE_SAMPLER_SHADOW |
                           DataType.DATA_TYPE_SAMPLERCUBE |
                           DataType.DATA_TYPE_DIMENSION1 |
                           DataType.DATA_TYPE_DIMENSION2 |
                           DataType.DATA_TYPE_DIMENSION3;
            case 1: return DataType.DATA_TYPE_GENTYPE;
            case 2: return DataType.DATA_TYPE_VEC1;
        }
        throw new RuntimeException(getVariantName(this.functionType) + " doesn't have " + (i + 1) + " aruments");
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < TextureAccessFunctionFactory.variants.length; i++) {
            if (TextureAccessFunctionFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    private static String getVariantName(int variant) {
        return TextureAccessFunctionFactory.variants[variant];
    }
    
    private String getFunctionName(int variant) throws ExportingExeption {
        String functionName = "";
        int dataType = this.inVariables.get(0).resolveDataType();
        switch (DataType.getDataTypeOnly(dataType)) {
            case DataType.DATA_TYPE_SAMPLER:
                functionName = "texture";
                break;
            case DataType.DATA_TYPE_SAMPLER_SHADOW:
                functionName = "shadow";
                break;
            case DataType.DATA_TYPE_SAMPLERCUBE:
                return "textureCube";
        }
        functionName += DataType.getDimensions(dataType) + "D";
        if (this.functionType == FUNCTION_TEXTURE_ACCESS_PROJECTIVE)
            functionName += "Proj";
        //TODO: dependend if on fragment or vertex side, the name must have a LOD appendix
        return functionName;
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
        for (int i = 0; i < 3; i++) {
            String name = getInputVarName(i);
            if (name != "lod" || getInVariables().get(i).hasValue())
                functionBody += "<" + name + ">, ";
        }
        return ValueAssignment.createCodeLineAssignment(functionName + "(" + functionBody.substring(0, functionBody.length() - 2) + ")",
                getSources(outputVariable, exportVisitor));
    }
    

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        int size = inVariables.size();
        if (!getInVariables().get(2).hasValue())
            size--;
        IExportable[] sources = new IExportable[size];
        for (int i = 0; i < size; i++)
            sources[i] = inVariables.get(i);
        return sources;
    }
    
    public Class getFactoryClass() {
        return TextureAccessFunctionFactory.class;
    }
    
    public String getVariant() {
        return getVariantName(functionType);
    }
}
