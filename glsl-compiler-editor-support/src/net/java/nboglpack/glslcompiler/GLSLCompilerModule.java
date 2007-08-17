package net.java.nboglpack.glslcompiler;

import net.java.nboglpack.glslcompiler.annotation.CompilerAnnotations;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle.
 * Created on 6. March 2007, 15:48
 * @autor Michael Bien
 */
public class GLSLCompilerModule extends ModuleInstall {
    

    @Override
    public void uninstalled() {
        CompilerAnnotations.clearAll();
        super.uninstalled();
 }
   
}
