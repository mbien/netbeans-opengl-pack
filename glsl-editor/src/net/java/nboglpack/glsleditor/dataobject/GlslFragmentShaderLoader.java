package net.java.nboglpack.glsleditor.dataobject;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class GlslFragmentShaderLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/x-glsl-fragment-shader";

    private static final long serialVersionUID = 1L;

    public GlslFragmentShaderLoader() {
        super("net.java.nboglpack.glsleditor.dataobject.GlslDataObject");
    }

    protected String defaultDisplayName() {
        return NbBundle.getMessage(GlslFragmentShaderLoader.class, "LBL_glsl_fragment_shader_loader_name");
    }

    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GlslFragmentShaderObject(primaryFile, this);
    }

    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }

}
