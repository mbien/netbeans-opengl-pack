/*
 * RestoreColoring.java
 *
 * Created on October 20, 2005, 5:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor;

import org.netbeans.editor.LocaleSupport;
import org.netbeans.editor.Settings;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbBundle;

/**
 * @deprecated migrate to lexer
 * @author Administrator
 */
public class GlslEditorModule extends ModuleInstall {
    
    /**
     * <code>Localizer</code> passed to editor.
     */
    private static LocaleSupport.Localizer localizer;
    
    /**
     * Registers properties editor, installs options and copies settings.
     * Overrides superclass method.
     */
    public void restored() {
        addInitializer();
        installOptions();
    }
    
    /**
     * Uninstalls properties options.
     * And cleans up editor settings copy.
     * Overrides superclass method.
     */
    public void uninstalled() {
        uninstallOptions();
    }
    
    /**
     * @deprecated migrate to lexer
     * Adds initializer and registers editor kit.
     */
    public void addInitializer() {
        Settings.addInitializer(new GlslSettingsInitializer());
    }
    
    /**
     * @deprecated migrate to lexer
     * Installs properties editor and print options.
     */
    public void installOptions() {
        // Adds localizer.
        LocaleSupport.addLocalizer(localizer = new LocaleSupport.Localizer() {
            public String getString(String key) {
                return NbBundle.getMessage(GlslEditorModule.class, key);
            }
        });
    }
    
    /** Uninstalls properties editor and print options.
 * @deprecated migrate to lexer
     */
    public void uninstallOptions() {
        // remove localizer
        LocaleSupport.removeLocalizer(localizer);
    }
    
}