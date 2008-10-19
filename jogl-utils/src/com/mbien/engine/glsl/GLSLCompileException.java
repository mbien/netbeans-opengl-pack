package com.mbien.engine.glsl;

/**
 * Created on 29. March 2007, 15:47
 * @author Michael Bien
 */
public class GLSLCompileException extends GLSLException {
    
    /** Creates a new instance of GLSLLinkException */
    public GLSLCompileException(GLSLShader shader, String massage) {
        super(shader, "Error compiling shader: "+format(shader, new String[] {massage}));
    }

    public GLSLCompileException(GLSLShader shader, String[] massages) {
        super(shader, "Error compiling shader: "+format(shader, massages));
    }

}
