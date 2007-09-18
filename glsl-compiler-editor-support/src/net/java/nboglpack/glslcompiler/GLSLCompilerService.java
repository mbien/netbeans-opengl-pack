/*
 * GLSLCompiler.java
 * 
 * Created on 25.07.2007, 19:58:53
 * 
 */

package net.java.nboglpack.glslcompiler;

import org.openide.loaders.DataObject;

/**
 * Service for GLSL shader handling.
 * @author Michael Bien
 */
public interface GLSLCompilerService {

    /**
     * Compiles the shader and checks for compilation errors. Annotations are 
     * automatically placed if errors accure.<p>
     * 
     * This is a fire and forget method. The OpenGL shader object will be immediately 
     * removed after compilation, successfull or not.
     * 
     * @return Returns true if shader compilation succeeds.
     * @param daos The DataObjects containing the shader sources
     * @param printOut if true, prints compiler messages to output window
     */
    public boolean compileShader(DataObject[] daos, boolean printOut);
    
    /**
     * Compiles and links the shaders and checks for errors. Annotations are 
     * automatically placed if errors accure.<p>
     * 
     * This is a fire and forget method. The OpenGL shader object will be immediately 
     * removed after compilation, successfull or not.
     * 
     * @return Returns true if shader program was successfully compiled and linked.
     * @param daos The DataObjects containing the shader sources
     * @param printOut if true, prints compiler messages to output window
     */
    public boolean compileAndLinkProgram(DataObject[] daos, boolean printOut);
    
}
