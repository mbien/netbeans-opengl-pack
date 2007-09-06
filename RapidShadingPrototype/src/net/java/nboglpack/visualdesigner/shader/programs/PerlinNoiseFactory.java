/*
 * PerlinNoiseFactory.java
 *
 * Created on 7. August 2007, 20:24
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IAssignable;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ShaderFunction;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class PerlinNoiseFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"1 Octave", "2 Octaves", "3 Octaves", "4 Octaves", "5 Octaves", "6 Octaves", "7 Octaves", "8 Octaves"};
    
    /**
     * Creates a new instance of PerlinNoiseFactory
     */
    public PerlinNoiseFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new PerlinNoise("1 Octave");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new PerlinNoise(variant);
    }

    public String getName() {
        return "Perlin Noise";
    }

    public Color getNodeBackgroundColor() {
        return ProceduralTexturingShaderPrograms.nodeBackgroundColor;
    }
}

/**
 *
 * @author Samuel Sperling
 */
class PerlinNoise extends ShaderNode implements IShaderProgram {
    
    private byte functionType;
    
    public PerlinNoise(String variant) {
        super("Perlin Noise " + getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(ProceduralTexturingShaderPrograms.nodeBackgroundColor);
        
        setUpVariables();
    }

    private static String getFunctionName(int variant) {
        return PerlinNoiseFactory.variants[variant];
    }
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }
    private static byte getFunctionType(String variant) {
        for (int i = 0; i < PerlinNoiseFactory.variants.length; i++) {
            if (PerlinNoiseFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }
    
    private void setUpVariables() {

        this.addOutputVariable(new ShaderProgramOutVariable(
                "perlinNoise",
                "Perlin Noise",
                DataType.DATA_TYPE_VEC1,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addInputVariable(new ShaderProgramInVariable(
                "coord",
                "Coordinate",
                DataType.DATA_TYPE_VEC3,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                ));
        this.addInputVariable(new ShaderProgramInVariable(
                "noiseMap",
                "Noise Sampler",
                DataType.DATA_TYPE_SAMPLER2D,
                ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
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
        // Add Random-Function
        exportVisitor.registerFragmentFunction(getRandomFunction());
        
        // Add Interpolation-Function
        exportVisitor.registerFragmentFunction(getSCurveFunction());
        
        // Add Noise Function
        exportVisitor.registerFragmentFunction(getNoiseFunction());
        
        // Return Perlin Noise
        return ValueAssignment.createCodeLineAssignment(getPerlinNoiseCode(functionType + 1), getSources(outputVariable, exportVisitor));
    }
    public Class getFactoryClass() {
        return PerlinNoiseFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(this.functionType);
    }
    
    private ShaderFunction functionRandom;
    private ShaderFunction functionSCurve;
    private ShaderFunction functionNoise;
    
    private ShaderFunction getRandomFunction() {
        if (functionRandom != null)
            return functionRandom;
        
        String code = "vec3 position = mod(vec3(x, y, z), 56.0) / 56.0;\n";
        code += "float index1 = (x * 6.6) + (y * 7.91) + (z * 8.21) * 0.001953125;\n";
        code += "float index2 = (y * 6.64) + (z * 7.851) + (x * 7.451) * 0.01953125;\n";
        code += "vec4 color = texture2D(sampler, vec2(index1, index2));\n";
        code += "return (-1.0 + (color.x + color.y + color.z + color.w) * 0.5);\n";
        
        IExportable[] inputVars = new IExportable[4];
        IAssignable[] outputVars = new IAssignable[1];
        inputVars[0] = new ShaderProgramInVariable("x", "X-Position", DataType.DATA_TYPE_VEC1);
        inputVars[1] = new ShaderProgramInVariable("y", "Y-Position", DataType.DATA_TYPE_VEC1);
        inputVars[2] = new ShaderProgramInVariable("z", "Z-Position", DataType.DATA_TYPE_VEC1);
        inputVars[3] = new ShaderProgramInVariable("sampler", "Noise Sampler", DataType.DATA_TYPE_SAMPLER2D);
        outputVars[0] = new ShaderProgramOutVariable("random", "Random Number", DataType.DATA_TYPE_VEC1);
        functionRandom = new ShaderFunction("random", inputVars, outputVars, code);
        return functionRandom;
    }

    private ShaderFunction getSCurveFunction() {
        if (functionSCurve != null)
            return functionSCurve;
        
        String code = "return x*x*(3.0-2.0*x);\n";
        
        IExportable[] inputVars = new IExportable[1];
        IAssignable[] outputVars = new IAssignable[1];
        inputVars[0] = new ShaderProgramInVariable("x", "value to Interpoate", DataType.DATA_TYPE_VEC3);
        outputVars[0] = new ShaderProgramOutVariable("y", "interpolated value", DataType.DATA_TYPE_VEC3);
        functionSCurve = new ShaderFunction("scurve", inputVars, outputVars, code);
        return functionSCurve;
    }

    private ShaderFunction getNoiseFunction() {
        if (functionNoise != null)
            return functionNoise;
        
        String code = "vec3 lp = floor(v);\n";
        code += "vec3 frac1 = scurve(fract(v));\n";
        code += "vec4 v1;\n\n";
        code += "v1.x = random(lp.x, lp.y, lp.z, g);\n";
        code += "v1.y = random(lp.x + 1.0, lp.y, lp.z, g);\n";
        code += "v1.z = random(lp.x, lp.y + 1.0, lp.z, g);\n";
        code += "v1.w = random(lp.x + 1.0, lp.y + 1.0, lp.z, g);\n\n";
        code += "vec2 i1 = mix(v1.xz, v1.yw, frac1.x);\n";
        code += "float a = mix(i1.x, i1.y, frac1.y);\n\n";
        code += "v1.x = random(lp.x, lp.y, lp.z + 1.0, g);\n";
        code += "v1.y = random(lp.x + 1.0, lp.y, lp.z + 1.0, g);\n";
        code += "v1.z = random(lp.x, lp.y + 1.0, lp.z + 1.0, g);\n";
        code += "v1.w = random(lp.x + 1.0, lp.y + 1.0, lp.z + 1.0, g);\n\n";
        code += "i1 = mix(v1.xz, v1.yw, frac1.x);\n";
        code += "float b = mix(i1.x, i1.y, frac1.y);\n\n";
        code += "return mix(a, b, frac1.z);\n";
//        code += "\n";
        
        IExportable[] inputVars = new IExportable[2];
        IAssignable[] outputVars = new IAssignable[1];
        inputVars[0] = new ShaderProgramInVariable("v", "Position", DataType.DATA_TYPE_VEC3);
        inputVars[1] = new ShaderProgramInVariable("g", "Noise Sampler", DataType.DATA_TYPE_SAMPLER2D);
        outputVars[0] = new ShaderProgramOutVariable("noise", "Noise Value", DataType.DATA_TYPE_VEC1);
        functionNoise = new ShaderFunction("noise", inputVars, outputVars, code);
        return functionNoise;
    }

    private String getPerlinNoiseCode(int octaves) {
        String code = "noise(<coord>, <noiseMap>)";
        for (int i = 1; i < octaves; i++)
            code += " + noise(<coord> * " + Math.pow(2f, i) + ", <noiseMap>) * " + (1f / Math.pow(2f, i));
        return code;
    }
    
}

