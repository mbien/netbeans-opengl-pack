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
import net.java.nboglpack.visualdesigner.shader.exporter.ShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 * Classes that support this interface are ShaderPrograms that function in a
 * Graph. Their IN and OUTput can be connected to other ShaderPrograms.
 * The main purpose of the ShaderProgram is the export of it's code
 * to a ShaderCodeExportVisitor
 *
 *
 * @author Samuel Sperling
 */
public interface ShaderProgram extends IPersistable {
    
    /**
     * Exports the code of this shader
     * @param outputVariable variable to assign the code to
     * @param exportVisitor Exporter that collects all the information from all shaderNodes to export the code
     * @return Variable assignment to the given outputVariable
     * @throws ExportingExeption Thrown, if Error occurs during export
     */
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, ShaderCodeExportVisitor exportVisitor)
    throws ExportingExeption;
    /**
     * Returns the graphnode that is supposed to be added to the graph to represent
     * this ShaderProgram
     * @return Graphnode that is supposed to be added to the Graph to represent this ShaderProgram
     */
    public ShaderNode getShaderNode();
    
    /**
     * Returns the panel that allows the user to configure this ProgramShader
     * @return Panel that allows the user to configure this ProgramShader
     */
    public JPanel getProperiesPanel();
    
    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, ShaderCodeExportVisitor exportVisitor)
        throws ExportingExeption;
    
    public boolean requiresSources(ShaderProgramOutVariable outputVariable, ShaderCodeExportVisitor exportVisitor)
        throws ExportingExeption;

    /**
     * Retrieves the factory class of this ShaderProgram
     */
    public Class getFactoryClass();
    
    /**
     * Get the variant of this class that was used to create this instance.
     */
    public String getVariant();
}
