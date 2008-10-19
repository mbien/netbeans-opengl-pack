package com.mbien.engine.glsl;

/**
 * Created on 29. March 2007, 15:55
 * @author Michael Bien
 */
public class GLSLLinkException extends GLSLException {
    
    /** Creates a new instance of GLSLLinkException */
    public GLSLLinkException(GLSLProgram program, String massage) {
        this(program, new String[]{massage});
    }
    
    /** Creates a new instance of GLSLLinkException */
    public GLSLLinkException(GLSLProgram program, String massages[]) {
        super(program, "Error linking shader: "+format(program, massages));
    }
    
}
