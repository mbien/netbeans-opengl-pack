package net.java.nboglpack.glslcompiler;

import com.mbien.engine.glsl.GLSLFragment;
import com.mbien.engine.util.ShaderSourceLoader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 * NetBeans specific shader source provider.
 * @author Michael Bien
 */
final class NBShaderSourceProvider extends ShaderSourceLoader<DataObject> {


    NBShaderSourceProvider() {
    }

    /**
     * Loades a shader from file.
     */
    public GLSLFragment<DataObject> loadShaderSource(String filePath) {

        File file = new File(filePath);
        FileObject fo = FileUtil.toFileObject(file);

        try {
            if (fo != null) {
                DataObject dao = DataObject.find(fo);
                if (dao != null) {
                    return loadShaderSource(dao);
                }
            }
        } catch (DataObjectNotFoundException ex) {
            log().log(Level.INFO, "can't read shader source", ex);
        }
        
        return null;
    }

    /**
     * Loades a shader from DataObject.
     */
    public final GLSLFragment<DataObject> loadShaderSource(DataObject dao) {
        EditorCookie cookie = dao.getCookie(EditorCookie.class);

        Document doc;
        try {
            doc = cookie.openDocument();
            return new GLSLFragment<DataObject>(dao.getName(), doc.getText(0, doc.getLength()), dao);
        }catch (BadLocationException ex) {
            log().log(Level.INFO, "unable to read shader source from document", ex);
            return null;
        } catch (IOException ex) {
            log().log(Level.INFO, "unable to read shader source from document", ex);
            return null;
        }
    }

    private static Logger log() {
        return Logger.getLogger(NBShaderSourceProvider.class.getName());
    }


}
