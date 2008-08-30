package net.java.nboglpack.glslcompiler;

import com.mbien.engine.util.ShaderSourceProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

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
                }else{
                    System.out.println("null dao");
                }
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
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
                    FileObject fo = dao.getFolder().getPrimaryFile();
                    if(fo != null) {
                        sourceFile = FileUtil.toFile(fo);
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

            if(sourceFile == null)
                return null;
            
            char[] buffer = new char[(int)sourceFile.length()];
            try {
                reader = new FileReader(sourceFile);
                reader.read(buffer);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return new String(buffer);
        }
        
    }

    DataObject[] getDependencies() {
        return daoList.toArray(new DataObject[daoList.size()]);
    }

}
