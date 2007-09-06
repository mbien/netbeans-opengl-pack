/*
 * VertexShader.java
 *
 * Created on March 28, 2007, 6:20 AM
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.io.IOException;
import java.io.Reader;
import javax.media.opengl.GL;

/**
 *
 * @author Samuel Sperling
 */
public class VertexShader extends Shader {
    
    public VertexShader(GL gl, String[] contents) {
        super(gl, gl.GL_VERTEX_SHADER_ARB, contents);
    }
    
    /** Creates a new instance of VertexShader */
    public VertexShader(GL gl, Reader contents) throws IOException {
        super(gl, gl.GL_VERTEX_SHADER_ARB, getContents(contents));
    }
     
    public void finalize() throws Throwable {
        if (program != null)
            program.detatchVertexShader();
        super.finalize();
    }   
}
