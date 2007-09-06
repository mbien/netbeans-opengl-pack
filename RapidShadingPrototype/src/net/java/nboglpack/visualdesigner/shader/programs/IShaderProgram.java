/*
 * IShader.java
 *
 * Created on April 20, 2007, 10:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.IPersistable;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 * Classes that support this interface are ShaderPrograms that function in a
 * Graph. Their IN and OUTput can be connected to other ShaderPrograms.
 * The main purpose of the IShaderProgram is the export of it's code
 * to a IShaderCodeExportVisitor
 * 
 * 
 * 
 * 
 * @author Samuel Sperling
 */
public interface IShaderProgram extends IPersistable {
    
    /**
     * Exports the code of this shader
     * @param outputVariable variable to assign the code to
     * @param exportVisitor Exporter that collects all the information from all shaderNodes to export the code
     * @return Variable assignment to the given outputVariable
     * @throws ExportingExeption Thrown, if Error occurs during export
     */
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
    throws ExportingExeption;
    /**
     * Returns the graphnode that is supposed to be added to the graph to represent
     * this IShaderProgram
     * 
     * @return Graphnode that is supposed to be added to the Graph to represent this IShaderProgram
     */
    public ShaderNode getShaderNode();
    
    /**
     * Returns the panel that allows the user to configure this ProgramShader
     * @return Panel that allows the user to configure this ProgramShader
     */
    public JPanel getProperiesPanel();
    
    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
        throws ExportingExeption;
    
    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
        throws ExportingExeption;

    /**
     * Retrieves the factory class of this IShaderProgram
     */
    public Class getFactoryClass();
    
    /**
     * Get the variant of this class that was used to create this instance.
     */
    public String getVariant();
}
