package com.mbien.engine.glsl;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.media.opengl.GL;

/**
 * Created on 27. August 2006, 13:40
 * @author Michael Bien<br><br>
 */
public class GLSLProgram {
    
 private int handle = -1;
 
 private final ArrayList<GLSLShader> shaders;
    
    /** Creates a new instance of GLSLProgram */
    public GLSLProgram() {
        shaders = new ArrayList<GLSLShader>(6);
    }
    
    public void createProgram(GL gl) {
        handle = gl.glCreateProgramObjectARB();
    }
    
    /**
     * creates a program, attaches all shaders to it and finally links the shaders
     */
    public void initProgram(GL gl, GLSLShader... shaders) throws GLSLLinkException {
        
        this.shaders.clear();
                
        if(handle == -1)
            createProgram(gl);
        
        for(int i = 0; i < shaders.length; i++)
            attachShader(gl, shaders[i]);
        
        linkProgram(gl);
        checkProgram(gl);
    }
    
    /**
     * deletes the program
     * @param deleteShaders - if true all previously attached shaders will be deleted too
     */
    public void deinitProgram(GL gl, boolean deleteShaders) {
        gl.glDeleteProgram(handle);
        
        if(deleteShaders)
            for(int i = 0; i < shaders.size(); i++)
                shaders.get(i).deleteShader(gl);
        
        handle = -1;
    }
    
    public void attachShader(GL gl, GLSLShader shader) {
        
        gl.glAttachObjectARB(handle, shader.getID());
        // mark shader to delete; this will be done if the program will be deleted by the application
        // TODO test detach behavior if marked as delete
        //gl.glDeleteObjectARB(shader.getID());
        shaders.add(shader);
    }
    
    public void detachShader(GL gl, GLSLShader shader) {
        gl.glDetachObjectARB(handle, shader.getID());
        shaders.add(shader);
    }
    
    public void linkProgram(GL gl) {
        gl.glLinkProgramARB(handle);
    }
    
    public int allocateUniform(GL gl, String name) {
        int uniform = gl.glGetUniformLocation(handle, name);

        if(uniform == -1){
            getLog().severe("uniform "+name+" not found in program");
            throw new IllegalArgumentException();
        }
                
        return uniform;
    }
    
    /**
     * enables this shader program
     */
    public void enable(GL gl) {
        gl.glUseProgramObjectARB(handle);
    }
    
    /**
     * returns to GL fixed function pipeline (shader is disabled)
     */
    public void disable(GL gl) {
        gl.glUseProgramObjectARB(0);
    }
    
        
    public void checkProgram(GL gl) throws GLSLLinkException {
        
        boolean error = false;
        
        int[] buffer = new int[1];
               
        // check link status
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_LINK_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL.GL_FALSE) // 1 or 0
            error = true;
//            getLog().warning("error linking program");
        
        // validate program
        gl.glValidateProgramARB(handle);
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_VALIDATE_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL.GL_FALSE)
            error = true;
//            getLog().warning("program validation reports error");
        
        // dump log
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, buffer, 0);
        byte[] log = new byte[buffer[0]];
        gl.glGetInfoLogARB(handle, buffer[0], buffer, 0, log, 0);
        
//        if(log[0] != 0) // 0 if empty
//            getLog().warning("linker info log:\n"+new String(log));
        
        
        if(error) {
            
            ArrayList<String> allNames = new ArrayList<String>();
            for(int i = 0; i < shaders.size(); i++) {
                GLSLShader shader = shaders.get(i);
                for(int n = 0; n < shader.shaderNames.length; n++)
                    allNames.add(shader.shaderNames[n]);
            }
            
            throw new GLSLLinkException(    allNames.toArray(new String[allNames.size()]), 
                                            new String(log, 0, log.length-1).split("\n"));
            
        }
        
    }
    
    private Logger getLog() {
        return Logger.getLogger(this.getClass().getPackage().getName());
    }
    
    
}