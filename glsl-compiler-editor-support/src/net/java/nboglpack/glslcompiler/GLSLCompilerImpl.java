
/*
 * Created on 14. March 2007, 23:51
 */

package net.java.nboglpack.glslcompiler;

import com.mbien.engine.glsl.GLSLCompileException;
import com.mbien.engine.glsl.CompilerMessage;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.glsl.GLSLCompilerMessageParser;
import com.mbien.engine.glsl.GLSLException;
import com.mbien.engine.glsl.GLSLIncludeUtil;
import com.mbien.engine.glsl.GLSLLinkException;
import com.mbien.engine.glsl.GLSLProgram;
import com.mbien.engine.glsl.GLSLShader;
import com.mbien.engine.util.GLWorker;
import java.io.File;
import javax.swing.text.BadLocationException;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotation;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.LineCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
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
    
 
 private final GLSLCompilerMessageParser compilerMSGParser;
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
        
        compilerMSGParser = new GLSLCompilerMessageParser(pattern);
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
                NBShader shader = compile(dao, printOut, true);
                if(shader != null)
                    annotateMessage(dao, shader, printOut);
            }catch(NBGLSLException ex){
                success = false;
                if(ex.nbs.getCompilerMsg() != null)
                    annotateMessage(dao, ex.nbs, printOut);
                else if(printOut)
                    io.getErr().println(ex.getMessage());
            }
        }
        
        if(success && printOut)
            io.getOut().println("compilation successful");
        
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
                NBShader shader = compile(daos[i], printOut, false);
                shaders[i] = shader;
                if(shaders[i] != null)
                    annotateMessage(daos[i], shader, printOut);
            }catch(NBGLSLException ex) {
                success = false;
                if(ex.nbs.getCompilerMsg() != null)
                    annotateMessage(daos[i], ex.nbs, printOut);
                else if(printOut)
                    io.getErr().println(ex.getMessage());
            }
            
        }
        
        // success == true if all shaders compiled without errors
        if(success) {
            if(printOut){
                io.getOut().println("compilation successful");
                io.getOut().println("linking shaders");
            }
            try{
                link(shaders);
                
                if(printOut)
                    io.getOut().println("link successful");
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
    private NBShader createShader(DataObject dao) {
        
        NBShader nbs = null;
        NBShaderSourceProvider provider = new NBShaderSourceProvider();
        String shaderSource;
        
        try {
            shaderSource = provider.readSourceFromDao(dao);

            if(shaderSource != null && dao.isValid()) {

                FileObject primaryFile = dao.getFolder().getPrimaryFile();

                File folder = FileUtil.toFile(primaryFile);
                shaderSource = GLSLIncludeUtil.includeAllDependencies(shaderSource, folder.getAbsolutePath(), provider);

                String shaderName = dao.getPrimaryFile().getNameExt();
                GLSLShader.TYPE shaderType = GLSLShader.TYPE.fromMime(dao.getPrimaryFile().getMIMEType());

                nbs = new NBShader(shaderSource, shaderName, provider.getDependencies(), shaderType);
//                System.out.println(" - - - - - - - - - - - -");
//                System.out.println(shaderSource);
//                System.out.println(" - - - - - - - - - - - -");
            }
            
        } catch (BadLocationException ex) {
            //this can happen in theory, we don't lock any files on read.
            Logger.getLogger(GLSLCompilerImpl.class.getName()).log(Level.INFO, "can't create shader", ex);
        }
        
//        shader.setThrowExceptionOnCompilerWarning(true);

        return nbs;
    }
    
    private NBShader compile(final DataObject dao, boolean printOut, final boolean deleteAfterCompilation) throws NBGLSLException {

        NBShader nbs = createShader(dao);
        
        if(nbs == null)
            return null;
        
        final GLSLShader shader = nbs;
        
        if(printOut) {
            io.getOut().println("compiling shader: "+shader.getName());
            if(nbs.dependencies != null && nbs.dependencies.length > 0) {
                for (DataObject dataObject : nbs.dependencies) {
                    io.getOut().println(" - including "+dataObject.getPrimaryFile().getNameExt());
                }
            }
        }
        
        CompilerAnnotations.removeAnnotations(dao);
        final GLSLCompileException[] exception = new GLSLCompileException[] {null};

        if(!shader.type.isSupported())
            throw new NBGLSLException(new GLSLCompileException(shader.getName(), shader.type.toString().toLowerCase()+" shaders not supported"), nbs);
        
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
       
       if(exception[0] != null) {
           throw new NBGLSLException(exception[0], nbs);
       }
       
       return nbs;
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
    
    private final CompilerMessage[] parseMessage(DataObject dao, NBShader shader) {
        int[] lines = countLines(dao, shader);
        String msg = shader.getCompilerMsg();
        return compilerMSGParser.parse(msg, lines);
    }
    
    private void annotateMessage(DataObject dao, NBShader shader, boolean printOut) {

        CompilerMessage[] msgs = parseMessage(dao, shader);

        for (CompilerMessage msg : msgs) {
            
            if(msg.type == CompilerMessage.COMPILER_EVENT_TYPE.MSG) {
                if(printOut)
                   io.getOut().println("compiler msg: " + msg);
            }else{
//                System.out.println(shader.getName() +" dep: "+shader.dependencies.length);
//                System.out.println(" - frag: "+msg.fragment);
                // the dao which caused the message (can be a dependency or the root dao)
                DataObject dao2annotate;
                if(msg.fragment < shader.dependencies.length && msg.fragment >= 0)
                   dao2annotate = shader.dependencies[msg.fragment];
                else
                   dao2annotate = dao;

                CompilerAnnotation.AnnotationType type;
                if(msg.type == CompilerMessage.COMPILER_EVENT_TYPE.ERROR)
                    type = CompilerAnnotation.AnnotationType.ERROR;
                else
                    type = CompilerAnnotation.AnnotationType.WARNING;

                CompilerAnnotations.addAnnotation(dao2annotate, type, msg.msg, msg.line);

                if(printOut){
                    try  {
                        io.getErr().println(msg.msg, new HyperlinkProvider(dao2annotate, msg));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
        
    }


    private final int[] countLines(DataObject dao, NBShader nbs) {
        int[] lines = new int[nbs.dependencies.length+1];

        EditorCookie cookie;
        for (int i = 0; i < nbs.dependencies.length; i++) {
            DataObject d = nbs.dependencies[i];
            cookie = d.getCookie(EditorCookie.class);
            lines[i] = cookie.getLineSet().getLines().size();
        }

        cookie = dao.getCookie(EditorCookie.class);
        lines[lines.length-1] = cookie.getLineSet().getLines().size();
        
        return lines;
    }

    
private static class HyperlinkProvider implements OutputListener{
    
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

/**
 * Wrapps netbeans specific dependencies into a shader.
 */
private final static class NBShader extends GLSLShader {

    private final DataObject[] dependencies;

        public NBShader(String source, String name, DataObject[] dependencies, GLSLShader.TYPE type) {
            super(source, name, type);
            this.dependencies = dependencies;
        }

}

private final static class NBGLSLException extends Exception {

    private final NBShader nbs;

    public NBGLSLException(GLSLException ex, NBShader nbs) {
        super(ex);
        this.nbs = nbs;
    }

    GLSLException getGLSLException() {
        return (GLSLException) getCause();
    }

}


}
