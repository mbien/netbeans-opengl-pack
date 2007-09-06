/*
 * FunctionPreview.java
 *
 * Created on May 23, 2007, 8:13 PM
 */

package net.java.nboglpack.visualdesigner.preview;

import net.java.nboglpack.visualdesigner.graphics3d.ShaderProgram;

/**
 * Uses the function visualisation Scene to make it conform to the 
 *
 * @author Samuel Sperling
 */
public class FunctionPreview extends FunctionVizScene implements IGLPreviewScene {
    
    /**
     * Creates a new instance of FunctionPreview
     */
    public FunctionPreview() {
    }

    public String[] getObjectNames() {
        return null;
    }

    /**
     * Applys a ShaderProgram to this scene
     * 
     * @param shaderProgram Shader Program that is applied to the scene
     */
    public void applyShaderProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    /**
     * No Objects supported in this preview scene
     */
    public void applyShaderProgram(String objectName, ShaderProgram shaderProgram) {
        applyShaderProgram(shaderProgram);
    }
    
}
