/*
 * IDataSource.java
 *
 * Created on May 9, 2007, 7:15 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

/**
 * Classes implemting this interface can provide constant data through an index
 *
 * @author Samuel Sperling
 */
public interface IDataSource<E extends IDataItem> {
    
    /**
     * Retrieves the number of records this dataSource holds.
     */
    public int size();
    
    /**
     * Returns the item with given ID
     */
    public E get(int id);
    
}
