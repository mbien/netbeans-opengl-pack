/*
 * ProceduralTexturingShaderPrograms.java
 *
 * Created on 6. August 2007, 20:05
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 *
 * @author Samuel Sperling
 */
public class ProceduralTexturingShaderPrograms implements IShaderProgramCollection  {
    
    protected static Color nodeBackgroundColor = new Color(75, 153, 75, 255);
    
    protected static Class[] shaderProgramFactoryNames = new Class[] {
        net.java.nboglpack.visualdesigner.shader.programs.SubSpacesFactory.class,
        net.java.nboglpack.visualdesigner.shader.programs.PerlinNoiseFactory.class
    
    };
    
    /** Creates a new instance of UtilShaderPrograms */
    public ProceduralTexturingShaderPrograms() {
    }
    
    public Class[] getShaderProgramFactoryClasses() {
        return shaderProgramFactoryNames;
    }
    
    public int availableShaders() {
        return shaderProgramFactoryNames.length;
    }
    
    public String getCollectionName() {
        return "Procedural Texturing Programs";
    }
    
}