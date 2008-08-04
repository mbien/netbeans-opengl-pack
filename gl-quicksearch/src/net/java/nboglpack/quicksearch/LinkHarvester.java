/*
 * Created on 4. August 2008, 22:51
 */

package net.java.nboglpack.quicksearch;

/**
 *
 * @author Michael Bien
 */
interface LinkHarvester {
    
    /**
     * Returns a valid link or null computed from the href attribute and the link name.
     */
    public String harvest(String href, String name);
}
