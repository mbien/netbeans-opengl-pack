package net.java.nboglpack.glsleditor.dataobject;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;


/**
 * @author Michael Bien
 */
public class GlslGeometryShaderDataLoader extends UniFileLoader {
    
    public static final String REQUIRED_MIME = "text/x-glsl-geometry-shader";
    
    private static final long serialVersionUID = 1L;
    
    public GlslGeometryShaderDataLoader() {
        super("net.java.nboglpack.glsleditor.dataobject.GlslGeometryShaderDataObject");
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(GlslGeometryShaderDataLoader.class, "LBL_GlslGeometryShader_loader_name");
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GlslGeometryShaderDataObject(primaryFile, this);
    }
    
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
    
}
