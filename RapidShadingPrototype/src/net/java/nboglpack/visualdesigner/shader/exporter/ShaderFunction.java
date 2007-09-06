/*
 * ShaderFunction.java
 *
 * Created on May 7, 2007, 3:17 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import net.java.nboglpack.visualdesigner.shader.variables.DataType;


/**
 * This class represents a function in a ShaderExporter
 *
 * @author Samuel Sperling
 */
public class ShaderFunction extends UniqueCodeElement {
    
    private String functionName;
    private IExportable[] inputVariables;
    private IAssignable[] outVariables;
    private String functionCode;
    
    /** Creates a new instance of ShaderFunction */
    public ShaderFunction(String functionName, IExportable[] inputVariables, IAssignable[] outVariables, String functionCode) {
        this.functionName = functionName;
        this.inputVariables = inputVariables;
        this.outVariables = outVariables;
        this.functionCode = functionCode;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShaderFunction)) return false;
        try {
            
            return hasSameSignature((ShaderFunction) obj);
        } catch (ExportingExeption ex) {
            return false;
        }
    }
    
    public boolean hasSameSignature(ShaderFunction shaderFunction) throws ExportingExeption {
        
        if (shaderFunction.functionName != this.functionName) return false;
        if (shaderFunction.inputVariables.length != this.inputVariables.length) return false;
        
        // Compare for actual variable Types
        for (int i = 0; i < this.inputVariables.length; i++)
            if (shaderFunction.inputVariables[i].resolveDataType() != this.inputVariables[i].resolveDataType()) return false;
        
        // Have valid difference of arguments
        if (shaderFunction.outVariables.length > 1) {
            if (shaderFunction.outVariables.length != this.outVariables.length) return false;
            
            // Compare for actual variable Types
            for (int i = 0; i < this.outVariables.length; i++)
                if (shaderFunction.outVariables[i].resolveDataType() != this.outVariables[i].resolveDataType()) return false;
        } else {
            if (this.outVariables.length > 1) return false;
        }
        
        return true;
    }
    
    public boolean operatesInFragmentShader() {
        return true;
    }
    
    public boolean operatesInVertexShader() {
        return true;
    }
    
    public boolean hasSameElement(UniqueCodeElement uniqueCodeElement) {
        if (!(uniqueCodeElement instanceof ShaderFunction)) return false;
        return this.outVariables[0] == ((ShaderFunction) uniqueCodeElement).outVariables[0];
    }
    
    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        String code;
        if (outVariables != null && outVariables.length > 0)
            code = DataType.getDataTypeShortName(outVariables[0].resolveDataType());
        else
            code = "void";
        code += " " + this.functionName + "(";
        if (inputVariables != null && inputVariables.length > 0) {
            for (IExportable exportable: this.inputVariables) {
                code += DataType.getDataTypeShortName(exportable.resolveDataType());
                code += " " + exportable.getElementName() + ", ";
            }
            code = code.substring(0, code.length() - 2);
        }
        code += ") {\n\t" + functionCode.replace("\n", "\n\t") + "\n}\n";
        return code;
    }
    
    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return this.inputVariables != null && this.inputVariables.length > 0;
    }
    
    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return this.inputVariables;
    }
    
    public String getElementName() throws ExportingExeption {
        return this.functionName;
    }

    public int resolveDataType() throws ExportingExeption {
        return outVariables[0].resolveDataType();
    }
    
}
