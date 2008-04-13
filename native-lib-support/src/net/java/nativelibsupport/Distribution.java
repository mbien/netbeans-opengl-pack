/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nativelibsupport;

/**
 *
 * @author Michael Bien
 */
public interface Distribution {

    /**
     * Returns the unique key for the distribution (eg linux-amd64).
     */
    public String key();

    public String getArch();

    public String getOs();



}
