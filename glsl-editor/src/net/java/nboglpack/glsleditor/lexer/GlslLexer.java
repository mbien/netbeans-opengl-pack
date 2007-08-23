/*
 * GlslLexer.java
 * 
 * Created on 19.08.2007, 18:31:16
 * 
 */

package net.java.nboglpack.glsleditor.lexer;

import net.java.nboglpack.glsleditor.GlslVocabularyManager;
import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;


/**
 * Lexer for the OpenGL Shading Language.
 * @author Michael Bien
 */
public class GlslLexer implements Lexer<GlslTokenId> {
 
 private static final int INIT          = 0;
 private static final int NAME          = 1;
 private static final int VALUE         = 2;// TODO editor; implement value tokenizing
 private static final int STRING_VALUE  = 3;
 private static final int PREPROCESSOR  = 4;
 private static final int COMMENT       = 5;
 private static final int ML_COMMENT    = 6;
 
 private final LexerInput input;
 private final TokenFactory<GlslTokenId> factory;
 private final GlslVocabularyManager manager;
 private final StringBuilder stringBuilder;
 
 private boolean inEscape = false;
    
 private int state = INIT;
 
 public GlslLexer(LexerRestartInfo<GlslTokenId> info, GlslVocabularyManager manager) {
     this.input = info.input();
     this.factory = info.tokenFactory();
     this.state = (info.state() != null) ? (Integer)info.state() : INIT;
     this.manager = manager;
     this.stringBuilder = new StringBuilder();
 }

    public Token<GlslTokenId> nextToken() {
        
        int character = input.read();
        
        while(character != LexerInput.EOF) {

            switch(state) {

                case INIT:

                    switch (character) {
                        case '(':
                        case ')':
                            state = INIT;
                            return factory.createToken(GlslTokenId.BRACE);
                        case '{':
                        case '}':
                            state = INIT;
                            return factory.createToken(GlslTokenId.CURLY_BRACE);
                        case ',':
                        case ';':
                            state = INIT;
                            return factory.createToken(GlslTokenId.SEPARATOR);
                        case '\r':
                            input.consumeNewline();
                        case '\n':
                            state = INIT;
                            return factory.createToken(GlslTokenId.END_OF_LINE);
                        case ' ':
                        case '\t':
                            state = INIT;
                            return factory.createToken(GlslTokenId.WHITESPACE);
                        case '#':
                            state = PREPROCESSOR;
                            break;
                        case '"':
                            state = STRING_VALUE;
                            break;
                        case '/':
                            int c = input.read();
                            if(c == '/') {
                                state = COMMENT;
                                character = c;
                            }else if(c == '*'){
                                state = ML_COMMENT;
                                character = c;
                            }else{
                                state = INIT;
                                input.backup(1);
                            }
                            break;
                        default:
                            if(Character.isJavaIdentifierPart(character)) {
                                state = NAME;
                            } else {
                                state = INIT;
                            }
                    }
                    break;

                case NAME:

                    switch (character) {
                        case '\r':
                            input.consumeNewline();
                        case '\n':
                            state = INIT;
                            return tokenize();
                        default:
                            if(!Character.isJavaIdentifierPart(character)) {
                                state = INIT;
                                return tokenize();
                            }
                    }
                    break;

                case STRING_VALUE:

                    switch (character) {
                        case '\\':
                            inEscape = true;
                            break;
                        case '"':
                            if(!inEscape) {
                                state = INIT;
                                return factory.createToken(GlslTokenId.STRING_VALUE);
                            }
                        default:
                            inEscape = false;
                            break;
                    }
                    break;

                case PREPROCESSOR:

                    switch (character) {
                        case '\r':
                            input.consumeNewline();
                        case '\n':
                            state = INIT;
                            return factory.createToken(GlslTokenId.PREPROCESSOR);
                    }
                    break;

                case COMMENT:

                    switch (character) {
                        case '\r':
                            input.consumeNewline();
                        case '\n':
                            state = INIT;
                            return factory.createToken(GlslTokenId.COMMENT);
                    }
                    break;

                case ML_COMMENT:

                    if (character == '*') {
                        if(input.read() == '/') {
                            state = INIT;
                            return factory.createToken(GlslTokenId.COMMENT);
                        }else{
                            input.backup(1);
                        }
                    }
                    break;

            }
            
            character = input.read();
            
        }
        
        return null;
    }
    
    private Token<GlslTokenId> tokenize() {
        
        if(stringBuilder.length() > 0)
            stringBuilder.delete(0, stringBuilder.length());
        
        // backup everything read
        input.backup(input.readLength()); 
        
        // assamble token
        char c;
        while(Character.isJavaIdentifierPart(c = ((char)input.read())))          
            stringBuilder.append(c);
        
        if(stringBuilder.length() > 0)
            input.backup(1);
                
        // categorise token
        GLSLElementDescriptor[] desc = manager.getDesc(stringBuilder.toString());
        
        if(desc != null) {
            
            if(desc[0].category != null) {
                
                if(desc[0].category == GLSLElementDescriptor.Category.BUILD_IN_FUNC) 
                    return factory.createToken(GlslTokenId.BUILD_IN_FUNC);

                if(desc[0].category == GLSLElementDescriptor.Category.BUILD_IN_VAR) 
                    return factory.createToken(GlslTokenId.BUILD_IN_VAR);
            
                return factory.createToken(GlslTokenId.KEYWORD);
                
            }
        }
       
        return factory.createToken(GlslTokenId.NAME);
    }

    public Object state() {
        return state;
    }

    public void release() {
    }

}
