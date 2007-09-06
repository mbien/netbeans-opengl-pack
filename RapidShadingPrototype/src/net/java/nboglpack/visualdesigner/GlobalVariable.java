/*
 * GlobalVariable.java
 *
 * Created on May 22, 2007, 5:47 PM
 */

package net.java.nboglpack.visualdesigner;

import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IAssignable;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.UniqueCodeElement;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;

/**
 * Global Variable.
 * Class holds information about where it's available and whether it is editable
 *
 * @author Samuel Sperling
 */
public class GlobalVariable extends ShaderVariable implements IAssignable {
    
    /** Normal global variable */
    public static final int GLOBAL_NORMAL = 0;
    
    /** Global, that represents a hardware supported global for the vertex shader Input */
    public static final int GLOBAL_HARDWARE_VERTEX_IN = 1;
    /** Global, that represents a hardware supported global for the vertex shader Input */
    public static final int GLOBAL_HARDWARE_VERTEX_OUT = 2;
    
    /** Global, that represents a hardware supported global for the fragment shader Input */    
    public static final int GLOBAL_HARDWARE_FRAGMENT_IN = 3;
    /** Global, that represents a hardware supported global for the fragment shader Input */    
    public static final int GLOBAL_HARDWARE_FRAGMENT_OUT = 4;
    
    private int globalType;
    
    private ShaderProgramOutVariable sourceVariable;
    
    /**
     * Creates a new instance of GlobalVariable
     */
    public GlobalVariable(String name, int dataType) {
        this(name, dataType, GLOBAL_NORMAL);
    }
    /**
     * Creates a new instance of GlobalVariable
     */
    public GlobalVariable(String name, int dataType, int globalType) {
        super(name, dataType);
        this.globalType = globalType;
    }
    
    /**
     * Sets the Source of this global variable.
     * informs the former source that the connection was removed.
     */
    public void setSourceVariable(ShaderProgramOutVariable sourceVariable) {
        if (this.sourceVariable == sourceVariable) return;
        if (this.sourceVariable != null)
            this.sourceVariable.globalOutputRemoved(this);
        this.sourceVariable = sourceVariable;
    }

    /**
     * Returns the source that's supposed to fill this global variable
     * @return The source that's supposed to fill this global variable
     */
    public ShaderProgramOutVariable getSourceVariable() {
        return sourceVariable;
    }


    /**
     * Tells whether this global variable is directed to the hardware globals
     * @return  True if this global variable is directed to a hardware global
     */
    public boolean isHardwareGiven() {
        return this.globalType > GLOBAL_NORMAL;
    }

    public int getGlobalType() {
        return globalType;
    }

    public boolean operatesInFragmentShader() {
        return  globalType == GLOBAL_HARDWARE_FRAGMENT_IN ||
                globalType == GLOBAL_HARDWARE_FRAGMENT_OUT ||
                globalType == GLOBAL_NORMAL;
    }

    public boolean operatesInVertexShader() {
        return  globalType == GLOBAL_HARDWARE_VERTEX_IN ||
                globalType == GLOBAL_HARDWARE_VERTEX_OUT ||
                globalType == GLOBAL_NORMAL;
    }
    public ValueAssignment exportValueAssignment(IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
        if (requiresSources(exportVisitor))
            return ValueAssignment.createVariableRedirectionAssignment(this.sourceVariable, getContext(this.globalType));
        else
            return ValueAssignment.createVoidAssignment();
    }
     private int getContext(int globalContext) {
         if (globalContext == GLOBAL_HARDWARE_VERTEX_IN ||
                 globalContext == GLOBAL_HARDWARE_VERTEX_OUT)
             return UniqueCodeElement.CONTEXT_VERTEX_SHADER;
         else if (globalContext == GLOBAL_HARDWARE_FRAGMENT_IN ||
                 globalContext == GLOBAL_HARDWARE_FRAGMENT_OUT)
             return UniqueCodeElement.CONTEXT_FRAGMENT_SHADER;
         else
             return UniqueCodeElement.CONTEXT_VERTEX_OR_FRAGMENT_SHADER;
     }

    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return exportVisitor.findValueAssignment(this).exportCode(exportVisitor);
    }

    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) {
//        return this.sourceVariable != null;
        // Every global needs an input except the ones that get their input from the hardware.
        return !(globalType == GLOBAL_HARDWARE_FRAGMENT_IN || globalType == GLOBAL_HARDWARE_VERTEX_IN);
    }

    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return new IExportable[] {this.sourceVariable};
    }

    public String getElementName() {
        return this.name;
    }

    public int resolveDataType() throws ExportingExeption {
        return this.dataType;
    }
    
}
