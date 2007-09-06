/*
 * NoiseGeneratorFactory.java
 *
 * Created on 7. August 2007, 19:05
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.Sampler;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class NoiseGeneratorFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"2D Noise 32", "2D Noise 64", "2D Noise 128"};
    
    /**
     * Creates a new instance of NoiseGeneratorFactory
     */
    public NoiseGeneratorFactory() {
    }
    
    public String[] getVariants() {
        return variants;
    }
    
    public IShaderProgram createShaderProgram() {
        return new NoiseGenerator(variants[0]);
    }
    
    public IShaderProgram createShaderProgram(String variant) {
        return new NoiseGenerator(variant);
    }
    
    public String getName() {
        return "Noise Generator";
    }
    
    public Color getNodeBackgroundColor() {
        return SimpleGeneratorsCollection.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class NoiseGenerator extends ShaderNode implements IShaderProgram {
//    
//    public static final byte FUNCTION_NOISE_GENERATOR_1D_32 = 0;
//    public static final byte FUNCTION_NOISE_GENERATOR_1D_64 = 1;
//    public static final byte FUNCTION_NOISE_GENERATOR_1D_128 = 2;
    public static final byte FUNCTION_NOISE_GENERATOR_2D_32 = 0;
    public static final byte FUNCTION_NOISE_GENERATOR_2D_64 = 1;
    public static final byte FUNCTION_NOISE_GENERATOR_2D_128 = 2;
//    public static final byte FUNCTION_NOISE_GENERATOR_3D_32 = 6;
//    public static final byte FUNCTION_NOISE_GENERATOR_3D_64 = 7;
//    public static final byte FUNCTION_NOISE_GENERATOR_3D_128 = 8;
    
    private byte functionType;
    private int dataType;
    private int resolution;
    
    public NoiseGenerator(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(SimpleGeneratorsCollection.nodeBackgroundColor);
        
        setUpVariables();
    }
    
    private static String getFunctionName(int variant) {
        return NoiseGeneratorFactory.variants[variant];
    }
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }
    private static byte getFunctionType(String variant) {
        for (int i = 0; i < NoiseGeneratorFactory.variants.length; i++) {
            if (NoiseGeneratorFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    
    private void setUpVariables() {
        dataType = DataType.DATA_TYPE_SAMPLER2D;
        
        switch(this.functionType) {
//            case FUNCTION_NOISE_GENERATOR_1D_32:
//            case FUNCTION_NOISE_GENERATOR_1D_64:
//            case FUNCTION_NOISE_GENERATOR_1D_128:
//                dataType = DataType.DATA_TYPE_SAMPLER1D;
//                break;
            case FUNCTION_NOISE_GENERATOR_2D_32:
                resolution = 32;
                break;
            case FUNCTION_NOISE_GENERATOR_2D_64:
                resolution = 64;
                break;
            case FUNCTION_NOISE_GENERATOR_2D_128:
                resolution = 128;
                break;
//            case FUNCTION_NOISE_GENERATOR_3D_32:
//            case FUNCTION_NOISE_GENERATOR_3D_64:
//            case FUNCTION_NOISE_GENERATOR_3D_128:
//                dataType = DataType.DATA_TYPE_SAMPLER3D;
//                break;
        }
        this.addOutputVariable(new ShaderProgramOutVariable(
                "noise",
                "Noise",
                dataType,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }
    
    public ShaderNode getShaderNode() {
        return this;
    }
    
    public JPanel getProperiesPanel() {
        return null;
    }
    
    /** {@inheritDoc} */
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
    throws ExportingExeption {
        Sampler sampler = new Sampler("noise", dataType, getBufferedImage());
        return ValueAssignment.createExternalAssignment(sampler);
    }
    
    private BufferedImage bufferedImage;
    
    private BufferedImage getBufferedImage() throws ExportingExeption {
        if (bufferedImage == null) {
            try {
                bufferedImage = net.java.nboglpack.visualdesigner.tools.NoiseGenerator.createNoiseTexture(resolution, BufferedImage.TYPE_INT_ARGB);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new ExportingExeption("Error while creating the Noise Sampler:\r\n" + ex.getMessage());
            }
        }
        return bufferedImage;
    }
    
    public Class getFactoryClass() {
        return NoiseGeneratorFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
}
