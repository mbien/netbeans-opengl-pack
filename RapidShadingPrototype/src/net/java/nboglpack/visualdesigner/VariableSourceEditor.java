/*
 * VariableSourceEditor.java
 *
 * Created on May 1, 2007, 1:31 PM
 *
 */

package net.java.nboglpack.visualdesigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import net.java.nboglpack.visualdesigner.shader.variables.DefaultDataSourceTableModel;

/**
 * UI to edit the sources of each variable.
 * Choose whether the source is constant, connectable, etc.
 * And edit the variables in case of a constant variable source choice.
 *
 * @author Samuel Sperling
 */
public class VariableSourceEditor extends JPanel {
    
    private ShaderNode shaderNode;
    private JSplitPane splitPane;
    private JTable inVarTable;
    private JTable outVarTable;
    private DefaultDataSourceTableModel inVarTableModel;
    private DefaultDataSourceTableModel outVarTableModel;
    private JPanel inVarTablePanel;
    private JPanel outVarTablePanel;
    
    /**
     * Creates a new instance of VariableSourceEditor
     */
    public VariableSourceEditor(ShaderNode shaderNode) {
        this.shaderNode = shaderNode;
        initComponents();
    }
    
    private void initComponents() {
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        inVarTablePanel = new JPanel();
        inVarTablePanel.setLayout(new BorderLayout());
        splitPane.setTopComponent(inVarTablePanel);
        outVarTablePanel = new JPanel();
        outVarTablePanel.setLayout(new BorderLayout());
        splitPane.setBottomComponent(outVarTablePanel);
        
        // IN Var Table
        inVarTableModel = new DefaultDataSourceTableModel<ShaderProgramInVariable>(
                shaderNode.getInVariables(), ShaderProgramInVariable.COLUMNS, ShaderProgramInVariable.COLUMN_CLASSES, ShaderProgramInVariable.COLUMN_NAMES
                );
        shaderNode.getInVariables().addCollectionChangedListener(inVarTableModel);
        inVarTable = new JTable(inVarTableModel);
        inVarTable.getColumn(ShaderProgramInVariable.COLUMN_NAMES[ShaderProgramInVariable.COLUMN_SOURCE]).setCellEditor(
                ShaderProgramInVariable.getValueSourceChooserEditor());
        inVarTable.getColumn(ShaderProgramInVariable.COLUMN_NAMES[ShaderProgramInVariable.COLUMN_VALUE]).setCellEditor(
                ShaderProgramInVariable.getValueEditor());
        inVarTable.getTableHeader().setResizingAllowed(true);
        inVarTable.getTableHeader().setReorderingAllowed(true);
        
        // OUT Var Table
        outVarTableModel = new DefaultDataSourceTableModel<ShaderProgramOutVariable>(
                shaderNode.getOutVariables(), ShaderProgramOutVariable.COLUMNS, ShaderProgramOutVariable.COLUMN_CLASSES, ShaderProgramOutVariable.COLUMN_NAMES
                );
        shaderNode.getOutVariables().addCollectionChangedListener(outVarTableModel);
        outVarTable = new JTable(outVarTableModel);
        outVarTable.getColumn(ShaderProgramOutVariable.COLUMN_NAMES[ShaderProgramOutVariable.COLUMN_SOURCE]).setCellEditor(
                ShaderProgramOutVariable.getValueSourceChooserEditor());
        outVarTable.getColumn(ShaderProgramOutVariable.COLUMN_NAMES[ShaderProgramOutVariable.COLUMN_VALUE]).setCellEditor(
                ShaderProgramOutVariable.getValueEditor());
        outVarTable.getTableHeader().setResizingAllowed(true);
        outVarTable.getTableHeader().setReorderingAllowed(true);
        // TODO: Sorting would be nice
        
        JPanel headerInPanel = new JPanel();
        headerInPanel.setLayout(new BorderLayout());
        JLabel lblTitle = new JLabel("Input varibles");
        headerInPanel.setBackground(new Color(64, 192, 64));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD));
        headerInPanel.add(lblTitle, BorderLayout.PAGE_START);
        headerInPanel.add(inVarTable.getTableHeader(), BorderLayout.CENTER);
        inVarTablePanel.add(headerInPanel, BorderLayout.PAGE_START);
        inVarTablePanel.add(inVarTable, BorderLayout.CENTER);
        
        headerInPanel = new JPanel();
        headerInPanel.setLayout(new BorderLayout());
        lblTitle = new JLabel("Output varibles");
        headerInPanel.setBackground(new Color(64, 64, 192));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD));
        headerInPanel.add(lblTitle, BorderLayout.PAGE_START);
        headerInPanel.add(outVarTable.getTableHeader(), BorderLayout.CENTER);
        outVarTablePanel.add(headerInPanel, BorderLayout.PAGE_START);
        outVarTablePanel.add(outVarTable, BorderLayout.CENTER);
        
        this.setLayout(new BorderLayout());
        this.add(splitPane);

//        resizeWindow();
    }
    
//    public void variableAdded(ShaderProgramVariable shaderVariable) {
//        tableModel.fireTableDataChanged();
////        resizeWindow();
//    }
//    public void variableRemoved(ShaderProgramVariable shaderVariable) {
//        tableModel.fireTableDataChanged();
////        resizeWindow();
//    }
//    private void resizeWindow(){
//        Dimension d = this.getPreferredSize();
//        d.setSize(d.width + 20, d.height + 35);
//        this.setSize(d);
//    }
    
    
//    public void updateData() {
//        this.getContentPane().removeAll();
//        for (int r = 0; r < tableModel.getRowCount(); r++) {
//            for (int c = 0; c < tableModel.getColumnCount(); c++) {
//                Object value = tableModel.getValueAt(r, c);
//                if (value instanceof Component)
//                    this.getContentPane().add((Component) value);
//                else
//                    this.getContentPane().add(new JLabel(value == null ? "" : value.toString()));
//            }
//        }
//        Dimension d = this.getContentPane().getPreferredSize();
//        d.setSize(d.width + 20, d.height + 40);
//        this.setSize(d);
//    }
}

//class VariableSourceTableModel extends AbstractTableModel {
//    
//    private ShaderNode shaderNode;
//    
//    public VariableSourceTableModel(ShaderNode shaderNode) {
//        this.shaderNode = shaderNode;
//    }
//    
//    public int getRowCount() {
//        return this.shaderNode.getVariableCount();
//    }
//    
//    public int getColumnCount() {
//        return ShaderProgramVariable.COLUMNS;
//    }
//    
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        return this.shaderNode.getVariable(rowIndex).getColumn(columnIndex);
//    }
//
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        this.shaderNode.getVariable(rowIndex).setVariableValue(columnIndex, aValue);
//    }
//    
//    public Class getColumnClass(int columnIndex) {
//        return ShaderProgramVariable.getColumnClass(columnIndex);
//    }
//    
//    public String getColumnName(int columnIndex) {
//        return ShaderProgramVariable.getColumnName(columnIndex);
//    }
//    
//    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        return this.shaderNode.getVariable(rowIndex).isColumnEditable(columnIndex);
//    }
//}
//
