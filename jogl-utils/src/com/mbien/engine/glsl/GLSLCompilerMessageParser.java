/*
 * Created on 29. March 2007, 17:07
 */

package com.mbien.engine.glsl;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for GLSL compiler messages. The main purose of this class is to find
 * warnings or erros in the compiler log.
 * @autor Michael Bien
 */
public class GLSLCompilerMessageParser {

  // may be null
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
            
            Matcher m = null;
            if(pattern != null)
                m = pattern.matcher(line);

            if(m != null && m.find()) {
                
                String g1 = m.group(1);
                String g2 = m.group(2);
                
                int linenumber;
                String type = null;
                try{
                    linenumber = Integer.parseInt(g1);
                    type = g2;
                }catch(NumberFormatException ex) {
                    try{
                        linenumber = Integer.parseInt(g2);
                        type = g1;
                    }catch(NumberFormatException ex2) {
                        linenumber = 0;
                        type = null;
                    }
                }
                                
                if(type == null)  {
                    messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.MSG,
                            "Internal error in "+GLSLCompilerMessageParser.class.getName()+" while parsing this line:\n    "+line, linenumber);
                }else if(type.equalsIgnoreCase("error"))  {
                    messages[i] = new CompilerMessage(CompilerMessage.COMPILER_EVENT_TYPE.ERROR, line, linenumber);
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
