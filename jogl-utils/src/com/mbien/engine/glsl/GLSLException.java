package com.mbien.engine.glsl;


/**
 * Created on 29. March 2007, 15:33
 * @author Michael Bien
 */
public class GLSLException extends Exception {
    
    /** Creates a new instance of GLSLException */
    public GLSLException(String message) {
        super(message);
    }
    
    protected static String format(String shaderNames[], String massages[]) {
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shaderNames.length; i++) {
            sb.append(shaderNames[i]);
            if(i < shaderNames.length-1)
                sb.append(", ");
        }
        sb.append("\n");
        
        for (int i = 0; i < massages.length; i++) {
            sb.append("    ");
            sb.append(massages[i]);
            if(i < massages.length-1)
                sb.append("\n");
        }
        
        return sb.toString();
    }
}
