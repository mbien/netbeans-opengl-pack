/*
 * UnsupportedShaderExporterException.java
 *
 * Created on April 26, 2007, 1:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;

/**
 *
 * @author Samuel Sperling
 */
public class UnsupportedShaderExporterException extends Exception {
    
    private IShaderProgram eventSource;
    
    /** Creates a new instance of UnsupportedShaderExporterException */
    public UnsupportedShaderExporterException(IShaderProgram eventSource) {
        this.eventSource = eventSource;
    }

    public IShaderProgram getEventSource() {
        return eventSource;
    }

    public void setEventSource(IShaderProgram eventSource) {
        this.eventSource = eventSource;
    }

}
