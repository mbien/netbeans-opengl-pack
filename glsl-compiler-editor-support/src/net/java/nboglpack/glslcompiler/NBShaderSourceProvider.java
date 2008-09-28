package net.java.nboglpack.glslcompiler;

import com.mbien.engine.util.ShaderSourceProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
final class NBShaderSourceProvider implements ShaderSourceProvider {

    private final ArrayList<DataObject> daoList;

    NBShaderSourceProvider() {
        super();
        this.daoList = new ArrayList<DataObject>();
    }

    public String provideShaderSource(String filePath) {
        File file = new File(filePath);
        return readSourceFile(file);
    }


    private final String readSourceFile(File sourceFile) {
        FileObject fo = FileUtil.toFileObject(sourceFile);
        try {
            if (fo != null) {
                DataObject dao = DataObject.find(fo);
                if (dao != null) {
                    String source = readSource(dao);
                    daoList.add(dao);
                    return source;
                }
            }
        } catch (BadLocationException ex) {
            log().log(Level.INFO, "can't read shader source", ex);
        } catch (DataObjectNotFoundException ex) {
            log().log(Level.INFO, "can't read shader source", ex);
        }
        return null;
    }
    
    public final String readSourceFromDao(DataObject dao) throws BadLocationException {
        return readSource(dao);
    }

    /**
     * tries first to read from DataObject and than from File.
     */
    private final String readSource(DataObject dao) throws BadLocationException {

        EditorCookie cookie = dao.getCookie(EditorCookie.class);

        Document doc;
        try {
            doc = cookie.openDocument();
            return doc.getText(0, doc.getLength());
        } catch (IOException ex) {
            log().log(Level.INFO, "unable to read shader source from document", ex);
            return null;
        }
        
    }

    DataObject[] getDependencies() {
        return daoList.toArray(new DataObject[daoList.size()]);
    }


    private static Logger log() {
        return Logger.getLogger(NBShaderSourceProvider.class.getName());
    }

}
