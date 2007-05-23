package com.mbien.glslcompiler.actions;

import com.mbien.glslcompiler.GLSLCompiler;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

/**
 * Created on 6. March 2007, 15:48
 * @author Michael Bien
 */
public final class CompileShaderAction extends NodeAction {
    

    protected void performAction(Node[] activatedNodes) {
            
        DataObject[] daos = new DataObject[activatedNodes.length];
        for (int i = 0; i < daos.length; i++) {
            daos[i] = (DataObject) activatedNodes[i].getLookup().lookup(DataObject.class);
        }
        
        GLSLCompiler c = GLSLCompiler.getInstance();
        c.compileShader(daos);
        
    }
    
    protected boolean enable(Node[] nodes) {
        
        for (int i = 0; i < nodes.length; i++) {
            
            DataObject dao = (DataObject) nodes[i].getLookup().lookup(DataObject.class);
            
            if(dao == null)
                return false;
            
            String mimeType = FileUtil.getMIMEType(dao.getPrimaryFile());

            if(mimeType == null)
                return false;

            if(!(  mimeType.equals("text/x-glsl-vertex-shader")
                || mimeType.equals("text/x-glsl-fragment-shader")
                || mimeType.equals("text/x-glsl-geometry-shader"))  ) {
                return false;
            }
        }
        
        return true;
    }
    
    public String getName() {
        return NbBundle.getMessage(CompileShaderAction.class, "CTL_CompileShaderAction");
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

