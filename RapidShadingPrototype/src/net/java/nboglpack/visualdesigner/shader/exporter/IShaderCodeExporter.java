/*
 * IShaderCodeExporter.java
 *
 * Created on May 17, 2007, 2:40 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;

/**
 * Exports a ShaderGraph to compilable ShaderCode
 *
 * @author Samuel Sperling
 */
public interface IShaderCodeExporter {
    
    /**
     * Exports a Shader-Graph staring with the given ouput var
     * @param vertexShaderSource Writer that stores all the Sourcecode for the
     *          vertexShader
     * @param fragmentShaderSource Writer that stores all the Sourcecode for the
     *          fragmentShader
     * @param resultVariables  Variables where the final result is stored
     *          for GLSL that would be the gl_FragColor var.
     *          Vars in this array can be of type GlobalVariable
     *          or ShaderProgramOutVariable
     * @return List of all dynamicattributes that this shader needs.
     */
    public ArrayList<ExternalAttribute> exportShader(Writer vertexShaderSource,
            Writer fragmentShaderSource)
            throws ExportingExeption, IOException;
    
    /**
     * Gets the value of an input variable.
     * which can be a regular value, or a link to a different variable
     * @param shaderProgramInVariable requested variable
     * @return String representing the value of the given variable
     */
    public String getInputValue(ShaderProgramInVariable shaderProgramInVariable)
    throws ExportingExeption;
    
    /**
     * Gets the name of an global variable in the current context.
     * @param globalVar variable whose name is requested
     * @return String Name of the global variable
     */
    public String getGlobalName(GlobalVariable globalVariable)
    throws ExportingExeption;
}
