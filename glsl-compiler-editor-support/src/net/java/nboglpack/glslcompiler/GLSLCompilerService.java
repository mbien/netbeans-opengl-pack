/*
 * GLSLCompiler.java
 * 
 * Created on 25.07.2007, 19:58:53
 * 
 */

package net.java.nboglpack.glslcompiler;

import org.openide.loaders.DataObject;

/**
 *
 * @author Michael Bien
 */
public interface GLSLCompilerService {

    public boolean compileShader(DataObject... daos);
    
    public boolean compileAndLinkProgram(DataObject... daos);

}
