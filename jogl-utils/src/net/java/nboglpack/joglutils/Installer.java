package net.java.nboglpack.joglutils;

import com.mbien.engine.util.GLWorker;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // eager init of GLWorker
        Lookup.getDefault().lookup(GLWorker.class);
    }

    @Override
    public void uninstalled() {
        Lookup.getDefault().lookup(GLWorker.class).destroy();
    }
    
}
