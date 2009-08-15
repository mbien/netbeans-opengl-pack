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
 * Utility class for native library deployment.
 * @author Michael Bien
 */
public class NativeLibSupport {
    
    private static WeakReference<JAXBContext> reference;

    private NativeLibSupport() {
    }
    
    /**
     * Deployes, re-deployes or does nothing dependent on the currently deployed native libraries.
     * @param jarName the name of the library.jar
     * @param configFile InputStream which reads the xml config file
     * @param distFolder The distribution folder containing the library.jar and all native libraries in sub folders
     * @throws DeploymentException Thrown when libraries were not successfully deployed.
     */
    public static void deploy(String jarName, InputStream configFile, File distributionFolder) throws LibDeploymentException {
        deploy(jarName, configFile, distributionFolder, null);
    }
    
    /**
     * Deployes, re-deployes or does nothing dependent on the currently deployed native libraries.
     * @param jarName the name of the library.jar
     * @param configFile InputStream which reads the xml config file
     * @param distFolder The distribution folder containing the library.jar and all native libraries in sub folders
     * @param archive The zip archive in which the native libraries are stored
     * @throws DeploymentException Thrown when libraries were not successfully deployed.
     */
    public static void deploy(String jarName, InputStream configFile, File distributionFolder, String archive) throws LibDeploymentException {
        
        assert jarName!=null;
        assert configFile!=null;
        assert distributionFolder!=null;

        try{
            JarFileSystem jarSystem = new JarFileSystem();
            
            // read jogl version from manifest and compare with deployed version
            jarSystem.setJarFile(new File(distributionFolder+File.separator+jarName));
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
            
            String deployedLibVersion = properties.getProperty(jarName, null);
            
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

                String osName = System.getProperty("os.name");
                String cpuName = System.getProperty("os.arch");

                // assamble path to platform dependent library folder
                StringBuilder path = new StringBuilder();
                path.append(distributionFolder.getName());
                path.append(File.separatorChar);

                FileObject libSource = null;

                // find library storage
                if(archive != null && archive.endsWith(".zip")) {
                    path.append(archive);

                    JarFileSystem jar = new JarFileSystem();
                    jar.setJarFile(new File(root+File.separator+path.toString()));

                    path.delete(0, path.length());
                    path.append(lib.getName());
                    if(!lib.isFlat())
                        path.append(File.separatorChar);
                    assambleLibPath(lib, osName, cpuName, path);

                    path.append(".jar");

                    libSource = jar.findResource(path.toString());

                }else{
                    path.append(lib.getName());
                    if(!lib.isFlat())
                        path.append(File.separatorChar);
                    assambleLibPath(lib, osName, cpuName, path);

                    libSource = FileUtil.toFileObject(
                            new File(root+File.separator+path.toString()));
                }
                
                if(libSource != null) {
                    
                    FileObject libTargetFolder = FileUtil.createFolder(new File(libFolderPath));

                    if(libSource.isFolder()) {
                        copyFolderEntries(libSource, libTargetFolder);
                    }else{
                        extractArchive(libSource, libTargetFolder);
                    }
                    
                    // update deployed version property
                    properties.put(jarName, jarVersion);
                    properties.store(new FileOutputStream(propertyFile), "deployed native libraries (remove entry and restart to force re-deployment)");

                    Logger.getLogger(NativeLibSupport.class.getName()).info(
                        "deployed "+jarName+" version: "+jarVersion );
                }else{
                    String os = System.getProperty("os.name");
                    String arch = System.getProperty("os.arch");
                    throw new LibDeploymentException(
                            String.format("The %1$s native libraries are either not available"+
                            " for this system (OS: %2$s CPU: %3$s) or an error accrued while deploying", lib.getName(), os, arch));
                }
            }else{
                Logger.getLogger(NativeLibSupport.class.getName()).info(
                        jarName+" version "+jarVersion +" is up to date");
            }
            
        }catch(Exception ex) {
            throw new LibDeploymentException("can not deploy "+ jarName +" natives", ex);
        }
        
    }
    
    /**
     * overwrites files
     */
    private static final void copyFolderEntries(FileObject src, FileObject targetFolder) throws IOException {
        FileObject[] entries = src.getChildren();
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

    private static void extractArchive(FileObject libSource, FileObject libTargetFolder) throws IOException {
        FileUtil.extractJar(libTargetFolder, libSource.getInputStream());
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
