/*
 * Glsl.java
 * 
 * Created on 24.09.2007, 00:46:53
 * 
 */

package net.java.nboglpack.glsleditor.glsl;

import java.util.List;
import javax.swing.text.AbstractDocument;
import net.java.nboglpack.glsleditor.lexer.GlslTokenId;
import org.netbeans.api.languages.ASTItem;
import org.netbeans.api.languages.ASTNode;
import org.netbeans.api.languages.ASTToken;
import org.netbeans.api.languages.SyntaxContext;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.lexer.TokenUtilities;

/**
 * Utilitie methods called from GLSL.nbs.
 * @author Michael Bien
 */
public final class Glsl {
    
 private final static String KEYWORD_FONT_COLOR = "<font color=808080>";
    
 private Glsl() {}
    
     /**
     * Assambles a human readable String containing the declaraton of a function.
     * Asumes that the current token of the SyntaxContext represents the function name.
     */
    public static final String createFunctionDeclarationString(SyntaxContext context) {
        
        AbstractDocument document = (AbstractDocument)context.getDocument();
        document.readLock();
        
        StringBuilder sb = new StringBuilder();
        
        try {
            
            TokenSequence sequence = TokenHierarchy.get(context.getDocument()).tokenSequence();
            sequence.move(context.getOffset());
            sequence.moveNext();
            
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
            
        } finally {
          document.readUnlock();
        }         
        
        return sb.toString();
    }
    
    /**
     * Assambles a human readable String containing the declaraton of a field and the field name itself.
     * Asumes that the current token of the SyntaxContext represents the field name.
     */
    public static final String createFieldDeclarationString(SyntaxContext context) {
        
        AbstractDocument document = (AbstractDocument)context.getDocument();
        document.readLock();
        
        StringBuilder sb = new StringBuilder();
        
        try {
            TokenSequence sequence = TokenHierarchy.get(context.getDocument()).tokenSequence();
            sequence.move(context.getOffset());
            sequence.moveNext();
            
            sb.append("<html>");
            sb.append(sequence.token().text());
            sb.append(KEYWORD_FONT_COLOR);
            sb.append(" :");

            int insertIndex = sb.length();

            // read forward
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

            // read backwards throw the declaration
            boolean skipToken = false;

            while(     sequence.movePrevious() 
                    && sequence.token().id() != GlslTokenId.SEMICOLON 
                    && sequence.token().id() != GlslTokenId.END_OF_LINE ) {

                token = sequence.token();

                if(!isIgnoredToken(token)) {

                    // we have a struct declaration; skip everything between { }
                    if(token.id() == GlslTokenId.BRACE && TokenUtilities.equals(token.text(), "}")) {
                        movePreviousUntil(sequence, GlslTokenId.BRACE, "}", "{");
                        continue;
                    }                

                    // skip token in case of an comma seperated identifier list
                    if(skipToken) {
                        if(     token.id() == GlslTokenId.BRACKET
                             && TokenUtilities.equals(token.text(), "]")    ) {
                            movePreviousUntil(sequence, GlslTokenId.BRACKET, "]", "[");
                            skipToken = false;
                        }else {
                            skipToken = false;
                        }
                        continue;
                    }

                    if(token.id() == GlslTokenId.COMMA) {
                        skipToken = true;
                        continue;
                    }

                    if(!TokenUtilities.equals(token.text(), "struct")) {
                        sb.insert(insertIndex, token.text());
                        sb.insert(insertIndex, " ");
                    }
                }

            }

            sb.append("</font></html>");
        } finally {
            document.readUnlock();
        } 
        
        
        return sb.toString();
    }
   
        
    public static final String createPreprocessorString(SyntaxContext context) {
        
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
    
    public static void process(SyntaxContext context) {
        System.out.println(context.getASTPath().getLeaf());
    }
    
    private static final void movePreviousUntil(TokenSequence sequence, GlslTokenId id, String countToken, String stopToken) {
        int counter = 1;
        while(sequence.movePrevious() && counter > 0) {
            if(sequence.token().id() == id) {
                if(TokenUtilities.equals(sequence.token().text(), stopToken)) {
                    counter--;
                }else if(TokenUtilities.equals(sequence.token().text(), countToken)){
                    counter++;
                }
            }
        }
    }
    
    private static final boolean isIgnoredToken(Token token) {
        return     token.id() == GlslTokenId.WHITESPACE
                || token.id() == GlslTokenId.COMMENT
                || token.id() == GlslTokenId.PREPROCESSOR;
    }
    
    private static final boolean isNode(ASTItem item, String nt) {
        return item != null && item instanceof ASTNode && ((ASTNode)item).getNT().equals(nt);
    }

    private static final boolean isToken(ASTItem item, String id) {
        return item != null && item instanceof ASTToken && ((ASTToken)item).getIdentifier().equals(id);
    }

    private static final boolean isTokenType(ASTItem item, String type) {
        return item != null && item instanceof ASTToken && ((ASTToken) item).getType().equals(type);
    }


}
