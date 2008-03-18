/*
 * Created on 15. March 2007, 16:10
 */
package net.java.nboglpack.glslcompiler.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;


/**
 * @author Michael Bien
 */
public final class GlslCompilerOptions extends AdvancedOption {
    
    public String getDisplayName() {
        return NbBundle.getMessage(GlslCompilerOptions.class, "Option_DisplayName_GlslCompiler");
    }
    
    public String getTooltip() {
        return NbBundle.getMessage(GlslCompilerOptions.class, "Option_Tooltip_GlslCompiler");
    }
    
    public OptionsPanelController create() {
        return new GlslCompilerOptionsPanelController();
    }
    
}
