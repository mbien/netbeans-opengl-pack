package com.mbien.engine.glsl;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.media.opengl.GL2;

/**
 * Created on 27. August 2006, 13:40
 * @author Michael Bien
 */
public class GLSLProgram {
    
 private int handle = -1;
 
 private final ArrayList<GLSLShader> shaders;
    
    /** Creates a new instance of GLSLProgram */
    public GLSLProgram() {
        shaders = new ArrayList<GLSLShader>();
    }
    
    public void createProgram(GL2 gl) {
        handle = gl.glCreateProgramObjectARB();
    }
    
    /**
     * creates a program, attaches all shaders to it and finally links the shaders
     */
    public void initProgram(GL2 gl, GLSLShader... shaders) throws GLSLLinkException {
        
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
    public void deinitProgram(GL2 gl, boolean deleteShaders) {
        gl.glDeleteProgram(handle);
        
        if(deleteShaders) {
            while(!shaders.isEmpty())
                shaders.remove(0).deleteShader(gl);
        }
        
        handle = -1;
    }
    
    public void attachShader(GL2 gl, GLSLShader shader) {
        
        gl.glAttachObjectARB(handle, shader.getID());
        // mark shader to delete; this will be done if the program will be deleted by the application
        // TODO test detach behavior if marked as delete
        //gl.glDeleteObjectARB(shader.getID());
        shaders.add(shader);
    }
    
    public void detachShader(GL2 gl, GLSLShader shader) {
        gl.glDetachObjectARB(handle, shader.getID());
        shaders.remove(shader);
    }
    
    public void linkProgram(GL2 gl) {
        gl.glLinkProgramARB(handle);
    }
    
    public int allocateUniform(GL2 gl, String name) {
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
    public void enable(GL2 gl) {
        gl.glUseProgramObjectARB(handle);
    }
    
    /**
     * returns to GL2 fixed function pipeline (shader is disabled)
     */
    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
    }
    
        
    public void checkProgram(GL2 gl) throws GLSLLinkException {
        
        boolean linked = true;
        boolean valid = true;
        
        int[] buffer = new int[1];
               
        // check link status
        gl.glGetObjectParameterivARB(handle, GL2.GL_OBJECT_LINK_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL2.GL_FALSE) {// 1 or 0
            linked = false;
//            getLog().warning("error linking program");
        }
        // validate program
        gl.glValidateProgramARB(handle);
        gl.glGetObjectParameterivARB(handle, GL2.GL_OBJECT_VALIDATE_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL2.GL_FALSE) {
            valid = false;
//            getLog().warning("program validation reports error");
        }
        // dump log
        gl.glGetObjectParameterivARB(handle, GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, buffer, 0);
        byte[] log = new byte[buffer[0]];
        gl.glGetInfoLogARB(handle, buffer[0], buffer, 0, log, 0);
        
//        if(log[0] != 0) // 0 if empty
//            getLog().warning("linker info log:\n"+new String(log));
        
        if(!linked || !valid) {
            
            ArrayList<String> allNames = new ArrayList<String>();
            for(int i = 0; i < shaders.size(); i++) {
                GLSLShader shader = shaders.get(i);
                for(int n = 0; n < shader.shaderNames.length; n++)
                    allNames.add(shader.shaderNames[n]);
            }
            
            throw new GLSLLinkException(this, new String(log, 0, log.length-1).split("\n"));
            
        }
        
    }
    
    private final Logger getLog() {
        return Logger.getLogger(this.getClass().getPackage().getName());
    }

    public GLSLShader[] getShaders() {
        return shaders.toArray(new GLSLShader[shaders.size()]);
    }
    
    
}