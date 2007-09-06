/*
 * ShaderVariable.java
 *
 * Created on May 4, 2007, 1:16 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

import net.java.nboglpack.visualdesigner.IPersistable;
import net.java.nboglpack.visualdesigner.PersistanceException;
import net.java.nboglpack.visualdesigner.ProjectPersistor;

/**
 * Represents a variable that can be accessed within the whole shader
 *
 * @author Samuel Sperling
 */
public class ShaderVariable implements IDataItem, IPersistable {
    
    public static final int COLUMNS = 2;
    public static final int COLUMN_NAME = 0;
    public static final int COLUMN_VALUE = 1;
    public static final String[] COLUMN_NAMES = new String[] {
        "Name",
        "Value"
    };
    public static final Class[] COLUMN_CLASSES = new Class[] {
        String.class,
        Object.class
    };
    
    /** Variablename */
    protected String name;
    /** Datatypes that this variabel is compatible to. Can be generic  */
    protected int dataType;
    /** Datatype that was chosen through an editor */
    protected int chosenDataType = 0;
    /** value of this variable */
    protected VariableValue value;
    /** Collection holding this variable */
    protected VariablesCollection variableCollection;
    
    /** Creates a new instance of ShaderVariable */
    public ShaderVariable(String name, int dataType) {
        setName(name);
        setDataType(dataType);
    }
    
    public boolean hasSameName(ShaderVariable shaderVariable) {
        return name.equals(shaderVariable.name);
    }
    
    public VariablesCollection getVariableCollection() {
        return variableCollection;
    }
    
    public void setVariableCollection(VariablesCollection variableCollection) {
        this.variableCollection = variableCollection;
    }
    
//    public int hashCode() {
//        return name.hashCode();
//    }
//
//    public boolean equals(Object obj) {
//	if (this == obj) return true;
//
//	if (obj instanceof ShaderVariable)
//            return name.equals(((ShaderVariable) obj).name);
//
//        return false;
//    }
    
    public int getDataType() {
        return dataType;
    }
    
    public void setDataType(int dataType) {
        this.dataType = dataType;
        setChosenDataType(dataType);
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Changes the name
     * @return true if the name change was succesfull
     *         false if the given name is already in use by the collection
     */
    public boolean setName(String name) {
        if (name.equals(this.name)) return true;
        if (variableCollection == null) {
            this.name = name;
        } else {
            if (variableCollection.get(name) != null) return false;
            this.name = name;
            variableCollection.nameChanged(this);
        }
        return true;
    }
    
    public Object getValueAt(int columnIndex) {
        switch(columnIndex) {
            case COLUMN_NAME:
                return this.name;
            case COLUMN_VALUE:
                return this;
        }
        return null;
    }
    
    public void setValueAt(Object aValue, int columnIndex) {
        if (columnIndex == COLUMN_VALUE)
            this.value = (VariableValue) aValue;
    }
    
    public boolean isCellEditable(int columnIndex) {
        return (columnIndex == COLUMN_VALUE);
    }
    
    public VariableValue getVariableValue() {
        return value;
    }
    
    public void setVariableValue(VariableValue value) {
        this.value = value;
        setChosenDataType(value.getDataType());
    }
    
    public void setChosenDataType(int chosenDataType) {
        this.chosenDataType = chosenDataType;
    }
    
    public int getChosenDataType() {
        return chosenDataType;
    }
    
    public String toString() {
        return value == null ? "no value set" : value.toString();
    }
    
    public int getIndex() {
        return variableCollection.indexOf(this);
    }

    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        saveVisitor.save("name", this.getName());
//        saveVisitor.save("value", this.value);
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        this.name = loadVisitor.loadString("name");
//        this.value = loadVisitor.loadVariableValue("value");
    }
}