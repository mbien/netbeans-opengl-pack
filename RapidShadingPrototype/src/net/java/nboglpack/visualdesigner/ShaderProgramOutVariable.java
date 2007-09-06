/*
 * ShaderProgramOutVariable.java
 *
 * Created on May 12, 2007, 12:17 PM
 */

package net.java.nboglpack.visualdesigner;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IAssignable;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderProgramOutVariable extends ShaderProgramVariable implements IAssignable {
    
    /*  CONSTANTS   */
    
    /** Value given through graph */
    public static final int VALUE_SOURCE_CONNECTABLE = 0;
    /** Retrieves it's value from a global variable */
    public static final int VALUE_SOURCE_GLOBAL = 1;
    
    public static final String[] VALUE_SOURCE_NAMES = new String[] {"Connectable", "Global"};
    
    /*  PRIVATES   */
    
    private NodeOutputConnector connector;
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     */
    public ShaderProgramOutVariable(String name, String description) {
        super(name, description);
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
    public ShaderProgramOutVariable(String name, String description, int dataType) {
        super(name, description, dataType);
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
    public ShaderProgramOutVariable(String name, String description, int dataType, int valueSource) {
        super(name, description, dataType, valueSource);
    }
    
    protected void valueSourceChanged() {
        if (shaderNode == null) return;
        
        switch (getValueSource()) {
            case VALUE_SOURCE_CONNECTABLE:
                // Add Connector to the GraphNode
                connector = new ShaderNodeOutputConnector(this);// {
//                    protected boolean isCompatibleHoverConnection(NodeConnector connector) {
//                        // On hovering over a connector dataTypes are compared whether their comaptible
//                        connector.ge
//                        ShaderProgramInVariable inVar = ((ShaderNode) connector.getParentNode()).getInVariables().get(connector.getName());
//                        try {
//                            if (DataType.isCompatible(resolveDataType(), inVar.resolveDataType())) return true;
//                            if (DataType.isCompatible(inVar.resolveDataType(), resolveDataType())) return true;
//                            return false;
//                        } catch (ExportingExeption ex) {
//                            return false;
//                        }
//                    }
//                };
                shaderNode.addOutputConnector(connector);
                break;
            case VALUE_SOURCE_GLOBAL:
                //TODO: Ask if connector should be removed if there are active connections
                if (connector != null) {
                    connector.delete();
                }
        }
        super.valueSourceChanged();
    }
    
    public static TableCellEditor getValueSourceChooserEditor() {
        return new DefaultCellEditor(new JComboBox(VALUE_SOURCE_NAMES));
    }
    
    public static TableCellEditor getValueEditor() {
        return new ShaderProgramVariableEditor();
    }
    
    public NodeConnector getConnector() {
        return connector;
    }
    
    public void setConnector(NodeOutputConnector connector) {
        this.connector = connector;
    }
    
    public boolean isCellEditable(int columnIndex) {
        return columnIndex == COLUMN_SOURCE ||
                (columnIndex == COLUMN_VALUE && this.valueSource == VALUE_SOURCE_GLOBAL);
    }
    
    private static int getValueSourceNum(String valueSourceName) {
        for (int i = 0; i < VALUE_SOURCE_NAMES.length; i++) {
            if (VALUE_SOURCE_NAMES[i].equals(valueSourceName))
                return i;
        }
        return -1;
    }
    
    public int resolveDataType() throws ExportingExeption {
        if (getDataType() != 0 && DataType.isDataTypeExplicit(getDataType()))
            return getDataType();
        
        switch (this.valueSource) {
            case VALUE_SOURCE_GLOBAL:
                if (this.value == null) throw new ExportingExeption("Global variable wasn't assigned");
                return this.value.getDataType();
            case VALUE_SOURCE_CONNECTABLE:
                return getShaderNode().resolveOutputDataType(this);
        }
        return 0;
    }
    
    protected String getValueSourceName() {
        return VALUE_SOURCE_NAMES[this.valueSource];
    }
    
    protected void setValueSource(Object aValue) {
        setValueSource(getValueSourceNum((String) aValue));
    }
    
    public void setConnector(NodeConnector connector) {
        this.connector = (NodeOutputConnector) connector;
    }

    public ValueAssignment exportValueAssignment(IShaderCodeExportVisitor exportVisitor)
                        throws ExportingExeption {
        return this.shaderNode.export(this, exportVisitor);
    }
    
    public void setVariableValue(VariableValue value) {
        super.setVariableValue(value);
        
        // inform global about connection.
        if (value.isReferencing())
            ((GlobalVariable) value.getShaderVariable()).setSourceVariable(this);
    }

    /**
     * This variable was connected to a global output var.
     * Now another output was connected to it.
     */
    public void globalOutputRemoved(GlobalVariable globalVariable) {
        //TODO: Set value to null
        setVariableValue(null);
    }

    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
//        return this.shaderNode.getShaderProgram().exportShaderCode(this, exportVisitor);
        return exportVisitor.findValueAssignment(this).exportCode(exportVisitor);
    }

    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return this.shaderNode.getShaderProgram().requiresSources(this, exportVisitor);
    }

    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return this.shaderNode.getShaderProgram().getSources(this, exportVisitor);
    }
    
    public boolean isHardwareGiven() {
        return false;
    }

    public String getElementName() {
        return this.name;
    }

}
