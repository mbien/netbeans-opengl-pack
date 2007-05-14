package com.mbien.engine.glsl;

import com.mbien.engine.glsl.GLSLShader;
import java.util.logging.Logger;
import javax.media.opengl.GL;

/**
 * Created on 27. August 2006, 13:40
 * @author Michael Bien<br><br>
 */
public class GLSLProgram {
    
 private int handle = -1;
    
    /** Creates a new instance of GLSLProgram */
    public GLSLProgram() {
    }
    
    public void createProgram(GL gl) {
        handle = gl.glCreateProgramObjectARB();
    }
    
    /**
     * creates a program, attaches all shaders to it and finally links the shaders
     */
    public void initProgram(GL gl, GLSLShader... shaders) {
        if(handle == -1)
            createProgram(gl);
        
        for(int i = 0; i < shaders.length; i++)
            attachShader(gl, shaders[i]);
        
        linkProgram(gl);
        checkProgram(gl);
    }
    
    public void deinitProgram(GL gl) {
        gl.glDeleteProgram(handle);
        handle = -1;
    }
    
    public void attachShader(GL gl, GLSLShader shader) {
        gl.glAttachObjectARB(handle, shader.getID());
        // mark shader to delete; this will be done if the program will be deleted by the application
        // TODO test detach behavior if marked as delete
        //gl.glDeleteObjectARB(shader.getID());
    }
    
    public void detachShader(GL gl, GLSLShader shader) {
        gl.glDetachObjectARB(handle, shader.getID());
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
    
    public void enable(GL gl) {
        gl.glUseProgramObjectARB(handle);
    }
    
    public void disable(GL gl) {
        gl.glUseProgramObjectARB(0);
    }
    
    private void checkProgram(GL gl) {
                
        int[] buffer = new int[1];
        
        // check link status
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_LINK_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL.GL_FALSE) // 1 or 0
            getLog().warning("error linking program");
        
        // validate program
        gl.glValidateProgramARB(handle);
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_VALIDATE_STATUS_ARB, buffer, 0);
        if(buffer[0] == GL.GL_FALSE)
            getLog().warning("program validation reports error");
        
        // dump log
        gl.glGetObjectParameterivARB(handle, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, buffer, 0);
        byte[] log = new byte[buffer[0]];
        gl.glGetInfoLogARB(handle, buffer[0], buffer, 0, log, 0);
        
        if(log[0] != 0) // 0 if empty
            getLog().warning("linker info log:\n"+new String(log));
    }
    
    private Logger getLog() {
        return Logger.getLogger(this.getClass().getPackage().getName());
    }
    
    
}