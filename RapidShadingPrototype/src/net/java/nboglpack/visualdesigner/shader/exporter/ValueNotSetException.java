/*
 * ValueNotSetException.java
 *
 * Created on May 23, 2007, 5:41 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

/**
 * This Exception is thrown if a value wasn't set during export.
 *
 * @author Samuel Sperling
 */
public class ValueNotSetException extends ExportingExeption {
    
    /** Creates a new instance of ValueNotSetException */
    public ValueNotSetException() {
    }
    
    /** Creates a new instance of ValueNotSetException */
    public ValueNotSetException(String message) {
        super(message);
    }
    
}
