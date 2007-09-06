/*
 * IGLPreviewScene.java
 *
 * Created on May 23, 2007, 7:27 PM
 */

package net.java.nboglpack.visualdesigner.preview;

import javax.media.opengl.GLEventListener;
import net.java.nboglpack.visualdesigner.graphics3d.ShaderProgram;

/**
 * Classes implementing this interface are used for preview Scenes
 *
 * @author Samuel Sperling
 */
public interface IGLPreviewScene extends GLEventListener {
    
    /**
     * Get names of all objects in this scene
     * @return Names of all objects in this scene
     *          Returns null if just one object is available
     */
    public String[] getObjectNames();
    
    /**
     * Applys a ShaderProgram to this scene
     * @param shaderProgram Shader Program that is applied to the scene
     */
    public void applyShaderProgram(ShaderProgram shaderProgram);
    
    /**
     * Applys a ShaderProgram to a specific object in this scene.
     * @param objectName Name of the object where this material is applied to.
     * @param shaderProgram Shader Program that is applied to the scene
     */
    public void applyShaderProgram(String objectName, ShaderProgram shaderProgram);
  
//    DEPRICATED since a propertiespanel can be opend via keyboard input.
//    /**
//     * Returns a JPanel that let's the user confugure the preview.
//     */
//    public JPanel getPropertiesPanel();
    
}
