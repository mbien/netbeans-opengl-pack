/*
 * GLSLFunctions.java
 *
 * Created on April 20, 2007, 6:09 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs.glsl;

import java.awt.Color;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgramCollection;

/**
 *
 * @author Samuel Sperling
 */
public class GLSLFunctions implements IShaderProgramCollection {
    
    public static Color nodeBackgroundColor = new Color(150, 75, 75, 255);
    protected static Class[] shaderProgramFactoryNames = new Class[] {
            net.java.nboglpack.visualdesigner.shader.programs.glsl.OperationsFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.TrigonometryFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.AngleFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.ExponentialFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.CommonFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.GeometricFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.FragmentFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.RelationalFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.TextureAccessFunctionFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.glsl.DataTypeConverterFactory.class
        
        };
    
    /** Creates a new instance of GLSLFunctions */
    public GLSLFunctions() {
    }

    public Class[] getShaderProgramFactoryClasses() {
        return shaderProgramFactoryNames;
    }

    public int availableShaders() {
        return shaderProgramFactoryNames.length;
    }

    public String getCollectionName() {
        return "GLSL Functions";
    }
}
