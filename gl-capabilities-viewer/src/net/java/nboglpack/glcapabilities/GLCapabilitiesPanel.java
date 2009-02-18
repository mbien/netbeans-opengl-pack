/*
 * GLCapabilityPanel.java
 *
 * Created on 19. Juni 2007, 18:51
 */
package net.java.nboglpack.glcapabilities;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
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
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
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
        tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ, capabilitiesModel.getExtensions(), extensionsJPanel.getTable());
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
        
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>)extensionsJPanel.getTable().getRowSorter();
        sorter.setModel(extensionsJPanel.getTable().getModel());
        
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
        bindingGroup = new BindingGroup();

        capabilitiesModel = new GLCapabilitiesModel();
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel overviewPanel = new JPanel();
        JPanel basicCapsPanel = new JPanel();
        glField = new JTextField();
        glslField = new JTextField();
        joglField = new JTextField();
        rendererField = new JTextField();
        vendorField = new JTextField();
        JLabel gl = new JLabel();
        JLabel glsl = new JLabel();
        JLabel jogl = new JLabel();
        JLabel renderer = new JLabel();
        JLabel vendor = new JLabel();
        JPanel wrapperPanel = new JPanel();
        gLCanvas = createGLDemoCanvas();

        JPanel overviewCapsPanel = new JPanel();
        viewportField = new JTextField();
        textureSizeField = new JTextField();
        textureUnitsField = new JTextField();
        textureUnitsVSField = new JTextField();
        lightsField = new JTextField();
        anisotropicFilteringField = new JTextField();
        fsaaField = new JTextField();
        textureUnitsFSField = new JTextField();
        textureUnitsGSField = new JTextField();
        renderBuffersField = new JTextField();
        JLabel viewportSizeLabel = new JLabel();
        JLabel textureSizeLabel = new JLabel();
        JLabel textureUnitsLabel = new JLabel();
        JLabel textureUnitsVSLabel = new JLabel();
        JLabel dynamicLightsLabel = new JLabel();
        JLabel anisotropicFilteringLabel = new JLabel();
        JLabel fsaaSamplesLabel = new JLabel();
        JLabel textureUnitsFSLabel = new JLabel();
        JLabel textureUnitsGSLabel = new JLabel();
        JLabel renderBuffersLabel = new JLabel();
        capabilitiesJPanel = new FilteredTable();
        extensionsJPanel = new FilteredTable();
        displayModesJPanel = new DisplayModesPanel();

        basicCapsPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.basicCapsPanel.border.title"))); // NOI18N
        glField.setEditable(false);
        glField.setHorizontalAlignment(JTextField.CENTER);

        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${glVersion}"), glField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        glslField.setEditable(false);
        glslField.setHorizontalAlignment(JTextField.CENTER);

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${glslVersion}"), glslField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        joglField.setEditable(false);
        joglField.setHorizontalAlignment(JTextField.CENTER);

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${implVersion}"), joglField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        rendererField.setEditable(false);
        rendererField.setHorizontalAlignment(JTextField.CENTER);

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${renderer}"), rendererField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        vendorField.setEditable(false);
        vendorField.setHorizontalAlignment(JTextField.CENTER);

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${vendor}"), vendorField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gl.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.gl.text")); // NOI18N
        glsl.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glsl.text")); // NOI18N
        jogl.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jogl.text")); // NOI18N
        renderer.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderer.text")); // NOI18N
        vendor.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.vendor.text")); // NOI18N
        GroupLayout wrapperPanelLayout = new GroupLayout(wrapperPanel);
        wrapperPanel.setLayout(wrapperPanelLayout);
        wrapperPanelLayout.setHorizontalGroup(
            wrapperPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(gLCanvas, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
        );
        wrapperPanelLayout.setVerticalGroup(
            wrapperPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(gLCanvas, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
        );

        GroupLayout basicCapsPanelLayout = new GroupLayout(basicCapsPanel);
        basicCapsPanel.setLayout(basicCapsPanelLayout);

        basicCapsPanelLayout.setHorizontalGroup(
            basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(vendor)
                            .addComponent(renderer))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                                .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(glslField, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                    .addComponent(joglField, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                    .addComponent(glField, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(wrapperPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(vendorField, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                            .addComponent(rendererField, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)))
                    .addComponent(jogl)
                    .addComponent(glsl)
                    .addComponent(gl))
                .addContainerGap())
        );
        basicCapsPanelLayout.setVerticalGroup(
            basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(gl)
                            .addComponent(glField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(glsl)
                            .addComponent(glslField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(jogl)
                            .addComponent(joglField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addComponent(wrapperPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(renderer)
                    .addComponent(rendererField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(vendor)
                    .addComponent(vendorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        overviewCapsPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewCapsPanel.border.title"))); // NOI18N
        viewportField.setEditable(false);
        viewportField.setHorizontalAlignment(JTextField.CENTER);
        viewportField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxViewPortSize}"), viewportField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureSizeField.setEditable(false);
        textureSizeField.setHorizontalAlignment(JTextField.CENTER);
        textureSizeField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxTextureSize}"), textureSizeField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsField.setEditable(false);
        textureUnitsField.setHorizontalAlignment(JTextField.CENTER);
        textureUnitsField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxTextureUnits}"), textureUnitsField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsVSField.setEditable(false);
        textureUnitsVSField.setHorizontalAlignment(JTextField.CENTER);
        textureUnitsVSField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxVertexTextureImageUnits}"), textureUnitsVSField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lightsField.setEditable(false);
        lightsField.setHorizontalAlignment(JTextField.CENTER);
        lightsField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxLights}"), lightsField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        anisotropicFilteringField.setEditable(false);
        anisotropicFilteringField.setHorizontalAlignment(JTextField.CENTER);
        anisotropicFilteringField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxAnisotropy}"), anisotropicFilteringField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        fsaaField.setEditable(false);
        fsaaField.setHorizontalAlignment(JTextField.CENTER);
        fsaaField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxSampleBuffers}"), fsaaField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsFSField.setEditable(false);
        textureUnitsFSField.setHorizontalAlignment(JTextField.CENTER);
        textureUnitsFSField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxTextureImageUnits}"), textureUnitsFSField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        textureUnitsGSField.setEditable(false);
        textureUnitsGSField.setHorizontalAlignment(JTextField.CENTER);
        textureUnitsGSField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxGeometryTextureImageUnits}"), textureUnitsGSField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        renderBuffersField.setEditable(false);
        renderBuffersField.setHorizontalAlignment(JTextField.CENTER);
        renderBuffersField.setMinimumSize(new Dimension(75, 0));

        binding = Bindings.createAutoBinding(UpdateStrategy.READ, capabilitiesModel, ELProperty.create("${maxDrawBuffers}"), renderBuffersField, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        viewportSizeLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.viewportSizeLabel.text")); // NOI18N
        textureSizeLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureSizeLabel.text")); // NOI18N
        textureUnitsLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsLabel.text")); // NOI18N
        textureUnitsVSLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsVSLabel.text")); // NOI18N
        dynamicLightsLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.dynamicLightsLabel.text")); // NOI18N
        anisotropicFilteringLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.anisotropicFilteringLabel.text")); // NOI18N
        fsaaSamplesLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.fsaaSamplesLabel.text")); // NOI18N
        textureUnitsFSLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsFSLabel.text")); // NOI18N
        textureUnitsGSLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsGSLabel.text")); // NOI18N
        renderBuffersLabel.setText(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderBuffersLabel.text")); // NOI18N
        GroupLayout overviewCapsPanelLayout = new GroupLayout(overviewCapsPanel);
        overviewCapsPanel.setLayout(overviewCapsPanelLayout);
        overviewCapsPanelLayout.setHorizontalGroup(
            overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(textureSizeLabel)
                    .addComponent(viewportSizeLabel)
                    .addComponent(textureUnitsLabel)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(dynamicLightsLabel))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.CENTER)
                    .addComponent(lightsField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(viewportField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureSizeField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsVSField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING, false)
                    .addComponent(anisotropicFilteringLabel)
                    .addComponent(fsaaSamplesLabel)
                    .addComponent(textureUnitsFSLabel)
                    .addComponent(textureUnitsGSLabel)
                    .addComponent(renderBuffersLabel))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.CENTER)
                    .addComponent(anisotropicFilteringField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(fsaaField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsFSField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(textureUnitsGSField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(renderBuffersField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewCapsPanelLayout.setVerticalGroup(
            overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(viewportSizeLabel)
                        .addComponent(viewportField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(anisotropicFilteringLabel))
                    .addComponent(anisotropicFilteringField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textureSizeLabel)
                        .addComponent(textureSizeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(fsaaSamplesLabel))
                    .addComponent(fsaaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textureUnitsLabel)
                        .addComponent(textureUnitsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textureUnitsFSLabel))
                    .addComponent(textureUnitsFSField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(textureUnitsGSField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsVSField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsGSLabel))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(dynamicLightsLabel)
                    .addComponent(renderBuffersField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(renderBuffersLabel))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout overviewPanelLayout = new GroupLayout(overviewPanel);
        overviewPanel.setLayout(overviewPanelLayout);

        overviewPanelLayout.setHorizontalGroup(
            overviewPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewPanelLayout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(overviewCapsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(basicCapsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewPanelLayout.setVerticalGroup(
            overviewPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basicCapsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(overviewCapsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewPanel.TabConstraints.tabTitle"), overviewPanel); // NOI18N
        tabbedPane.addTab(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.capabilitiesJPanel.TabConstraints.tabTitle"), capabilitiesJPanel); // NOI18N
        tabbedPane.addTab(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.extensionsJPanel.TabConstraints.tabTitle"), extensionsJPanel); // NOI18N
        tabbedPane.addTab(NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.displayModesJPanel.TabConstraints.tabTitle"), displayModesJPanel); // NOI18N
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField anisotropicFilteringField;
    private FilteredTable capabilitiesJPanel;
    private GLCapabilitiesModel capabilitiesModel;
    private DisplayModesPanel displayModesJPanel;
    private FilteredTable extensionsJPanel;
    private JTextField fsaaField;
    private GLCanvas gLCanvas;
    private JTextField glField;
    private JTextField glslField;
    private JTextField joglField;
    private JTextField lightsField;
    private JTextField renderBuffersField;
    private JTextField rendererField;
    private JTextField textureSizeField;
    private JTextField textureUnitsFSField;
    private JTextField textureUnitsField;
    private JTextField textureUnitsGSField;
    private JTextField textureUnitsVSField;
    private JTextField vendorField;
    private JTextField viewportField;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    
}
