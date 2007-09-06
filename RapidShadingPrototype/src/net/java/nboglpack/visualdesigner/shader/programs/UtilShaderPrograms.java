/*
 * UtilShaderPrograms.java
 *
 * Created on June 9, 2007, 8:25 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 *
 * @author Samuel Sperling
 */
public class UtilShaderPrograms implements IShaderProgramCollection  {
    
    protected static Color nodeBackgroundColor = new Color(75, 75, 75, 255);
    
    protected static Class[] shaderProgramFactoryNames = new Class[] {
        net.java.nboglpack.visualdesigner.shader.programs.ModelViewVarsFactory.class
    
    };
    
    /** Creates a new instance of UtilShaderPrograms */
    public UtilShaderPrograms() {
    }

    public Class[] getShaderProgramFactoryClasses() {
        return shaderProgramFactoryNames;
    }

    public int availableShaders() {
        return shaderProgramFactoryNames.length;
    }

    public String getCollectionName() {
        return "Utility Shader Programs";
    }
    
}
