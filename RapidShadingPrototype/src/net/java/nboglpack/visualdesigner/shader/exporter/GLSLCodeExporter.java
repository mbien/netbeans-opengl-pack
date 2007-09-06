/*
 * GLSLCodeExporter.java
 *
 * Created on May 25, 2007, 5:16 PM
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
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 * This class collects information from ShaderNodes and exports the shader
 * created through the graph into GLSL ShaderCode.
 *
 * @author Samuel Sperling
 */
public class GLSLCodeExporter implements IShaderCodeExportVisitor, IShaderCodeExporter {
    
    private static final String NEW_LINE = "\n";
    private boolean isCurrentActivityFragment;
    private Writer vertexMainfunction;
    private Writer fragmentMainfunction;
    private static String[] reservedWords = new String[] {
        "attriobute", "varying", "uniform", "const",
        "bool", "int", "float", "vec2", "vec3", "vec4", "ivec2", "ivec3", "ivec4", "bvec2", "bvec3", "bvec4", "mat2", "mat3", "mat4",
        "x", "y", "z", "w", "r", "g", "b", "a", "s", "t", "p", "q",
        "sampler1D", "sampler2D", "sampler3D", "samplerCube", "sampler1DShadow", "sampler2DShadow",
        "struct", "if", "for",
        "in", "out", "inout", "input", "output"
    };
    private UniqueCodeElementCollection uniqueCodeElementCollection;
    private HashMap<IAssignable, ValueAssignment> assignedValues;
    private HashMap<IExportable, String> exportedValues;
    private ArrayList<ShaderFunction> shaderFunctions;
    private int version = 110;
    
    /** Creates a new instance of GLSLCodeExporter */
    public GLSLCodeExporter() {
    }

    public ArrayList<ExternalAttribute> exportShader(Writer vertexShaderSource, Writer fragmentShaderSource) throws ExportingExeption, IOException {
        
        assignedValues = new HashMap<IAssignable, ValueAssignment>();
        exportedValues = new HashMap<IExportable, String>();
        shaderFunctions = new ArrayList<ShaderFunction>();
        uniqueCodeElementCollection = new UniqueCodeElementCollection(this, reservedWords);
        vertexMainfunction = new StringWriter();
        fragmentMainfunction = new StringWriter();
        isCurrentActivityFragment = false;
        
        // Start exporting
        for (GlobalVariable globalVariable : Editor.globalVariables.getAll()) {
            try {
                if (globalVariable.isHardwareGiven()) {
                    resolveExportable(globalVariable);
                }
            } catch (ValueNotSetException ex) {
                
                //TODO: remove all variables that where added during this invalid resolving
                
                // Variable is either just not in use, or a connector is
                // not connected to a valid source
                System.out.println("ResultVar " + globalVariable.getName() + " wasn't completaly resolvable\r\n");
                System.out.println("Errormsg: " + ex.getMessage());
            }
        }
        
        // Define vars
        Writer vertexVarDefinitions = new StringWriter();
        Writer fragmentVarDefinitions = new StringWriter();
        vertexShaderSource.write("#version " + version + NEW_LINE);
        fragmentShaderSource.write("#version " + version + NEW_LINE);
        ArrayList<ExternalAttribute> attributes = new ArrayList<ExternalAttribute>();
        
        for (ValueAssignment valueAssignment : assignedValues.values()) {
            if (!valueAssignment.getOutVariable().isHardwareGiven()) {
                String dataType = getDataTypeName(valueAssignment.resolveDataType());
                
                if(valueAssignment instanceof ExternalAssignment) {
                    vertexShaderSource.write("uniform " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                    fragmentShaderSource.write("uniform " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                    // Add attribute to collection
                    attributes.add(((ExternalAssignment) valueAssignment).getAttribute());
                } else {
                    if (valueAssignment.isAvailableInVertexShader() && valueAssignment.isAvailableInFragmentShader()) {
                        vertexShaderSource.write("varying " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                        fragmentShaderSource.write("varying " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                    } else if (valueAssignment.isAvailableInVertexShader()) {
                        vertexVarDefinitions.write("  " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                    } else {
                        fragmentVarDefinitions.write("  " + dataType + " " + valueAssignment.getUniqueName() + ";" + NEW_LINE);
                    }
                }
            }
        }
        
        for (ShaderFunction shaderfunction : shaderFunctions) {
            String functionCode = shaderfunction.exportCode(this) + NEW_LINE;
            if (shaderfunction.isAvailableInVertexShader())
                vertexShaderSource.write(functionCode);
            if (shaderfunction.isAvailableInFragmentShader())
                fragmentShaderSource.write(functionCode);
        }
        
        // write functions
        vertexShaderSource.write("void main() {" + NEW_LINE);
        vertexShaderSource.write(vertexVarDefinitions.toString());
        vertexShaderSource.write(vertexMainfunction.toString());
        vertexShaderSource.write("}");
        
        fragmentShaderSource.write("void main() {" + NEW_LINE);
        fragmentShaderSource.write(fragmentVarDefinitions.toString());
        fragmentShaderSource.write(fragmentMainfunction.toString());
        fragmentShaderSource.write("}");
        
        return attributes;
    }
    
    private void resolveExportable(IExportable exportable) throws ExportingExeption, IOException {
        if (exportedValues.containsKey(exportable)) return;
        
        if (exportable instanceof IAssignable)
            addValueAssignment((IAssignable) exportable);
        else if (exportable.requiresSources(this)) {
            for (IExportable newExportable : exportable.getSources(this)) {
                if (newExportable == null) throw new ValueNotSetException(
                        "Exportable " + exportable.getElementName() + " is missing a source");
                resolveExportable(newExportable);
            }
        }
        
        String code = exportable.exportCode(this);
        if (code == null)
            throw new ExportingExeption("Exportable " + exportable.getElementName() + " didn't export any value");
        exportedValues.put(exportable, code);
    }
    
    private void addValueAssignment(IAssignable assignable) throws ExportingExeption, IOException {
        ValueAssignment valueAssignment;
        valueAssignment = assignedValues.get(assignable);
        if (valueAssignment != null) {
            valueAssignment.setShaderAvailablitily(isCurrentActivityFragment);
            return;
        }
        
        // Load ValueAssignment
        valueAssignment = assignable.exportValueAssignment(this);
        valueAssignment.setExporter(this);
        valueAssignment.setOutVariable(assignable);
        uniqueCodeElementCollection.addElement(valueAssignment);

        // Set Context
        boolean wasCurrentActivityFragment = setShaderContext(valueAssignment);
        valueAssignment.setShaderAvailablitily(wasCurrentActivityFragment);
        resolveExportable(valueAssignment);
        
        // Get Sources
        // If one of the sources operates in a different context it will be
        // changed and the valueAssignment needs to be notified.
        String value = valueAssignment.exportAssignmentCode(this);
        valueAssignment.setShaderAvailablitily(isCurrentActivityFragment);
        
        // Export
        String varName = valueAssignment.getUniqueName();
        if (value != null && value.length() > 0) {
            getActiveWriter().write("  " + varName + " = " + value + ";" + NEW_LINE);
        }
        isCurrentActivityFragment = wasCurrentActivityFragment;
        
        assignedValues.put(assignable, valueAssignment);
        
    }
    
    public String getExportableValue(IExportable exportable) throws ExportingExeption {
        // Check for Context
        if (exportable instanceof GlobalVariable) {
            GlobalVariable globalVar = (GlobalVariable) exportable;
            if (isCurrentActivityFragment && !globalVar.operatesInFragmentShader())
                isCurrentActivityFragment = false;
            else if (!isCurrentActivityFragment && !globalVar.operatesInVertexShader())
                throw new ExportingExeption("Fragment value cannot be used for a Vertex operation");
        }
        return this.exportedValues.get(exportable);
    }
    
    public ValueAssignment findValueAssignment(IAssignable assignable) {
        return this.assignedValues.get(assignable);
    }
    
    private String getDataTypeName(int dataType) throws ExportingExeption {
        if (!DataType.isDataTypeExplicit(dataType))
            throw new ExportingExeption("given Output dataType is not explicit");
        
        return DataType.getDataTypeShortName(dataType);
    }
    
    private Writer getActiveWriter() {
        if (isCurrentActivityFragment)
            return this.fragmentMainfunction;
        else
            return this.vertexMainfunction;
    }
    
    /**
     * Determines whether the shader context needs to be switched to
     * Vertex-/Fragment-Shader
     */
    private boolean setShaderContext(UniqueCodeElement uniqueCodeElement) {
        boolean wasCurrentActivityFragment = isCurrentActivityFragment;
        if (isCurrentActivityFragment && !uniqueCodeElement.operatesInFragmentShader()) {
            isCurrentActivityFragment = false;
        } else if (!isCurrentActivityFragment && !uniqueCodeElement.operatesInVertexShader()) {
            isCurrentActivityFragment = true;
        }
        return wasCurrentActivityFragment;
    }
    
    public String getInputValue(ShaderProgramInVariable shaderProgramInVariable) throws ExportingExeption {
        return null;
    }

    public String getGlobalName(GlobalVariable globalVariable) throws ExportingExeption {
        return null;
    }

    public String getShaderLanguageName() {
        return "GLSL";
    }

    public void registerVertexFunction(ShaderFunction shaderFunction) throws ExportingExeption {
        if (uniqueCodeElementCollection.findElement(shaderFunction) == null) {
            this.shaderFunctions.add(shaderFunction);
            uniqueCodeElementCollection.addElement(shaderFunction);
        }
//        if (uniqueCodeElementCollection findElement(functionName)) {
//            // function already exists
//        } else {
//            
//        }
    }

    public void registerFragmentFunction(ShaderFunction shaderFunction) throws ExportingExeption {
        if (uniqueCodeElementCollection.findElement(shaderFunction) == null) {
            this.shaderFunctions.add(shaderFunction);
            uniqueCodeElementCollection.addElement(shaderFunction);
        }
    }
    
    public void registerCodeElement(UniqueCodeElement uniqueCodeElement) throws ExportingExeption {
//        if (uniqueCodeElementCollection.findElement(uniqueCodeElement) == null) {
//            uniqueCodeElementCollection.addElement(uniqueCodeElement);
//        }
        uniqueCodeElement = this.uniqueCodeElementCollection.addElement(uniqueCodeElement);
        uniqueCodeElement.setShaderAvailablitily(this.isCurrentActivityFragment);
    }

    public String registerVaryingVariable(ShaderVariable variable) {
        return null;
    }
    public String autoExport(VariableValue variableValue) throws ExportingExeption {
        Object value = variableValue.getValue();
        if (value instanceof ShaderVariable)
            return ((ShaderVariable) value).getName();
        
        String code = "";
        int dataTypeOnly = DataType.getDataTypeOnly(variableValue.getDataType());
        int dimensions = DataType.getDimensions(variableValue.getDataType());
        if (dimensions > 1)
            code = DataType.getDataTypeShortName(variableValue.getDataType());
        if ((dataTypeOnly &
                (DataType.DATA_TYPE_SAMPLER |
                DataType.DATA_TYPE_SAMPLER_SHADOW |
                DataType.DATA_TYPE_SAMPLERCUBE))
                > 0) {
            // Sampler
            //TODO: create Sampler value
//            code = DataType.getDataTypeName(dataTypeOnly);
        } else {
            if (dimensions > 1) code += "(";
            for (int i = 0; i < dimensions; i++) {
                switch (dataTypeOnly) {
                    case DataType.DATA_TYPE_BOOL:
                        code += variableValue.getBooleanString(i);
                        break;
                    case DataType.DATA_TYPE_INT:
                        code += variableValue.getIntegerString(i);
                        break;
                    case DataType.DATA_TYPE_FLOAT:
                        code += variableValue.getFloatString(i);
                        break;
                    case DataType.DATA_TYPE_MAT:
                        code += variableValue.getMatrixString(i).replace('{', '(').replace('}', ')');
                        break;
                }
                if (i != dimensions - 1) code += ", ";
            }
            if (dimensions > 1) code += ")";
        }
        return code;
    }

    
}
