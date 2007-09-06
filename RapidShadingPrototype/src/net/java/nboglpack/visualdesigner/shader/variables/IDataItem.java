/*
 * IDataItem.java
 *
 * Created on May 9, 2007, 7:37 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

/**
 * Classes implementing this interface can give information about their data
 * structur. In terms of how many fields are provided, what type each field has
 * save and load contents.
 *
 * @author Samuel Sperling
 */
public interface IDataItem {
    /**
     * Returns the value at the given column index
     */
    public Object getValueAt(int columnIndex);
    
    /**
     * sets the value at a specific column index
     */
    public void setValueAt(Object aValue, int columnIndex);
    
    /**
     * defines whether the column is editable or not.
     */
    public boolean isCellEditable(int columnIndex);
    
    /**
     * returns the index of this element in the collection
     */
    public int getIndex();
    
}
