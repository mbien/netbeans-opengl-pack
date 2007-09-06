/*
 * IExportable.java
 *
 * Created on May 28, 2007, 2:32 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

/**
 * Classes supporting this interface can be exported to shaderCode
 *
 * @author Samuel Sperling
 */
public interface IExportable {
    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption;
    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption;
    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption;
    public String getElementName() throws ExportingExeption;
    public int resolveDataType() throws ExportingExeption;
}
