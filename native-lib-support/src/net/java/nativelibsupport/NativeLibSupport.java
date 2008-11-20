/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nativelibsupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.java.nativelibsupport.natives_config.Library;
import net.java.nativelibsupport.natives_config.Library.Os;
import net.java.nativelibsupport.natives_config.Library.Os.Cpu;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.JarFileSystem;

/**
 * Utility class for native librarie deployment.
 * @author Michael Bien
 */
public class NativeLibSupport {
    
    private static WeakReference<JAXBContext> reference;

    private NativeLibSupport() {
    }
    
    /**
     * Deployes, re-deployes or does nothing dependent on the currently deployed native libraries.
     * @param libraryName the name of the library.jar (without the '.jar')
     * @param configFile InputStream which reads the xml config file
     * @param distFolder The distribution folder containing the library.jar and all native libraries in sub folders
     * @throws DeploymentException Thrown when libraries were not successfully deployed.
     */
    public static void deploy(String libraryName, InputStream configFile, File distributionFolder) throws LibDeploymentException {
        
        assert libraryName!=null;
        assert configFile!=null;
        assert distributionFolder!=null;
        
        try{
            JarFileSystem jarSystem = new JarFileSystem();
            
            // read jogl version from manifest and compare with deployed version
            jarSystem.setJarFile(new File(distributionFolder+File.separator+libraryName+".jar"));
            String jarVersion = jarSystem.getManifest().getMainAttributes().getValue("Implementation-Version");
            
            if(jarVersion == null)
                throw new NullPointerException(jarSystem.getJarFile()+" has no 'Implementation-Version' property in manifest file");
            
            String root = distributionFolder.getCanonicalPath();
            root = root.substring(0, root.lastIndexOf(File.separator));
            
            String libFolderPath = root + File.separator + "modules" + File.separator + "lib";
            
            // read property file with deployed libraries version entries
            File propertyFile = new File(libFolderPath + File.separator + "deployed-natives.properties");
            
            Properties properties = new Properties();
            if(propertyFile.exists()) {
                properties.load(new FileInputStream(propertyFile));
            }else{
                propertyFile.getParentFile().mkdirs();
                propertyFile.createNewFile();
            }
            
            String deployedLibVersion = properties.getProperty(libraryName+".jar", null);
            
            
            //  check if we've already deployed
            if(deployedLibVersion == null || !jarVersion.equals(deployedLibVersion)) {
                
                // load native libraries configuration file
                Object obj = null;
                try {
                    // jaxb context loading is expensive load it only once
                    JAXBContext jc = getJAXBContext();

                    Unmarshaller unmarshaller = jc.createUnmarshaller();
                    obj = unmarshaller.unmarshal(configFile);
                    
                } catch (JAXBException ex) {
//                    Logger.getLogger(NativeLibSupport.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("error reading deployment file", ex);
                }

                if(obj instanceof Library == false) {
                    throw new IllegalArgumentException("wrong root element in config file. 'Library' expected but got "+obj);
                }

                Library lib = (Library)obj;
                
                // assamble path to platform dependent library folder
                StringBuilder nativesFolderPath = new StringBuilder();
                nativesFolderPath.append(distributionFolder.getName());
                nativesFolderPath.append(File.separatorChar);
                nativesFolderPath.append(lib.getName());
                if(!lib.isFlat())
                    nativesFolderPath.append(File.separatorChar);
                
                String osName = System.getProperty("os.name");
                String cpuName = System.getProperty("os.arch");
                
                assambleLibPath(lib, osName, cpuName, nativesFolderPath);
                
                FileObject libSourceFolder = FileUtil.toFileObject(
                        new File(root+File.separator+nativesFolderPath.toString()));
                
                if(libSourceFolder != null) {
                    
                    FileObject libTargetFolder = FileUtil.createFolder(new File(libFolderPath));
                    
                    copyFolderEntries(libSourceFolder, libTargetFolder);
                    
                    // update deployed version property
                    properties.put(lib.getName(), jarVersion);
                    properties.store(new FileOutputStream(propertyFile), "deployed native libraries (remove entry and restart to force re-deployment)");
                    
                    Logger.getLogger(NativeLibSupport.class.getName()).info(
                        "deployed "+lib.getName()+" version: "+jarVersion );
                }else{
                    String os = System.getProperty("os.name");
                    String arch = System.getProperty("os.arch");
                    throw new LibDeploymentException(
                            String.format("The %1$s native libraries are either not available"+
                            " for this system (OS: %2$s CPU: %3$s) or an error accrued while deploying", lib.getName(), os, arch));
                }
            }else{
                Logger.getLogger(NativeLibSupport.class.getName()).info(
                        libraryName+" version "+jarVersion +" is up to date");
            }
            
        }catch(Exception ex) {
            throw new LibDeploymentException("can not deploy "+ libraryName +" natives", ex);
        }
        
    }
    
    
    /**
     * overwrites files
     */
    private static final void copyFolderEntries(FileObject srcFolder, FileObject targetFolder) throws IOException {
        FileObject[] entries = srcFolder.getChildren();
        for (int i = 0; i < entries.length; i++) {
            FileObject entry = entries[i];
            
            // delete old files (overwrite)
            FileObject old = targetFolder.getFileObject(entry.getNameExt());
            if(old != null) {
                Logger.getLogger(NativeLibSupport.class.getName()).info(
                    "remove old file: "+old.getPath() );
                old.delete();
            }
            
            // copy into target folder
            Logger.getLogger(NativeLibSupport.class.getName()).info(
                    "copy "+entry.getPath() +" to "+targetFolder.getPath() );
            FileUtil.copyFile(entry, targetFolder, entry.getName());
        }
    }
    
    private final static void assambleLibPath(Library lib, String osName, String cpuName, StringBuilder nativesFolderPath) {
        
        List<Os> oses = lib.getOs();

        for (Os os : oses) {
            
            Matcher osMatcher = Pattern.compile(os.getRegex()).matcher(osName);
            
            if(osMatcher.find()) {

                nativesFolderPath.append(os.getFolder());
                if(!lib.isFlat())
                    nativesFolderPath.append(File.separatorChar);

                List<Cpu> cpus = os.getCpu();

                for (Cpu cpu : cpus) {
                    
                    Matcher cpuMatcher = Pattern.compile(cpu.getRegex()).matcher(cpuName);
                    
                    if(cpuMatcher.find()) {
                        nativesFolderPath.append(cpu.getFolder());
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private static final synchronized JAXBContext getJAXBContext() throws JAXBException {
        
        JAXBContext jc;
        
        if(reference == null) {
            jc = createJAXBContext();
            reference = new WeakReference<JAXBContext>(jc);
        }else{
            jc = reference.get();
            if(jc == null) {
                jc = createJAXBContext();
                reference = new WeakReference<JAXBContext>(jc);
            }
        }
        System.out.println(jc);
        return jc;
    }
    
    private final static JAXBContext createJAXBContext() throws JAXBException {
        return JAXBContext.newInstance(
                         "net.java.nativelibsupport.natives_config",
                         NativeLibSupport.class.getClassLoader()    );
    }
    
    

}
