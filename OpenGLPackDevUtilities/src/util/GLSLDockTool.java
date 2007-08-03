/*
 * DockTool.java
 *
 * Created on 28. Mai 2007, 15:07
 */

package util;

import java.awt.EventQueue;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor;
import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor.Category;
import net.java.nboglpack.glsleditor.vocabulary.GLSLVocabulary;

/**
 * Fast hacked tool for GLSL vocabulary doc editing.
 * @author  Michael Bien
 */
public class GLSLDockTool extends javax.swing.JFrame {

    private VocabularyTableModel mainTableModel;
    private VocabularyTableModel vertTableModel;
    private VocabularyTableModel fragTableModel;
    private VocabularyTableModel geomTableModel;

    private final String file = "src/util/GLslVocabulary.xml";

    
    private DocumentListener docTextListener;
    private DocumentListener htmlTextListener;

    private JAXBContext jaxbContext = null;

    /** Creates new form DockTool */
    public GLSLDockTool() {
        List<Wrapper> mainVocab;
        List<Wrapper> vertVocab;
        List<Wrapper> fragVocab;
        List<Wrapper> geomVocab;

        mainVocab = new ArrayList<GLSLDockTool.Wrapper>();
        fragVocab = new ArrayList<GLSLDockTool.Wrapper>();
        vertVocab = new ArrayList<GLSLDockTool.Wrapper>();
        geomVocab = new ArrayList<GLSLDockTool.Wrapper>();

        try {
            jaxbContext = JAXBContext.newInstance(GLSLVocabulary.class, GLSLElementDescriptor.class);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            GLSLVocabulary vocab = (GLSLVocabulary) um.unmarshal(new File(file));

            Set<String> keys = vocab.mainVocabulary.keySet();
            for (String key : keys) {
                GLSLElementDescriptor[] elements = vocab.mainVocabulary.get(key);
                for (GLSLElementDescriptor elem : elements) {
                    mainVocab.add(new Wrapper(key, elem));
                }
            }

            keys = vocab.vertexShaderVocabulary.keySet();
            for (String key : keys) {
                GLSLElementDescriptor[] elements = vocab.vertexShaderVocabulary.get(key);
                for (GLSLElementDescriptor elem : elements) {
                    vertVocab.add(new Wrapper(key, elem));
                }
            }

            keys = vocab.fragmentShaderVocabulary.keySet();
            for (String key : keys) {
                GLSLElementDescriptor[] elements = vocab.fragmentShaderVocabulary.get(key);
                for (GLSLElementDescriptor elem : elements) {
                    fragVocab.add(new Wrapper(key, elem));
                }
            }

            keys = vocab.geometryShaderVocabulary.keySet();
            for (String key : keys) {
                GLSLElementDescriptor[] elements = vocab.geometryShaderVocabulary.get(key);
                for (GLSLElementDescriptor elem : elements) {
                    geomVocab.add(new Wrapper(key, elem));
                }
            }

            mainTableModel = new VocabularyTableModel(mainVocab);
            fragTableModel = new VocabularyTableModel(fragVocab);
            vertTableModel = new VocabularyTableModel(vertVocab);
            geomTableModel = new VocabularyTableModel(geomVocab);
            
        } catch (JAXBException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
            return;
        }

        initComponents();
        
        table.getSelectionModel().addListSelectionListener(createTableSelectionListener());

        table.getColumnModel().getColumn(1).setCellEditor(createTableCellEditor());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
                        
                
        // not DRY ;)
        docTextListener = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateText(e);
            }

            public void removeUpdate(DocumentEvent e) {
                updateText(e);
            }

            private void updateText(DocumentEvent e) {
                htmlTextEditor.getDocument().removeDocumentListener(htmlTextListener);
                htmlTextEditor.setText(docTextEditor.getText());
                htmlTextEditor.getDocument().addDocumentListener(htmlTextListener);
            }

            public void changedUpdate(DocumentEvent e) {
            }
        };
        
        htmlTextListener = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateText(e);
            }

            public void removeUpdate(DocumentEvent e) {
                updateText(e);
            }

            private void updateText(DocumentEvent e) {
                docTextEditor.getDocument().removeDocumentListener(docTextListener);
                try {
                    docTextEditor.setText(e.getDocument().getText(0, e.getDocument().getLength()));
                } catch (BadLocationException ex) {
                    Logger.getLogger(GLSLDockTool.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    docTextEditor.getDocument().addDocumentListener(docTextListener);
                }
            }

            public void changedUpdate(DocumentEvent e) {
            }
        };
        
        docTextEditor.getDocument().addDocumentListener(docTextListener);
        htmlTextEditor.getDocument().addDocumentListener(htmlTextListener);
    }
    
    
    
    private ListSelectionListener createTableSelectionListener() {
        return new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1 && !e.getValueIsAdjusting()) {
                    Wrapper entry = ((VocabularyTableModel)table.getModel()).getEntry(table.getSelectedRow());
                    if (entry.getDesc() != null) {
                        docTextEditor.setText(entry.getDesc().doc);
                        htmlTextEditor.setText(docTextEditor.getText());
                    }
                }
            }
        };
    }


    private TableCellEditor createTableCellEditor() {
        JComboBox combo = new JComboBox(GLSLElementDescriptor.Category.values());
        combo.setBorder(BorderFactory.createEmptyBorder());

        return new DefaultCellEditor(combo);
    }
    


    private final class VocabularyTableModel extends AbstractTableModel {

        List<Wrapper> list;

        private VocabularyTableModel(List<Wrapper> list) {
            this.list = list;
        }

        public void addEntry(Wrapper entry) {
            list.add(entry);
            fireTableDataChanged();
        }

        public boolean removeEntry(Wrapper entry) {
            boolean b = list.remove(entry);
            fireTableDataChanged();
            return b;
        }

        public Wrapper removeEntry(int index) {
            Wrapper entry = list.remove(table.getRowSorter().convertRowIndexToModel(index));
            fireTableDataChanged();
            return entry;
        }

        public Wrapper getEntry(int index) {
            return list.get(table.getRowSorter().convertRowIndexToModel(index));
        }

        public int getRowCount() {
            return list.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Wrapper entry = list.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return entry.key;
                case 1:
                    if (entry.getDesc() == null) {
                        return null;
                    }
                    return entry.getDesc().category;
                case 2:
                    if (entry.getDesc() == null) {
                        return null;
                    }
                    return entry.getDesc().type;
                case 3:
                    if (entry.getDesc() == null) {
                        return null;
                    }
                    return entry.getDesc().arguments;
                case 4:
                    if (entry.getDesc() == null) {
                        return null;
                    }
                    return entry.getDesc().tooltip;
                case 5:
                    if (entry.getDesc() == null) {
                        return null;
                    }
                    return entry.getDesc().doc;
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            Wrapper entry = list.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    entry.key = (String) value;
                    break;
                case 1:
                    if (entry.getDesc() == null) {
                        entry.element = new GLSLElementDescriptor();
                    }
                    entry.getDesc().category = (Category) value;
                    break;
                case 2:
                    if (entry.getDesc() == null) {
                        entry.element = new GLSLElementDescriptor();
                    }
                    entry.getDesc().type = ((String) value);
                    break;
                case 3:
                    if (entry.getDesc() == null) {
                        entry.element = new GLSLElementDescriptor();
                    }
                    entry.getDesc().arguments = ((String) value);
                    break;
                case 4:
                    if (entry.getDesc() == null) {
                        entry.element = new GLSLElementDescriptor();
                    }
                    entry.getDesc().tooltip = ((String) value);
                    break;
                case 5:
                    if (entry.getDesc() == null) {
                        entry.element = new GLSLElementDescriptor();
                    }
                    entry.getDesc().doc = ((String) value);
                    break;
            }
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "string";
                case 1:
                    return "category";
                case 2:
                    return "type";
                case 3:
                    return "arguments";
                case 4:
                    return "tooltip";
                case 5:
                    return "doc";
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 5;
        }
    }

    private class Wrapper {

        private String key;
        private GLSLElementDescriptor element;

        private Wrapper(String key, GLSLElementDescriptor element) {
            this.key = key;
            this.element = element;
        }

        private GLSLElementDescriptor getDesc() {
            return element;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new GLSLDockTool().setVisible(true);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JButton removeButton = new javax.swing.JButton();
        javax.swing.JButton addButton = new javax.swing.JButton();
        javax.swing.JButton saveButton = new javax.swing.JButton();
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane();
        javax.swing.JPanel docPanel = new javax.swing.JPanel();
        cleanupTextField = new javax.swing.JTextField();
        javax.swing.JButton cleanupButton = new javax.swing.JButton();
        javax.swing.JButton saveHTMLButton = new javax.swing.JButton();
        javax.swing.JSplitPane docSplitPane = new javax.swing.JSplitPane();
        javax.swing.JPanel htmlPanel = new javax.swing.JPanel();
        javax.swing.JLabel htmlLabel = new javax.swing.JLabel();
        javax.swing.JScrollPane htmlScrollPane = new javax.swing.JScrollPane();
        htmlTextEditor = new javax.swing.JTextArea();
        javax.swing.JPanel textPanel = new javax.swing.JPanel();
        javax.swing.JLabel textLabel = new javax.swing.JLabel();
        javax.swing.JScrollPane docScrollPane = new javax.swing.JScrollPane();
        docTextEditor = new javax.swing.JEditorPane();
        javax.swing.JPanel vocabutaryPanel = new javax.swing.JPanel();
        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        groupComboBox = new javax.swing.JComboBox();
        javax.swing.JLabel groupLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vocabulary Tool");

        removeButton.setText("remove selected");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        addButton.setText("add new");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        saveButton.setText("save all");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        splitPane.setDividerLocation(200);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);

        cleanupTextField.setText("<div> </div> <nobr> </nobr>");

        cleanupButton.setText("cleanup");
        cleanupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanupButtonActionPerformed(evt);
            }
        });

        saveHTMLButton.setText("save HTML");
        saveHTMLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveHTMLButtonActionPerformed(evt);
            }
        });

        docSplitPane.setBorder(null);
        docSplitPane.setDividerLocation(200);
        docSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        docSplitPane.setContinuousLayout(true);

        htmlLabel.setText("2. Edit HTML:");

        htmlTextEditor.setColumns(20);
        htmlTextEditor.setRows(5);
        htmlTextEditor.setTabSize(4);
        htmlScrollPane.setViewportView(htmlTextEditor);

        javax.swing.GroupLayout htmlPanelLayout = new javax.swing.GroupLayout(htmlPanel);
        htmlPanel.setLayout(htmlPanelLayout);
        htmlPanelLayout.setHorizontalGroup(
            htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 820, Short.MAX_VALUE)
            .addGroup(htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(htmlPanelLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addGroup(htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(htmlScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                        .addComponent(htmlLabel))
                    .addGap(9, 9, 9)))
        );
        htmlPanelLayout.setVerticalGroup(
            htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
            .addGroup(htmlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(htmlPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(htmlLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(htmlScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        docSplitPane.setBottomComponent(htmlPanel);

        textLabel.setText("1. Paste formated Text here:");

        docTextEditor.setContentType("text/html"); // NOI18N
        docScrollPane.setViewportView(docTextEditor);

        javax.swing.GroupLayout textPanelLayout = new javax.swing.GroupLayout(textPanel);
        textPanel.setLayout(textPanelLayout);
        textPanelLayout.setHorizontalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(docScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(textPanelLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(textLabel)
                    .addContainerGap(650, Short.MAX_VALUE)))
        );
        textPanelLayout.setVerticalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
            .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(textPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(textLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(docScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        docSplitPane.setLeftComponent(textPanel);

        javax.swing.GroupLayout docPanelLayout = new javax.swing.GroupLayout(docPanel);
        docPanel.setLayout(docPanelLayout);
        docPanelLayout.setHorizontalGroup(
            docPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, docPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cleanupTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cleanupButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveHTMLButton)
                .addContainerGap())
            .addComponent(docSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
        );

        docPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cleanupButton, saveHTMLButton});

        docPanelLayout.setVerticalGroup(
            docPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, docPanelLayout.createSequentialGroup()
                .addComponent(docSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(docPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cleanupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveHTMLButton)
                    .addComponent(cleanupButton))
                .addContainerGap())
        );

        splitPane.setBottomComponent(docPanel);

        table.setModel(mainTableModel);
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setFillsViewportHeight(true);
        tableScrollPane.setViewportView(table);

        groupComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "main", "vert", "frag", "geom" }));
        groupComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupComboBoxActionPerformed(evt);
            }
        });

        groupLabel.setText("select Vocabulary Group:");

        javax.swing.GroupLayout vocabutaryPanelLayout = new javax.swing.GroupLayout(vocabutaryPanel);
        vocabutaryPanel.setLayout(vocabutaryPanelLayout);
        vocabutaryPanelLayout.setHorizontalGroup(
            vocabutaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vocabutaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vocabutaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addGroup(vocabutaryPanelLayout.createSequentialGroup()
                        .addComponent(groupLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        vocabutaryPanelLayout.setVerticalGroup(
            vocabutaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vocabutaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vocabutaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPane.setTopComponent(vocabutaryPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 483, Short.MAX_VALUE)
                        .addComponent(saveButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, removeButton, saveButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(removeButton)
                    .addComponent(addButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void groupComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupComboBoxActionPerformed
    if (groupComboBox.getSelectedItem().equals("vert")) {
        table.setModel(vertTableModel);
    } else if (groupComboBox.getSelectedItem().equals("frag")) {
        table.setModel(fragTableModel);
    } else if (groupComboBox.getSelectedItem().equals("geom")) {
        table.setModel(geomTableModel);
    } else if (groupComboBox.getSelectedItem().equals("main")) {
        table.setModel(mainTableModel);
    }
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
    table.setRowSorter(sorter);
    
    table.getColumnModel().getColumn(1).setCellEditor(createTableCellEditor());
    
    
}//GEN-LAST:event_groupComboBoxActionPerformed
    
// netbeans bug cant get rid of that method ;)
private void asdf(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_asdf
    }//GEN-LAST:event_asdf


    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                
                GLSLVocabulary vocab = new GLSLVocabulary();

                for (Wrapper wrapper : mainTableModel.list) {
                    GLSLElementDescriptor[] elements = vocab.mainVocabulary.get(wrapper.key);
                    if (elements == null) {
                        vocab.mainVocabulary.put(wrapper.key, new GLSLElementDescriptor[]{wrapper.element});
                    } else {
                        GLSLElementDescriptor[] newElements = new GLSLElementDescriptor[elements.length + 1];
                        for (int i = 0; i < elements.length; i++) {
                            newElements[i] = elements[i];
                        }
                        newElements[newElements.length - 1] = wrapper.element;
                        vocab.mainVocabulary.put(wrapper.key, newElements);
                    }
                }

                for (Wrapper wrapper : fragTableModel.list) {
                    GLSLElementDescriptor[] elements = vocab.fragmentShaderVocabulary.get(wrapper.key);
                    if (elements == null) {
                        vocab.fragmentShaderVocabulary.put(wrapper.key, new GLSLElementDescriptor[]{wrapper.element});
                    } else {
                        GLSLElementDescriptor[] newElements = new GLSLElementDescriptor[elements.length + 1];
                        for (int i = 0; i < elements.length; i++) {
                            newElements[i] = elements[i];
                        }
                        newElements[newElements.length - 1] = wrapper.element;
                        vocab.fragmentShaderVocabulary.put(wrapper.key, newElements);
                    }
                }

                for (Wrapper wrapper : vertTableModel.list) {
                    GLSLElementDescriptor[] elements = vocab.vertexShaderVocabulary.get(wrapper.key);
                    if (elements == null) {
                        vocab.vertexShaderVocabulary.put(wrapper.key, new GLSLElementDescriptor[]{wrapper.element});
                    } else {
                        GLSLElementDescriptor[] newElements = new GLSLElementDescriptor[elements.length + 1];
                        for (int i = 0; i < elements.length; i++) {
                            newElements[i] = elements[i];
                        }
                        newElements[newElements.length - 1] = wrapper.element;
                        vocab.vertexShaderVocabulary.put(wrapper.key, newElements);
                    }
                }
                for (Wrapper wrapper : geomTableModel.list) {
                    GLSLElementDescriptor[] elements = vocab.geometryShaderVocabulary.get(wrapper.key);
                    if (elements == null) {
                        vocab.geometryShaderVocabulary.put(wrapper.key, new GLSLElementDescriptor[]{wrapper.element});
                    } else {
                        GLSLElementDescriptor[] newElements = new GLSLElementDescriptor[elements.length + 1];
                        for (int i = 0; i < elements.length; i++) {
                            newElements[i] = elements[i];
                        }
                        newElements[newElements.length - 1] = wrapper.element;
                        vocab.geometryShaderVocabulary.put(wrapper.key, newElements);
                    }
                }
                
                File file = new File(GLSLDockTool.this.file);
                if (!file.exists()){
                    try{
                        file.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger("global").log(Level.SEVERE, null, ex);
                        return;
                    }
                }
                
                
                try {

                    OutputStream os = null;
                    try{
                        Marshaller marshaller = jaxbContext.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        os = new FileOutputStream(file);
                        marshaller.marshal(vocab, os);
                        os.flush();
                        os.close();
                    } catch (IOException ex) {
                        Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    }
                } catch (JAXBException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        });
}//GEN-LAST:event_saveButtonActionPerformed

private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
    if (table.getSelectedRow() != -1) {
        ((VocabularyTableModel) table.getModel()).removeEntry(table.getSelectedRow());
        table.repaint();
    }
}//GEN-LAST:event_removeButtonActionPerformed

private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    ((VocabularyTableModel) table.getModel()).addEntry(new Wrapper("<newItem>", new GLSLElementDescriptor()));
    table.repaint();
}//GEN-LAST:event_addButtonActionPerformed

private void cleanupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanupButtonActionPerformed
    StringTokenizer tokenizer = new StringTokenizer(cleanupTextField.getText());
    String str = htmlTextEditor.getText();
    while(tokenizer.hasMoreElements())
        str = str.replaceAll(tokenizer.nextToken(), "");
    htmlTextEditor.setText(str);
}//GEN-LAST:event_cleanupButtonActionPerformed

private void saveHTMLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveHTMLButtonActionPerformed
    if (table.getSelectedRow() != -1) {
        Wrapper entry = ((VocabularyTableModel)table.getModel()).getEntry(table.getSelectedRow());
        if (entry.getDesc() == null) {
            entry.element = new GLSLElementDescriptor();
        }
        entry.getDesc().doc = (htmlTextEditor.getText());
    }
    table.repaint();
}//GEN-LAST:event_saveHTMLButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cleanupTextField;
    private javax.swing.JEditorPane docTextEditor;
    private javax.swing.JComboBox groupComboBox;
    private javax.swing.JTextArea htmlTextEditor;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
