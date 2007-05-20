package net.highteq.gamedev.nbm.glsleditor.dataobject;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class GlslVertexShaderLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/x-glsl-vertex-shader";

    private static final long serialVersionUID = 1L;

    public GlslVertexShaderLoader() {
        super("net.highteq.gamedev.nbm.glsleditor.dataobject.GlslDataObject");
    }

    protected String defaultDisplayName() {
        return NbBundle.getMessage(GlslFragmentShaderLoader.class, "LBL_glsl_vertex_shader_loader_name");
    }

    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GlslVertexShaderObject(primaryFile, this);
    }

    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }

}
