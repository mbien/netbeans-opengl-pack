/*
 * GLSLCodeExporter2.java
 *
 * Created on May 7, 2007, 12:34 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;
import net.java.nboglpack.visualdesigner.shader.variables.VariablesCollection;

/**
 * This class collects information from ShaderNodes and exports the shader
 * created through the graph into GLSL ShaderCode.
 *
 * @author Samuel Sperling
 */
public class GLSLCodeExporter2 {// implements ShaderCodeExportVisitor, ShaderCodeExporter {
    
    private static final String NEW_LINE = "\n";
    private static final String AUTO_TRANSFER_NAME = "vv_"; // vertex variable
    
    private ArrayList<ShaderFunction> vertexFunctions = new ArrayList<ShaderFunction>();
    private ArrayList<ShaderFunction> fragmentFunctions = new ArrayList<ShaderFunction>();
    private VariablesCollection<ShaderVariable> varyingVariables;
    private HashMap<ShaderProgramInVariable, Object> usedInVars;
    private HashMap<ShaderProgramOutVariable, ValueAssignment> usedOutVarsVertex;
    private HashMap<ShaderProgramOutVariable, ValueAssignment> usedOutVarsFragment;
    private HashMap<GlobalVariable, ShaderProgramOutVariable> usedGlobalVars;
    private HashMap<ShaderProgramOutVariable, String> outVarNamesVertex;
    private HashMap<ShaderProgramOutVariable, String> outVarNamesFragment;
    private HashMap<GlobalVariable, String> automaticVaryingNames;
    private boolean isCurrentActivityFragment;
    private Writer vertexShaderSource;
    private Writer fragmentShaderSource;
    private Writer vertexMainfunction;
    private Writer fragmentMainfunction;
    
    /**
     * Creates a new instance of GLSLCodeExporter2
     */
    public GLSLCodeExporter2() {
    }

//    /**
//     * Exports a Shader-Graph staring with the given ouput var
//     * @param vertexShaderSource Writer that stores all the Sourcecode for the
//     *          vertexShader
//     * @param fragmentShaderSource Writer that stores all the Sourcecode for the
//     *          fragmentShader
//     * @param resultVariable  Variable where the final result is stored
//     *          should be gl_FragColor
//     *          Vars in this array can be of type GlobalVariable
//     *          or ShaderProgramOutVariable
//     */
//    public void exportShader(Writer vertexShaderSource,
//            Writer fragmentShaderSource) throws ExportingExeption, IOException {
//        this.vertexShaderSource = vertexShaderSource;
//        this.fragmentShaderSource = fragmentShaderSource;
//        //TODO: check graph for recursions because of the dataType resolving methods
//        
//        // Clean Up
//
//        vertexFunctions = new ArrayList<ShaderFunction>();
//        fragmentFunctions = new ArrayList<ShaderFunction>();
//        varyingVariables = new VariablesCollection<ShaderVariable>();
//        if (usedInVars == null) usedInVars = new HashMap<ShaderProgramInVariable, Object>();
//        else usedInVars.clear();
//        if (usedOutVarsVertex == null) usedOutVarsVertex = new HashMap<ShaderProgramOutVariable, ValueAssignment>();
//        else usedOutVarsVertex.clear();
//        if (usedOutVarsFragment == null) usedOutVarsFragment = new HashMap<ShaderProgramOutVariable, ValueAssignment>();
//        else usedOutVarsFragment.clear();
//        if (outVarNamesVertex == null) outVarNamesVertex = new HashMap<ShaderProgramOutVariable, String>();
//        else outVarNamesVertex.clear();
//        if (outVarNamesFragment == null) outVarNamesFragment = new HashMap<ShaderProgramOutVariable, String>();
//        else outVarNamesFragment.clear();
//        if (usedGlobalVars == null) usedGlobalVars = new HashMap<GlobalVariable, ShaderProgramOutVariable>();
//        else usedGlobalVars.clear();
//        if (automaticVaryingNames == null) automaticVaryingNames = new HashMap<GlobalVariable, String>();
//        else automaticVaryingNames.clear();
//        
//        vertexMainfunction = new StringWriter();
//        fragmentMainfunction = new StringWriter();
//        isCurrentActivityFragment = true;
//        
//        // Find all global variables that are outputting data to the hardware
//        for (GlobalVariable globalVariable : Editor.globalVariables.getAll()) {
//            try {
//                if (globalVariable.isHardwareGiven())
//                    resolveGlobalOutput(globalVariable);
//            } catch (ValueNotSetException ex) {
//                
//                // Variable is either just not in use, or a connector is
//                // not connected to a valid source
//                System.out.println("ResultVar " + globalVariable.getName() + " wasn't completaly resolvable\r\n");
//                System.out.println(ex.getMessage());
//            }
//        }
//        
//        // Write main method();
//        vertexShaderSource.write("void main()" + NEW_LINE + "{" + NEW_LINE);
//        vertexShaderSource.write(vertexMainfunction.toString());
//        vertexShaderSource.write(NEW_LINE + "}");
//        fragmentShaderSource.write("void main()" + NEW_LINE + "{" + NEW_LINE);
//        fragmentShaderSource.write(fragmentMainfunction.toString());
//        fragmentShaderSource.write(NEW_LINE + "}");
//    }
//    
//    private Writer getActiveMainSource() {
//        if (isCurrentActivityFragment)
//            return this.fragmentMainfunction;
//        else
//            return this.vertexMainfunction;
//    }
//    
//    private HashMap<ShaderProgramOutVariable, ValueAssignment> getActiveUsedOutVars() {
//        if (isCurrentActivityFragment)
//            return this.usedOutVarsFragment;
//        else
//            return this.usedOutVarsVertex;
//    }
//    
//    private HashMap<ShaderProgramOutVariable, String> getActiveOutVarNames() {
//        if (isCurrentActivityFragment)
//            return this.outVarNamesFragment;
//        else
//            return this.outVarNamesVertex;
//    }
//
//    private void resolveInputVar(ShaderProgramInVariable shaderVariable) throws ExportingExeption, IOException {
//        
//        // Get value of this Input
//        Object value = shaderVariable.export();
//        this.addInputVar(shaderVariable, value);
//        
//        if (value instanceof ShaderProgramOutVariable) {
//            // follow connector
//            resolveOutputVar((ShaderProgramOutVariable) value);
//            
//        } else if (value instanceof GlobalVariable) {
//            // follow global variable
//            resolveGlobalOutput((GlobalVariable) value);
//            
//        } else if (value instanceof VariableValue) {
//            // set Static value
//            
//        } else {
//            // returned value is not handable
//            throw new ExportingExeption("ShaderProgramInVariable " + shaderVariable.getName() +
//                    " returned unknown value type:" + value.getClass().getName());
//        }
//        
//        // All links should be resolved.
//    }
//    
//    private void resolveOutputVar(ShaderProgramOutVariable shaderProgramOutVariable)
//            throws ExportingExeption, IOException {
//        
//        ValueAssignment valueAssignment = addOutputVar(shaderProgramOutVariable);
//        if (valueAssignment != null) {
//            // OutVar hadn't been resolved before...
//
//            ShaderVariable[] inVars = valueAssignment.getInVariables();
//            if (inVars != null)
//                for (ShaderVariable inVar : inVars) {
//                    if (inVar instanceof GlobalVariable) {
//                        resolveGlobalInput((GlobalVariable) inVar);
//                    } else if (inVar instanceof ShaderProgramInVariable) {
//                        resolveInputVar((ShaderProgramInVariable) inVar);
//                    } else {
//                        
//                    }
//                }
//        } else {
//            valueAssignment = getActiveUsedOutVars().get(shaderProgramOutVariable);
//        }
//        
//        // So actual code can be generated
//        String varName = confirmOutputVarName(shaderProgramOutVariable);
//        getActiveMainSource().write("\t" + getDataTypeName(shaderProgramOutVariable.resolveDataType()) + " ");
//        getActiveMainSource().write(varName + " = " + valueAssignment.getCodeLine() + ";" + NEW_LINE);
//    }
//
//    private void resolveGlobalInput(GlobalVariable globalVariable) {
//        if (!globalVariable.isHardwareGiven()) {
//            //TODO: load global var connections
//        }
//    }
//
//    private void resolveGlobalOutput(GlobalVariable globalVariable) throws ExportingExeption, IOException {
//        
//        // Get Source
//        ShaderProgramOutVariable outVar = addGlobalVar(globalVariable);
//        boolean wasCurrentActivityFragment = this.isCurrentActivityFragment;
//        
//        if (outVar == null) return;
//        // GlobalVar hadn't been resolved before...
//
//        // Switch to vertex / fragment shader
//        switch(globalVariable.getGlobalType()) {
//            case GlobalVariable.GLOBAL_HARDWARE_FRAGMENT_OUT:
//
//                // Global can't be transfered from fragment to vertex shader
//                if (!this.isCurrentActivityFragment)
//                    throw new ExportingExeption("Global " + globalVariable.getName() +
//                            " can't be transfered from fragment to vertex shader");
//                this.isCurrentActivityFragment = true;
//                break;
//
//            case GlobalVariable.GLOBAL_HARDWARE_VERTEX_OUT:
//
//                this.isCurrentActivityFragment = false;
//                break;
//        }
//
//        resolveOutputVar(outVar);
//
//        // So actual code can be generated
//        String varName = globalVariable.getName();
//        getActiveMainSource().write("\t" + varName + " = " + confirmOutputVarName(outVar) + ";" + NEW_LINE);
//
//        if (!globalVariable.isHardwareGiven()) {
//            //TODO: load global var connections
//        }
//
//        this.isCurrentActivityFragment = wasCurrentActivityFragment;
//    }
//
//    private String getDataTypeName(int dataType) throws ExportingExeption {
//        if (!DataType.isDataTypeExplicit(dataType))
//            throw new ExportingExeption("given Output dataType is not explicit");
//        
//        return DataType.getDataTypeShortName(dataType);
//    }
//
//    private ValueAssignment addOutputVar(ShaderProgramOutVariable shaderProgramOutVariable) throws ExportingExeption {
//        if (shaderProgramOutVariable == null) return null;
//        
//        if (!getActiveUsedOutVars().containsKey(shaderProgramOutVariable)) {
//            ValueAssignment valueAssignment = shaderProgramOutVariable.exportValueAssignment(this);
//            valueAssignment.setExporter(this);
//            valueAssignment.setOutVariable(shaderProgramOutVariable);
//            getActiveUsedOutVars().put(shaderProgramOutVariable, valueAssignment);
//            return valueAssignment;
//        }
//        return null;
//    }
//
//    private ShaderProgramOutVariable addGlobalVar(GlobalVariable globalVariable) throws ExportingExeption {
//        if (globalVariable == null) return null;
//        if (!usedGlobalVars.containsKey(globalVariable)) {
//            ShaderProgramOutVariable outVar = globalVariable.getSourceVariable();
//            if (outVar == null)
//                throw new ValueNotSetException("Global var " + globalVariable.getName() + " has no source");
//            usedGlobalVars.put(globalVariable, outVar);
//            return outVar;
//        }
//        return null;
//    }
//
////    private void attachUnresolvedInVars(ShaderProgramInVariable[] shaderProgramInVariable) {
////        if (shaderProgramInVariable == null) return;
////        
////        for (ShaderProgramInVariable var : shaderProgramInVariable) {
////            if (!usedInVars.contains(var))
////                addInputVar(var);
////        }
////    }
//
//    private void addInputVar(ShaderProgramInVariable var, Object value) {
//        usedInVars.put(var, value);
//    }
//
//    private String confirmOutputVarName(ShaderProgramOutVariable shaderProgramOutVariable) {
//        
//        String varName = getActiveOutVarNames().get(shaderProgramOutVariable);
//        if (varName == null) {
//            varName = shaderProgramOutVariable.getName();
//            
//            // make sure varname is unique
//            int i = 2;
//            while(getActiveOutVarNames().containsValue(varName)) {
//                varName = shaderProgramOutVariable.getName() + i++;
//            }
//            getActiveOutVarNames().put(shaderProgramOutVariable, varName);
//            return varName;
//        } else {
//            return varName;
//        }
//    }
//    private String getOutputVarName(ShaderProgramOutVariable shaderProgramOutVariable) {
//        return getActiveOutVarNames().get(shaderProgramOutVariable);
//    }
//    
//    /**
//     * Gets the value of an input variable.
//     * which can be a regular value, or a link to a different variable
//     * @param shaderProgramInVariable requested variable
//     * @return String representing the value of the given variable
//     */
//    public String getInputValue(ShaderProgramInVariable shaderProgramInVariable) throws ExportingExeption {
//        Object value = usedInVars.get(shaderProgramInVariable);
//        
//        if (value instanceof ShaderProgramOutVariable) {
//            // Connector
//            return getOutputVarName((ShaderProgramOutVariable) value);
//        } else if (value instanceof VariableValue) {
//            // Value or Global
//            return toCode((VariableValue) value);
//        } else {
//            throw new ExportingExeption("ShaderProgramInVariable " + shaderProgramInVariable.getName() +
//                    " returned unknown value type:" + value.getClass().getName());
//        }
//    }
//    
//    /**
//     * Gets the name of an global variable in the current context.
//     * @param globalVar variable whos name is requested
//     * @return String Name of the global variable
//     */
//    public String getGlobalName(GlobalVariable globalVar) throws ExportingExeption {
//        
//    }
//
//    private String toCode(VariableValue variableValue) {
//        Object value = variableValue.getValue();
//        if (value instanceof ShaderVariable)
//            return ((ShaderVariable) value).getName();
//        
//        String code = "";
//        int dataTypeOnly = DataType.getDataTypeOnly(variableValue.getDataType());
//        int dimensions = DataType.getDimensions(variableValue.getDataType());
//        if ((dataTypeOnly &
//                (DataType.DATA_TYPE_SAMPLER |
//                DataType.DATA_TYPE_SAMPLER_SHADOW |
//                DataType.DATA_TYPE_SAMPLERCUBE))
//                > 0) {
//            // Sampler
//            //TODO: create Sampler value
//            code = DataType.getDataTypeName(dataTypeOnly);
//        } else {
//            if (dimensions > 1) code = "(";
//            for (int i = 0; i < dimensions; i++) {
//                switch (dataTypeOnly) {
//                    case DataType.DATA_TYPE_BOOL:
//                        code += variableValue.getBooleanString(i);
//                        break;
//                    case DataType.DATA_TYPE_INT:
//                        code += variableValue.getIntegerString(i);
//                        break;
//                    case DataType.DATA_TYPE_FLOAT:
//                        code += variableValue.getFloatString(i);
//                        break;
//                    case DataType.DATA_TYPE_MAT:
//                        code += variableValue.getMatrixString(i).replace('{', '(').replace('}', ')');
//                        break;
//                }
//                if (i != dimensions - 1) code += ", ";
//            }
//            if (dimensions > 1) code += ")";
//        }
//        return code;
//    }
//    
//    /**
//     * Shader language that this exporter uses/understands.
//     * @return Shader language that this exporter uses/understands.
//     */
//    public String getShaderLanguageName() {
//        return "GLSL";
//    }
//    
//    /**
//     * Registeres a function for the vertex Processor. That can than be used
//     * within other vertex functions.
//     * To use this function as main return function use the
//     * setVertexFunctionAsOutput function
//     * If a function with the same name already exists
//     * that uses the same variables this call is ignored.
//     *
//     *
//     * @param functionName Name of this function.
//     * @param inputVariables All input variables that this function uses
//     * @param outVariables All output variables that this function uses
//     * @param functionCode Code of this function. Use input names in your code
//     *      that are used in your definition givin through the
//     *      ShaderProgramVariablee class.
//     *      Use the output variable names for output data.
//     *      Don't use any return command.
//     */
//    public void registerVertexFunction(String functionName, ShaderProgramInVariable[] inputVariables, ShaderProgramOutVariable[] outVariables, String functionCode) throws ExportingExeption {
//        ShaderFunction newFunction = new ShaderFunction(functionName, inputVariables, outVariables, functionCode);
//        if (checkForSignature(vertexFunctions, newFunction))
//            vertexFunctions.add(newFunction);
//    }
//    
//    /**
//     * Registeres a function for the fragment Processor. That can than be used
//     * within other fragment functions.
//     * To use this function as main return function use the
//     * setFragmentFunctionAsOutput function
//     * If a function with the same name already exists
//     * that uses the same variables this call is ignored.
//     *
//     *
//     * @param functionName Name of this function.
//     * @param inputVariables All input variables that this function uses
//     * @param outVariables All output variables that this function uses
//     * @param functionCode Code of this function. Use input names in your code
//     *      that are used in your definition givin through the
//     *      ShaderProgramVariablee class.
//     *      Use the output variable names for output data.
//     *      Don't use any return command.
//     */
//    public void registerFragmentFunction(String functionName, ShaderProgramInVariable[] inputVariables, ShaderProgramOutVariable[] outVariables, String functionCode) throws ExportingExeption {
//        ShaderFunction newFunction = new ShaderFunction(functionName, inputVariables, outVariables, functionCode);
//        if (checkForSignature(fragmentFunctions, newFunction))
//            fragmentFunctions.add(newFunction);
//    }
//    
//    /**
//     * Registeres a variable that needs to be transfered from Vertex to FragmentShader.
//     * @param variable Definition of the variable that needs to be registered.
//     * @return In case the VariableName was in use already, the altered, unique name is returned
//     */
//    public String registerVaryingVariable(ShaderVariable variable) {
//        String name = variable.getName();
//        int i = 1;
//        while (varyingVariables.get(name) != null) {
//            name = variable.getName() + i;
//            i++;
//        }
//        if (i > 1)
//            variable.setName(name);
//        
//        varyingVariables.add(variable);
//        return name;
//    }
//    
//    /**
//     * In case this function needs to just return one line of code, use this
//     * function Otherwise register a function through registerVertexFunction
//     * and use setVertexOutputCodeLine to create the output.
//     * @param CodeLine  Line of Code that returns the output;
//     */
//    public void setVertexOutputCodeLine(String CodeLine) {
//    }
//    
//    public void setFragmentOutputCodeLine(String CodeLine) {
//    }
//    
//    public void setVertexFunctionAsOutput(String functionName) {
//    }
//    
//    public void setFragmentFunctionAsOutput(String functionName) {
//    }
//    
//    private boolean checkForSignature(ArrayList<ShaderFunction> existingFunctions, ShaderFunction newFunction) throws ExportingExeption {
//        for (ShaderFunction function : existingFunctions)
//            if (function.hasSameSignature(newFunction)) return false;
//        return true;
//    }
//
//    private void addVaryingVariable(ShaderVariable shaderVariable) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
    
}
