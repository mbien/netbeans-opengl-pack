package net.java.nboglpack.glslcompiler;

import com.mbien.engine.util.ShaderSourceProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

final class NBShaderSourceProvider implements ShaderSourceProvider {

    private final ArrayList<DataObject> daoList;

    NBShaderSourceProvider() {
        super();
        this.daoList = new ArrayList<DataObject>();
    }

    public String provideShaderSource(String path) {
        File file = new File(path);
        return readSourceFile(file);
    }


    private final String readSourceFile(File sourceFile) {
        FileObject fo = FileUtil.toFileObject(sourceFile);
        try {
            if (fo != null) {
                DataObject dao = DataObject.find(fo);
                if (dao != null) {
                    String source = readSource(dao, sourceFile);
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
        return readSource(dao, null);
    }

    /**
     * tries first to read from DataObject and than from File.
     */
    private final String readSource(DataObject dao, File sourceFile) throws BadLocationException {

        Document doc = dao.getCookie(EditorCookie.class).getDocument();

        if (doc != null) {
            return doc.getText(0, doc.getLength());
        } else {
            FileReader reader = null;
            
            if(sourceFile == null) {
                try{
                    FileObject fo = dao.getPrimaryFile();
                    if(fo != null) {
                        sourceFile = FileUtil.toFile(fo);
                    }
                }catch(Exception ex) {
                    log().log(Level.INFO, null, ex);
                }
            }

            if(sourceFile == null || !sourceFile.exists())
                return null;
            
            char[] buffer = new char[(int)sourceFile.length()];
            try {
                reader = new FileReader(sourceFile);
                reader.read(buffer);
            } catch (FileNotFoundException ex) {
                log().log(Level.INFO, null, ex);
            } catch (IOException ex) {
                log().log(Level.INFO, null, ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        log().log(Level.INFO, null, ex);
                    }
                }
            }
            return new String(buffer);
        }
        
    }

    DataObject[] getDependencies() {
        return daoList.toArray(new DataObject[daoList.size()]);
    }


    private static Logger log() {
        return Logger.getLogger(NBShaderSourceProvider.class.getName());
    }

}
