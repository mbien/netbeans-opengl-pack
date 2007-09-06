/*
 * GlobalVariablesEditor.java
 *
 * Created on May 9, 2007, 11:31 AM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Writer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.nboglpack.visualdesigner.shader.variables.DefaultDataSourceTableModel;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariableEditor;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.VariablesCollection;
import net.java.nboglpack.visualdesigner.tools.LineLayout;

/**
 * UI to edit and create global variables
 *
 * @author Samuel Sperling
 */
public class GlobalVariablesEditor extends JPanel {
    
    private JMenuItem mnuInsert;
    private VariablesCollection<GlobalVariable> globalVariables;
    private JMenuItem mnuEdit;
    private JTable table;
    private DefaultDataSourceTableModel tableModel;
    private VariablePropertiesEditor variablePropertiesEditor;
    private JMenuItem mnuDelete;
    
    /** Creates a new instance of GlobalVariablesEditor */
    public GlobalVariablesEditor(VariablesCollection<GlobalVariable> globalVariables) {
        this.globalVariables = globalVariables;
        initComponents();
    }
    
    private void initComponents() {
        
        // This Panel
        this.setLayout(new BorderLayout());
        
        // Table
        tableModel = new DefaultDataSourceTableModel<GlobalVariable>(
                globalVariables, GlobalVariable.COLUMNS, GlobalVariable.COLUMN_CLASSES, GlobalVariable.COLUMN_NAMES
                );
        globalVariables.addCollectionChangedListener(tableModel);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, new ShaderVariableEditor());
        table.getTableHeader().setResizingAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectionChanged();
                }
            }
        });
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
        
        // ContextMenu
        JPopupMenu contextMenu = new JPopupMenu();
        table.setComponentPopupMenu(contextMenu);
        
        // Insert
        mnuInsert = new JMenuItem("Insert new Global");
        mnuInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertNewItem();
            }
        });
        contextMenu.add(mnuInsert);
        
        // Delete
        mnuDelete = new JMenuItem("Remove Global");
        mnuDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteItem();
            }
        });
        mnuDelete.setEnabled(false);
        contextMenu.add(mnuDelete);
        
        // Edit
        mnuEdit = new JMenuItem("Edit Global");
        mnuEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editItem();
            }
        });
        mnuEdit.setEnabled(false);
        contextMenu.add(mnuEdit);
        
        // Editing area
        variablePropertiesEditor = new VariablePropertiesEditor(this);
        add(variablePropertiesEditor, BorderLayout.EAST);
        variablePropertiesEditor.setVisible(false);
    }
    
    protected void selectionChanged() {
        mnuDelete.setEnabled(true);
        mnuEdit.setEnabled(true);
    }
    
    protected void deleteItem() {
        globalVariables.remove(table.getSelectedRow());
    }
    
    protected void editItem() {
        variablePropertiesEditor.showDialog(globalVariables.get(table.getSelectedRow()));
    }
    
    protected void insertNewItem() {
        variablePropertiesEditor.showNewVariableDialog();
    }
    
    public void itemAdded(GlobalVariable shaderVariable) {
        addRow(shaderVariable);
    }
    
    public void itemRemoved(GlobalVariable shaderVariable) {
        removeRow(shaderVariable);
    }
    
    private void addRow(GlobalVariable shaderVariable) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void removeRow(GlobalVariable shaderVariable) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    protected void save(GlobalVariable shaderVariable) {
        GlobalVariable var = globalVariables.add(shaderVariable);
        if (var != shaderVariable) {
            JOptionPane.showMessageDialog(this, "Name '" + shaderVariable.getName() + "' is already in use.", "Creating new Global failed", JOptionPane.INFORMATION_MESSAGE);
            // TODO: Select element with that name
        } else {
            variablePropertiesEditor.setVisible(false);
        }
    }

    void saveGlobals(Writer writer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}

class VariablePropertiesEditor extends JPanel {
    
    private GlobalVariable shaderVariable;
    private GlobalVariablesEditor globalVariablesEditor;
    
    public VariablePropertiesEditor(GlobalVariablesEditor globalVariablesEditor) {
        this.globalVariablesEditor = globalVariablesEditor;
        initComponents();
    }
    
    public void showNewVariableDialog() {
        this.shaderVariable = null;
        setTitle("Create new Global");
        this.setVisible(true);
    }
    
    public void showDialog(GlobalVariable shaderVariable) {
        setTitle("Edit Global");
        this.shaderVariable = shaderVariable;
        dataTypeSelection.setSelectedIndex(DataType.getDataTypeIndex(shaderVariable.getDataType()));
        dataTypeDimensionSelection.setSelectedIndex(DataType.getDimensions(shaderVariable.getDataType()) - 1);
        name.setText(shaderVariable.getName());
        this.setVisible(true);
    }
    
    private void setTitle(String name) {
        lblTitle.setText(" " + name);
    }
    
    private JPanel fieldsPanel;
    private JComboBox dataTypeSelection;
    private JComboBox dataTypeDimensionSelection;
    private JTextField name;
    private JLabel lblTitle;
    
    private void initComponents() {
        this.setLayout(new LineLayout());
        
        lblTitle = new JLabel();
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD));
        add(lblTitle);
        
        fieldsPanel = new JPanel();
        fieldsPanel.setBorder(new TitledBorder("Variable Properties"));
        GridLayout layout = new GridLayout(0, 2, 2, 2);
        fieldsPanel.setLayout(layout);
        
        // DataType
        JLabel lblDataType = new JLabel("DataType:");
        lblDataType.setHorizontalTextPosition(SwingConstants.RIGHT);
        fieldsPanel.add(lblDataType);
        
        dataTypeSelection = new JComboBox();
        for (int i = DataType.DATA_TYPE_LOWEST_EXPLICIT_BIT; i <= DataType.DATA_TYPE_HIGHEST_EXPLICIT_BIT; i++) {
            dataTypeSelection.addItem(DataType.getDataTypeName(1 << i));
        }
        fieldsPanel.add(dataTypeSelection);
        
        // Dimension
        JLabel lblDimension = new JLabel("Dimension:");
        lblDimension.setHorizontalTextPosition(SwingConstants.RIGHT);
        fieldsPanel.add(lblDimension);
        
        dataTypeDimensionSelection = new JComboBox();
        for (int i = DataType.DATA_TYPE_LOWEST_DIMENSION_BIT; i <= DataType.DATA_TYPE_HIGHEST_DIMENSION_BIT; i++) {
            dataTypeDimensionSelection.addItem(DataType.getDataTypeName(1 << i));
        }
        fieldsPanel.add(dataTypeDimensionSelection);
        
        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setHorizontalTextPosition(SwingConstants.RIGHT);
        fieldsPanel.add(lblName);
        name = new JTextField();
        fieldsPanel.add(name);
        
        add(fieldsPanel);
        
        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.setDefaultCapable(true);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        add(saveButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        add(cancelButton);
        
    }
    
    private void save() {
        if (shaderVariable == null) {
            // New Var
            // TODO: Check name validity towards GLSL names like glsl-function names

            String name = this.name.getText();
            if (name.length() < 1) {
                JOptionPane.showMessageDialog(this, "Specify a name first", "Creating new Global failed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                GlobalVariable newShaderVariable = new GlobalVariable(name, getDataType());
                globalVariablesEditor.save(newShaderVariable);
            }
        } else {
            // Edited Var
            if (!shaderVariable.setName(this.name.getText())) {
                JOptionPane.showMessageDialog(this, "Name '" + this.name.getText() + "' is already in use.", "Editing new Global failed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                shaderVariable.setDataType(getDataType());
                this.setVisible(false);
            }
        }
    }
    private void cancel() {
        this.setVisible(false);
    }
    
    private int getDataType() {
        return (
                1 << (dataTypeSelection.getSelectedIndex() + DataType.DATA_TYPE_LOWEST_EXPLICIT_BIT) |
                1 << (dataTypeDimensionSelection.getSelectedIndex() + DataType.DATA_TYPE_LOWEST_DIMENSION_BIT)
                );
    }
}
