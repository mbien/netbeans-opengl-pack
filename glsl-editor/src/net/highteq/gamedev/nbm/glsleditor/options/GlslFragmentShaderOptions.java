/*
 * ManifestOptions.java
 *
 * Created on October 20, 2005, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.highteq.gamedev.nbm.glsleditor.options;

import java.util.MissingResourceException;
import net.highteq.gamedev.nbm.glsleditor.GlslFragmentShaderEditorKit;
import org.netbeans.modules.editor.options.BaseOptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 *
 * @author Administrator
 */
public class GlslFragmentShaderOptions extends BaseOptions {
    
    public static String GLSL = "Glsl_fragment_shader"; // NOI18N
    
    /** Name of property. */
    private static final String HELP_ID = "editing.editor.glsl"; // NOI18N
    
    //no manifest specific options at this time
    static final String[] GLSL_PROP_NAMES = new String[] {};
    
    
    public GlslFragmentShaderOptions() {
        super(GlslFragmentShaderEditorKit.class, GLSL);
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
            return NbBundle.getMessage(GlslFragmentShaderOptions.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
    
}