/*
 * GlslGeometryShaderEditorKit.java
 * 
 * Created on 25.07.2007, 15:52:56
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor;

import javax.swing.text.Document;
import org.netbeans.editor.Syntax;
import org.netbeans.modules.editor.NbEditorKit;
import org.openide.ErrorManager;

/**
 *
 * @author Michael Bien
 */
public class GlslGeometryShaderEditorKit extends NbEditorKit {
    
    private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslGeometryShaderEditorKit.class.getName());
    private static final boolean LOG = LOGGER.isLoggable(ErrorManager.INFORMATIONAL);
    
    public static final String MIME_TYPE = "text/x-glsl-geometry-shader"; // NOI18N
    
    /** 
     * Creates a new instance of ManifestEditorKit 
     */
    public GlslGeometryShaderEditorKit() { 
    }
    
    /**
     * Create a syntax object suitable for highlighting Manifest file syntax
     */
    @Override
    public Syntax createSyntax(Document doc) {
        if (LOG) {
            LOGGER.log(ErrorManager.INFORMATIONAL, "createSyntax"); // NOI18N
        }
        return new GlslSyntax(MIME_TYPE);
    }
    
    
    /**
     * Retrieves the content type for this editor kit
     */
    @Override
    public String getContentType() {
        return MIME_TYPE;
    }
    
}