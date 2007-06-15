/*
 * ManifestOptionsBeanInfo.java
 *
 * Created on October 21, 2005, 12:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor.options;

import java.util.MissingResourceException;
import org.netbeans.modules.editor.options.BaseOptionsBeanInfo;
import org.netbeans.modules.editor.options.OptionSupport;
import org.openide.util.NbBundle;

/**
 *
 * @author Administrator
 */
public class GlslVertexShaderOptionsBeanInfo extends BaseOptionsBeanInfo {
    
    /**
     * Constructor. The parameter in the superclass constructor is the
     * icon prefix.
     */
    public GlslVertexShaderOptionsBeanInfo() {
        super("/net/java/nboglpack/glsleditor/resources/OptionsIcon"); // NOI18N
    }
    
    /*
     * Gets the property names after merging it with the set of properties
     * available from the BaseOptions from the editor module
     */
    protected String[] getPropNames() {
        return OptionSupport.mergeStringArrays(
                super.getPropNames(),
                GlslVertexShaderOptions.GLSL_PROP_NAMES);
    }
    
    /**
     * Get the class described by this bean info
     */
    protected Class getBeanClass() {
        return GlslVertexShaderOptions.class;
    }
    
    /**
     * Look up a resource bundle message, if it is not found locally defer to
     * the super implementation
     */
    protected String getString(String key) {
        try {
            return NbBundle.getMessage(GlslVertexShaderOptionsBeanInfo.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
}