package com.mbien.engine.glsl;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorkerImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

/**
 * Created on 26. August 2006, 15:26
 * @author Michael Bien<br><br>
 */
public class GLSLShader {
    
 public final TYPE type;
 
 private final String source;
 final String[] shaderNames;
 
 private int handle;
 
 private final static EnumMap<TYPE, Boolean> SUPPORTED_SHADER = new EnumMap<TYPE, Boolean>(TYPE.class);
 
 private boolean throwExceptionOnCompilerWarning = false;
 
    public GLSLShader(String... fileLocation) {
        
        if(fileLocation[0].endsWith(".frag"))
            this.type = TYPE.FRAGMENT;
        else if(fileLocation[0].endsWith(".vert"))
            this.type = TYPE.VERTEX;
        else if(fileLocation[0].endsWith(".geom"))
            this.type = TYPE.GEOMETRY;
        else
            throw new IllegalArgumentException("Wrong file ending; only .frag, .vert and .geom endings are allowed");
        
        shaderNames = new String[fileLocation.length];
        File[] files = new File[fileLocation.length];
        
        for(int i = 0; i < files.length; i++) 
            files[i] = new File(fileLocation[i]);
        
        source = readSourceFile(files);
    }
    
    public GLSLShader(File... files) {
        
        if(files[0].getName().endsWith(".frag"))
            this.type = TYPE.FRAGMENT;
        else if(files[0].getName().endsWith(".vert"))
            this.type = TYPE.VERTEX;
        else if(files[0].getName().endsWith(".geom"))
            this.type = TYPE.GEOMETRY;
        else
            throw new IllegalArgumentException("Wrong file ending; only .frag, .vert and .geom endings are allowed");
        
        shaderNames = new String[files.length];
        source = readSourceFile(files);
    }
    
    public GLSLShader(String source, String name, TYPE type) {
        this.type = type;
        shaderNames = new String[]{ name };
        this.source = source;
    }
    
    public GLSLShader(String[] sources, String[] names,  TYPE type) {
        this.type = type;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sources.length; i++) {
            sb.append(sources[i]);
        }
        
        shaderNames = names;
        source = sb.toString();
    }
    
    
    private final String readSourceFile(File... files) {
        
        StringBuilder sb = new StringBuilder();
                
        for(int i = 0; i < files.length; i ++) {
            
            FileReader reader = null;
            
            int length = 0;
            File sourceFile = files[i];
            char[] buffer = new char[(int)sourceFile.length()];
            shaderNames[i] = sourceFile.getName();
            try {
                reader = new FileReader(sourceFile);
                length = reader.read(buffer);
            } catch (FileNotFoundException ex) {
                getLog().severe("shader source not found\n"+ex.getMessage());
            } catch (IOException ex) {
                getLog().severe("exception until shader source read\n"+ex.getMessage());
            }finally {
                if(reader != null) {
                    try{
                        reader.close();
                    }catch(IOException e){
                        getLog().severe("can't close opened stream\n"+e.getMessage());                 
                    }
                }
            }
            
            sb.append(buffer, 0, length);
        }             

        getLog().fine("shader source read done");
        
        return sb.toString();
    }
    
    public void initShader(GL gl) throws GLSLCompileException {
        handle = gl.glCreateShaderObjectARB(type.GL_TYPE);
        gl.glShaderSourceARB(handle, 1, new String[] {source}, new int[] {source.length()}, 0);
        gl.glCompileShaderARB(handle);
        checkShader(gl);
    }
    
    public void deleteShader(GL gl) {
        gl.glDeleteShader(handle);
    }
    
    private void checkShader(GL gl) throws GLSLCompileException {
        
        boolean error = false;
        
        // check compile state
        int[] buffer = new int[1];
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_COMPILE_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL.GL_FALSE) {
//            getLog().warning("error compiling shader:\n"+getName());
            error = true;
        }
        
        // log info log
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, buffer, 0);
        byte[] log = new byte[buffer[0]];
        gl.glGetInfoLogARB(handle, buffer[0], buffer, 0, log, 0);
        
        if(log[0] != 0)  {// 0 if empty
            error = throwExceptionOnCompilerWarning | error; // TODO setup exception level
//            getLog().warning("compiler info log:\n"+new String(log, 0, log.length-1));
        }
        
        if(error)
            throw new GLSLCompileException(shaderNames, new String(log, 0, log.length-1).split("\n"));
    }
    
    public static Logger getLog() {
        return Logger.getLogger(GLSLShader.class.getPackage().getName());
    }
    
    public int getID() {
        return handle;
    }
    
    public void setThrowExceptionOnCompilerWarning(boolean b) {
        this.throwExceptionOnCompilerWarning = b;
    }
    
    public String getName() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < shaderNames.length; i++) {
            sb.append(shaderNames[i]);
            if(i+1 < shaderNames.length)
                sb.append("\n");
        }
        return sb.toString();
    }
    
    private static boolean isShaderSupported(TYPE type) {
        
        if(SUPPORTED_SHADER.get(type) == null) {
            
           GLWorkerImpl worker = new GLWorkerImpl();

           worker.work(new GLRunnable() {
              public void run(GLContext context) {
                  GL gl = context.getGL();
                  SUPPORTED_SHADER.put(TYPE.VERTEX, gl.isExtensionAvailable(TYPE.VERTEX.EXT_STRING));
                  SUPPORTED_SHADER.put(TYPE.FRAGMENT, gl.isExtensionAvailable(TYPE.FRAGMENT.EXT_STRING));
                  SUPPORTED_SHADER.put(TYPE.GEOMETRY, gl.isExtensionAvailable(TYPE.GEOMETRY.EXT_STRING));
              }
           });
           
           worker.destroy();
            
        }
        
        return SUPPORTED_SHADER.get(type);
    }
    
    public enum TYPE {
        
        VERTEX(GL.GL_VERTEX_SHADER, "GL_ARB_vertex_shader"),
        FRAGMENT(GL.GL_FRAGMENT_SHADER, "GL_ARB_fragment_shader"),
        GEOMETRY(GL.GL_GEOMETRY_SHADER_EXT, "GL_EXT_geometry_shader4"); //TODO not sure if this is correct: GL_EXT_geometry_shader4
                
        public final int GL_TYPE;
        public final String EXT_STRING;
        
        private TYPE(int gltype, String extention) {
            GL_TYPE = gltype;
            EXT_STRING = extention;
        }
        
        public static TYPE parse(String string) {
            if(string.equalsIgnoreCase("frag"))
                return TYPE.FRAGMENT;
            else if(string.equalsIgnoreCase("vert"))
                return TYPE.VERTEX;
            else if(string.equalsIgnoreCase("geom"))
                return TYPE.GEOMETRY;
            return null;
        }
        
        public boolean isSupported() {
            return isShaderSupported(this);
        }
        
    }
    
       
}
