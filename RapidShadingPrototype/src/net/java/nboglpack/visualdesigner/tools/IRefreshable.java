/*
 * IRefreshable.java
 *
 * Created on April 18, 2007, 5:02 PM
 */

package net.java.nboglpack.visualdesigner.tools;

/**
 *
 * @author Samuel Sperling
 */
public interface IRefreshable {
    
    /**
     * @return  Returns true if this Class needs to be refreshed.
     *          Returns false if this Class doesn't need to be refreshed.
     */
    public boolean needsRefresh();
    
    public void refresh();
        
}
