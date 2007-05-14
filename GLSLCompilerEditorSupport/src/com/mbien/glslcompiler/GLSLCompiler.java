package com.mbien.glslcompiler;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.glsl.GLSLCompileException;
import com.mbien.engine.glsl.CompilerEventListener;
import com.mbien.engine.glsl.CompilerEvent;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.glsl.GLSLCompileException;
import com.mbien.engine.glsl.GLSLCompilerMassageHandler;
import com.mbien.engine.glsl.GLSLShader;
import com.mbien.glslcompiler.annotation.CompilerAnnotation;
import com.mbien.glslcompiler.annotation.CompilerAnnotations;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.LineCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.DataObject;
import org.openide.text.Line;
import org.openide.util.Exceptions;
import org.openide.filesystems.FileUtil;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

/**
 * Created on 14. March 2007, 23:51
 * @author Michael Bien
 */
public class GLSLCompiler implements CompilerEventListener {
    
 static GLSLCompiler instance;
 
 private final GLSLCompilerMassageHandler massageHandler;
 private final GLWorker glWorker;
 private final InputOutput io;
 
 
    /** Creates a new instance of GLSLCompiler */
    GLSLCompiler(Pattern pattern) {
        massageHandler = new GLSLCompilerMassageHandler(pattern);
        glWorker = new GLWorker();
        io = IOProvider.getDefault().getIO("GLSL Compiler Output", false);
        
        massageHandler.addCompilerEventListener(this);
    }
    
    public static GLSLCompiler getInstance() {
//        if(instance == null)
//            instance = new GLSLCompiler();
        
        return instance;
    }
    
    
    public void compileShader(DataObject dao) {
        
        try{
            io.getOut().reset();
        }catch(IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        CompilerAnnotations.removeAnnotations(dao);
        
        Document doc = dao.getCookie(EditorCookie.class).getDocument();
        final GLSLShader shader;
        
        try{
            if(doc != null){
                String shaderSource = doc.getText(0, doc.getLength());
                String shaderName = dao.getPrimaryFile().getNameExt();
                GLSLShader.ShaderType shaderType = GLSLShader.ShaderType.parse(dao.getPrimaryFile().getExt());

                
                shader = new GLSLShader(shaderSource, shaderName, shaderType);
            }else{
                shader = new GLSLShader(FileUtil.toFile(dao.getPrimaryFile()));
            }
            shader.setThrowExceptionOnCompilerWarning(true);
            massageHandler.setSource(dao);
            
            GLRunnable work = new GLRunnable() {
                public void run(GLContext context) {
                    GL gl = context.getGL();

                    try{
                        shader.initShader(gl);
                    }catch(GLSLCompileException ex) {
                        massageHandler.parse(ex.getMessage());
                    }

                    shader.deleteShader(gl);
               }

            };
            glWorker.addWork(work);
            glWorker.work();
            
        }catch(BadLocationException e) {
            Exceptions.printStackTrace(e);
        }
        
    }
        
    
    public void compilerEvent(CompilerEvent e) {
               
        if(e.type == CompilerEvent.COMPILER_EVENT_TYPE.MSG) {
            io.getOut().println(e.msg);
        }else{
            
            CompilerAnnotation.AnnotationType type;
            if(e.type == CompilerEvent.COMPILER_EVENT_TYPE.ERROR)
                type = CompilerAnnotation.AnnotationType.ERROR;
            else
                type = CompilerAnnotation.AnnotationType.WARNING;
            
            CompilerAnnotations.addAnnotation((DataObject)e.source, type, e.msg, e.line);
            
            try  {
                io.getOut().println(e.msg, new HyperlinkProvider(e));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
    }
    
private class HyperlinkProvider implements OutputListener{
    
 private CompilerEvent event;
        
    private HyperlinkProvider(CompilerEvent event) {
        this.event = event;
    }
    
    public void outputLineAction(OutputEvent e) {
        
        //open file in editor and go to annotated line        
        DataObject dao = (DataObject)event.source;
        dao.getCookie(OpenCookie.class).open();
        
        LineCookie lineCookie = dao.getCookie(LineCookie.class);
        
        if(event.line > 0 && event.line < lineCookie.getLineSet().getLines().size())
            lineCookie.getLineSet().getCurrent(event.line-1).show(Line.SHOW_SHOW, 0);
        
    }

    public void outputLineCleared(OutputEvent arg0) {   }
    public void outputLineSelected(OutputEvent arg0) {    }
    
}


}
