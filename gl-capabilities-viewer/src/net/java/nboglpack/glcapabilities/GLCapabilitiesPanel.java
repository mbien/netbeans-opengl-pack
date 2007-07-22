/*
 * GLCapabilityPanel.java
 *
 * Created on 19. Juni 2007, 18:51
 */
package net.java.nboglpack.glcapabilities;

import java.util.Comparator;
import javax.beans.binding.Binding;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.binding.ParameterKeys;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 *
 * @author Michael Bien
 */
public class GLCapabilitiesPanel extends javax.swing.JPanel {
    
    
   
    /** Creates new form GLCapabilityPanel */
    public GLCapabilitiesPanel() {
        
        initComponents();
        
        // bind capabilities table to model
        Binding binding = new Binding(model, "${capabilities}", capabilitiesTable, "elements");
        binding.putParameter(ParameterKeys.EDITABLE, false);
        
        binding.addChildBinding("${name}", null)     
                .putParameter(ParameterKeys.COLUMN, 0)
                .putParameter(ParameterKeys.COLUMN_CLASS, String.class);
        binding.addChildBinding("${value}", null)     
                .putParameter(ParameterKeys.COLUMN, 1)
                .putParameter(ParameterKeys.COLUMN_CLASS, String.class);

        bindingContext.addBinding(binding);
        binding.bind();
        
        // bind extensions table to model
        binding = new Binding(model, "${extensions}", extensionsTable, "elements");
        binding.putParameter(ParameterKeys.EDITABLE, false);
        
        bindingContext.addBinding(binding);
        binding.bind();
        
        
        // add filter capability to tables
        createFilter(capabilitiesTable, capabilitiesSearchField);
        createFilter(extensionsTable, extensionsSearchField);
        
    }

    private void createFilter(JTable table, JTextField searchField) {
        
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateSearch(e.getDocument());
            }

            public void removeUpdate(DocumentEvent e) {
                updateSearch(e.getDocument());
            }
            
            private void updateSearch(Document doc) {
                
                try {
                    final String[] search = doc.getText(0, doc.getLength()).toLowerCase().trim().split(" ");
                    
                    if(search.length == 0) {
                        sorter.setRowFilter(null);
                        return;
                    }
                    
                    sorter.setRowFilter(new RowFilter<Object, Object>() {
                        
                        public boolean include(Entry<? extends Object, ? extends Object> entry) {
                            
                            for (int i = 0; i < entry.getValueCount(); i++) {
                                boolean contains = true;
                                for(int n = 0; n < search.length; n++){
                                    if (!entry.getStringValue(i).toLowerCase().contains(search[n])){
                                        contains = false;
                                        break;
                                    }
                                }
                                if(contains)
                                    return true;
                            }
                            
                            return false;
                        }
                                                
                    });
                } catch (BadLocationException ex) {
                    sorter.setRowFilter(null);
                    Exceptions.printStackTrace(ex);
                }
            }

            public void changedUpdate(DocumentEvent e) {}
            
        });
    }
    
    
    
    public void updateFromModel() {
        
        bindingContext.unbind();
        bindingContext.bind();
        
        TableRowSorter<TableModel> sorter = (TableRowSorter)extensionsTable.getRowSorter();
        sorter.setModel(extensionsTable.getModel());
        
        sorter = (TableRowSorter)capabilitiesTable.getRowSorter();
        sorter.setModel(capabilitiesTable.getModel());
        
        sorter.setComparator(1, new Comparator<String>() {
            
            public int compare(String str1, String str2) {
                
                int i1;
                int i2;
                
                if(str1.contains(","))
                    i1 = Integer.parseInt(str1.substring(0, str1.indexOf(",")));
                else
                    i1 = Integer.parseInt(str1);
                
                if(str2.contains(","))
                    i2 = Integer.parseInt(str2.substring(0, str2.indexOf(",")));
                else
                    i2 = Integer.parseInt(str2);
  
                return i1-i2;
            }
            
        });
        capabilitiesTable.getColumnModel().getColumn(1).setMaxWidth(80);
    }
    
    public GLCapabilitiesModel getModel() {
        return model;
    }
    void setModel(GLCapabilitiesModel model) {
        this.model = model;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingContext = new javax.beans.binding.BindingContext();

        model = new net.java.nboglpack.glcapabilities.GLCapabilitiesModel();
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        javax.swing.JPanel overviewPanel = new javax.swing.JPanel();
        javax.swing.JPanel basicCapsPanel = new javax.swing.JPanel();
        glField = new javax.swing.JTextField();
        glslField = new javax.swing.JTextField();
        joglField = new javax.swing.JTextField();
        rendererField = new javax.swing.JTextField();
        vendorField = new javax.swing.JTextField();
        javax.swing.JLabel vendor = new javax.swing.JLabel();
        javax.swing.JLabel renderer = new javax.swing.JLabel();
        javax.swing.JLabel gl = new javax.swing.JLabel();
        javax.swing.JLabel glsl = new javax.swing.JLabel();
        javax.swing.JLabel jogl = new javax.swing.JLabel();
        javax.swing.JPanel overviewCapsPanel = new javax.swing.JPanel();
        viewportField = new javax.swing.JTextField();
        textureSizeField = new javax.swing.JTextField();
        textureUnitsField = new javax.swing.JTextField();
        textureUnitsVSField = new javax.swing.JTextField();
        lightsField = new javax.swing.JTextField();
        anisotropicFilteringField = new javax.swing.JTextField();
        fsaaField = new javax.swing.JTextField();
        textureUnitsFSField = new javax.swing.JTextField();
        textureUnitsGSField = new javax.swing.JTextField();
        renderBuffersField = new javax.swing.JTextField();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        javax.swing.JPanel capabilitiesPanel = new javax.swing.JPanel();
        javax.swing.JScrollPane capabilitiesScrollPane = new javax.swing.JScrollPane();
        capabilitiesTable = new javax.swing.JTable();
        javax.swing.JLabel findLabel = new javax.swing.JLabel();
        capabilitiesSearchField = new javax.swing.JTextField();
        javax.swing.JPanel extensionsPanel = new javax.swing.JPanel();
        extensionsSearchField = new javax.swing.JTextField();
        javax.swing.JLabel findLabel1 = new javax.swing.JLabel();
        javax.swing.JScrollPane extensionsScrollPane = new javax.swing.JScrollPane();
        extensionsTable = new javax.swing.JTable();
        javax.swing.JPanel displayPanel = new javax.swing.JPanel();

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        basicCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.basicCapsPanel.border.title"))); // NOI18N

        glField.setEditable(false);
        glField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        glField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glField.text")); // NOI18N

        bindingContext.addBinding(model, "${glVersion}", glField, "text");

        glslField.setEditable(false);
        glslField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        glslField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glslField.text")); // NOI18N

        bindingContext.addBinding(model, "${glslVersion}", glslField, "text");

        joglField.setEditable(false);
        joglField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        joglField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.joglField.text")); // NOI18N

        bindingContext.addBinding(model, "${implVersion}", joglField, "text");

        rendererField.setEditable(false);
        rendererField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rendererField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.rendererField.text")); // NOI18N

        bindingContext.addBinding(model, "${renderer}", rendererField, "text");

        vendorField.setEditable(false);
        vendorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        vendorField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.vendorField.text")); // NOI18N

        bindingContext.addBinding(model, "${vendor}", vendorField, "text");

        vendor.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.vendor.text")); // NOI18N

        renderer.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderer.text")); // NOI18N

        gl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.gl.text")); // NOI18N

        glsl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glsl.text")); // NOI18N

        jogl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jogl.text")); // NOI18N

        javax.swing.GroupLayout basicCapsPanelLayout = new javax.swing.GroupLayout(basicCapsPanel);
        basicCapsPanel.setLayout(basicCapsPanelLayout);
        basicCapsPanelLayout.setHorizontalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gl)
                    .addComponent(glsl)
                    .addComponent(jogl)
                    .addComponent(renderer)
                    .addComponent(vendor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(vendorField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(glField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(glslField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(joglField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(rendererField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addContainerGap())
        );
        basicCapsPanelLayout.setVerticalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gl)
                    .addComponent(glField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(glsl)
                    .addComponent(glslField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jogl)
                    .addComponent(joglField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renderer)
                    .addComponent(rendererField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendor)
                    .addComponent(vendorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        overviewCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewCapsPanel.border.title"))); // NOI18N

        viewportField.setEditable(false);
        viewportField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        viewportField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.viewportField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxViewPortSize}", viewportField, "text");

        textureSizeField.setEditable(false);
        textureSizeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureSizeField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureSizeField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxTextureSize}", textureSizeField, "text");

        textureUnitsField.setEditable(false);
        textureUnitsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxTextureUnits}", textureUnitsField, "text");

        textureUnitsVSField.setEditable(false);
        textureUnitsVSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsVSField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsVSField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxVertexTextureImageUnits}", textureUnitsVSField, "text");

        lightsField.setEditable(false);
        lightsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lightsField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.lightsField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxLights}", lightsField, "text");

        anisotropicFilteringField.setEditable(false);
        anisotropicFilteringField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anisotropicFilteringField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.anisotropicFilteringField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxAnisotropy}", anisotropicFilteringField, "text");

        fsaaField.setEditable(false);
        fsaaField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fsaaField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.fsaaField.text")); // NOI18N

        textureUnitsFSField.setEditable(false);
        textureUnitsFSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsFSField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsFSField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxTextureImageUnits}", textureUnitsFSField, "text");

        textureUnitsGSField.setEditable(false);
        textureUnitsGSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsGSField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsGSField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxGeometryTextureImageUnits}", textureUnitsGSField, "text");

        renderBuffersField.setEditable(false);
        renderBuffersField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        renderBuffersField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderBuffersField.text")); // NOI18N

        bindingContext.addBinding(model, "${maxDrawBuffers}", renderBuffersField, "text");

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel1.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel4.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel5.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel6.text")); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel7.text")); // NOI18N

        jLabel8.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel8.text")); // NOI18N

        jLabel9.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel9.text")); // NOI18N

        jLabel10.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel10.text")); // NOI18N

        jLabel11.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jLabel11.text")); // NOI18N

        javax.swing.GroupLayout overviewCapsPanelLayout = new javax.swing.GroupLayout(overviewCapsPanel);
        overviewCapsPanel.setLayout(overviewCapsPanelLayout);
        overviewCapsPanelLayout.setHorizontalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addGap(5, 5, 5)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(viewportField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(textureSizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(textureUnitsField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(lightsField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(fsaaField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewCapsPanelLayout.setVerticalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(viewportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(textureSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(fsaaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(textureUnitsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout overviewPanelLayout = new javax.swing.GroupLayout(overviewPanel);
        overviewPanel.setLayout(overviewPanelLayout);
        overviewPanelLayout.setHorizontalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(overviewCapsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(basicCapsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewPanelLayout.setVerticalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basicCapsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overviewCapsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewPanel.TabConstraints.tabTitle"), overviewPanel); // NOI18N

        capabilitiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        capabilitiesTable.setFillsViewportHeight(true);
        capabilitiesScrollPane.setViewportView(capabilitiesTable);

        findLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.findLabel.text")); // NOI18N

        capabilitiesSearchField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.capabilitiesSearchField.text")); // NOI18N

        javax.swing.GroupLayout capabilitiesPanelLayout = new javax.swing.GroupLayout(capabilitiesPanel);
        capabilitiesPanel.setLayout(capabilitiesPanelLayout);
        capabilitiesPanelLayout.setHorizontalGroup(
            capabilitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, capabilitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(capabilitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(capabilitiesScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                    .addGroup(capabilitiesPanelLayout.createSequentialGroup()
                        .addComponent(findLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(capabilitiesSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)))
                .addContainerGap())
        );
        capabilitiesPanelLayout.setVerticalGroup(
            capabilitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capabilitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(capabilitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(findLabel)
                    .addComponent(capabilitiesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(capabilitiesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.capabilitiesPanel.TabConstraints.tabTitle"), capabilitiesPanel); // NOI18N

        extensionsSearchField.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.extensionsSearchField.text")); // NOI18N

        findLabel1.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.findLabel1.text")); // NOI18N

        extensionsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        extensionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        extensionsTable.setFillsViewportHeight(true);
        extensionsTable.setTableHeader(null);
        extensionsScrollPane.setViewportView(extensionsTable);

        javax.swing.GroupLayout extensionsPanelLayout = new javax.swing.GroupLayout(extensionsPanel);
        extensionsPanel.setLayout(extensionsPanelLayout);
        extensionsPanelLayout.setHorizontalGroup(
            extensionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extensionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(extensionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(extensionsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                    .addGroup(extensionsPanelLayout.createSequentialGroup()
                        .addComponent(findLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extensionsSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)))
                .addContainerGap())
        );
        extensionsPanelLayout.setVerticalGroup(
            extensionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extensionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(extensionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(findLabel1)
                    .addComponent(extensionsSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(extensionsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.extensionsPanel.TabConstraints.tabTitle"), extensionsPanel); // NOI18N

        javax.swing.GroupLayout displayPanelLayout = new javax.swing.GroupLayout(displayPanel);
        displayPanel.setLayout(displayPanelLayout);
        displayPanelLayout.setHorizontalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
        );
        displayPanelLayout.setVerticalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.displayPanel.TabConstraints.tabTitle"), displayPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingContext.bind();
    }// </editor-fold>//GEN-END:initComponents
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anisotropicFilteringField;
    private javax.swing.JTextField capabilitiesSearchField;
    private javax.swing.JTable capabilitiesTable;
    private javax.swing.JTextField extensionsSearchField;
    private javax.swing.JTable extensionsTable;
    private javax.swing.JTextField fsaaField;
    private javax.swing.JTextField glField;
    private javax.swing.JTextField glslField;
    private javax.swing.JTextField joglField;
    private javax.swing.JTextField lightsField;
    private net.java.nboglpack.glcapabilities.GLCapabilitiesModel model;
    private javax.swing.JTextField renderBuffersField;
    private javax.swing.JTextField rendererField;
    private javax.swing.JTextField textureSizeField;
    private javax.swing.JTextField textureUnitsFSField;
    private javax.swing.JTextField textureUnitsField;
    private javax.swing.JTextField textureUnitsGSField;
    private javax.swing.JTextField textureUnitsVSField;
    private javax.swing.JTextField vendorField;
    private javax.swing.JTextField viewportField;
    private javax.beans.binding.BindingContext bindingContext;
    // End of variables declaration//GEN-END:variables
    
}
