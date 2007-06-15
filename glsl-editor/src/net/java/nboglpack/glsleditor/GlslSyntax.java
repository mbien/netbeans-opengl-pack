/*
 * ManifestSyntax.java
 *
 * Created on July 20, 2005, 10:37 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.java.nboglpack.glsleditor;

import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor;
import org.netbeans.editor.Syntax;
import org.netbeans.editor.TokenID;
import org.openide.ErrorManager;

/**
 *
 * @author Mathias Henze
 * @author Michael Bien
 */
public class GlslSyntax extends Syntax {
    
    private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslSyntax.class.getName());
    private static final boolean LOG = LOGGER.isLoggable(ErrorManager.INFORMATIONAL);
    private int tokenStart;
    private boolean inEscape;
    private GlslVocabularyManager vocabularies;
    
    private static final int ISI_NAME = 1;
    private static final int ISI_KEYWORD = 2;
    private static final int ISA_CR = 3;
    private static final int ISI_VALUE = 5;
    private static final int ISI_STRING_VALUE = 7;
    private static final int ISI_PREPROC = 9;
    private static final int ISI_COMMENT = 10;
    private static final int ISI_ML_COMMENT = 11;
    
    /** Creates a new instance of ManifestSyntax */
    public GlslSyntax(String mimetype) {
        tokenContextPath = GlslTokenContext.contextPath;
        vocabularies = GlslVocabularyManager.getInstance(mimetype);
    }
    
    protected TokenID parseToken() {
        TokenID result = doParseToken();
        if (LOG) {
            LOGGER.log(ErrorManager.INFORMATIONAL, "parseToken: " + result);
        }
        return result;
    }
    
    private TokenID doParseToken() {
        
        char lastChar=' ';
        char actChar;
        
        while (offset < stopOffset) {
            
            if(offset > 0)
                lastChar = buffer[offset-1];
            
            actChar = buffer[offset];
            
            switch (state) {
                
                case INIT:
                    
                    tokenStart = offset;    // mark start of token
                    
                    switch (actChar) {
                        case '(':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.BRACE;
                        case ')':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.BRACE;
                        case '{':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.CURLY_BRACE;
                        case '}':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.CURLY_BRACE;
                        case ',':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.SEPARATOR;
                        case '\n':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.END_OF_LINE;
                        case ' ':
                        case '\t':
                            state = INIT;
                            offset++;
                            return GlslTokenContext.WHITESPACE;
                        case '\r':
                            state = ISA_CR;
                            break;
                        case '#':
                            state = ISI_PREPROC;
                            break;
                        case '"':
                            state = ISI_STRING_VALUE;
                            break;
                        case '/':
                            if(lastChar=='/') 
                                state = ISI_COMMENT;
                            break;
                        case '*':
                            if(lastChar=='/') 
                                state = ISI_ML_COMMENT;
                            break;
                        default:
                            if(Character.isJavaIdentifierPart(actChar)) {
                                state = ISI_NAME;
                            } else {
                                state = INIT;
                            }
                    }
                    break;

                case ISI_NAME:
                    
                    switch (actChar) {
                        case '\r':
                            state = ISA_CR;
                            return findTokenID(buffer, tokenStart, offset);
                        default:
                            if(!Character.isJavaIdentifierPart(actChar)) {
                                state = INIT;
                                return findTokenID(buffer, tokenStart, offset);
                            }
                    }
                    break;

                case ISI_STRING_VALUE:
                    
                    switch (actChar) {
                        case '\\':
                            inEscape=true;
                            break;
                        case '"':
                            if(!inEscape) {
                                offset++;
                                state = INIT;
                                return GlslTokenContext.STRING_VALUE;
                            }
                        default:
                            inEscape= false;
                            break;
                    }
                    break;

                case ISI_PREPROC:
                    
                    switch (actChar) {
                        case '\n':
                            offset++;
                            state = INIT;
                            return GlslTokenContext.PREPROCESSOR;
                        case '\r':
                            offset++;
                            state = ISA_CR;
                            return GlslTokenContext.PREPROCESSOR;
                    }
                    break;

                case ISI_COMMENT:
                    
                    switch (actChar) {
                        case '\n':
                            offset++;
                            state = INIT;
                            return GlslTokenContext.COMMENT;
                        case '\r':
                            offset++;
                            state = ISA_CR;
                            return GlslTokenContext.COMMENT;
                    }
                    break;

                case ISI_ML_COMMENT:
                    
                    if (actChar == '/') {
                        if(lastChar=='*') {
                            offset++;
                            state = INIT;
                            return GlslTokenContext.COMMENT;
                        }
                    }
                    break;

                case ISA_CR:
                    
                    if (actChar == '\n') {
                        offset++;       
                        state = INIT;
                        return GlslTokenContext.END_OF_LINE;
                    }

            }
            offset++;
        }
        
        switch (state) {
            case ISI_NAME:
                state = INIT;
                return findTokenID(buffer, tokenStart, offset);
            case ISI_STRING_VALUE:
                state = INIT;
                return GlslTokenContext.STRING_VALUE;
            case ISI_COMMENT:
                state = INIT;
                return GlslTokenContext.COMMENT;
            case ISI_ML_COMMENT:
                return GlslTokenContext.COMMENT;
        }
        
        return null;
    }
    
    private TokenID findTokenID(char[] buffer, int start, int end) {
        
        // it is possible that the syntax lexer stops scanning inside a token.
        // => search real token start and/or end indices and assamble token
        // search end of token to the right
        if(end < buffer.length && Character.isJavaIdentifierPart(buffer[end])) {
            int index = end;
            while(index < buffer.length) {
                if(!Character.isJavaIdentifierPart(buffer[index])) {
                    end = index;
                    break;
                }
                index++;
            }
        }           
        // search end of token to the left
        if(start-1 >= 0 && Character.isJavaIdentifierPart(buffer[start-1])) {
            int index = start;
            while(index >= 0) {
                if(!Character.isJavaIdentifierPart(buffer[index-1])) {
                    start = index;
                    break;
                }
                index--;
            }
        }
        String token = new String(buffer, start, end-start);
        GLSLElementDescriptor[] desc = vocabularies.getDesc(token);
        
        if(desc != null) {
            
            if(desc[0].category != null) {
                
                if(desc[0].category == GLSLElementDescriptor.Category.BUILD_IN_FUNC) 
                    return GlslTokenContext.BUILD_IN_FUNC;

                if(desc[0].category == GLSLElementDescriptor.Category.BUILD_IN_VAR) 
                    return GlslTokenContext.BUILD_IN_VAR;
            
//                if(     "keyword".equals(desc.getCategory())
//                    ||  "iteration".equals(desc.getCategory())
//                    ||  "selection".equals(desc.getCategory())
//                    ||  "qualifier".equals(desc.getCategory())
//                    ||  "type".equals(desc.getCategory())
//                    ||  desc.getCategory().startsWith("jump")  ) 
                return GlslTokenContext.KEYWORD;
                
            }
        }
        
        return GlslTokenContext.NAME;
    }
}
