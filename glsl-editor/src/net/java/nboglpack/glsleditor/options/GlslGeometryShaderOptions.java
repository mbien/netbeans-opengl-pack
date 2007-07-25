package net.java.nboglpack.glsleditor.options;

import java.util.MissingResourceException;
import net.java.nboglpack.glsleditor.GlslGeometryShaderEditorKit;
import org.netbeans.modules.editor.options.BaseOptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 *
 * @author Michael Bien
 */
public class GlslGeometryShaderOptions extends BaseOptions {
    
    public static String GLSL = "Glsl_geometry_shader"; // NOI18N
    
    /** Name of property. */
    private static final String HELP_ID = "editing.editor.glsl"; // NOI18N
    
    //no manifest specific options at this time
    static final String[] GLSL_PROP_NAMES = new String[] {};
    
    
    public GlslGeometryShaderOptions() {
        super(GlslGeometryShaderEditorKit.class, GLSL);
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
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(HELP_ID);
    }
    
    /**
     * Look up a resource bundle message, if it is not found locally defer to
     * the super implementation
     */
    @Override
    protected String getString(String key) {
        try {
            return NbBundle.getMessage(GlslFragmentShaderOptions.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
    
}