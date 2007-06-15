package net.java.nboglpack.glslcompiler;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Created on 6. March 2007, 15:48
 * @autor Michael Bien
 * Manages a module's lifecycle. 
 */
public class Installer extends ModuleInstall {
    
//    private Lookup.Result<DataObject> result;   // don't loose reference its week!
//    private ShaderFileObserver observer;
    
    
    public void restored() {
        
        final InputOutput out = IOProvider.getDefault().getIO("compiler debug output", false);


        final String[] strings = new String[2];
        GLWorker worker = new GLWorker();
        worker.addWork(new GLRunnable() {
            public void run(GLContext context) {
                GL gl = context.getGL();
                strings[0] = gl.glGetString(GL.GL_VERSION);
                strings[1] = gl.glGetString(GL.GL_VENDOR);
            }
        });
        worker.work();
        worker.destroy();
        
        int mayorVersion = Integer.parseInt(strings[0].substring(0, strings[0].indexOf(".")));
        if(mayorVersion < 2) {
            out.getOut().println("OpenGL 2.0 or higher required. Detected version is "+strings[0]+". Module is disabled.");
            
            return;
        }
        
        Preferences preferences = NbPreferences.forModule(Installer.class);
        preferences.put("GLVersion", strings[0]);
        preferences.put("GLVendor", strings[1]);
        preferences.put("JOGLVersion", Package.getPackage("javax.media.opengl").getImplementationVersion());
        
        // samples of compiler errors:
        // NV:  "(267) : error C0000: syntax error, unexpected identifier, expecting ';' or ',' at token ius";
        // ATI: "ERROR: 0:17: '-' :  wrong operand types  no operation '-' exists that takes a left-hand operand of type 'const int' and a right operand of type 'float' (or there is no acceptable conversion)"

        Pattern pattern;
        if(strings[1].contains("NVIDIA")) {
            pattern = Pattern.compile("\\((\\d+)\\)\\s*:\\s*(\\w+)");
        }else if(strings[1].contains("ATI")) {
            pattern = Pattern.compile("(\\w+):\\s*\\d+:(\\d+):");
        }else{
            pattern = Pattern.compile("()()");
        }
        preferences.put("GlslCompilerLogPattern", pattern.pattern());
        
        
        // TODO use lookup...
        GLSLCompiler.instance = new GLSLCompiler(pattern);
        
// cylab: I directly add the observer to the document in the GlslXXXShaderObject now
/*
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                observer = new ShaderFileObserver();
                result = Utilities.actionsGlobalContext().lookupResult(DataObject.class);
                result.addLookupListener(observer);
            }
        });
 */
    }
    

    @Override
    public void uninstalled() {
        super.uninstalled();
// cylab: I directly add the observer to the document in the GlslXXXShaderObject now
/*
        if(result != null){
            result.removeLookupListener(observer);
            result = null;
        }
*/ 
        CompilerAnnotations.clearAll();
 }
   
}
