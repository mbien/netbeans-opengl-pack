/*
 * Created on 17. March 2008, 21:26
 */
package net.java.nboglpack.jogl;

import java.io.File;
import java.io.InputStream;
import net.java.nativelibsupport.LibDeploymentException;
import net.java.nativelibsupport.NativeLibSupport;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. This Installer is responsible for propper jogl natives depoyment.
 * @author Michael Bien
 */
public class Installer extends ModuleInstall {
    
    
    @Override
    public void restored() {
        
        File joglDistFolder = InstalledFileLocator.getDefault().locate("jogl-runtime2", "javax.media.opengl", false);
        
        try {
            InputStream stream = this.getClass().getResourceAsStream("jogl-natives-config.xml");
            NativeLibSupport.deploy("jogl.all.jar", stream, joglDistFolder, "jogl-2.0-webstart.zip");

            stream = this.getClass().getResourceAsStream("nativewindow-natives-config.xml");
            NativeLibSupport.deploy("nativewindow.all.jar", stream, joglDistFolder, "nativewindow-2.0-webstart.zip");

            stream = this.getClass().getResourceAsStream("newt-natives-config.xml");
            NativeLibSupport.deploy("newt.all.jar", stream, joglDistFolder, "newt-2.0-webstart.zip");
        } catch (LibDeploymentException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    
    
}
