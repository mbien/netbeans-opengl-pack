/*
 * Created on 23. May 2008
 */
package net.java.nboglpack.gluegen;

import java.io.File;
import java.io.InputStream;
import net.java.nativelibsupport.LibDeploymentException;
import net.java.nativelibsupport.NativeLibSupport;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Installes GlueGens platform dependent libraries.
 * @author Michael Bien
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        File gluegenDistFolder = InstalledFileLocator.getDefault().locate(
                "gluegen-runtime2", "com.sun.gluegen", false);
        try {
            InputStream stream = this.getClass().getResourceAsStream("gluegen-natives-config.xml");
            NativeLibSupport.deploy("gluegen-rt.jar", stream, gluegenDistFolder, "gluegen-rt-2.0-webstart.zip");
        } catch (LibDeploymentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
