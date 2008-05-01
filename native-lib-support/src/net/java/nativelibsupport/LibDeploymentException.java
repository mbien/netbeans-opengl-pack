/*
 * Created on 1. May 2008, 19:36
 */

package net.java.nativelibsupport;

/**
 * DeploymentException
 * @author Michael Bien
 */
public class LibDeploymentException extends Exception {

    public LibDeploymentException(Throwable cause) {
        super(cause);
    }

    public LibDeploymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibDeploymentException(String message) {
        super(message);
    }

    public LibDeploymentException() {
    }

}
