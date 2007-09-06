/*
 * LightingShaderPrograms.java
 *
 * Created on June 9, 2007, 11:01 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 *
 * @author Samuel Sperling
 */
public class LightingShaderPrograms implements IShaderProgramCollection  {
    
    protected static Color nodeBackgroundColor = new Color(153, 127, 75, 255);
    
    protected static Class[] shaderProgramFactoryNames = new Class[] {
        net.java.nboglpack.visualdesigner.shader.programs.DiffuseLightingFactory.class,
        net.java.nboglpack.visualdesigner.shader.programs.SpecularLightingFactory.class
    
    };
    
    /** Creates a new instance of UtilShaderPrograms */
    public LightingShaderPrograms() {
    }

    public Class[] getShaderProgramFactoryClasses() {
        return shaderProgramFactoryNames;
    }

    public int availableShaders() {
        return shaderProgramFactoryNames.length;
    }

    public String getCollectionName() {
        return "Lighting Shader Programs";
    }
    
}