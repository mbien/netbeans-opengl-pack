/*
 * ShaderFieldEditor.java
 *
 * Created on May 8, 2007, 1:55 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import javax.swing.FocusManager;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;
import net.java.nboglpack.visualdesigner.tools.LineLayout;

/**
 * This class provides an editor for any kind of a shaderField
 *
 * @author Samuel Sperling
 */
public class ShaderFieldEditor extends JPanel implements TableCellEditor {
    
    protected int dataType;
    protected int chosenDataTypeExplicit;
    protected int chosenDataTypeDimension;
    protected JPanel dynamicContent;
    protected JFormattedTextField.AbstractFormatter integerFormatter;
    protected JFormattedTextField.AbstractFormatter floatFormatter;
    protected JTable table;
    protected int row;
    protected int column;
    protected int defaultRowHeight = 0;
    
    
    /** Creates a new instance of ShaderFieldEditor */
    public ShaderFieldEditor() {
        
        // initialize
        NumberFormat format = DecimalFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        integerFormatter = new NumberFormatter(format);
        
        format = DecimalFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(10);
        floatFormatter = new NumberFormatter(format);
        
        this.setLayout(new LineLayout());
        
        // Dynamic Content - supposed to be filled later
        dynamicContent = new JPanel();
        dynamicContent.setOpaque(false);
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        if (dataType == 0) {
            this.dataType = 0;
            return;
        }
            
        // If the dimenstion is not set at all it's set by default to DATA_TYPE_DIMENSION1
        if (DataType.countSetBits(dataType, DataType.DATA_TYPE_HIGHEST_EXPLICIT_BIT + 1, DataType.DATA_TYPE_HIGHEST_DIMENSION_BIT)
            == 0)
            dataType = dataType | DataType.DATA_TYPE_DIMENSION1;
        this.dataType = dataType;
        initComponents();
    }


    private JComboBox boxDataType;
    private JComboBox boxDataTypeDimension;
    
    private void initComponents() {
        this.removeAll();
        boolean isDataTypeGeneric = DataType.isDataTypeGeneric(dataType);
        boolean isDataTypeDimensionGeneric = DataType.isDimensionGeneric(dataType);
        boxDataType = null;
        boxDataTypeDimension = null;
        
        this.add(dynamicContent);
        
        chosenDataTypeExplicit = 0;
        chosenDataTypeDimension = 0;
    
        if (!isDataTypeGeneric)
            setChosenDataTypeExplicit(DataType.getDataTypeOnly(dataType));
        if (!isDataTypeDimensionGeneric)
            setChosenDataTypeDimension(DataType.getDimensionOnly(dataType));

        // Depending on the options the dataType leaves comboBoxes are created
        // to create options on type (float, int, ...) and dimension
        
        // Create ComboBox for Dimension choice
        if (isDataTypeDimensionGeneric) {
            boxDataTypeDimension = new JComboBox();
            for (int i = DataType.DATA_TYPE_LOWEST_DIMENSION_BIT; i <= DataType.DATA_TYPE_HIGHEST_DIMENSION_BIT; i++) {
                if ((dataType & (1 << i)) > 0)
                    boxDataTypeDimension.addItem(DataType.getDataTypeName((1 << i) | chosenDataTypeExplicit));
            }
            boxDataTypeDimension.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == e.SELECTED) {
                        setChosenDataTypeDimension(1 << (boxDataTypeDimension.getSelectedIndex() + DataType.DATA_TYPE_LOWEST_DIMENSION_BIT));
                    }
                }
            });
            this.add(boxDataTypeDimension);
        }
        
        // Create ComboBox for DataType choice
        if (isDataTypeGeneric) {
            boxDataType = new JComboBox();
            for (int i = DataType.DATA_TYPE_LOWEST_EXPLICIT_BIT; i <= DataType.DATA_TYPE_HIGHEST_EXPLICIT_BIT; i++) {
                if ((dataType & (1 << i)) > 0)
                    boxDataType.addItem(DataType.getDataTypeName((1 << i) | chosenDataTypeDimension));
            }
            boxDataType.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == e.SELECTED) {
                        setChosenDataTypeExplicit(1 << boxDataType.getSelectedIndex());
                    }
                }
            });
            this.add(boxDataType);
        }
        
        // If the DataType is not generic dataType name is displayed:
        if (!isDataTypeGeneric && !isDataTypeDimensionGeneric) {
            JLabel lblDataTypeName = new JLabel(DataType.getDataTypeName(dataType));
            this.add(lblDataTypeName);
        } else {
            // preselect first item in Combobox
            if (boxDataTypeDimension != null)
                setChosenDataTypeDimension(1 << DataType.DATA_TYPE_LOWEST_DIMENSION_BIT);
            if (boxDataType != null)
                setChosenDataTypeExplicit(1 << 0);
        }
        
        refreshCellSize();
    }

    public int getChosenDataTypeDimension() {
        return chosenDataTypeDimension;
    }

    /**
     * Sets the active choice of dimension
     * @param dataTypeDimension  New chosen dataType dimension.
     *          Is not applied if given dataType is generic
     */
    public void setChosenDataTypeDimension(int dataTypeDimension) {
        if (DataType.isDimensionGeneric(dataTypeDimension)) return;
        if (this.chosenDataTypeDimension == dataTypeDimension) return;
        
        this.chosenDataTypeDimension = dataTypeDimension;
        updateInputarea();
    }

    public int getChosenDataTypeExplicit() {
        return chosenDataTypeExplicit;
    }

    /**
     * Sets the active choice of dimension
     * @param dataTypeExplicit  New chosen dataType.
     *          Is not applied if given dataType is generic
     */
    public void setChosenDataTypeExplicit(int dataTypeExplicit) {
        if (DataType.isDataTypeGeneric(dataTypeExplicit)) return;
        if (this.chosenDataTypeExplicit == dataTypeExplicit) return;
        
        this.chosenDataTypeExplicit = dataTypeExplicit;
        updateInputarea();
    }

    /**
     * Updates the input area, creates neccessary input fields
     */
    protected void updateInputarea() {
        if (chosenDataTypeExplicit == 0 || chosenDataTypeDimension == 0) return;
        
        removeDynamicComponents();
        createInputFields(DataType.getDimensions(chosenDataTypeDimension));
    }
    
    private void removeDynamicComponents() {
        dynamicContent.removeAll();
    }

    /**
     * Creates all editing fields for the chosen dataType
     */
    protected void createInputFields(int dimension) {
        if (dimension == 0) return;
        
        if ((chosenDataTypeExplicit &
                (DataType.DATA_TYPE_SAMPLER |
                DataType.DATA_TYPE_SAMPLER_SHADOW |
                DataType.DATA_TYPE_SAMPLERCUBE))
                > 0) {
            // Sampler
            //TODO: create Sampler choice
        } else {
            dynamicContent.setLayout(new GridLayout(1, dimension, 2, 2));
            for (int i = 0; i < dimension; i++) {
                switch (chosenDataTypeExplicit) {
                    case DataType.DATA_TYPE_BOOL:
                        dynamicContent.add(new JCheckBox(getFieldDescription(i)));
                        break;
                    case DataType.DATA_TYPE_INT:
                        JFormattedTextField textFieldInt = new JFormattedTextField(integerFormatter);
                        textFieldInt.setToolTipText(getFieldDescription(i));
                        dynamicContent.add(textFieldInt);
                        break;
                    case DataType.DATA_TYPE_FLOAT:
                        JFormattedTextField textFieldFloat = new JFormattedTextField(floatFormatter);
                        textFieldFloat.setToolTipText(getFieldDescription(i));
                        dynamicContent.add(textFieldFloat);
                        break;
                    case DataType.DATA_TYPE_MAT:
                        JFormattedTextField textFieldMat;
                        for (int j = 0; j < dimension; j++) {
                            textFieldMat = new JFormattedTextField(floatFormatter);
                            textFieldMat.setToolTipText(i + "," + j);
                            dynamicContent.add(textFieldMat);
                        }
                        break;
                }
            }
        }
        refreshCellSize();
    }
    
    private void refreshCellSize() {
        if (table == null) return;
        
        this.table.setRowHeight(row, (int) this.getPreferredSize().getHeight());
        this.table.getColumnModel().getColumn(column).setMinWidth((int) this.getPreferredSize().getWidth());
    }
    
    protected String getFieldDescription(int dimension) {
        return DataType.DATA_TYPE_DIMENSION_DESC_COORD[dimension];
    }
    
    /**
     * Converts the entered values into one string that can be displayed 
     * in the table cell
     */
    public Object getCellEditorValue() {
        String value = "";
        int dimension = DataType.getDimensions(chosenDataTypeDimension);
        if ((chosenDataTypeExplicit &
                (DataType.DATA_TYPE_SAMPLER |
                DataType.DATA_TYPE_SAMPLER_SHADOW |
                DataType.DATA_TYPE_SAMPLERCUBE))
                > 0) {
            // Sampler
            //TODO: create Sampler value
        } else {
            if (dimension > 1) value = "{";
            for (int i = 0; i < dimension; i++) {
                switch (chosenDataTypeExplicit) {
                    case DataType.DATA_TYPE_BOOL:
                        value += ((JCheckBox) dynamicContent.getComponent(i)).isSelected();
                        break;
                    case DataType.DATA_TYPE_INT:
                    case DataType.DATA_TYPE_FLOAT:
                        value += ((JFormattedTextField) dynamicContent.getComponent(i)).getValue();
                        break;
                    case DataType.DATA_TYPE_MAT:
                        if (dimension > 1) value += "{";
                        for (int j = 0; j < dimension; j++) {
                            value += ((JFormattedTextField) dynamicContent.getComponent(i * dimension + j)).getValue();
                            if (j != dimension - 1) value += ", ";
                        }
                        if (dimension > 1) value = "}";
                        break;
                }
                if (i != dimension - 1) value += ", ";
            }
            if (dimension > 1) value += "}";
        }

        return value;
    }
    
    private ShaderVariable value;
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.value = (ShaderVariable) value;
        if (defaultRowHeight == 0) defaultRowHeight = table.getRowHeight();
        
        setDataType(this.value.getChosenDataType());
        refreshCellSize();
        return this;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" AbstractCellEditor Functions ">                          
    //-----------------------------------------------------------------------//
    //     The following is just a copy of the abstractCellEditor class      //
    //-----------------------------------------------------------------------//
/*
 * @(#)AbstractCellEditor.java	1.13 06/04/07
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/**
 * @version 1.13 04/07/06 
 * 
 * A base class for <code>CellEditors</code>, providing default
 * implementations for the methods in the <code>CellEditor</code>
 * interface except <code>getCellEditorValue()</code>. 
 * Like the other abstract implementations in Swing, also manages a list 
 * of listeners. 
 *
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 * 
 * @author Philip Milne
 * @since 1.3
 */
    
    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    // Force this to be implemented. 
    // public Object  getCellEditorValue()  

    /**
     * Returns true.
     * @param e  an event object
     * @return true
     */
    public boolean isCellEditable(EventObject e) { 
	return true; 
    } 

    /**
     * Returns true.
     * @param anEvent  an event object
     * @return true
     */
    public boolean shouldSelectCell(EventObject anEvent) { 
	return true; 
    }
    
    /**
     * Calls <code>fireEditingStopped</code> and returns true.
     * @return true
     */
    public boolean stopCellEditing() {
        Component comp = FocusManager.getCurrentManager().getFocusOwner();
        if (comp instanceof JFormattedTextField) {
            JFormattedTextField textField = (JFormattedTextField) comp;
            try {
                textField.commitEdit();
            } catch (ParseException ex) {
                // doesn't matter    
            }
        }
        this.table.setRowHeight(row, defaultRowHeight);
	fireEditingStopped(); 
	return true;
    }

    /**
     * Calls <code>fireEditingCanceled</code>.
     */
    public void cancelCellEditing() { 
	fireEditingCanceled(); 
    }

    /**
     * Adds a <code>CellEditorListener</code> to the listener list.
     * @param l  the new listener to be added
     */
    public void addCellEditorListener(CellEditorListener l) {
	listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Removes a <code>CellEditorListener</code> from the listener list.
     * @param l  the listener to be removed
     */
    public void removeCellEditorListener(CellEditorListener l) {
	listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Returns an array of all the <code>CellEditorListener</code>s added
     * to this AbstractCellEditor with addCellEditorListener().
     *
     * @return all of the <code>CellEditorListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public CellEditorListener[] getCellEditorListeners() {
        return (CellEditorListener[])listenerList.getListeners(
                CellEditorListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==CellEditorListener.class) {
		// Lazily create the event:
		if (changeEvent == null)
		    changeEvent = new ChangeEvent(this);
		((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
	    }	       
	}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==CellEditorListener.class) {
		// Lazily create the event:
		if (changeEvent == null)
		    changeEvent = new ChangeEvent(this);
		((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
	    }	       
	}
    }
    // </editor-fold>

}
