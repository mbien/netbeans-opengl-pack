/*
 * DefaultDataSourceTableModel.java
 *
 * Created on May 9, 2007, 7:25 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

import javax.swing.table.AbstractTableModel;

/**
 * Default table-model operating on the IDataSource and IDataItem interfaces
 * to populate a table and notify about data-changes.
 *
 * @author Samuel Sperling
 */
public class DefaultDataSourceTableModel<E extends IDataItem>
        extends AbstractTableModel implements ICollectionChangedListener<E> {
    
    protected IDataSource<E> dataSource;
    protected int columnCount;
    protected Class[] columnClasses;
    protected String[] columnNames;
    
    public DefaultDataSourceTableModel(IDataSource<E> dataSource,
            int columnCount, Class[] columnClasses, String[] columnNames) {
        this.dataSource = dataSource;
        this.columnCount = columnCount;
        this.columnClasses = columnClasses;
        this.columnNames = columnNames;
    }
    
    public int getRowCount() {
        return dataSource == null ? 0 : dataSource.size();
    }
    
    public int getColumnCount() {
        return columnCount;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataSource.get(rowIndex).getValueAt(columnIndex);
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dataSource.get(rowIndex).setValueAt(aValue, columnIndex);
    }
    
    public Class getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }
    
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return dataSource.get(rowIndex).isCellEditable(columnIndex);
    }
    
    public void itemAdded(E element) {
        fireTableDataChanged();
    }
    
    public void itemRemoved(E element) {
        // TODO: Could be replaced by fireTableRowsDeleted(...)
        fireTableDataChanged();
    }
    
    public void itemChanged(E element) {
        fireTableRowsUpdated(element.getIndex(), element.getIndex());
    }
    
}
