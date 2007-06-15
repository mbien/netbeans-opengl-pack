/*
 * ManifestOptions.java
 *
 * Created on October 20, 2005, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor.options;

import java.util.MissingResourceException;
import org.netbeans.modules.editor.options.BaseOptions;
import net.java.nboglpack.glsleditor.GlslVertexShaderEditorKit;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 *
 * @author Administrator
 */
public class GlslVertexShaderOptions extends BaseOptions {
    
    public static String GLSL = "Glsl_vertex_shader"; // NOI18N
    
    /** Name of property. */
    private static final String HELP_ID = "editing.editor.glsl"; // NOI18N
    
    //no manifest specific options at this time
    static final String[] GLSL_PROP_NAMES = new String[] {};
    
    
    public GlslVertexShaderOptions() {
        super(GlslVertexShaderEditorKit.class, GLSL);
    }
    
    /**
     * Determines the class of the default indentation engine, in this case
     * ManifestIndentEngine.class
     */
//    protected Class getDefaultIndentEngineClass() {
//        return ManifestIndentEngine.class;
//    }
    
    /**
     * Gets the help ID
     */
    public HelpCtx getHelpCtx() {
        return new HelpCtx(HELP_ID);
    }
    
    /**
     * Look up a resource bundle message, if it is not found locally defer to
     * the super implementation
     */
    protected String getString(String key) {
        try {
            return NbBundle.getMessage(GlslVertexShaderOptions.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
    
}