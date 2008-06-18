package com.mbien.engine.glsl;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 29. March 2007, 17:07
 * @autor Michael Bien
 */
public class GLSLCompilerMessageParser {
    
  private Pattern pattern;
    
    /** Creates a new instance of CompilerOutputHandler */
    public GLSLCompilerMessageParser(Pattern pattern) {
        this.pattern = pattern;
    }
    
    public CompilerMessage[] parse(String str) {
        
        StringTokenizer tokenizer = new StringTokenizer(str, "\n");
        CompilerMessage[] messages = new CompilerMessage[tokenizer.countTokens()];
        
        for (int i = 0; i < messages.length; i++) {
            
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
                    try{
                        linenumber = Integer.parseInt(g2);
                        type = g1;
                    }catch(NumberFormatException iex) {
                        linenumber = 0;
                        type = "internal";
                    }
                }
                                
                if(type.equalsIgnoreCase("error"))  {
                    messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.ERROR, line, linenumber);
                }else if(type.equalsIgnoreCase("internal"))  {
                    messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.ERROR, "Internal error in "+CompilerMessage.class.getName()+" while parsing this line: "+line, linenumber);
                }else{
                    messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.WARNING, line, linenumber);
                }
                
            }else{
                messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.MSG, line);
            }
        }
        
        return messages;
    }
    
}
