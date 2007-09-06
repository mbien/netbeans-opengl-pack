/*
 * IAssignable.java
 *
 * Created on May 28, 2007, 1:23 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

/**
 * Classes supporting this interface have a name can be assigned.
 *
 * @author Samuel Sperling
 */
public interface IAssignable extends IExportable {
    public ValueAssignment exportValueAssignment(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption;
    public boolean isHardwareGiven();
}
