/*
 * ValueAssignment.java
 *
 * Created on May 17, 2007, 3:55 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;


/**
 * This class represents the assignment value for an ouput variable
 * and is delivered to the exporter, wich assembles the complete shadercode
 *
 * @author Samuel Sperling
 */
public abstract class ValueAssignment extends UniqueCodeElement {
    
    protected IShaderCodeExporter exporter;
    protected IAssignable outVariable;
    protected IExportable[] sources;
    protected int assignmentContext;
    
    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return sources;
    }

    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        if (sources == null) return false;
        return sources.length > 0;
    }
    
    protected void setSources(IExportable[] sources) {
        this.sources = sources;
    }
    
    protected IAssignable getOutVariable() {
        return outVariable;
    }
    
    /**
     * Will be set by the exporter
     */
    protected void setOutVariable(IAssignable outVariable) {
        this.outVariable = outVariable;
    }

    protected IShaderCodeExporter getExporter() {
        return exporter;
    }

    protected void setExporter(IShaderCodeExporter exporter) {
        this.exporter = exporter;
    }
    
    public String getName() throws ExportingExeption {
        return this.outVariable.getElementName();
    }
    
    public String exportCode(IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption {
        return this.getUniqueName();
    }
    
    public abstract String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption;
    
    public boolean operatesInFragmentShader() {
        return (assignmentContext & CONTEXT_FRAGMENT_SHADER) > 0;
    }
    
    public boolean operatesInVertexShader() {
        return (assignmentContext & CONTEXT_VERTEX_SHADER) > 0;
    }

    public int getAssignmentContext() {
        return assignmentContext;
    }

    public void setAssignmentContext(int assignmentContext) {
        this.assignmentContext = assignmentContext;
    }

    public String getElementName() throws ExportingExeption {
        return this.outVariable.getElementName();
    }

    public boolean hasSameElement(UniqueCodeElement uniqueCodeElement) {
        if (!(uniqueCodeElement instanceof ValueAssignment)) return false;
        return this.outVariable == ((ValueAssignment) uniqueCodeElement).outVariable;
    }
    
    public int resolveDataType() throws ExportingExeption {
        return this.outVariable.resolveDataType();
    }
    
    protected String replaceInputVarNames(String code, IExportable[] exportables, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        if (exportables == null) return code;
        for (IExportable inVar : exportables) {
                code = code.replace("<" + inVar.getElementName() + ">",
                        exportVisitor.getExportableValue(inVar));
            
//            if (inVar instanceof ShaderProgramInVariable)
//                code = code.replace("<" + inVar.getName() + ">",
//                        this.getExporter().getInputValue((ShaderProgramInVariable) inVar));
//            else
//                code = code.replace("<" + inVar.getName() + ">", inVar.getName());
        }
        return code;
    }
    
    /**
     * Creates an instance of the VariableRedirectionAssignment class
     * The output will be directly directed to given variable
     * @param variable Variable to be redirected to.
     * @param context determines in what context the assignment can operate
     *          use CONTEXT_VERTEX_SHADER, ...
     * @return instance of the VariableRedirectionAssignment class
     */
    public static ValueAssignment createVariableRedirectionAssignment(IExportable variable, int assignmentContext) {
        ValueAssignment valueAssignment = new VariableRedirectionAssignment(variable);
        valueAssignment.setAssignmentContext(assignmentContext);
        return valueAssignment;
    }
    public static ValueAssignment createVariableRedirectionAssignment(IExportable variable) {
        return createVariableRedirectionAssignment(variable, CONTEXT_VERTEX_OR_FRAGMENT_SHADER);
    }
    
    /**
     * Creates an instance of the RepresentativeAssignment class
     * This Assignment is just representing another Unique CodeElement
     * @param attribute Attribute which to represent.
     * @return instance of the VariableRedirectionAssignment class
     */
    public static ValueAssignment createExternalAssignment(ExternalAttribute attribute) {
        ValueAssignment valueAssignment = new ExternalAssignment(attribute);
        valueAssignment.setAssignmentContext(CONTEXT_VERTEX_OR_FRAGMENT_SHADER);
        return valueAssignment;
    }
    
    /**
     * Create an instance of the CodeLineAssignment class.
     * You can give a single line of shaderCode wich will be assigned to the
     * output variable.
     * @param codeLine Line of code that will be assigned to the output variable
     *          IMPORTANT: all input variables that are used need to be marked
     *          with &lt; and &gt;
     *          e.g. instead of "sin(alpha)" codeline must be "sin(&lt;alpha&gt;)"
     *          Don't use any semicolon!
     * @param variables Variables used in this codeline.
     *          All variables that make use of the &lt; / &gt; masking need to
     *          be given in this collection. Otherwise a compiling error will be
     *          thrown.
     * @param context determines in what context the assignment can operate
     *          use CONTEXT_VERTEX_SHADER, ...
     * @return instance of the VariableRedirectionAssignment class
     */
    public static ValueAssignment createCodeLineAssignment(String codeLine, IExportable[] variables, int assignmentContext) {
        ValueAssignment valueAssignment = new CodeLineAssignment(codeLine, variables);
        valueAssignment.setAssignmentContext(assignmentContext);
        return valueAssignment;
    }
    public static ValueAssignment createCodeLineAssignment(String codeLine, IExportable[] variables) {
        return createCodeLineAssignment(codeLine, variables, CONTEXT_VERTEX_OR_FRAGMENT_SHADER);
    }
    
    /**
     * Create an instance of the CodeLineAssignment class.
     * Let's you access a function that was registred through the Exporter
     * and assign the returnvalue to the requested output variable
     * 
     * @param functionName name of the function that was used to register
     *          the function within the exporter.
     * @param sources Collection of variables that will be submitted to the
     *          function. Make sure the order equals the order made during
     *          the registration.
     * @return instance of the CodeLineAssignment class.
     */
    public static ValueAssignment createFunctionAssignment(String functionName, String code, IExportable[] sources, int assignmentContext) {
        ValueAssignment valueAssignment = new FunctionAssignment(functionName, code, sources);
        valueAssignment.setAssignmentContext(assignmentContext);
        return valueAssignment;
    }
    public static ValueAssignment createFunctionAssignment(String functionName, String code, IExportable[] sources) {
        return createFunctionAssignment(functionName, code, sources, CONTEXT_VERTEX_OR_FRAGMENT_SHADER);
    }

    /**
     * Creates an instance of VoidAssignment.
     * This ValueAssignment returns nothing.
     * Mostly used for global variables that will be further processed within 
     * in the hardware.
     */
    public static ValueAssignment createVoidAssignment() {
        return new VoidAssignment();
    }
}

class VoidAssignment extends ValueAssignment {
    public VoidAssignment() {
    }
    public String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return "";
    }
}

class VariableRedirectionAssignment extends ValueAssignment {

    /**
     * Creates an instance of the VariableRedirectionAssignment class
     * The output will be directly directed to given variable
     * @param variable Variable to be redirected to.
     */
    public VariableRedirectionAssignment (IExportable variable) {
        setSources(new IExportable[] {variable});
    }

    public String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return exportVisitor.getExportableValue(this.getSources(exportVisitor)[0]);
//        IExportable variable = this.getSources()[0];
//        if (variable instanceof ShaderProgramInVariable)
//            return this.getExporter().getInputValue((ShaderProgramInVariable) variable);
//        else
//            return this.getExporter().getGlobalName((GlobalVariable) variable);
    }
}

class ExternalAssignment extends ValueAssignment {

    private ExternalAttribute attribute;
    
    /**
     * Creates an instance of the RepresentativeAssignment class
     * @param element Element which to represent.
     */
    public ExternalAssignment (ExternalAttribute attribute) {
        this.attribute = attribute;
        attribute.setRepresentative(this);
    }

    public String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return "";
    }

    public String getElementName() throws ExportingExeption {
        return attribute.getName();
    }

    public void setUniqueName(String uniqueName) {
        super.setUniqueName(uniqueName);
        attribute.setName(uniqueName);
    }

    public ExternalAttribute getAttribute() {
        return attribute;
    }


}

class CodeLineAssignment extends ValueAssignment {
    private String codeLine;
    
    /**
     * Create an instance of the CodeLineAssignment class.
     * You can give a single line of shaderCode wich will be assigned to the
     * output variable.
     * @param codeLine Line of code that will be assigned to the output variable
     *          IMPORTANT: all input variables that are used need to be marked
     *          with &lt; and &gt;
     *          e.g. instead of "sin(alpha)" codeline must be "sin(&lt;alpha&gt;)"
     *          Don't use any semicolon!
     * @param variables Variables used in this codeline.
     *          All variables that make use of the &lt; / &gt; masking need to
     *          be given in this collection. Otherwise a compiling error will be
     *          thrown.
     * @return instance of the VariableRedirectionAssignment class
     */
    public CodeLineAssignment(String codeLine, IExportable[] variables) {
        this.codeLine = codeLine;
        setSources(variables);
    }    

    public String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return replaceInputVarNames(codeLine, getSources(exportVisitor), exportVisitor);
    }
}

class FunctionAssignment extends ValueAssignment {
    private String functionName;
    private String code;

    /**
     * Create an instance of the CodeLineAssignment class.
     * Let's you access a function that was registred through the Exporter
     * and assign the returnvalue to the requested output variable
     * 
     * @param functionName name of the function that was used to register
     *          the function within the exporter.
     * @param code Code of the function
     * @param sources Collection of variables that will be submitted to the
     *          function. Make sure the order equals the order made during
     *          the registration.
     * @return instance of the CodeLineAssignment class.
     */
    public FunctionAssignment(String functionName, String code, IExportable[] sources) {
        // TODO: Maybe function sould be registred automatically here.
        this.functionName = functionName;
        this.code = code;
        setSources(sources);
        // TODO: add mutliple outputVars
//        setOutVariables(outVariables);
    }    

    public String exportAssignmentCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        IExportable[] sources = getSources(exportVisitor);
        String functionCall = functionName + "(";
        if (sources != null && sources.length > 0) {
            for (IExportable exportable : sources)
                functionCall += "<" + exportable.getElementName() + ">, ";
            functionCall = functionCall.substring(0, functionCall.length() - 2);
        }
        functionCall += ")";
        
        switch (this.assignmentContext) {
            case CONTEXT_FRAGMENT_SHADER:
                exportVisitor.registerFragmentFunction(new ShaderFunction(functionName, sources, new IAssignable[] {outVariable}, code));
                return replaceInputVarNames(functionCall, sources, exportVisitor);
            case CONTEXT_VERTEX_SHADER:
                exportVisitor.registerVertexFunction(new ShaderFunction(functionName, sources, new IAssignable[] {outVariable}, code));
                return replaceInputVarNames(functionCall, sources, exportVisitor);
            case CONTEXT_VERTEX_OR_FRAGMENT_SHADER:
        }
        return null;
    }
}