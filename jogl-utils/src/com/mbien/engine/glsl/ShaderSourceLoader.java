/*
 *Created on 25. Aug 2007, 17:07
 */

package com.mbien.engine.glsl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract shader source loader.
 * @author Michael Bien
 */
public abstract class ShaderSourceLoader<T> {

    private final static Pattern includePattern = Pattern.compile("//import\\s+((?:\\w|/|.)+)\\s*");


    public abstract CodeFragment<T> loadShaderSource(T t);

    public abstract CodeFragment<T> loadShaderSource(String filePath);

    public abstract boolean sameSource(T t, String path);

    public final CodeFragment<T>[] loadWithDependencies(String filePath) {

        CodeFragment<T> main = loadShaderSource(filePath);
        return loadWithDependencies(main, parent(filePath));
    }

    public final CodeFragment<T>[] loadWithDependencies(CodeFragment<T> main, String path) {

        ArrayList<CodeFragment<T>> fragments = new ArrayList<CodeFragment<T>>();

        includeAllDependencies(main, path, fragments);

        Collections.reverse(fragments);

        return fragments.toArray(new CodeFragment[fragments.size()]);
    }


    /**
     * concatenates shaders recursively - deepest first.
     */
    private final void includeAllDependencies(CodeFragment<T> fragment, String sourcePackage, ArrayList<CodeFragment<T>> fragments) {

        fragments.add(fragment);
        
        Matcher matcher = includePattern.matcher(fragment.source);
        while (matcher.find()) {

            String include = matcher.group(1);
            String path = sourcePackage;

            while(include.startsWith("../")) {

                include = include.substring(3);

                path = parent(path);
            }


            path += "/" + include;

            // check if already included
            boolean alreadyIncluded = false;

            for (int i = 0; i < fragments.size(); i++) {

                CodeFragment<T> codeFragment = fragments.get(i);

                // if included, move code fragment to end of list
                if(sameSource(codeFragment.sourceObj, path)) {
                    fragments.add(fragments.size()-1, fragments.remove(i));
                    alreadyIncluded = true;
                    break;
                }
            }

            if(!alreadyIncluded) {
                CodeFragment<T> srcToInclude = loadShaderSource(path);

                if(srcToInclude != null) {
                    includeAllDependencies(srcToInclude, path.substring(0, path.lastIndexOf("/")), fragments);
                }

            }

        }

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
