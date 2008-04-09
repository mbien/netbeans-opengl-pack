/*
 * Created on 17. March 2008, 21:26
 */
package net.java.nboglpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.nboglpack.jogl.util.Distribution;
import net.java.nboglpack.jogl.util.JOGLDistribution;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.JarFileSystem;
import org.openide.filesystems.LocalFileSystem;
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
        
        deploy(joglDistFolder, "jogl", "jogl.jar-natives-", distribution);
        deploy(joglDistFolder, "gluegen-rt", "gluegen-rt.jar-natives-", distribution);
        
    }
    /**
     * Deployes, re-deployes or does nothing dependent on the currently deployed native libraries.
     * @param distFolder - distribution folder containing the jar file and native libraries in sub folders.
     * @param libraryJarName - name of the jar file inside the distribution folder
     * @param nativesFolderPrefix - library specific prefix of the sub folders in the distibution folder containing native libraries.
     * @param distribution - the Distribution object defining the postfix of the folder containing native files.
     */
    private void deploy(File distFolder, String libraryJarName, String nativesFolderPrefix, Distribution distribution) {
        
        try{
            JarFileSystem jarSystem = new JarFileSystem();
            
            // read jogl version from manifest and compare with deployed version
            jarSystem.setJarFile(new File(distFolder+File.separator+libraryJarName+".jar"));
            String jarVersion = jarSystem.getManifest().getMainAttributes().getValue("Implementation-Version");
            
            if(jarVersion == null)
                throw new NullPointerException(jarSystem.getJarFile()+" has no version property in manifest file");
            
            String root = distFolder.getAbsolutePath();
            root = root.substring(0, root.lastIndexOf(File.separator));
            
            String libFolderPath = root + File.separator + "modules" + File.separator + "lib";
            
            // read property file with deployed libraries version entries
            File propertyFile = new File(libFolderPath + File.separator + "deployed-natives.properties");
            
            Properties properties = new Properties();
            if(propertyFile.exists()) {
                properties.load(new FileInputStream(propertyFile));
            }else{
                propertyFile.createNewFile();
            }
            
            String deployedLibVersion = properties.getProperty(libraryJarName, null);
            
            //  check if we've already deployed
            if(deployedLibVersion == null || !jarVersion.equals(deployedLibVersion)) {
                
                LocalFileSystem fileSystem = new LocalFileSystem();
                fileSystem.setRootDirectory(new File(root));
                
                if(distribution == null)
                    throw new NullPointerException("distribution not found");
                
                String postfix = distribution.key();
                String nativesFolderPath = distFolder.getName() + File.separator + nativesFolderPrefix + postfix;

                FileObject nativesFolder = fileSystem.findResource(nativesFolderPath);
                
                if(nativesFolder != null) {
                    
                    FileObject libDestFolder = FileUtil.createFolder(new File(libFolderPath));
                    
                    copyFolderEntries(nativesFolder, libDestFolder);
                    
                    // update deployed version property
                    properties.put(libraryJarName, jarVersion);
                    properties.store(new FileOutputStream(propertyFile), "deployed native libraries (remove entry and restart to force re-deployment)");
                    
                    Logger.getLogger(this.getClass().getName()).info(
                        "deployed "+libraryJarName+" runtime version: "+jarVersion );
                }else{
                    String os = System.getProperty("os.name").toLowerCase();
                    String arch = System.getProperty("os.arch").toLowerCase();
                    throw new IOException(
                            String.format("The %1$s native libraries are either not available"+
                            " for this system (OS: %2$s CPU: %3$s) or an error accrued while deploying", libraryJarName, os, arch));
                }
            }else{
                Logger.getLogger(this.getClass().getName()).info(
                        libraryJarName+" runtime version "+jarVersion +" is up to date");
            }
            
        }catch(Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(
                    Level.SEVERE, "can not deploy "+ libraryJarName +" natives", ex);
        }
        
    }
    
    
    /**
     * overwrites files
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
