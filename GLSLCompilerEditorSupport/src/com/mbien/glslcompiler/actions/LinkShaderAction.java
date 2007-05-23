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
public final class LinkShaderAction extends NodeAction {
    
    protected void performAction(Node[] activatedNodes) {
        
        DataObject[] daos = new DataObject[activatedNodes.length];
        for(int i = 0; i < activatedNodes.length; i++)
            daos[i] = (DataObject) activatedNodes[i].getLookup().lookup(DataObject.class);
        
        GLSLCompiler.getInstance().compileAndLinkProgram(daos);
    }
    
    protected boolean enable(Node[] nodes) {
        
        // just one fragment, vertex, and geometry shader can be linked together to a program
        if(nodes.length > 3)
            return false;
        
        DataObject[] dataObjects = new DataObject[nodes.length];
        boolean containsFragmentFile = false;
        boolean containsVertexFile = false;
        boolean containsGeometryFile = false;
        
        for (int i = 0; i < dataObjects.length; i++) {
            dataObjects[i] = (DataObject) nodes[i].getLookup().lookup(DataObject.class);
            
            
            String mimeType = FileUtil.getMIMEType(dataObjects[i].getPrimaryFile());

            if(mimeType == null)
                return false;

            if(mimeType.equals("text/x-glsl-vertex-shader")){
                if(containsVertexFile)
                    return false;
                containsVertexFile = true;
            }else if(mimeType.equals("text/x-glsl-fragment-shader")) {
                if(containsFragmentFile)
                    return false;
                containsFragmentFile = true;
            }else if(mimeType.equals("text/x-glsl-geometry-shader")) {
                if(containsGeometryFile)
                    return false;
                containsGeometryFile = true;
            }else{
                return false;
            }
        }

        return true;
    }
    
    public String getName() {
        return NbBundle.getMessage(LinkShaderAction.class, "CTL_LinkShaderAction");
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

