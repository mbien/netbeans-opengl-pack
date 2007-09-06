/*
 * SimpleGeneratorsCollection.java
 *
 * Created on June 13, 2007, 5:00 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 *
 * @author Samuel Sperling
 */
public class SimpleGeneratorsCollection implements IShaderProgramCollection {
    
    protected static Color nodeBackgroundColor = new Color(75, 75, 153, 255);
    protected static Class[] shaderProgramFactoryNames = new Class[] {
        net.java.nboglpack.visualdesigner.shader.programs.ColorGeneratorFactory.class,
        net.java.nboglpack.visualdesigner.shader.programs.SamplerGeneratorFactory.class,
        net.java.nboglpack.visualdesigner.shader.programs.NoiseGeneratorFactory.class
    
    };
    
    /** Creates a new instance of SimpleGeneratorsCollection */
    public SimpleGeneratorsCollection() {
    }
    
    public Class[] getShaderProgramFactoryClasses() {
        return shaderProgramFactoryNames;
    }
    
    public int availableShaders() {
        return shaderProgramFactoryNames.length;
    }
    
    public String getCollectionName() {
        return "Simple Generator Nodes";
    }
}
