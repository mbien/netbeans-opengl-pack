/*
 * PersistanceException.java
 *
 * Created on June 4, 2007, 10:57 AM
 */

package net.java.nboglpack.visualdesigner;

/**
 *
 * @author Samuel Sperling
 */
public class PersistanceException extends Exception {
    
    /** Creates a new instance of PersistanceException */
    public PersistanceException() {
        super();
    }
    
    public PersistanceException(String message) {
	super(message);
    }
    
    public PersistanceException(Throwable cause) {
        super(cause);
    }
    
}
