package net.java.nboglpack.glslcompiler;


/*
 * Created on 14. March 2007, 23:51
 */
import com.mbien.engine.glsl.GLSLCompileException;
import com.mbien.engine.glsl.CompilerMessage;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.glsl.GLSLCompilerMessageParser;
import com.mbien.engine.glsl.GLSLLinkException;
import com.mbien.engine.glsl.GLSLProgram;
import com.mbien.engine.glsl.GLSLShader;
import com.mbien.engine.util.GLWorker;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotation;
import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.LineCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.Line;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

/**
 * Implementation for the GLSL compiler service.
 * @author Michael Bien
 */
public class GLSLCompilerImpl implements GLSLCompilerService {
    
 
 private final GLSLCompilerMessageParser compilerParser;
 private final GLWorker glWorker;
 private final InputOutput io;
 
 
    public GLSLCompilerImpl() {
        
        glWorker = Lookup.getDefault().lookup(GLWorker.class);
        
        Preferences pref = NbPreferences.forModule(GLSLCompilerService.class);
        String patternString = pref.get("GlslCompilerLogPattern", null);
        
        if(patternString == null || patternString.trim().isEmpty()) {
            
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
                patternString = null;
            }
            
            pref.put("GlslCompilerLogPattern", patternString);
        }
        
        Pattern pattern = null;

        if(patternString != null) {
            try{
                pattern = Pattern.compile(patternString);
            }catch(PatternSyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        compilerParser = new GLSLCompilerMessageParser(pattern);
        io = IOProvider.getDefault().getIO("GLSL Compiler Output", false);
        
    }
   
    
    public boolean compileShader(DataObject[] daos, boolean printOut) {
        
        boolean success = true;
        
        if(printOut) {
            try{
                io.getOut().reset();
            }catch(IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        for (DataObject dao : daos) {
            try{
                GLSLShader shader = compile(dao, printOut, true);
                if(shader != null)
                    processCompilerMsg(dao, shader, printOut);
            }catch(GLSLCompileException ex){
                success = false;
                annotateMessage(dao, compilerParser.parse(ex.getMessage()), printOut);
            }
        }
        
        if(success && printOut)
            io.getOut().println("compilation successfull");
        
        return success;
    }
    
    
    public boolean compileAndLinkProgram(DataObject[] daos, boolean printOut) {
        
        boolean success = true;
        
        if(printOut) {
            try{
                io.getOut().reset();
            }catch(IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        GLSLShader[] shaders = new GLSLShader[daos.length];
        
        for (int i = 0; i < shaders.length; i++) {
            
            try{
                shaders[i] = compile(daos[i], printOut, false);
                if(shaders[i] != null)
                    processCompilerMsg(daos[i], shaders[i], printOut);
            }catch(GLSLCompileException ex) {
                success = false;
                annotateMessage(daos[i], compilerParser.parse(ex.getMessage()), printOut);
            }
            
        }
        
        // success == true if all shaders compiled without errors
        if(success) {
            if(printOut){
                io.getOut().println("compilation successfull");
                io.getOut().println("linking shaders");
            }
            try{
                link(shaders);
                
                if(printOut)
                    io.getOut().println("link successfull");
            }catch(GLSLLinkException ex) {
                success = false;
                if(printOut){
                    io.getErr().println("link error");
                    io.getErr().println(ex.getMessage()); // TODO we need a testcase for not linkable shader
                }
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
                GLSLShader.TYPE shaderType = GLSLShader.TYPE.fromMime(dao.getPrimaryFile().getMIMEType());
                shader = new GLSLShader(shaderSource, shaderName, shaderType);
            }catch(BadLocationException ex){
                // not possible
                Exceptions.printStackTrace(ex);
            }
        }else{
            GLSLShader.TYPE shaderType = GLSLShader.TYPE.fromMime(dao.getPrimaryFile().getMIMEType());
            
            if(shaderType == null)
                return null;
            
            shader = new GLSLShader(shaderType, FileUtil.toFile(dao.getPrimaryFile()));
        }
//        shader.setThrowExceptionOnCompilerWarning(true);
        
        return shader;
    }
    
    private GLSLShader compile(final DataObject dao, boolean printOut, final boolean deleteAfterCompilation) throws GLSLCompileException {

        final GLSLShader shader = createShader(dao);
        
        if(shader == null)
            return null;
        
        if(printOut)
            io.getOut().println("compiling shader: "+shader.getName());
        
        CompilerAnnotations.removeAnnotations(dao);
        final GLSLCompileException[] exception = new GLSLCompileException[] {null};

        if(!shader.type.isSupported())
            throw new GLSLCompileException(shader.getName(), shader.type.toString().toLowerCase()+" shaders not supported");
        
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
    
    
    private void annotateMessage(DataObject dao, CompilerMessage[] msgs, boolean printOut) {
        
        for (CompilerMessage msg : msgs) {
            
            if(msg.type == CompilerMessage.COMPILER_EVENT_TYPE.MSG) {
                if(printOut)
                    io.getErr().println(msg.msg);
            }else{

                CompilerAnnotation.AnnotationType type;
                if(msg.type == CompilerMessage.COMPILER_EVENT_TYPE.ERROR)
                    type = CompilerAnnotation.AnnotationType.ERROR;
                else
                    type = CompilerAnnotation.AnnotationType.WARNING;

                CompilerAnnotations.addAnnotation(dao, type, msg.msg, msg.line);

                if(printOut){
                    try  {
                        io.getErr().println(msg.msg, new HyperlinkProvider(dao, msg));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
        
    }

    private final void processCompilerMsg(DataObject dao, GLSLShader shader, boolean printOut) {
        
        String msg = shader.getCompilerMsg();

        if(msg.contains("WARNING")) {
            annotateMessage(dao, compilerParser.parse(msg), printOut);
        }

        if(printOut) {
           if(msg != null && msg.length() != 0)
               io.getOut().println("compiler msg:\n    " + msg);
        }
    }
    
private class HyperlinkProvider implements OutputListener{
    
 private CompilerMessage msg;
 private DataObject dao;
        
    private HyperlinkProvider(DataObject dao, CompilerMessage msg) {
        this.msg = msg;
        this.dao = dao;
    }
    
    public void outputLineAction(OutputEvent e) {
        
        //open file in editor and go to annotated line        
        dao.getCookie(OpenCookie.class).open();
        
        LineCookie lineCookie = dao.getCookie(LineCookie.class);
        
        if(msg.line > 0 && msg.line < lineCookie.getLineSet().getLines().size())
            lineCookie.getLineSet().getCurrent(msg.line-1).show(Line.SHOW_SHOW, 0);
        
    }

    public void outputLineCleared(OutputEvent arg0) {}
    public void outputLineSelected(OutputEvent arg0) {}
    
}


}
