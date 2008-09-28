/*
 * Created on 24. Aug 2007, 17:07
 */
package com.mbien.engine.glsl;

import com.mbien.engine.util.ShaderSourceProvider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Michael Bien
 */
public class GLSLIncludeUtil {
    
    private final static Pattern includePattern = Pattern.compile("//concat\\s+((?:\\w|/|.)+)\\s*");
 
    private GLSLIncludeUtil() {
    }

    public final static String includeAllDependencies(String shaderSource, String path, ShaderSourceProvider provider) {
        
        StringBuilder sb = new StringBuilder();
        includeAllDependencies(shaderSource, path, provider, sb);

        return sb.toString();
    }
    
    /**
     * concatenates shaders recursively - deepest first.
     */
    private final static void includeAllDependencies(String source, String sourcePackage, ShaderSourceProvider provider, StringBuilder sb) {

        Matcher matcher = includePattern.matcher(source);
        while (matcher.find()) {

            String include = matcher.group(1);
            String path = sourcePackage;

            while(include.startsWith("../")) {
                
                include = include.substring(3);

                //goto parent folder
                for(int i = path.length()-1; i >= 0; i--) {
                    if(path.charAt(i) == '/') {
                        path = path.substring(0, i);
                        break;
                    }
                }
            }
            
            path += "/" + include;
            String srcToInclude = provider.provideShaderSource(path);
            if(srcToInclude != null)
                includeAllDependencies(srcToInclude, path, provider, sb);

        }
        sb.append(source);
        sb.append("\n");

    }

}
