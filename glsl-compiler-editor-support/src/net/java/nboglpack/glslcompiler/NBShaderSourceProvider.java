package net.java.nboglpack.glslcompiler;

import com.mbien.engine.glsl.CodeFragment;
import com.mbien.engine.glsl.ShaderSourceLoader;
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
    @Override
    public CodeFragment<DataObject> loadShaderSource(String filePath) {

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
    @Override
    public final CodeFragment<DataObject> loadShaderSource(DataObject dao) {
        
        if(!dao.isValid())
            return null;

        EditorCookie cookie = dao.getCookie(EditorCookie.class);

        Document doc;
        try {
            doc = cookie.openDocument();
            return new CodeFragment<DataObject>(dao.getPrimaryFile().getNameExt(), doc.getText(0, doc.getLength()), dao);
        }catch (BadLocationException ex) {
            log().log(Level.INFO, "unable to read shader source from document", ex);
            return null;
        } catch (IOException ex) {
            log().log(Level.INFO, "unable to read shader source from document", ex);
            return null;
        }
    }

    @Override
    public boolean sameSource(DataObject t, String path) {
        String filePath = t.getPrimaryFile().getPath();

        if(filePath.charAt(0) == '/')
            filePath = filePath.substring(1);

        if(path.charAt(0) == '/')
            path = path.substring(1);

        return filePath.equals(path);
    }



    private static Logger log() {
        return Logger.getLogger(NBShaderSourceProvider.class.getName());
    }


}
