/*
 * ManifestEditorKit.java
 *
 * Created on October 20, 2005, 5:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor;

import javax.swing.text.Document;
import org.netbeans.editor.Syntax;
import org.netbeans.modules.editor.NbEditorKit;
import org.openide.ErrorManager;

/**
 *
 * @author Administrator
 */
public class GlslFragmentShaderEditorKit extends NbEditorKit {
    
    private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslFragmentShaderEditorKit.class.getName());
    private static final boolean LOG = LOGGER.isLoggable(ErrorManager.INFORMATIONAL);
    
    public static final String MIME_TYPE = "text/x-glsl-fragment-shader"; // NOI18N
    
    /** 
     * Creates a new instance of ManifestEditorKit 
     */
    public GlslFragmentShaderEditorKit() { 
    }
    
    /**
     * Create a syntax object suitable for highlighting Manifest file syntax
     */
    public Syntax createSyntax(Document doc) {
        if (LOG) {
            LOGGER.log(ErrorManager.INFORMATIONAL, "createSyntax"); // NOI18N
        }
        return new GlslSyntax(MIME_TYPE);
    }
    
    
    /**
     * Retrieves the content type for this editor kit
     */
    public String getContentType() {
        return MIME_TYPE;
    }
}