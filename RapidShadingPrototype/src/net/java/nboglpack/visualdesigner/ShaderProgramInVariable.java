/*
 * ShaderProgramInVariable.java
 *
 * Created on April 30, 2007, 2:27 PM
 *
 */

package net.java.nboglpack.visualdesigner;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueNotSetException;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 * Represeents an input Variable of a ShaderNode that can be defined in
 * different ways as Constant, Global, Editable or Connectable
 *
 * @author Samuel Sperling
 */
public class ShaderProgramInVariable extends ShaderProgramVariable implements IExportable, IPersistable {
    
    /** Value given through graph */
    public static final int VALUE_SOURCE_CONNECTABLE = 0;
    /** Retrieves it's value from a global variable */
    public static final int VALUE_SOURCE_GLOBAL = 1;
    /** Defined just in the Variable Editor */
    public static final int VALUE_SOURCE_CONST = 2;
    /** Editable in the Node */
    public static final int VALUE_SOURCE_EDITABLE = 3;
    
    public static final String[] VALUE_SOURCE_NAMES = new String[] {"Connectable", "Global", "Constant", "Editable"};
    
    private NodeInputConnector connector;
    
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     */
    public ShaderProgramInVariable(String name, String description) {
        super(name, description);
    }
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     * @param dataType  One of the DataType constant definition
     *                  such as DataType.DATA_TYPE_GENTYPE
     */
    public ShaderProgramInVariable(String name, String description, int dataType) {
        super(name, description, dataType);
    }
    
    /**
     * Creates a new instance of ShaderProgramInVariable
     *
     * @param name  Name of this variable
     * @param description  Description of the meaning of this variable
     * @param dataType  One of the DataType constant definition
     *                  such as DataType.DATA_TYPE_GENTYPE
     * @param valueSource  Source Type of this variable must be one of
     *                     ShaderProgramInVariable.VALUE_SOURCE_* constants
     */
    public ShaderProgramInVariable(String name, String description, int dataType, int valueSource) {
        super(name, description, dataType, valueSource);
    }
    
    public NodeInputConnector getConnector() {
        return connector;
    }
    
    public void setConnector(NodeInputConnector connector) {
        this.connector = connector;
    }
    
    protected void valueSourceChanged() {
        if (shaderNode == null) return;
        
        switch (getValueSource()) {
            case VALUE_SOURCE_CONNECTABLE:
                // Add Connector to the GraphNode
//                connector = new NodeInputConnector(getName());
                connector = new ShaderNodeInputConnector(this); //{
//                    protected boolean isCompatibleHoverConnection(NodeConnector connector) {
//                        // On hovering over a connector dataTypes are compared whether their comaptible
//                        ShaderProgramOutVariable outVar = ((ShaderNode) connector.getParentNode()).getOutVariables().get(connector.getName());
//                        try {
//                            if (DataType.isCompatible(resolveDataType(), outVar.resolveDataType())) return true;
//                            if (DataType.isCompatible(outVar.resolveDataType(), resolveDataType())) return true;
//                            return false;
//                        } catch (ExportingExeption ex) {
//                            return false;
//                        }
//                    }
//                };
                shaderNode.addInputConnector(connector);
                break;
            case VALUE_SOURCE_GLOBAL:
            case VALUE_SOURCE_CONST:
            case VALUE_SOURCE_EDITABLE:
                //TODO: Ask if connector should be removed if there are active connections
                if (connector != null)
                    connector.delete();
        }
        super.valueSourceChanged();
    }
    
    public boolean isCellEditable(int columnIndex) {
        return columnIndex == COLUMN_SOURCE ||
                (columnIndex == COLUMN_VALUE && (
                this.valueSource == VALUE_SOURCE_CONST ||
                this.valueSource == VALUE_SOURCE_GLOBAL
                ));
    }
    
    private static String getValueSourceDescription(int valueSource) {
        return VALUE_SOURCE_NAMES[valueSource];
    }
    
    private static int getValueSourceNum(String valueSourceName) {
        for (int i = 0; i < VALUE_SOURCE_NAMES.length; i++) {
            if (VALUE_SOURCE_NAMES[i].equals(valueSourceName))
                return i;
        }
        return -1;
    }
    
    public int resolveDataType() throws ExportingExeption {
        if (getChosenDataType() != 0 && DataType.isDataTypeExplicit(getChosenDataType()))
            return getChosenDataType();
        
        switch (this.valueSource) {
            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
                if (this.value == null) throw new ExportingExeption("Global variable wasn't assigned");
                return this.value.getDataType();
            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
                NodeOutputConnector outputConnector = (connector).getConnectionOutput();
                if (outputConnector == null || !(outputConnector.getParent() instanceof ShaderNode))
                    throw new ExportingExeption("The input " + getName() + " has no connection");
                return ((ShaderNode) outputConnector.getParent()).resolveOutputDataType(outputConnector.getName());
            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
                if (this.value == null) throw new ExportingExeption("Constant variable wasn't assigned");
                return this.value.getDataType();
            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
                //TODO: implement in-node editables
                break;
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
        this.connector = (NodeInputConnector) connector;
    }
    
    public static TableCellEditor getValueSourceChooserEditor() {
        return new DefaultCellEditor(new JComboBox(VALUE_SOURCE_NAMES));
    }
    
    public static TableCellEditor getValueEditor() {
        return new ShaderProgramVariableEditor();
    }

    
//    public String toString() {
//        String output = "";
//        switch (this.valueSource) {
//            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
//                this.value.toString();
//                break;
//            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
//                output = super.toString();
//                break;
//        }
//        return output;
//    }

    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        switch (this.valueSource) {
            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
                return exportVisitor.getExportableValue(getSources(exportVisitor)[0]);
            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
                if (this.value == null) throw new ExportingExeption("Variable " + this.getName() + " of Node " + this.getShaderNode().getName() + " was not set.");
                return exportVisitor.autoExport(this.value);
            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
                //TODO: implement in-node editables
                break;
        }
        return null;
    }

    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) {
        return this.valueSource == ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE ||
               this.valueSource == ShaderProgramInVariable.VALUE_SOURCE_GLOBAL;
    }

    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption {
        switch (this.valueSource) {
            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
                if (connector != null) {
                    NodeOutputConnector outConnector = connector.getConnectionOutput();
                    if (outConnector == null)
                        throw new ValueNotSetException("Input variable " + this.getName() +
                                " has no connection on connector " + connector.getName());
                    return new IExportable[] {
                        ((ShaderNode) outConnector.getParentNode()).getOutVariables().get(outConnector.getName())
                    };
                } else {
                    throw new RuntimeException("Node " + this.getName() + " is set to VALUE_SOURCE_CONNECTABLE but has no connector!");
                }
            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
                return new IExportable[] {
                        (GlobalVariable) this.value.getShaderVariable()
                    };
            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
                return new IExportable[] {
                        this.value
                    };
        }
        return null;
    }

    public String getElementName() {
        return this.getName();
    }
    
    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        saveVisitor.save("name", this.name);
//        saveVisitor.save("valuesource", this.valueSource);
//        switch (this.valueSource) {
//            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
//                if (this.connector != null) {
//                    NodeOutputConnector outConnector = connector.getConnectionOutput();
//                    if (outConnector != null) {
//                        saveVisitor.save("connectednodename", outConnector.getParentNode().getName());
//                        saveVisitor.save("connectedname", outConnector.getName());
//                    }
//                }
//                break;
//            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
//            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
//            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
//                saveVisitor.save("variablevalue", this.value);
//                break;
//        }
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        this.name = loadVisitor.loadString("name");
//        setValueSource(loadVisitor.loadInteger("valuesource").intValue());
//        switch (this.valueSource) {
//            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
//                String connectedNodename = loadVisitor.loadString("connectednodename");
//                if (connectedNodename != null && connectedNodename.length() > 0) {
//                    String connectedName = loadVisitor.loadString("connectedname");
//                    this.connector.setConnectionOutput(this.getShaderNode().getGraph().getNode(connectedNodename).getOutputConnector(connectedName));
//                }
//                break;
//            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
//            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
//            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
//                this.value = new VariableValue();
//                loadVisitor.load("variablevalue", this.value);
//                break;
//        }
    }

    public boolean hasValue() {
        switch (this.valueSource) {
            case ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE:
                if (this.connector == null) return false;
                return connector.getConnectionOutput() != null;
            case ShaderProgramInVariable.VALUE_SOURCE_GLOBAL:
            case ShaderProgramInVariable.VALUE_SOURCE_CONST:
            case ShaderProgramInVariable.VALUE_SOURCE_EDITABLE:
                return this.value.hasValue();
        }
        return false;
    }
}
