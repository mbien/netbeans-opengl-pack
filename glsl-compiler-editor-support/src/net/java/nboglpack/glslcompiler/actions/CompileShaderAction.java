/*
 * Created on 6. March 2007, 15:48
 */

package net.java.nboglpack.glslcompiler.actions;

import net.java.nboglpack.glslcompiler.GLSLCompilerService;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

/**
 * Action triggering shader compilation.
 * @author Michael Bien
 */
public final class CompileShaderAction extends NodeAction {
    

    protected void performAction(Node[] activatedNodes) {
            
        DataObject[] daos = new DataObject[activatedNodes.length];
        for (int i = 0; i < daos.length; i++) 
            daos[i] = activatedNodes[i].getLookup().lookup(DataObject.class);
        
        
        GLSLCompilerService compiler = Lookup.getDefault().lookup(GLSLCompilerService.class);
        
        compiler.compileShader(daos, true);
        
    }
    
    protected boolean enable(Node[] nodes) {
        
        for (int i = 0; i < nodes.length; i++) {
            
            DataObject dao = nodes[i].getLookup().lookup(DataObject.class);
            
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
    
    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }


    
}

