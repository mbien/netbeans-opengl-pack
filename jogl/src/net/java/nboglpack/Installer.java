/*
 * Created on 17. March 2008, 21:26
 */
package net.java.nboglpack;

import java.io.File;
import net.java.nativelibsupport.Distribution;
import net.java.nativelibsupport.NativeLibSupport;
import net.java.nboglpack.jogl.util.JOGLDistribution;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. This Installer is responsible for propper jogl natives depoyment.
 * @author Michael Bien
 */
public class Installer extends ModuleInstall {
    
    
    @Override
    public void restored() {
        
        File joglDistFolder = InstalledFileLocator.getDefault().locate("jogl-runtime", "javax.media.opengl", false);
        
        Distribution distribution = JOGLDistribution.getCompatible();
        
        NativeLibSupport.deploy("jogl", distribution, joglDistFolder, "jogl.jar-natives-");
        NativeLibSupport.deploy("gluegen-rt", distribution, joglDistFolder, "gluegen-rt.jar-natives-");
        
    }
    
    
    
}
