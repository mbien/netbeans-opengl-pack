/*
 * Glsl.java
 * 
 * Created on 24.09.2007, 00:46:53
 * 
 */

package net.java.nboglpack.glsleditor.glsl;

import java.util.List;
import net.java.nboglpack.glsleditor.lexer.GlslTokenId;
import org.netbeans.api.languages.ASTItem;
import org.netbeans.api.languages.ASTNode;
import org.netbeans.api.languages.ASTToken;
import org.netbeans.api.languages.SyntaxContext;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.lexer.TokenUtilities;

/**
 *
 * @author Michael Bien
 */
public class Glsl {
    
 private final static String KEYWORD_FONT_COLOR = "<font color=808080>";
    
 private Glsl() {}
    
    public static String createFunctionDeclarationString(SyntaxContext context) {
        
        TokenSequence sequence = context.getTokenSequence();        
        
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        
        int moved = 0;
        while(sequence.movePrevious() && isIgnoredToken(sequence.token()))
            moved++;
        
        String type = sequence.token().toString();
        while(moved-- >= 0)
            sequence.moveNext();
        
        // append function name
        sb.append(sequence.token().text());
        
        while(!TokenUtilities.equals(sequence.token().text(), "("))
            sequence.moveNext();

        sb.append("(");
        
        Token token;
        boolean first = true;
        while(sequence.moveNext() && !TokenUtilities.equals(sequence.token().text(), ")")) {
            
            token = sequence.token();
            
            if(!isIgnoredToken(token)) {
                
                if(first) {
                    sb.append(KEYWORD_FONT_COLOR);
                }else if(token.id() != GlslTokenId.COMMA && token.id() != GlslTokenId.BRACKET && token.id() != GlslTokenId.INTEGER_LITERAL) {
                    sb.append(" ");
                }
                
                if(!TokenUtilities.equals(token.text(), "void")) {

                    moved = 0;
                    while(sequence.moveNext() && isIgnoredToken(sequence.token())) 
                        moved++;
                    
                    if(sequence.token().id() == GlslTokenId.COMMA || TokenUtilities.equals(sequence.token().text(), ")"))
                        sb.append("</font>");
                    
                    while(moved-- >= 0)
                        sequence.movePrevious();
                    
                    sb.append(token.text());
                    
                    if(token.id() == GlslTokenId.COMMA) 
                        sb.append(KEYWORD_FONT_COLOR);
                }
                
                first = false;
            }
            
        }
        
        sb.append("</font>)");
        if(!"void".equals(type)) {
            sb.append(" : ");
            sb.append(KEYWORD_FONT_COLOR);
            sb.append(type);
            sb.append("</font>");
        }
        sb.append("</html>");
        
        return sb.toString();
    }
    
    
    public static String createFieldDeclarationString(SyntaxContext context) {
        
        TokenSequence sequence = context.getTokenSequence();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<html>");
        sb.append(sequence.token().text());
        
        int moved = 0;
        Token token;
        while(     sequence.moveNext()
                && sequence.token().id() != GlslTokenId.SEMICOLON
                && sequence.token().id() != GlslTokenId.COMMA
                && sequence.token().id() != GlslTokenId.EQ  ) {
            token = sequence.token();
            if(!isIgnoredToken(token)) 
                sb.append(token);
            moved++;
        }
        while(moved-- >= 0)
            sequence.movePrevious();
        
        sb.append(" : ");
        sb.append(KEYWORD_FONT_COLOR);
        int insertIndex = sb.length();
        
        boolean skipToken = false;
        boolean skipArrayIndex = false;
        while(     sequence.movePrevious() 
                && sequence.token().id() != GlslTokenId.SEMICOLON 
                && sequence.token().id() != GlslTokenId.END_OF_LINE ) {
            
            token = sequence.token();
            if(!isIgnoredToken(token)) {
                
                if(skipToken) {
                    if(     token.id() == GlslTokenId.BRACKET
                         && TokenUtilities.equals(token.text(), "]")    ) {
                        skipArrayIndex = true;
                    }else if(   skipArrayIndex
                             && token.id() == GlslTokenId.BRACKET
                             && TokenUtilities.equals(token.text(), "[")    ) {
                        skipArrayIndex = false;
                        skipToken = false;
                    }
                    continue;
                }
                
                if(token.id() == GlslTokenId.COMMA) {
                    skipToken = true;
                    continue;
                }
                sb.insert(insertIndex, " ");
                sb.insert(insertIndex, token.text());
            }
            
        }
        sb.append("</font>");
        sb.append("</html>");
        
        
        return sb.toString();
    }
    
//    public static String createStructFieldDeclarationString(SyntaxContext context) {
//        TokenSequence sequence = context.getTokenSequence();
//        Token fieldToken = sequence.token();
//    }
    
        
    public static String createPreprocessorString(SyntaxContext context) {
        
        ASTNode node = (ASTNode)context.getASTPath().getLeaf();
        List<ASTItem> children = node.getChildren();
        
        String str = null;
        
        for (ASTItem item : children)
            if (isTokenType(item, GlslTokenId.PREPROCESSOR.name()))
                str = ((ASTToken)item).getIdentifier();
        
        
        for(int i = 0; i < str.length(); i++) {
            
            char c = str.charAt(i);
            
            if(c != '#' && !Character.isWhitespace(c))
                for(int j = str.length()-1; j > i; j--)
                    if(!Character.isWhitespace(str.charAt(j)))
                        return str.substring(i, j+1);
            
        }
        
        return str;
    }
    
    public static boolean isIgnoredToken(Token token) {
        return     token.id() == GlslTokenId.WHITESPACE
                || token.id() == GlslTokenId.COMMENT
                || token.id() == GlslTokenId.PREPROCESSOR;
    }
    
    public static final boolean isNode(ASTItem item, String nt) {
        return item != null && item instanceof ASTNode && ((ASTNode)item).getNT().equals(nt);
    }

    public static final boolean isToken(ASTItem item, String id) {
        return item != null && item instanceof ASTToken && ((ASTToken)item).getIdentifier().equals(id);
    }

    public static final boolean isTokenType(ASTItem item, String type) {
        return item != null && item instanceof ASTToken && ((ASTToken) item).getType().equals(type);
    }


}
