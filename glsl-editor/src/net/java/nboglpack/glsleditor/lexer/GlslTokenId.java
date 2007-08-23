/*
 * GlslTokenId.java
 * 
 * Created on 19.08.2007, 18:09:00
 * 
 */

package net.java.nboglpack.glsleditor.lexer;

import org.netbeans.api.lexer.TokenId;

/**
 * Enumeration of GLSL token ids.
 * @author Michael Bien
 */
public enum GlslTokenId implements TokenId {
    
 ERROR("glsl-error"),
 NAME("glsl-name"),
 FUNCTION("glsl-function"),
 KEYWORD("glsl-keyword"),
 BUILD_IN_FUNC("glsl-build-in-func"),
 BUILD_IN_VAR("glsl-build-in-var"),
 BRACE("glsl-brace"),
 VALUE("glsl-value"),
 CURLY_BRACE("glsl-curly-brace"),
 STRING_VALUE("glsl-string-value"),
 COMMENT("glsl-comment"),
 SEPARATOR("glsl-separator"),
 WHITESPACE("glsl-whitespace"),
 END_OF_LINE("glsl-end-of-line"),
 PREPROCESSOR("glsl-preprocessor");

  
 private final String primaryCategory;
   
 private GlslTokenId(String primaryCategory) {
    this.primaryCategory = primaryCategory;
 }

    public String primaryCategory() {
        return primaryCategory;
    }
  
}
