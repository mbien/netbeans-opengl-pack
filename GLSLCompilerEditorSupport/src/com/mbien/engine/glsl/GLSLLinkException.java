package com.mbien.engine.glsl;

/**
 * Created on 29. March 2007, 15:55
 * @author Michael Bien
 */
public class GLSLLinkException extends GLSLException {
    
    /** Creates a new instance of GLSLLinkException */
    public GLSLLinkException(String shaderNames[], String massages[]) {
        super("Error linking shader: "+format(shaderNames, massages));
    }
    
}
