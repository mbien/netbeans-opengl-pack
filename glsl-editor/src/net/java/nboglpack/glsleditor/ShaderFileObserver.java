package net.java.nboglpack.glslcompiler;

import java.io.IOException;
import java.util.Iterator;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.RequestProcessor;

/**
 * Created on 26. March 2007, 00:49
 * @author Michael Bien
 */
public class ShaderFileObserver implements LookupListener, DocumentListener, Runnable {
    
 private DataObject currentDao = null;
 private final static RequestProcessor RP = new RequestProcessor("compiler");
 private final RequestProcessor.Task compilerTask;
 private int compileDelay = 500;
    
    /** Creates a new instance of ShaderFileObserver */
    public ShaderFileObserver() {
        this(null);
    }
    
    public ShaderFileObserver(DataObject dao) {
        currentDao = dao;
        compilerTask = RP.create(this);
        compilerTask.setPriority(Thread.MIN_PRIORITY);
    }

    public void resultChanged(LookupEvent e) {
        
        Lookup.Result<DataObject> res = (Lookup.Result<DataObject>)e.getSource();
//        System.out.println(res.allInstances().iterator().next());
        
        Iterator<DataObject> i = (Iterator<DataObject>)res.allInstances().iterator();
        if(!i.hasNext())
            return;
        
        DataObject dao = i.next();
        String mimeType = FileUtil.getMIMEType(dao.getPrimaryFile());

        if(mimeType == null)
            return;

        if( mimeType.equals("text/x-glsl-vertex-shader")
         || mimeType.equals("text/x-glsl-fragment-shader")
         || mimeType.equals("text/x-glsl-geometry-shader")) {

            if(currentDao != null) {
                EditorCookie ec = currentDao.getCookie(EditorCookie.class);
                if(ec.getDocument() != null)
                    ec.getDocument().removeDocumentListener(this);
            }
            currentDao = dao;

            EditorCookie cookie = currentDao.getCookie(EditorCookie.class);
            
            // TODO find out when a shader file has been opened; currently every shader document is loaded if focus changed to its file!
            try{// hack - thats not lazy!
                if(cookie.getDocument() == null) cookie.openDocument();
            }catch(IOException ex){};
            
            
            if(cookie.getDocument() != null) {
                cookie.getDocument().addDocumentListener(this);
                runCompileTask();
            }

        }
    }
    
    // DocumentListener
    public void insertUpdate(DocumentEvent arg0) {
        runCompileTask();
    }
    public void removeUpdate(DocumentEvent arg0) {
        runCompileTask();
    }
    public void changedUpdate(DocumentEvent arg0) {
        // endless loop if uncomented; seems like annatations fire changed events...
//        runCompileTask();
    }
    
    public void run() {
        GLSLCompiler.getInstance().compileShader(currentDao);
    }
    
    public final void runCompileTask() {
        compilerTask.schedule(compileDelay);
    }

}
