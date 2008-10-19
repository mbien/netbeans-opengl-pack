/*
 *Created on 25. Aug 2007, 17:07
 */

package com.mbien.engine.util;

import com.mbien.engine.glsl.GLSLFragment;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Michael Bien
 */
public abstract class ShaderSourceLoader<T> {

    private final static Pattern includePattern = Pattern.compile("//import\\s+((?:\\w|/|.)+)\\s*");


    public abstract GLSLFragment<T> loadShaderSource(T t);

    public abstract GLSLFragment<T> loadShaderSource(String filePath);



    public final GLSLFragment<T>[] loadWithDependencies(String filePath) {

        GLSLFragment<T> main = loadShaderSource(filePath);
        return loadWithDependencies(main, parent(filePath));
    }

    public final GLSLFragment<T>[] loadWithDependencies(GLSLFragment<T> main, String path) {

        ArrayList<GLSLFragment<T>> fragments = new ArrayList<GLSLFragment<T>>();

        includeAllDependencies(main, path, fragments);

        return fragments.toArray(new GLSLFragment[fragments.size()]);
    }


    /**
     * concatenates shaders recursively - deepest first.
     */
    private final void includeAllDependencies(GLSLFragment<T> fragment, String sourcePackage, ArrayList<GLSLFragment<T>> fragments) {

        Matcher matcher = includePattern.matcher(fragment.source);
        while (matcher.find()) {

            String include = matcher.group(1);
            String path = sourcePackage;

            while(include.startsWith("../")) {

                include = include.substring(3);

                path = parent(path);
            }

            path += "/" + include;
            GLSLFragment srcToInclude = loadShaderSource(path);
            if(srcToInclude != null)
                includeAllDependencies(srcToInclude, path, fragments);

        }
        fragments.add(fragment);

    }

    /**
     * goto parent folder
     */
    protected final String parent(String path) {
        for(int i = path.length()-1; i >= 0; i--) {
            if(path.charAt(i) == '/') {
                return path.substring(0, i);
            }
        }
        return path;
    }

}
