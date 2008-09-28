package com.mbien.engine.glsl;


/**
 * Created on 15. March 2007, 15:11
 * @author Michael Bien
 */
public class CompilerMessage {
    
    
 public enum COMPILER_EVENT_TYPE {MSG, WARNING, ERROR}
    
 public final COMPILER_EVENT_TYPE type;
 public final String msg;

 /**
  * Line of the annotation relative to the fragment.
  */
 public final int line;

 /**
  * Shader fragment index.
  */
 public final int fragment;
 
 
   /** Creates a new instance of CompilerEvent */
    public CompilerMessage(COMPILER_EVENT_TYPE type, String msg) {
        this(type, msg, -1, -1);
    }
    
    /** Creates a new instance of CompilerEvent */
    public CompilerMessage(COMPILER_EVENT_TYPE type, String msg, int line, int fragment) {
        this.type = type;
        this.msg = msg;
        this.line = line;
        this.fragment = fragment;
    }

    
}
