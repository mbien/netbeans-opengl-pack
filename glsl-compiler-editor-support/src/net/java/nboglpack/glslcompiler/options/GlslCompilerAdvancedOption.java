package net.java.nboglpack.glslcompiler.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;


/**
 * Created on 15. March 2007, 16:10
 * @author Michael Bien
 */
public final class GlslCompilerAdvancedOption extends AdvancedOption {
    
    public String getDisplayName() {
        return NbBundle.getMessage(GlslCompilerAdvancedOption.class, "AdvancedOption_DisplayName_GlslCompiler");
    }
    
    public String getTooltip() {
        return NbBundle.getMessage(GlslCompilerAdvancedOption.class, "AdvancedOption_Tooltip_GlslCompiler");
    }
    
    public OptionsPanelController create() {
        return new GlslCompilerOptionsPanelController();
    }
    
}
