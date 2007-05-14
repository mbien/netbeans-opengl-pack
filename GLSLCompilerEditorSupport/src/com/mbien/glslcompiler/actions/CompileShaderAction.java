package com.mbien.glslcompiler.actions;

import com.mbien.glslcompiler.GLSLCompiler;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * Created on 6. March 2007, 15:48
 * @author Michael Bien
 */
public final class CompileShaderAction extends CookieAction {
    

    protected void performAction(Node[] activatedNodes) {
        DataObject dataObject = (DataObject) activatedNodes[0].getLookup().lookup(DataObject.class);
        
        GLSLCompiler c = GLSLCompiler.getInstance();
        c.compileShader(dataObject);
        
    }
    
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }
    
    public String getName() {
        return NbBundle.getMessage(CompileShaderAction.class, "CTL_CompileShaderAction");
    }
    
    protected Class[] cookieClasses() {
        return new Class[] {
            DataObject.class
        };
    }
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}

