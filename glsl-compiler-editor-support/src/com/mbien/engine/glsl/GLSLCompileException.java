package com.mbien.engine.glsl;

/**
 * Created on 29. March 2007, 15:47
 * @author Michael Bien
 */
public class GLSLCompileException extends GLSLException {
    
    /** Creates a new instance of GLSLCompileException */
    public GLSLCompileException(String shaderNames[], String massages[]) {
        super("Error compiling shader: "+format(shaderNames, massages));
    }
        

}
