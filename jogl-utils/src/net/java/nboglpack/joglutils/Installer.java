package net.java.nboglpack.joglutils;

import com.mbien.engine.util.GLWorker;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

/**
 * Module installer.
 * @author Michel Bien
 */
public class Installer extends ModuleInstall {


    @Override
    public void uninstalled() {
        GLWorker worker = Lookup.getDefault().lookup(GLWorker.class);
        if(worker != null)
            worker.destroy();
    }
    
}
