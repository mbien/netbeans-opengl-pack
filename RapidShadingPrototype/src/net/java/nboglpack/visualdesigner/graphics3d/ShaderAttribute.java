/*
 * ShaderArgument.java
 *
 * Created on April 4, 2007, 4:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.security.InvalidParameterException;
import javax.media.opengl.GL;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderAttribute {
    
    private GL gl;
    
    /** Programm that uses this attribute */
    private ShaderProgram program;
    
    /** Name of the argument */
    public String name;
    
    /** This value is an array of ints */
    public static final int INT_ARRAY = 1;

    /** This value is an array of floats */
    public static final int FLOAT_ARRAY = 2;

    /** This value is a matrix */
    public static final int MATRIX = 3;

    /** This value is a single int for a sampler Id */
    public static final int SAMPLER = 4;

    /** The type of data for this value. See constants above */
    public int dataType;

    /** The data when an array of ints are provided */
    public int[] intData;

    /** The data when an array of floats are provided */
    public float[] floatData;
    
    /** The data when a smaple is provided */
    public TextureMap textureMapData;

    /** The number of items in a single value - 1, 2, 3 or 4 */
    public int size;

    /** The number of values to use out of the array */
    public int count;

    /** Flag for whether the matrix values should be transposed */
    public boolean transposeMatrix;

    /** Index of the uniform location value once fetched from GL */
    private int uniformLocation = -1;

    
    /** Creates a new instance of ShaderArgument */
    public ShaderAttribute(GL gl) {
        this.gl = gl;
    }
    
    /**
     * Creates a new instance of ShaderAttribute of Type INT_ARRAY
     *
     * @param gl Open GL reference
     * @param name Name of this attribute
     * @param values values of this parameter
     * @param size Size if each element 1, 2, 3 or 4
     */
    public ShaderAttribute(GL gl, String name, int[] values, int size) {
        if (size < 1 || size > 4) throw new InvalidParameterException("size parameter must be between 1 and 4");
        
        this.gl = gl;
        this.name = name;
        this.dataType = INT_ARRAY;
        this.intData = values;
        this.size = size;
        this.count = values.length / size;
    }    
    
    /**
     * Creates a new instance of ShaderAttribute of Type FLOAT_ARRAY
     *
     * @param gl Open GL reference
     * @param name Name of this attribute
     * @param values values of this parameter
     * @param size Size if each element 1, 2, 3 or 4
     */
    public ShaderAttribute(GL gl, String name, float[] values, int size) {
        if (size < 1 || size > 4) throw new InvalidParameterException("size parameter must be between 1 and 4");
        
        this.gl = gl;
        this.name = name;
        this.dataType = FLOAT_ARRAY;
        this.floatData = values;
        this.size = size;
        this.count = values.length / size;
    }
    
    /**
     * Creates a new instance of ShaderAttribute
     *
     * @param gl Open GL reference
     * @param name Name of this attribute
     * @param dataType Type of the attribute. needs to be of type FLOAT_ARRAY
     *                   or MATRIX
     * @param values values of this parameter
     * @param size Size if each element 1, 2, 3 or 4
     */
    public ShaderAttribute(GL gl, String name, int dataType, float[] values, int size) {
        if (size < 1 || size > 4) throw new InvalidParameterException("size parameter must be between 1 and 4");
        
        this.gl = gl;
        this.name = name;
        this.dataType = dataType;
        this.floatData = values;
        this.size = size;
        if (dataType == MATRIX)
            this.count = values.length / size;
        else
            this.count = values.length / size / size;
    }
    
    /**
     * Creates a new instance of ShaderAttribute
     *
     * @param gl Open GL reference
     * @param name Name of this attribute
     * @param dataType Type of the attribute. needs to be of type FLOAT_ARRAY
     *                   or MATRIX
     * @param values values of this parameter
     * @param size Size if each element 1, 2, 3 or 4
     */
    public ShaderAttribute(GL gl, String name, TextureMap textureMapData) {
        this.gl = gl;
        this.name = name;
        this.dataType = SAMPLER;
        this.textureMapData = textureMapData;
        this.size = size;
        this.count = 1;
        this.isProcessed = false;
    }
    
    public void setProgram(ShaderProgram program) {
        this.program = program;
    }
    
    private boolean isProcessed = true;
    public void apply() {
        if(uniformLocation < 0)
        {
            uniformLocation = getLoc();
            
            // not valid
            if(uniformLocation < 0)
                return;
        }
        
        if (!isProcessed) {
            process();
        }

        switch(dataType)
        {
            case INT_ARRAY:
                switch(size)
                {
                    case 1:
                        gl.glUniform1ivARB(uniformLocation,
                                           count,
                                           intData,
                                           0);
                        break;

                    case 2:
                        gl.glUniform2ivARB(uniformLocation,
                                           count,
                                           intData,
                                           0);
                        break;

                    case 3:
                        gl.glUniform3ivARB(uniformLocation,
                                           count,
                                           intData,
                                           0);
                        break;

                    case 4:
                        gl.glUniform4ivARB(uniformLocation,
                                           count,
                                           intData,
                                           0);
                        break;
                }
                break;

            case FLOAT_ARRAY:
                switch(size)
                {
                    case 1:
                        gl.glUniform1fvARB(uniformLocation,
                                           count,
                                           floatData,
                                           0);
                        break;

                    case 2:
                        gl.glUniform2fvARB(uniformLocation,
                                           count,
                                           floatData,
                                           0);
                        break;

                    case 3:
                        gl.glUniform3fvARB(uniformLocation,
                                           count,
                                           floatData,
                                           0);
                        break;

                    case 4:
                        gl.glUniform4fvARB(uniformLocation,
                                           count,
                                           floatData,
                                           0);
                        break;
                }
                break;

            case MATRIX:
                switch(size)
                {
                    case 2:
                        gl.glUniformMatrix2fvARB(uniformLocation,
                                                 count,
                                                 transposeMatrix,
                                                 floatData,
                                                 0);
                        break;

                    case 3:
                        gl.glUniformMatrix3fvARB(uniformLocation,
                                                 count,
                                                 transposeMatrix,
                                                 floatData,
                                                 0);
                        break;

                    case 4:
                        gl.glUniformMatrix4fvARB(uniformLocation,
                                                 count,
                                                 transposeMatrix,
                                                 floatData,
                                                 0);
                        break;
                }
                break;

            case SAMPLER:
                gl.glUniform1iARB(uniformLocation, this.intData[0]);
                break;
        }
    }
    
    private int getLoc() {
        return gl.glGetUniformLocationARB(this.program.getProgramId(), this.name);
    }

    void clearLocation() {
        this.uniformLocation = -1;
    }

    private void process() {
        switch(dataType)
        {
            case SAMPLER:
                textureMapData.process(GL.GL_TEXTURE0 + this.intData[0]);
        }
        isProcessed = true;
    }

    public boolean isSampler() {
        return dataType == SAMPLER;
    }
    
    public void setTextureNum(int textureNum) {
        this.intData = new int[] {textureNum};
    }
    
}
