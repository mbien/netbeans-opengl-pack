
/*
 * Created on 14. March 2007, 23:51
 */

package net.java.nboglpack.glslcompiler;

import com.mbien.engine.glsl.GLSLCompileException;
import com.mbien.engine.glsl.CompilerMessage;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.glsl.GLSLCompilerMessageParser;
import com.mbien.engine.glsl.GLSLException;
import com.mbien.engine.glsl.GLSLFragment;
import com.mbien.engine.glsl.GLSLLinkException;
import com.mbien.engine.glsl.GLSLProgram;
import com.mbien.engine.glsl.GLSLShader;
import com.mbien.engine.util.GLWorker;
import java.io.File;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotation;
import java.io.IOException;
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
                GLSLShader shader = compile(dao, printOut, true);
                if(shader != null)
                    annotateMessage(dao, shader, printOut);
            }catch(GLSLCompileException ex){
                success = false;
                GLSLShader source = (GLSLShader) ex.source;
                if(source.getCompilerMsg() != null) {
                    if(printOut)
                        io.getErr().println("error compiling shader");
                    annotateMessage(dao, source, printOut);
                }else if(printOut){
                    io.getErr().println(ex.getMessage());
                }
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
                GLSLShader shader = compile(daos[i], printOut, false);
                shaders[i] = shader;
                if(shaders[i] != null)
                    annotateMessage(daos[i], shader, printOut);
            }catch(GLSLCompileException ex) {
                success = false;
                GLSLShader source = (GLSLShader) ex.source;
                if(source.getCompilerMsg() != null) {
                    if(printOut)
                        io.getErr().println("error compiling shader");
                    annotateMessage(daos[i], source, printOut);
                }else if(printOut){
                    io.getErr().println(ex.getMessage());
                }
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
    private GLSLShader createShader(DataObject dao) {
        
        GLSLShader shader = null;
        NBShaderSourceProvider provider = new NBShaderSourceProvider();
        GLSLFragment main;
        
        main = provider.loadShaderSource(dao);

        if(main != null && dao.isValid()) {

            FileObject foFolder = dao.getFolder().getPrimaryFile();
            File folder = FileUtil.toFile(foFolder);

            GLSLShader.TYPE shaderType = GLSLShader.TYPE.fromMime(dao.getPrimaryFile().getMIMEType());

            shader = new GLSLShader(shaderType, main, folder.getAbsolutePath(), provider);
        }
            
//        shader.setThrowExceptionOnCompilerWarning(true);

        return shader;
    }
    
    private GLSLShader compile(final DataObject dao, boolean printOut, final boolean deleteAfterCompilation) throws GLSLCompileException {

        final GLSLShader shader = createShader(dao);
        
        if(shader == null)
            return null;
        
        if(printOut) {
            io.getOut().println("compiling shader:");
            if(shader.fragments != null && shader.fragments.length > 0) {
                for (GLSLFragment fragment : shader.fragments) {
                    io.getOut().println(" - including "+fragment.name);
                }
            }else{
                io.getOut().println(shader.getName());
            }
        }
        
        CompilerAnnotations.removeAnnotations(dao);
        final GLSLCompileException[] exception = new GLSLCompileException[] {null};

        if(!shader.type.isSupported())
            throw new GLSLCompileException(shader, shader.type.toString().toLowerCase()+" shaders not supported");
        
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
           throw exception[0];
       }
       
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
    
    private final CompilerMessage[] parseMessage(DataObject dao, GLSLShader shader) {
        int[] lines = countLines(dao, shader);
        String msg = shader.getCompilerMsg();
        return compilerMSGParser.parse(msg, lines);
    }
    
    private void annotateMessage(DataObject dao, GLSLShader shader, boolean printOut) {

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
                if(msg.fragment < shader.fragments.length && msg.fragment >= 0)
                   dao2annotate = (DataObject) shader.fragments[msg.fragment].sourceObj;
                else
                   dao2annotate = dao;

                CompilerAnnotation.AnnotationType type;
                if(msg.type == CompilerMessage.COMPILER_EVENT_TYPE.ERROR)
                    type = CompilerAnnotation.AnnotationType.ERROR;
                else
                    type = CompilerAnnotation.AnnotationType.WARNING;

                // add editor annotation to dao
                CompilerAnnotations.addAnnotation(dao2annotate, type, msg.msg, msg.line);

                // add hyperlink in output window if print out enabled
                if(printOut){
                    try  {
                        String txt = dao2annotate.getName() + " (line "+msg.line+"): "+msg.msg;
                        io.getErr().println(txt, new HyperlinkProvider(dao2annotate, msg.line));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
        
    }


    private final int[] countLines(DataObject dao, GLSLShader nbs) {
        int[] lines = new int[nbs.fragments.length+1];

        EditorCookie cookie;
        for (int i = 0; i < nbs.fragments.length; i++) {
            GLSLFragment<DataObject> d = nbs.fragments[i];
            cookie = d.sourceObj.getCookie(EditorCookie.class);
            lines[i] = cookie.getLineSet().getLines().size();
        }

        cookie = dao.getCookie(EditorCookie.class);
        lines[lines.length-1] = cookie.getLineSet().getLines().size();
        
        return lines;
    }

    
private static class HyperlinkProvider implements OutputListener{
    
    private final int line;
    private final DataObject dao;
        
    private HyperlinkProvider(DataObject dao, int lineNumber) {
        this.line = lineNumber;
        this.dao = dao;
    }
    
    public void outputLineAction(OutputEvent e) {
        
        //open file in editor and go to annotated line        
        dao.getCookie(OpenCookie.class).open();
        
        LineCookie lineCookie = dao.getCookie(LineCookie.class);
        
        if(line > 0 && line < lineCookie.getLineSet().getLines().size()) {
            Line current = lineCookie.getLineSet().getCurrent(line - 1);
            current.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FRONT);
        }
        
    }

    public void outputLineCleared(OutputEvent arg0) {}
    public void outputLineSelected(OutputEvent arg0) {}
    
}

/**
 * Wrapps netbeans specific dependencies into a shader.
 */
private final static class NBShader extends GLSLShader {

    private final DataObject[] dependencies;

    public NBShader(GLSLShader.TYPE type, String source, String name, DataObject[] dependencies) {
        super(type, source, name);
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
