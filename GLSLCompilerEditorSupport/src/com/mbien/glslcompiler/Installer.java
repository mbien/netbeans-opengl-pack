package com.mbien.glslcompiler;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.glslcompiler.annotation.CompilerAnnotations;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import org.openide.loaders.DataObject;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Created on 6. March 2007, 15:48
 * @autor Michael Bien
 * Manages a module's lifecycle. 
 */
public class Installer extends ModuleInstall {
    
    private Lookup.Result<DataObject> result;   // don't loose reference its week!
    private ShaderFileObserver observer;
    
    
    public void restored() {
        
        final InputOutput out = IOProvider.getDefault().getIO("compiler debug output", false);

        String nativesSource = GL.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        nativesSource = nativesSource.substring(0, nativesSource.lastIndexOf("/"));
        
        String moduleHome = nativesSource.substring(0, nativesSource.lastIndexOf("/"));
        
        nativesSource += "/jogl-natives.zip";

        out.getOut().println("natives path: "+nativesSource);
        out.getOut().println("module home: "+moduleHome);
        
                
        try{
            unzipFile(nativesSource, moduleHome+"/lib");
//            apendToLibraryPath(dest+File.separator+"jogl-natives");
//        }catch (NoSuchFieldException ex) {
//            Exceptions.printStackTrace(ex);
        }catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
//        }catch (IllegalAccessException ex) {
//            Exceptions.printStackTrace(ex);
        }catch(IOException ex){
            Exceptions.printStackTrace(ex);
        }
        
        final String[] strings = new String[2];
        GLWorker worker = new GLWorker();
        worker.addWork(new GLRunnable() {
            public void run(GLContext context) {
                GL gl = context.getGL();
                strings[0] = gl.glGetString(GL.GL_VERSION);
                strings[1] = gl.glGetString(GL.GL_VENDOR);
            }
        });
        worker.work();
        worker.destroy();
        
        int mayorVersion = Integer.parseInt(strings[0].substring(0, strings[0].indexOf(".")));
        if(mayorVersion < 2) {
            out.getOut().println("OpenGL 2.0 or higher required. Detected version is "+strings[0]+". Module is disabled.");
            
            return;
        }
        
        Preferences preferences = NbPreferences.forModule(Installer.class);
        preferences.put("GLVersion", strings[0]);
        preferences.put("GLVendor", strings[1]);
        preferences.put("JOGLVersion", Package.getPackage("javax.media.opengl").getImplementationVersion());
        
        // samples of compiler errors:
        // NV:  "(267) : error C0000: syntax error, unexpected identifier, expecting ';' or ',' at token ius";
        // ATI: "ERROR: 0:17: '-' :  wrong operand types  no operation '-' exists that takes a left-hand operand of type 'const int' and a right operand of type 'float' (or there is no acceptable conversion)"

        Pattern pattern;
        if(strings[1].contains("NVIDIA")) {
            pattern = Pattern.compile("\\((\\d+)\\)\\s*:\\s*(\\w+)");
        }else if(strings[1].contains("ATI")) {
            pattern = Pattern.compile("(\\w+):\\s*\\d+:(\\d+):");
        }else{
            pattern = Pattern.compile("()()");
        }
        preferences.put("GlslCompilerLogPattern", pattern.pattern());
        
        
        // TODO use lookup...
        GLSLCompiler.instance = new GLSLCompiler(pattern);
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                observer = new ShaderFileObserver();
                result = Utilities.actionsGlobalContext().lookupResult(DataObject.class);
                result.addLookupListener(observer);
            }
        });
    }
    

    @Override
    public void uninstalled() {
        super.uninstalled();
        if(result != null){
            result.removeLookupListener(observer);
            result = null;
        }
        CompilerAnnotations.clearAll();
    }
    

    private void unzipFile(String source, String destDir) throws IOException {
        
        ZipFile zip = new ZipFile(source);
        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        ZipEntry srcZipEntry;
        File destFile;
        byte[] buf = new byte[1024];
        
        destFile = new File(destDir);
        if (!destFile.exists()) // if its a directory, create it
            destFile.mkdir();
        
        while(en.hasMoreElements()) {
            srcZipEntry = (ZipEntry) en.nextElement();
            System.out.println(srcZipEntry.getName());
            destFile = new File(destDir + File.separator + srcZipEntry.getName());
            
            if (srcZipEntry.isDirectory()) { // if its a directory, create it
                destFile.mkdir();
                continue;
            }
            
            if(destFile.exists())
                continue;
            
            InputStream is = zip.getInputStream(srcZipEntry); // get the input stream
            FileOutputStream fos = new FileOutputStream(destFile);
            
            try{
                int n = 0;
                while ((n = is.read(buf, 0, 1024)) > -1)
                    fos.write(buf, 0, n);
            }finally{
                fos.close();
                is.close();
            }
        }
    }
    
    /*
     * a really bad hack... but it works
     */
    private void apendToLibraryPath(String path) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        
        
//        StringTokenizer tokenizer = new StringTokenizer(System.getProperty("java.library.path"), ";");
//        while(tokenizer.hasMoreTokens()) {
//            String token = tokenizer.nextToken().trim();
//            if(path.equals(token)) {
//                return;
//            }
//        }        
        
        Class clazz = ClassLoader.class;
        Field field = clazz.getDeclaredField("sys_paths");
        
        boolean accessible = field.isAccessible();
        if (!accessible)
            field.setAccessible(true);

        String[] original = (String[]) field.get(clazz);
        String[] newPaths;
        if(original == null) {
            newPaths = new String[] {path};
        }else{
            newPaths = new String[original.length+1];
            System.arraycopy(original, 0, newPaths, 0, original.length);
            newPaths[original.length] = path;
        }
        field.set(clazz, null);
        field.set(clazz, newPaths);
        field.setAccessible(accessible);

    } 
            

}
