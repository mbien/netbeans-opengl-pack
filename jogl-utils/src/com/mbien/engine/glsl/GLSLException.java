package com.mbien.engine.glsl;


/**
 * Created on 29. March 2007, 15:33
 * @author Michael Bien
 */
public class GLSLException extends Exception {

    public final Object source;
    
    /** Creates a new instance of GLSLException */
    public GLSLException(Object source, String message) {
        super(message);
        this.source = source;
    }

    
    protected static String format(GLSLShader shader, String massages[]) {
        
        StringBuilder sb = new StringBuilder();
        sb.append(shader.getName());
        if(shader.fragments != null) {
            for (int i = 0; i < shader.fragments.length; i++) {
                sb.append(shader.fragments[i].name);
                if(i < shader.fragments.length-1)
                    sb.append(", ");
            }
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
    protected static String format(GLSLProgram program, String massages[]) {

        StringBuilder sb = new StringBuilder();
//        sb.append(program.getName());
//        if(program.fragments != null) {
//            for (int i = 0; i < program.fragments.length; i++) {
//                sb.append(program.fragments[i].name);
//                if(i < program.fragments.length-1)
//                    sb.append(", ");
//            }
//        }
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
