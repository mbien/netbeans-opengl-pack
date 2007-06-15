package com.mbien.engine.glsl;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 29. March 2007, 17:07
 * @autor Michael Bien
 */
public class GLSLCompilerMassageHandler {
    
  private Object source;
  private Pattern pattern;
  private final ArrayList<CompilerEventListener> listeners;
    
    /** Creates a new instance of CompilerOutputHandler */
    public GLSLCompilerMassageHandler(Pattern pattern) {
        this.listeners = new ArrayList<CompilerEventListener>();
        this.pattern = pattern;
    }
    
    public void parse(String str) {
        
        StringTokenizer tokenizer = new StringTokenizer(str, "\n");
        
        while(tokenizer.hasMoreTokens()) {
            
            String line = tokenizer.nextToken();
            
            Matcher m = pattern.matcher(line);
            if(m.find()) {
                
                String g1 = m.group(1);
                String g2 = m.group(2);
                
                int linenumber;
                String type;
                try{
                    linenumber = Integer.parseInt(g1);
                    type = g2;
                }catch(NumberFormatException ex) {
                    linenumber = Integer.parseInt(g2);
                    type = g1;
                }
                                
                if(type.equalsIgnoreCase("error"))  {
                    fireEvent(new CompilerEvent(source, CompilerEvent.COMPILER_EVENT_TYPE.ERROR, line, linenumber));
                }else{
                    fireEvent(new CompilerEvent(source, CompilerEvent.COMPILER_EVENT_TYPE.WARNING, line, linenumber));
                }
                
            }else{
                System.out.println("not matched: "+line);
                fireEvent(new CompilerEvent(source, CompilerEvent.COMPILER_EVENT_TYPE.MSG, line));
            }
        }
    }
    
    private final void fireEvent(CompilerEvent e) {
        for (CompilerEventListener compilerEventListener : listeners) {
            compilerEventListener.compilerEvent(e);
        }
    }
    
    public void setSource(Object source) {
        this.source = source;
    }
    
    public void addCompilerEventListener(CompilerEventListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeCompilerEventListener(CompilerEventListener listener) {
        this.listeners.remove(listener);
    } 
}
