package com.mbien.engine.glsl;


/**
 * Created on 15. March 2007, 15:11
 * @author Michael Bien
 */
public class CompilerMessage {
    
    
 public enum COMPILER_EVENT_TYPE {MSG, WARNING, ERROR}
    
 public final COMPILER_EVENT_TYPE type;
 public final String msg;
 public final int line;
 
 
   /** Creates a new instance of CompilerEvent */
    public CompilerMessage(COMPILER_EVENT_TYPE type, String msg) {
        this(type, msg, -1);
    }
    
    /** Creates a new instance of CompilerEvent */
    public CompilerMessage(COMPILER_EVENT_TYPE type, String msg, int line) {
        this.type = type;
        this.msg = msg;
        this.line = line;
    }

    
}
