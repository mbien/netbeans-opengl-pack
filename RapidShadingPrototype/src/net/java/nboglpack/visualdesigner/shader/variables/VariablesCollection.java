/*
 * VariablesCollection.java
 *
 * Created on May 4, 2007, 1:13 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import net.java.nboglpack.visualdesigner.shader.variables.ICollectionChangedListener;

/**
 * This class holds variables.
 * New ones can be registeres and existing ones can be retrieved.
 *
 * @author Samuel Sperling
 */
public class VariablesCollection<E extends ShaderVariable> implements IDataSource<E> {
    
    // TODO: could be a map using <String, ShaderVariable> but needs to update
    // when the name of ShaderVariable is changed.
    // And access via index is not available anymore
    private ArrayList<E> variables;
    protected ArrayList<ICollectionChangedListener<E>> listeners = new ArrayList<ICollectionChangedListener<E>>();
    
    /**
     * Creates a new instance of VariablesCollection
     */
    public VariablesCollection() {
    }
    
    /**
     * Adds a new global variable.
     *
     * @param shaderVariable  Variable that should be added as a global variable
     * @return  Returns the shaderVariable parameter if there was no variable
     *          set already using the same name.
     *          If there was a variable set already using the same name that
     *          one will be returned.
     */
    public E add(E shaderVariable) {
        for (E global : getAll())
            if (global.hasSameName(shaderVariable))
                return global;
        
        getAll().add(shaderVariable);
        shaderVariable.setVariableCollection(this);
        fireItemAdded(shaderVariable);
        return shaderVariable;
    }
    
    /**
     * Removes shaderVariable from the globalsCollection
     */
    public boolean remove(E shaderVariable) {
        if (getAll().remove(shaderVariable)) {
            shaderVariable.setVariableCollection(null);
            fireItemRemoved(shaderVariable);
            return true;
        }
        return false;
    }
    
    /**
     * Removes shaderVariable from the globalsCollection
     */
    public E remove(int variableIndex) {
        E shaderVariable = getAll().remove(variableIndex);
        if (shaderVariable != null) {
            fireItemRemoved(shaderVariable);
            shaderVariable.setVariableCollection(null);
        }
        return shaderVariable;
    }
    
    /**
     * Retrieve all ShaderVariables as ArrayList
     */
    public ArrayList<E> getAll() {
        if (variables == null)
            variables = new ArrayList<E>();
        return variables;
    }
    
    /**
     * Names of all global variables are returned
     */
    public String[] getAllNames() {
        String[] names = new String[getAll().size()];
        for (int i = 0; i < names.length; i++)
            names[i] = variables.get(i).getName();
        
        return names;
    }
    
    /**
     * Names of all global variables are returned
     */
    public String[] getAllNames(int dataType) {
        String[] names = new String[getAll().size()];
        int j = 0;
        for (int i = 0; i < names.length; i++) {
            if (DataType.isCompatible(dataType, variables.get(i).getDataType())) {
                names[j++] = variables.get(i).getName();
            }
        }
        if (names.length == j) return names;
        
        // Copy
        String[] filteredNames = new String[j];
        for (int i = 0; i < filteredNames.length; i++)
            filteredNames[i] = names[i];
        return filteredNames;
    }
    
    /**
     * Retrieves a variable by name
     */
    public E get(String name) {
        for (E global : getAll())
            if (global.getName().equals(name))
                return global;
        return null;
    }
    
    /**
     * Finds the index of the given variable in the collection
     */
    public int indexOf(E shaderVariable) {
        return variables.indexOf(shaderVariable);
    }
    
    protected void nameChanged(E shaderVariable) {
        // update collectionset
        ItemChanged(shaderVariable);
    }
    
    public void ItemChanged(E shaderVariable) {
        fireItemChanged(shaderVariable);
    }
    
    /**
     * Adds an <code>ICollectionChangedListener</code> to the listener list.
     * 
     * @param l  the new listener to be added
     */
    public void addCollectionChangedListener(ICollectionChangedListener l) {
        listeners.add(l);
    }
    
    /**
     * Removes a <code>ICollectionChangedListener</code> from the listener list.
     * 
     * @param l  the listener to be removed
     */
    public void removeCollectionChangedListener(ICollectionChangedListener l) {
        listeners.remove(l);
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     */
    protected void fireItemAdded(E shaderVariable) {
        for (ICollectionChangedListener<E> listener : listeners)
            listener.itemAdded(shaderVariable);
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     */
    protected void fireItemRemoved(E shaderVariable) {
        for (ICollectionChangedListener<E> listener : listeners)
            listener.itemRemoved(shaderVariable);
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     */
    protected void fireItemChanged(E shaderVariable) {
        for (ICollectionChangedListener<E> listener : listeners)
            listener.itemChanged(shaderVariable);
    }
    
    /**
     * Returns the number of variables
     */
    public int size() {
        return getAll().size();
    }
    
    /**
     * Returns variable associated with the given index
     */
    public E get(int index) {
        return this.variables.get(index);
    }
    
}

