/*
 * Shader.java
 *
 * Created on March 26, 2007, 12:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import com.sun.opengl.util.BufferUtil;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.media.opengl.GL;
import net.java.nboglpack.visualdesigner.tools.ArraySet;

/**
 *
 * @author Samuel Sperling
 */
public class Shader {
    
    private GL gl;
    private String[] contents;
    private int shaderId;
    private boolean isProcessed;
    private int shaderType;
    protected ShaderProgram program;
    private boolean containsErrors = false;
    
    
    /**
     * Creates a new instance of Shader
     * @param gl  OpenGL Device
     * @param contents  Shader source
     */
    public Shader(GL gl, int shaderType, String[] contents) {
        this.gl = gl;
        this.shaderId = gl.glCreateShaderObjectARB(shaderType);
        this.shaderType = shaderType;
        this.contents = contents;
        this.isProcessed = false;
    }   
    
    /** Creates a new instance of Shader */
    public Shader(GL gl, int shaderType, Reader contents) throws IOException {
        this(gl, shaderType, getContents(contents));
    }

    
    public static String[] getContents(Reader input) throws IOException {
        int size = 1;
        String[] contents = new String[size];
        char[] buffer = new char[1024];
        
        int i = 0;
        int readLength = 0;
        while ((readLength = input.read(buffer)) > 0) {
            if (i >= size)
                contents = addElement(contents, new String(buffer, 0, readLength));
            else
                contents[i] = new String(buffer, 0, readLength);// String.copyValueOf(buffer).trim();
            i++;
        }
        return contents;
    }
    
    private static String[] addElement(String[] arr, String newItem) {
        String[] tmpArray = new String[arr.length + 1];

        // Daten kopieren
        for (int i = 0; i < arr.length; i++) {
            tmpArray[i] = arr[i];
        }
        tmpArray[arr.length] = newItem;
        return tmpArray;
    }
    public boolean apply() {
        if (!this.isProcessed)
            process();
        return !containsErrors;
    }
    private void process() {
        loadShadersource();
        gl.glCompileShader(this.shaderId);
        isProcessed = true;
        String info = fetchShaderLogInfo();
        containsErrors = info.length() > 0;
        System.out.println("Shader: " + info);
        System.out.println("Shader-source: " + fetchShaderSource());
    }
    
    private void loadShadersource() {
        int[] length = new int[this.contents.length];
        for (int i = 0; i < this.contents.length; i++)
            length[i] = this.contents[i].length();
        gl.glShaderSource(this.shaderId,  this.contents.length, this.contents, length, 0);
    }

    
    public String fetchShaderLogInfo()
    {
        int[] length = new int[1];
        gl.glGetShaderiv(this.shaderId, GL.GL_INFO_LOG_LENGTH, length, 0);

        if (length[0] <= 0) return "";
        
        byte[] info = new byte[length[0]];
        gl.glGetShaderInfoLog(this.shaderId, info.length, length, 0, info, 0);

        return new String(info, 0, length[0]);
    }
    
    public String fetchShaderSource()
    {
        int[] length = new int[1];
        gl.glGetShaderiv(this.shaderId, GL.GL_SHADER_SOURCE_LENGTH, length, 0);

        if (length[0] <= 0) return "";
        
        byte[] source = new byte[length[0]];
        gl.glGetShaderSourceARB(this.shaderId, source.length, length, 0, source, 0);

        return new String(source, 0, length[0]);
    }

    /**
     * Getter for property contents.
     * @return Value of property contents.
     */
    public String[] getContents() {
        return this.contents;
    }

    /**
     * Setter for property contents.
     * @param contents New value of property contents.
     */
    public void setContents(String[] contents) {
        this.contents = contents;
        this.isProcessed = false;
    }

    /**
     * Getter for property program.
     * @return Value of property program.
     */
    public ShaderProgram getProgram() {
        return this.program;
    }

    /**
     * Setter for property program.
     * @param program New value of property program.
     */
    public void setProgram(ShaderProgram program) {
        this.program = program;
    }

    /**
     * Getter for property shaderId.
     * @return Value of property shaderId.
     */
    public int getShaderId() {
        return this.shaderId;
    }

    /**
     * Setter for property shaderId.
     * @param shaderId New value of property shaderId.
     */
    public void setShaderId(int shaderId) {
        this.shaderId = shaderId;
    }
    
}
