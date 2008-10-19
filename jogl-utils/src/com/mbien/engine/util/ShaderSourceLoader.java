/*
 *Created on 25. Aug 2007, 17:07
 */

package com.mbien.engine.util;

import com.mbien.engine.glsl.GLSLFragment;

/**
 *
 * @author Michael Bien
 */
public abstract class ShaderSourceLoader<T> {

    public abstract GLSLFragment<T> loadShaderSource(String filePath);
    
    public abstract GLSLFragment<T> loadShaderSource(T t);

    public abstract GLSLFragment<T>[] loadWithDependencies(String filePath);

    public abstract GLSLFragment<T>[] loadWithDependencies(GLSLFragment<T> main, String path);

}
