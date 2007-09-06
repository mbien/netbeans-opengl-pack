/*
 * IShaderCodeExportVisitor.java
 *
 * Created on April 26, 2007, 12:46 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 * Classes that implement this interface are collecting all necessary
 * information to create a complete shader code that can be compiled and
 * used to e.g. transfer to the grafic card
 *
 * @author Samuel Sperling
 */
public interface IShaderCodeExportVisitor {
    
    
    /**
     * Shader language that this exporter uses/understands.
     * @return Shader language that this exporter uses/understands.
     */
    public String getShaderLanguageName();
    
    /**
     * Registeres a function for the vertex Processor. That can than be used
     * within other vertex functions.
     * To use this function as main return function use the
     * setVertexFunctionAsOutput function
     * If a function with the same name already exists
     * that uses the same variables this call is ignored.
     * 
     * @param shaderFunction ShaderFunction that will be registered.
     * @param functionName Name of this function.
     * @param inputVariables All input variables that this function uses
     * @param outVariables All output variables that this function uses
     * @param functionCode Code of this function. Use input names in your code 
     *      that are used in your definition givin through the
     *      ShaderProgramVariablee class.
     *      Use the output variable names for output data.
     *      Don't use any return command.
     */
    public void registerVertexFunction(ShaderFunction shaderFunction) throws ExportingExeption;
    
    
    /**
     * Registeres a function for the fragment Processor. That can than be used
     * within other fragment functions.
     * To use this function as main return function use the
     * setFragmentFunctionAsOutput function
     * If a function with the same name already exists
     * that uses the same variables this call is ignored.
     * 
     * 
     * @param functionName Name of this function.
     * @param inputVariables All input variables that this function uses
     * @param outVariables All output variables that this function uses
     * @param functionCode Code of this function. Use input names in your code 
     *      that are used in your definition givin through the
     *      ShaderProgramVariablee class.
     *      Use the output variable names for output data.
     *      Don't use any return command.
     */
    public void registerFragmentFunction(ShaderFunction shaderFunction) throws ExportingExeption;
    
    /**
     * Registeres a variable that needs to be transfered from Vertex to FragmentShader.
     * @param variable Definition of the variable that needs to be registered.
     * @return In case the VariableName was in use already, the altered, unique name is returned
     */
    public String registerVaryingVariable(ShaderVariable variable);
    
    /**
     * Returns the value of an exportable
     * @param exportable Element whoes name is needed
     * @return The value of an exportable
     */
    public String getExportableValue(IExportable exportable) throws ExportingExeption ;
    
    /**
     * Finds a exporter saved assignable
     * @param assignable source of the value assignment
     * @return The ValueAssignment to the given element.
     *          null if it wasn't found.
     */
    public ValueAssignment findValueAssignment(IAssignable assignable);
    
    /**
     * Tries to automaticly export this object to the shading language.
     */
    public String autoExport(VariableValue variableValue) throws ExportingExeption;
    
    /**
     * Registers a unique Code Element
     */
    public void registerCodeElement(UniqueCodeElement uniqueCodeElement) throws ExportingExeption;
    
}
