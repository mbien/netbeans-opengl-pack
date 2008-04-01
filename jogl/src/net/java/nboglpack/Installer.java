/*
 * Created on 17. March 2008, 21:26
 */
package net.java.nboglpack;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.java.nboglpack.jogl.util.JOGLDistribution;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.JarFileSystem;
import org.openide.filesystems.LocalFileSystem;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

/**
 * Manages a module's lifecycle. This Installer is responsible for propper jogl natives depoyment.
 * @author Michael Bien
 */
public class Installer extends ModuleInstall {
    
    
    @Override
    public void restored() {
        
        final String JOGL_PREFIX = "/jogl.jar-natives-";
        final String GLUEGEN_PREFIX = "/gluegen-rt.jar-natives-";
        final String JOGL_DIST = "jogl-runtime";
        final String VERSION_KEY = "DeployedJOGLVersion";
        
        File joglDistFolder = InstalledFileLocator.getDefault().locate(JOGL_DIST, "javax.media.opengl", false);
        JarFileSystem jarSystem = new JarFileSystem();

        Preferences preferences = NbPreferences.forModule(Installer.class);
        String deployedVersion = preferences.get(VERSION_KEY, null);
        
        try{
            // read jogl version from manifest and compare with deployed version
            jarSystem.setJarFile(new File(joglDistFolder+File.separator+"jogl.jar"));
            String version = jarSystem.getManifest().getMainAttributes().getValue("Implementation-Version");
            
            //  check if we've already deployed
            if(version == null || !version.equals(deployedVersion)) {
                
                String root = joglDistFolder.getAbsolutePath();
                root = root.substring(0, root.lastIndexOf(File.separator));
                
                LocalFileSystem fileSystem = new LocalFileSystem();
                fileSystem.setRootDirectory(new File(root));
                
                JOGLDistribution distribution = JOGLDistribution.getCompatible();
                if(distribution == null)
                    throw new NullPointerException("distribution not found");
                
                String postfix = distribution.key();
                String joglNativesFolderPath = JOGL_DIST+JOGL_PREFIX + postfix;
                String gluegenNativesFolderPath = JOGL_DIST+GLUEGEN_PREFIX + postfix;

                FileObject joglNativesFolder = fileSystem.findResource(joglNativesFolderPath);
                FileObject gluegenNativesFolder = fileSystem.findResource(gluegenNativesFolderPath);
                
                if(joglNativesFolder != null && gluegenNativesFolder != null) {
                    
                    FileObject libDestFolder = FileUtil.createFolder(
                            new File(root+File.separator+"modules"+File.separator+"lib"));
                    
                    copyFolderEntries(joglNativesFolder, libDestFolder);
                    copyFolderEntries(gluegenNativesFolder, libDestFolder);
                    
                    // update deployed version property
                    Logger.getLogger(this.getClass().getName()).info(
                        "deployed JOGL runtime version: "+version );
                    
                    preferences.put(VERSION_KEY, version);
                }else{
                    String os = System.getProperty("os.name").toLowerCase();
                    String arch = System.getProperty("os.arch").toLowerCase();
                    throw new IOException(
                            String.format("The JOGL native libraries are either not available"+
                            " for this system (OS: %1$s CPU: %2$s) or an error accrued while deploying", os, arch));
                }
            }else{
                Logger.getLogger(this.getClass().getName()).info(
                        "JOGL runtime version "+version +" is up to date");
            }
            
        }catch(Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(
                    Level.SEVERE, "can not deploy jogl natives", ex);
        }
        
        
    }
    
    /**
     * overwrites files if nessasery
     */
    private final void copyFolderEntries(FileObject srcFolder, FileObject destFolder) throws IOException {
        FileObject[] entries = srcFolder.getChildren();
        for (int i = 0; i < entries.length; i++) {
            FileObject entry = entries[i];
            
            // delete old files (overwrite)
            FileObject old = destFolder.getFileObject(entry.getNameExt());
            if(old != null) {
                Logger.getLogger(this.getClass().getName()).info(
                    "remove old file: "+old.getPath() );
                old.delete();
            }
            
            // copy into dest folder
            Logger.getLogger(this.getClass().getName()).info(
                    "copy "+entry.getPath() +" to "+destFolder.getPath() );
            FileUtil.copyFile(entry, destFolder, entry.getName());
        }
    }
    
    
    
}