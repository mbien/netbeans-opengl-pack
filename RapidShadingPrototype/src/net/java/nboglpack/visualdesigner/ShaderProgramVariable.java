/*
 * ShaderProgramVariable.java
 *
 * Created on May 14, 2007, 11:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariableEditor;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 *
 * @author Samuel Sperling
 */
public abstract class ShaderProgramVariable extends ShaderVariable{
    
    
    public static final int COLUMN_DATA_TYPE = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_SOURCE = 2;
    public static final int COLUMN_VALUE = 3;
    
    public static final int COLUMNS = 4;
    public static final String[] COLUMN_NAMES = new String[] {
        "DataType",
        "Name",
        "Source",
        "Value"
    };
    public static final Class[] COLUMN_CLASSES = new Class[] {
        String.class,
        String.class,
        String.class,
        Object.class
    };
    
    protected int valueSource = 0;
    protected String description;
    protected ShaderNode shaderNode;
    
    // TODO: create isHidden field
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     */
    public ShaderProgramVariable(String name, String description) {
        super(name, 0);
        setDescription(description);
    }
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     *
     *
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     * @param dataType  One of the DataType constant definition
     *                  such as DataType.DATA_TYPE_GENTYPE
     */
    public ShaderProgramVariable(String name, String description, int dataType) {
        super(name, dataType);
        setDescription(description);
    }
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     *
     *
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     * @param dataType  One of the DataType constant definition
     *                  such as DataType.DATA_TYPE_GENTYPE
     * @param valueSource  Source Type of this variable must be one of
     *                     ShaderProgramInVariable.VALUE_SOURCE_* constants
     */
    public ShaderProgramVariable(String name, String description, int dataType, int valueSource) {
        super(name, dataType);
        setDescription(description);
        setValueSource(valueSource);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getValueSource() {
        return valueSource;
    }
    
    public void setValueSource(int valueSource) {
        if (this.valueSource == valueSource) return;
        this.valueSource = valueSource;
        valueSourceChanged();
    }

    public void setShaderNode(ShaderNode shaderNode) {
        if (this.shaderNode == shaderNode) return;
        this.shaderNode = shaderNode;
        
        // To create the connectors...
        valueSourceChanged();
    }

    public ShaderNode getShaderNode() {
        return shaderNode;
    }
    
    /**
     * Retrieves the column object representing this variable in ie. an editor
     */
    public Object getValueAt(int columnIndex) {
        switch (columnIndex) {
            case COLUMN_DATA_TYPE:
                return DataType.getDataTypeName(this.dataType);
            case COLUMN_NAME:
                return this.name;
            case COLUMN_SOURCE:
                return getValueSourceName();
            case COLUMN_VALUE:
                return this;
        }
        return null;
    }
    
    public void setValueAt(Object aValue, int columnIndex) {
        switch (columnIndex) {
            case COLUMN_SOURCE:
                setValueSource(aValue);
                break;
            case COLUMN_VALUE:
                setVariableValue((VariableValue) aValue);
                break;
        }
    }
    
    protected void valueSourceChanged() {
        this.variableCollection.ItemChanged(this);
    }
    
    public abstract NodeConnector getConnector();
    
    public abstract void setConnector(NodeConnector connector);
    
    public abstract boolean isCellEditable(int columnIndex);
        
    public abstract int resolveDataType() throws ExportingExeption;

    protected abstract String getValueSourceName();

    protected abstract void setValueSource(Object aValue);
}
