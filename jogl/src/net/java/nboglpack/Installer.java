/*
 * Created on 17. March 2008, 21:26
 */
package net.java.nboglpack;

import java.io.File;
import java.io.InputStream;
import net.java.nativelibsupport.NativeLibSupport;
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
        
        InputStream stream = this.getClass().getResourceAsStream("jogl/jogl-natives-config.xml");
        NativeLibSupport.deploy("jogl", stream, joglDistFolder);
        
        stream = this.getClass().getResourceAsStream("jogl/gluegen-natives-config.xml");
        NativeLibSupport.deploy("gluegen-rt", stream, joglDistFolder);
        
    }
    
    
    
}
