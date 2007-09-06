/*
 * ShaderProgramFactory.java
 *
 * Created on April 24, 2007, 2:50 PM
 *
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;

/**
 *
 * @author Samuel Sperling
 */
public interface IShaderProgramFactory {
    
    public String[] getVariants();
    
    public IShaderProgram createShaderProgram();
    
    public IShaderProgram createShaderProgram(String variant);
    
    public String getName();
    
    /**
     * @return Color of the nodes that this factory produces
     */
    public Color getNodeBackgroundColor();
}
