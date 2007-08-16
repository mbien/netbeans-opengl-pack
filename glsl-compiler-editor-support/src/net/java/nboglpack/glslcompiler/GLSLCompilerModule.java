package net.java.nboglpack.glslcompiler;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorkerImpl;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorkerImpl;
import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import java.util.prefs.Preferences;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

/**
 * Manages a module's lifecycle.
 * Created on 6. March 2007, 15:48
 * @autor Michael Bien
 */
public class GLSLCompilerModule extends ModuleInstall {
    
    
    @Override
    public void restored() {
        
        //final InputOutput out = IOProvider.getDefault().getIO("compiler debug output", false);

        final String[] buffer = new String[2];
        GLWorkerImpl worker = new GLWorkerImpl();
        worker.addWork(new GLRunnable() {
            public void run(GLContext context) {
                GL gl = context.getGL();
                buffer[0] = gl.glGetString(GL.GL_VERSION);
                buffer[1] = gl.glGetString(GL.GL_VENDOR);
            }
        });
        worker.work();
        worker.destroy();
        
        Preferences preferences = NbPreferences.forModule(GLSLCompilerModule.class);
        preferences.put("GLVersion", buffer[0]);
        preferences.put("GLVendor", buffer[1]);
        preferences.put("JOGLVersion", Package.getPackage("javax.media.opengl").getImplementationVersion());
        preferences.put("GlslCompilerLogPattern", "");
        
        int mayorVersion = Integer.parseInt(buffer[0].substring(0, buffer[0].indexOf(".")));
        if(mayorVersion < 2) {
           // out.getOut().println("OpenGL 2.0 or higher required. Detected version is "+buffer[0]);
        }
       
    }
    

    @Override
    public void uninstalled() {
        CompilerAnnotations.clearAll();
        super.uninstalled();
 }
   
}
