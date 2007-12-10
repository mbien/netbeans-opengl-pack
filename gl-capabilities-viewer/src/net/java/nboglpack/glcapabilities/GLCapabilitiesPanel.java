/*
 * GLCapabilityPanel.java
 *
 * Created on 19. Juni 2007, 18:51
 */
package net.java.nboglpack.glcapabilities;

import net.java.nboglpack.glcapabilities.demo.JOGLGearsDemo;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Comparator;
import java.util.ResourceBundle;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.swing.JPanel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.openide.util.NbBundle;

/**
 *
 * @author Michael Bien
 */
public class GLCapabilitiesPanel extends JPanel {
    
   
    /** Creates new form GLCapabilityPanel */
    @SuppressWarnings("unchecked")
    public GLCapabilitiesPanel() {
        
        initComponents();
        
        ResourceBundle bundle = NbBundle.getBundle(GLCapabilitiesPanel.class);
        // bind capabilities table to model
        JTableBinding tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ, capabilitiesModel.getCapabilities(), capabilitiesJPanel.getTable());
        tableBinding.setEditable(false);
        
        tableBinding.addColumnBinding(BeanProperty.create("name")).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.Name"));
        tableBinding.addColumnBinding(BeanProperty.create("value")).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.Value"));
        
        bindingGroup.addBinding(tableBinding);
        
        
        // bind extensions table to model
        tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ, capabilitiesModel.getExtensions(), extentionsJPanel.getTable());
        tableBinding.setEditable(false);
        tableBinding.addColumnBinding(ObjectProperty.create()).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.Name"));
        
        bindingGroup.addBinding(tableBinding);
        
        
        // bind display modes table
        tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ, capabilitiesModel.getDisplayModes(), displayModesJPanel.getTable());
        tableBinding.setEditable(false);
        
        tableBinding.addColumnBinding(BeanProperty.create("hardwareAccelerated")).setColumnClass(Boolean.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.HWAccel"));
        tableBinding.addColumnBinding(BeanProperty.create("doubleBuffered")).setColumnClass(Boolean.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.DoubleBuff"));
        tableBinding.addColumnBinding(BeanProperty.create("stereo")).setColumnClass(Boolean.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.Stereo"));
        tableBinding.addColumnBinding(BeanProperty.create("sampleBuffers")).setColumnClass(Boolean.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.sampleBuffers"));
        
        tableBinding.addColumnBinding(BeanProperty.create("numSamples")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.NumSamples"));
        tableBinding.addColumnBinding(BeanProperty.create("depthBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.Depth_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("redBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.R_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("greenBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.G_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("blueBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.B_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("alphaBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.A_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("accumRedBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.AccumR_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("accumGreenBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.AccumG_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("accumBlueBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.AccumB_bits"));
        tableBinding.addColumnBinding(BeanProperty.create("accumAlphaBits")).setColumnClass(Integer.class).setColumnName(bundle.getString("GLCapabilitiesPanel.tablecolumn.AccumA_bits"));
        
        bindingGroup.addBinding(tableBinding);
        
        
        final JOGLGearsDemo demo = new JOGLGearsDemo(gLCanvas);
        
        gLCanvas.addHierarchyListener(new HierarchyListener() {

            public void hierarchyChanged(HierarchyEvent e) {
                if(e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED) {
                    if(e.getComponent().isShowing())
                        demo.start();
                    else
                        demo.stop();
                }
            }
            
        });
         
    }
    
    @SuppressWarnings("unchecked")
    public void updateFromModel() {
        
        bindingGroup.unbind();
        bindingGroup.bind();
        
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>)extentionsJPanel.getTable().getRowSorter();
        sorter.setModel(extentionsJPanel.getTable().getModel());
        
        sorter = (TableRowSorter)displayModesJPanel.getTable().getRowSorter();
        sorter.setModel(displayModesJPanel.getTable().getModel());
        
        sorter = (TableRowSorter)capabilitiesJPanel.getTable().getRowSorter();
        sorter.setModel(capabilitiesJPanel.getTable().getModel());

        
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
        capabilitiesJPanel.getTable().getColumnModel().getColumn(1).setMaxWidth(80);
        
    }

    
    private GLCanvas createGLDemoCanvas() {
        
        GLCapabilities caps = new GLCapabilities();
        caps.setNumSamples(4);
        caps.setSampleBuffers(true);
        
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser(){

            @Override
            public int chooseCapabilities(GLCapabilities desired, GLCapabilities[] available, int arg2) {
                int max = 0;
                for (GLCapabilities elem : available) {
                    if(elem == null)
                        continue;
                    capabilitiesModel.getDisplayModes().add(elem);
                    if(elem.getHardwareAccelerated() && max < elem.getNumSamples())
                        max = elem.getNumSamples();
                }
                capabilitiesModel.setMaxSampleBuffers(max+"x");
//                updateFromModel();
                
                return super.chooseCapabilities(desired, available, arg2);
            }
        };
        
        
        return new GLCanvas(caps, chooser, null, device);
    }
    
    
    public GLCapabilitiesModel getModel() {
        return capabilitiesModel;
    }
    void setModel(GLCapabilitiesModel model) {
        this.capabilitiesModel = model;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        capabilitiesModel = new net.java.nboglpack.glcapabilities.GLCapabilitiesModel();
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        javax.swing.JPanel overviewPanel = new javax.swing.JPanel();
        javax.swing.JPanel basicCapsPanel = new javax.swing.JPanel();
        glField = new javax.swing.JTextField();
        glslField = new javax.swing.JTextField();
        joglField = new javax.swing.JTextField();
        rendererField = new javax.swing.JTextField();
        vendorField = new javax.swing.JTextField();
        javax.swing.JLabel gl = new javax.swing.JLabel();
        javax.swing.JLabel glsl = new javax.swing.JLabel();
        javax.swing.JLabel jogl = new javax.swing.JLabel();
        javax.swing.JLabel renderer = new javax.swing.JLabel();
        javax.swing.JLabel vendor = new javax.swing.JLabel();
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel();
        gLCanvas = createGLDemoCanvas();
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
        javax.swing.JLabel viewportSizeLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureSizeLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsVSLabel = new javax.swing.JLabel();
        javax.swing.JLabel dynamicLightsLabel = new javax.swing.JLabel();
        javax.swing.JLabel anisotropicFilteringLabel = new javax.swing.JLabel();
        javax.swing.JLabel fsaaSamplesLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsFSLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsGSLabel = new javax.swing.JLabel();
        javax.swing.JLabel renderBuffersLabel = new javax.swing.JLabel();
        capabilitiesJPanel = new net.java.nboglpack.glcapabilities.FilteredTable();
        extentionsJPanel = new net.java.nboglpack.glcapabilities.FilteredTable();
        displayModesJPanel = new net.java.nboglpack.glcapabilities.DisplayModesPanel();

        basicCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.basicCapsPanel.border.title"))); // NOI18N

        glField.setEditable(false);
        glField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${glVersion}"), glField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        glslField.setEditable(false);
        glslField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${glslVersion}"), glslField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        joglField.setEditable(false);
        joglField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${implVersion}"), joglField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        rendererField.setEditable(false);
        rendererField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${renderer}"), rendererField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        vendorField.setEditable(false);
        vendorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${vendor}"), vendorField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.gl.text")); // NOI18N

        glsl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glsl.text")); // NOI18N

        jogl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jogl.text")); // NOI18N

        renderer.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderer.text")); // NOI18N

        vendor.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.vendor.text")); // NOI18N

        javax.swing.GroupLayout wrapperPanelLayout = new javax.swing.GroupLayout(wrapperPanel);
        wrapperPanel.setLayout(wrapperPanelLayout);
        wrapperPanelLayout.setHorizontalGroup(
            wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gLCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
        );
        wrapperPanelLayout.setVerticalGroup(
            wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gLCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout basicCapsPanelLayout = new javax.swing.GroupLayout(basicCapsPanel);
        basicCapsPanel.setLayout(basicCapsPanelLayout);
        basicCapsPanelLayout.setHorizontalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vendor)
                            .addComponent(renderer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(glslField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(joglField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(glField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(wrapperPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(vendorField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(rendererField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)))
                    .addComponent(jogl)
                    .addComponent(glsl)
                    .addComponent(gl))
                .addContainerGap())
        );
        basicCapsPanelLayout.setVerticalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gl)
                            .addComponent(glField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(glsl)
                            .addComponent(glslField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jogl)
                            .addComponent(joglField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(wrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renderer)
                    .addComponent(rendererField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendor)
                    .addComponent(vendorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        overviewCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewCapsPanel.border.title"))); // NOI18N

        viewportField.setEditable(false);
        viewportField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        viewportField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxViewPortSize}"), viewportField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureSizeField.setEditable(false);
        textureSizeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureSizeField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxTextureSize}"), textureSizeField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsField.setEditable(false);
        textureUnitsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxTextureUnits}"), textureUnitsField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsVSField.setEditable(false);
        textureUnitsVSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsVSField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxVertexTextureImageUnits}"), textureUnitsVSField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lightsField.setEditable(false);
        lightsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lightsField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxLights}"), lightsField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        anisotropicFilteringField.setEditable(false);
        anisotropicFilteringField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anisotropicFilteringField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxAnisotropy}"), anisotropicFilteringField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        fsaaField.setEditable(false);
        fsaaField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fsaaField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxSampleBuffers}"), fsaaField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsFSField.setEditable(false);
        textureUnitsFSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsFSField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxTextureImageUnits}"), textureUnitsFSField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsGSField.setEditable(false);
        textureUnitsGSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textureUnitsGSField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxGeometryTextureImageUnits}"), textureUnitsGSField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        renderBuffersField.setEditable(false);
        renderBuffersField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        renderBuffersField.setMinimumSize(new java.awt.Dimension(75, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, capabilitiesModel, org.jdesktop.beansbinding.ELProperty.create("${maxDrawBuffers}"), renderBuffersField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        viewportSizeLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.viewportSizeLabel.text")); // NOI18N

        textureSizeLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureSizeLabel.text")); // NOI18N

        textureUnitsLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsLabel.text")); // NOI18N

        textureUnitsVSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsVSLabel.text")); // NOI18N

        dynamicLightsLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.dynamicLightsLabel.text")); // NOI18N

        anisotropicFilteringLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.anisotropicFilteringLabel.text")); // NOI18N

        fsaaSamplesLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.fsaaSamplesLabel.text")); // NOI18N

        textureUnitsFSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsFSLabel.text")); // NOI18N

        textureUnitsGSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsGSLabel.text")); // NOI18N

        renderBuffersLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderBuffersLabel.text")); // NOI18N

        javax.swing.GroupLayout overviewCapsPanelLayout = new javax.swing.GroupLayout(overviewCapsPanel);
        overviewCapsPanel.setLayout(overviewCapsPanelLayout);
        overviewCapsPanelLayout.setHorizontalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textureSizeLabel)
                    .addComponent(viewportSizeLabel)
                    .addComponent(textureUnitsLabel)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(dynamicLightsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lightsField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(viewportField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureSizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(anisotropicFilteringLabel)
                    .addComponent(fsaaSamplesLabel)
                    .addComponent(textureUnitsFSLabel)
                    .addComponent(textureUnitsGSLabel)
                    .addComponent(renderBuffersLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(fsaaField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewCapsPanelLayout.setVerticalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewportSizeLabel)
                        .addComponent(viewportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(anisotropicFilteringLabel))
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textureSizeLabel)
                        .addComponent(textureSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fsaaSamplesLabel))
                    .addComponent(fsaaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textureUnitsLabel)
                        .addComponent(textureUnitsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textureUnitsFSLabel))
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsGSLabel))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dynamicLightsLabel)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renderBuffersLabel))
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
                .addComponent(basicCapsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overviewCapsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewPanel.TabConstraints.tabTitle"), overviewPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.capabilitiesJPanel.TabConstraints.tabTitle"), capabilitiesJPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.extentionsJPanel.TabConstraints.tabTitle"), extentionsJPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.displayModesJPanel.TabConstraints.tabTitle"), displayModesJPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anisotropicFilteringField;
    private net.java.nboglpack.glcapabilities.FilteredTable capabilitiesJPanel;
    private net.java.nboglpack.glcapabilities.GLCapabilitiesModel capabilitiesModel;
    private net.java.nboglpack.glcapabilities.DisplayModesPanel displayModesJPanel;
    private net.java.nboglpack.glcapabilities.FilteredTable extentionsJPanel;
    private javax.swing.JTextField fsaaField;
    private javax.media.opengl.GLCanvas gLCanvas;
    private javax.swing.JTextField glField;
    private javax.swing.JTextField glslField;
    private javax.swing.JTextField joglField;
    private javax.swing.JTextField lightsField;
    private javax.swing.JTextField renderBuffersField;
    private javax.swing.JTextField rendererField;
    private javax.swing.JTextField textureSizeField;
    private javax.swing.JTextField textureUnitsFSField;
    private javax.swing.JTextField textureUnitsField;
    private javax.swing.JTextField textureUnitsGSField;
    private javax.swing.JTextField textureUnitsVSField;
    private javax.swing.JTextField vendorField;
    private javax.swing.JTextField viewportField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    
}
