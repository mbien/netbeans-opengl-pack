/*
 * ShaderProgramCollection.java
 *
 * Created on April 20, 2007, 6:00 PM
 *
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 * Classes that implement this interface must have the ability to create one or
 * more ShaderPrograms.
 *
 * @author Samuel Sperling
 */
public interface IShaderProgramCollection {
    
    /**
     * @return All available ShaderProgram by Class-Name
     */
    public Class[] getShaderProgramFactoryClasses();
    
    /**
     * @return Number of available Shaders
     */
    public int availableShaders();
    
    /**
     * @return Retrieves the name of this collection
     */
    public String getCollectionName();
    
}
