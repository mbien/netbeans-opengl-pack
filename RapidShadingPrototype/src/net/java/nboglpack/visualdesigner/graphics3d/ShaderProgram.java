/*
 * ShaderProgram.java
 *
 * Created on March 29, 2007, 12:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.util.ArrayList;
import java.util.HashMap;
import javax.media.opengl.GL;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderProgram {
    
    private GL gl;
    private VertexShader vertexShader;
    private FragmentShader fragmentShader;
    private boolean isProcessed = false;
    private HashMap<String, ShaderAttribute> attributes;
    private int programId;
    private boolean containsErrors = false;
    private ArrayList<ShaderAttribute> samplers;
    
    
    /**
     * Creates a new instance of ShaderProgram
     */
    public ShaderProgram(GL gl, VertexShader vertexShader, FragmentShader fragmentShader) {
        this.gl = gl;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        attributes = new HashMap<String, ShaderAttribute>();
        samplers = new ArrayList<ShaderAttribute>();
    }
    
    public void apply() {
        if (!isProcessed)
            process();
        if (containsErrors)
            return;
        gl.glUseProgramObjectARB(programId);
        
        // apply all attributes
        for (ShaderAttribute attribute : this.attributes.values())
            attribute.apply();
    }

    /**
     * Processes the programm in terms of compiling shaders attaching these
     * and linking the program.
     * This needs to be done before the first use of apply.
     */
    public void process() {
        if (!vertexShader.apply()) {
            containsErrors = true;
            return;
        }
        if (!fragmentShader.apply()) {
            containsErrors = true;
            return;
        }
        
        programId = gl.glCreateProgram();
        gl.glAttachShader(programId, fragmentShader.getShaderId());
        gl.glAttachShader(programId, vertexShader.getShaderId());
        gl.glLinkProgram(programId);
        clearAttributeLocations();
        isProcessed = true;
        String ProgramInfo = fetchProgramLogInfo();
        containsErrors = ProgramInfo.length() > 0;
        System.out.println("Program: " + ProgramInfo);
    }
    
    public void detatchVertexShader() {
        if (this.vertexShader != null)
            gl.glDetachShader(this.programId, this.vertexShader.getShaderId());
    }
    
    public void detatchFragmentShader() {
        if (this.fragmentShader != null)
            gl.glDetachShader(this.programId, this.fragmentShader.getShaderId());
    }

    private void clearAttributeLocations() {
        for (ShaderAttribute attribute : this.attributes.values()) {
            attribute.clearLocation();
        }
    }

    
    /**
     * Changes a uniform attribute of the shader program
     * @return Returns false if program wasn't compiled successfully yet or
     *         Returns false if the attributeName couldn't be found in the
     *         program.
     *         Returns true if attribute was found.
     * @param attributeName Name of the Attribute to change
     * @param value new value of the attribute
     */
    public void setAttribute(ShaderAttribute attribute) {
        
        // TODO: what about overwriting an old attribute?
        attributes.put(attribute.name, attribute);
        attribute.setProgram(this);
        
        if (attribute.isSampler()) {
            samplers.add(attribute);
            attribute.setTextureNum(samplers.indexOf(attribute));
        }
    }
    
    public ShaderAttribute getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }
    
    public String fetchProgramLogInfo()
    {
        int[] length = new int[1];
        gl.glGetProgramivARB(this.programId, GL.GL_INFO_LOG_LENGTH, length, 0);

        if (length[0] <= 0) return "";
        
        byte[] info = new byte[length[0]];
        gl.glGetProgramInfoLog(this.programId, info.length, length, 0, info, 0);

        return new String(info, 0, length[0]);
    }
    
    /**
     * Getter for property vertexShader.
     * @return Value of property vertexShader.
     */
    public VertexShader getVertexShader() {
        return this.vertexShader;
    }

    /**
     * Setter for property vertexShader.
     * @param vertexShader New value of property vertexShader.
     */
    public void setVertexShader(VertexShader vertexShader) {
        vertexShader.setProgram(this);
        this.vertexShader = vertexShader;
    }

    /**
     * Getter for property fragmentShader.
     * @return Value of property fragmentShader.
     */
    public FragmentShader getFragmentShader() {
        return this.fragmentShader;
    }

    /**
     * Setter for property fragmentShader.
     * @param fragmentShader New value of property fragmentShader.
     */
    public void setFragmentShader(FragmentShader fragmentShader) {
        fragmentShader.setProgram(this);
        this.fragmentShader = fragmentShader;
    }

    /**
     * Getter for property programId.
     * @return Value of property programId.
     */
    public int getProgramId() {
        return this.programId;
    }

    /**
     * Setter for property programId.
     * @param programId New value of property programId.
     */
    public void setProgramId(int programId) {
        this.programId = programId;
    }

    
    
}
