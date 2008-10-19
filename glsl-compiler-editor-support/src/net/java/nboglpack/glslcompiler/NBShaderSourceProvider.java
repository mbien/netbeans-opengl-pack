package net.java.nboglpack.glslcompiler;

import com.mbien.engine.glsl.GLSLFragment;
import com.mbien.engine.util.ShaderSourceLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private final static Pattern includePattern = Pattern.compile("//import\\s+((?:\\w|/|.)+)\\s*");

    NBShaderSourceProvider() {
    }

    public GLSLFragment<DataObject> loadShaderSource(String filePath) {
        File file = new File(filePath);
        return readSourceFile(file);
    }

    /**
     * Loades the shader from DataObject.
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

    public GLSLFragment<DataObject>[] loadWithDependencies(String filePath) {

        GLSLFragment<DataObject> main = readSourceFile(new File(filePath));
        return loadWithDependencies(main, parent(filePath));
    }

    public GLSLFragment<DataObject>[] loadWithDependencies(GLSLFragment<DataObject> main, String path) {

        ArrayList<GLSLFragment<DataObject>> fragments = new ArrayList<GLSLFragment<DataObject>>();

        includeAllDependencies(main, path, fragments);

        return fragments.toArray(new GLSLFragment[fragments.size()]);
    }

    /**
     * concatenates shaders recursively - deepest first.
     */
    private final void includeAllDependencies(GLSLFragment<DataObject> fragment, String sourcePackage, ArrayList<GLSLFragment<DataObject>> fragments) {

        Matcher matcher = includePattern.matcher(fragment.source);
        while (matcher.find()) {

            String include = matcher.group(1);
            String path = sourcePackage;

            while(include.startsWith("../")) {

                include = include.substring(3);

                path = parent(path);
            }

            path += "/" + include;
            GLSLFragment srcToInclude = readSourceFile(new File(path));
            if(srcToInclude != null)
                includeAllDependencies(srcToInclude, path, fragments);

        }
        fragments.add(fragment);

    }


    private final GLSLFragment<DataObject> readSourceFile(File sourceFile) {
        FileObject fo = FileUtil.toFileObject(sourceFile);
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
     * goto parent folder
     */
    private String parent(String path) {
        for(int i = path.length()-1; i >= 0; i--) {
            if(path.charAt(i) == '/') {
                return path.substring(0, i);
            }
        }
        return path;
    }

    private static Logger log() {
        return Logger.getLogger(NBShaderSourceProvider.class.getName());
    }


}
