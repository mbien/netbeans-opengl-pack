package net.java.nboglpack.glslcompiler;

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
import com.mbien.engine.glsl.GLSLLinkException;
import com.mbien.engine.glsl.GLSLProgram;
import com.mbien.engine.glsl.GLSLShader;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotation;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import java.io.IOException;
import java.util.prefs.Preferences;
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
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

/**
 * Created on 14. March 2007, 23:51
 * @author Michael Bien
 */
public class GLSLCompilerImpl implements CompilerEventListener, GLSLCompilerService {
    
 
 private final GLSLCompilerMassageHandler massageHandler;
 private final GLWorker glWorker;
 private final InputOutput io;
 
 
    /** Creates a new instance of GLSLCompiler */
    public GLSLCompilerImpl() {
        
        glWorker = new GLWorker();
        
        Preferences pref = NbPreferences.forModule(GLSLCompilerService.class);
        String patternString = pref.get("GlslCompilerLogPattern", null);
        
        
        if(patternString == null || patternString.equals("")) {
        
            final String[] buffer = new String[1];
            glWorker.addWork(new GLRunnable() {
                public void run(GLContext context) {
                    GL gl = context.getGL();
                    buffer[0] = gl.glGetString(GL.GL_VENDOR);
                }
            });
            glWorker.work();
            
            // samples of compiler errors:
            // NV:  "(267) : error C0000: syntax error, unexpected identifier, expecting ';' or ',' at token ius";
            // ATI: "ERROR: 0:17: '-' :  wrong operand types  no operation '-' exists that takes a left-hand operand of type 'const int' and a right operand of type 'float' (or there is no acceptable conversion)"

            if(buffer[0].contains("NVIDIA")) {
                patternString = "\\((\\d+)\\)\\s*:\\s*(\\w+)";
            }else if(buffer[0].contains("ATI") || buffer[0].contains("AMD") ) {
                patternString = "(\\w+):\\s*\\d+:(\\d+):";
            }else{
                patternString = "()()";
            }
            
            pref.put("GlslCompilerLogPattern", patternString);
        }
        
        Pattern pattern = Pattern.compile(patternString);
        
        
        massageHandler = new GLSLCompilerMassageHandler(pattern);
        io = IOProvider.getDefault().getIO("GLSL Compiler Output", false);
        
        massageHandler.addCompilerEventListener(this);
        
    }
   
    
    /**
     * Compiles the shader and checks for compilation errors. Annotations are 
     * automatically placed if errors accure.
     * 
     * This is a fire and forget method. The OpenGL shader object will be immediately 
     * removed after compilation, successfull or not.
     * 
     * @return Returns true if shader compilation succeeds.
     */
    public boolean compileShader(DataObject... daos) {
        
        boolean success = true;
        
        try{
            io.getOut().reset();
        }catch(IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        for (DataObject dao : daos) {
            try{
                compile(dao, true);
            }catch(GLSLCompileException ex){
                success = false;
                massageHandler.parse(ex.getMessage());
            }
        }
        if(success)
            io.getOut().println("compilation successfull");
        
        return success;
    }
    
    
    /**
     * Compiles and links the shaders and checks for errors. Annotations are 
     * automatically placed if errors accure.
     * 
     * This is a fire and forget method. The OpenGL shader object will be immediately 
     * removed after compilation, successfull or not.
     * 
     * @return Returns true if shader program was successfully compiled and linked.
     */
    public boolean compileAndLinkProgram(DataObject... daos) {
        
        boolean success = true;
        
        try{
            io.getOut().reset();
        }catch(IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        GLSLShader[] shaders = new GLSLShader[daos.length];
        
        for (int i = 0; i < shaders.length; i++) {
            
            try{
                shaders[i] = compile(daos[i], false);
            }catch(GLSLCompileException ex) {
                success = false;
                massageHandler.parse(ex.getMessage());
            }
            
        }
        
        // success == true if all shaders compiled without errors
        if(success) {
            io.getOut().println("compilation successfull");
            try{
                io.getOut().println("linking shaders");
                link(shaders);
                io.getOut().println("link successfull");
            }catch(GLSLLinkException ex) {
                success = false;
                io.getErr().println("link error");
                io.getErr().println(ex.getMessage()); // TODO we need a testcase for not linkable shader
//                massageHandler.parse(ex.getMessage());
            }
        }
        return success;
    }
    
    /**
     * creates a new GLSLShader object from the given DataObject. This shader is
     * not initialized or bound to any GL context.
     * 
     * @param dao - the DataObject of the shader source
     */
    public GLSLShader createShader(DataObject dao) {
        
        GLSLShader shader = null;
        
        Document doc = dao.getCookie(EditorCookie.class).getDocument();
        if(doc != null) {
            try{
                String shaderSource = doc.getText(0, doc.getLength());
                String shaderName = dao.getPrimaryFile().getNameExt();
                GLSLShader.TYPE shaderType = GLSLShader.TYPE.parse(dao.getPrimaryFile().getExt());
                shader = new GLSLShader(shaderSource, shaderName, shaderType);
            }catch(BadLocationException ex){
                // not possible
                ex.printStackTrace();
            }
        }else{
            shader = new GLSLShader(FileUtil.toFile(dao.getPrimaryFile()));
        }
        shader.setThrowExceptionOnCompilerWarning(true);
        
        return shader;
    }
    
    private GLSLShader compile(final DataObject dao, final boolean deleteAfterCompilation) throws GLSLCompileException {

        
        final GLSLShader shader = createShader(dao);
        io.getOut().println("compiling shader: "+shader.getName());
        
        massageHandler.setSource(dao);
        CompilerAnnotations.removeAnnotations(dao);
        final GLSLCompileException[] exception = new GLSLCompileException[] {null};
        
        if(!shader.type.isSupported())
            throw new GLSLCompileException(shader.getName(), shader.type.toString().toLowerCase()+" shader not supported");
        
        GLRunnable compilerTask = new GLRunnable(){
            public void run(GLContext context) {
                GL gl = context.getGL();

                try{
                    shader.initShader(gl);
                }catch(GLSLCompileException ex) {
                    exception[0] = ex;
                }finally{
                    if(deleteAfterCompilation)
                        shader.deleteShader(gl);
                }
           }
       };
       glWorker.work(compilerTask);
       
       if(exception[0] != null)
            throw exception[0];
       
       return shader;
    }
    
    private void link(final GLSLShader[] shaders) throws GLSLLinkException {
        
        final GLSLLinkException[] exception = new GLSLLinkException[] {null};
        
        GLRunnable linkerTask = new GLRunnable(){
            public void run(GLContext context) {
                GL gl = context.getGL();
                GLSLProgram program = new GLSLProgram();
                try{
                    program.initProgram(gl, shaders);
                }catch(GLSLLinkException ex) {
                    exception[0] = ex;
                }finally{
                    // delete program and all attached shaders
                    program.deinitProgram(gl, true);
                }
           }
       };
       glWorker.work(linkerTask);
       
       // rethrow exception from Runnable
       if(exception[0] != null)
            throw exception[0];
    }
    
    
    public void compilerEvent(CompilerEvent e) {
               
        if(e.type == CompilerEvent.COMPILER_EVENT_TYPE.MSG) {
            io.getErr().println(e.msg);
        }else{
            
            CompilerAnnotation.AnnotationType type;
            if(e.type == CompilerEvent.COMPILER_EVENT_TYPE.ERROR)
                type = CompilerAnnotation.AnnotationType.ERROR;
            else
                type = CompilerAnnotation.AnnotationType.WARNING;
            
            CompilerAnnotations.addAnnotation((DataObject)e.source, type, e.msg, e.line);
            
            try  {
                io.getErr().println(e.msg, new HyperlinkProvider(e));
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

    public void outputLineCleared(OutputEvent arg0) {}
    public void outputLineSelected(OutputEvent arg0) {}
    
}


}
