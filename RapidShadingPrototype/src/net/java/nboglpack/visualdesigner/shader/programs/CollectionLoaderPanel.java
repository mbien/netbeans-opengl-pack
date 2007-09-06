/*
 * CollectionLoaderPanel.java
 *
 * Created on April 20, 2007, 6:40 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.TreeMap;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.tools.LineLayout;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 *
 * @author Samuel Sperling
 */
public class CollectionLoaderPanel extends JPanel {
    
    private Settings settings;
    private Settings shaderProgramListSettings;
    private TreeMap<String, ShaderProgramListing> allShaderCollections;
    private TreeMap<String, ShaderProgramListing> shaderCollectionNames;
    
    /**
     * Creates a new instance of CollectionLoaderPanel
     */
    public CollectionLoaderPanel() {
        initComponents();
    }

    public void loadSettings() {
//        shaderProgramListSettings = Editor.mainSettings.getChildSettings("ShaderProgramList");
//        settings = Editor.mainSettings.getChildSettings("ShaderCollectionPanel");
//        Settings[] shaderProgramCollections = shaderProgramListSettings.getChildrenSettings("ShaderProgramCollection");
//        if (shaderProgramCollections == null) {
//            loadShaderProgramFactories(shaderProgramListSettings.getValueStringArray("ShaderProgramCollection"
//                    , new String[] {
//                        "net.java.nboglpack.visualdesigner.shader.programs.glsl.GLSLFunctions",
//                        "net.java.nboglpack.visualdesigner.shader.programs.UtilShaderPrograms",
//                        "net.java.nboglpack.visualdesigner.shader.programs.SimpleGeneratorsCollection",
//                        "net.java.nboglpack.visualdesigner.shader.programs.LightingShaderPrograms",
//                        "net.java.nboglpack.visualdesigner.shader.programs.ProceduralTexturingShaderPrograms"
//                    }));
//        } else {
//            loadShaderProgramFactories(shaderProgramCollections);
//        }
//        isLoaded = true;
//        String selectedCollection = /*settings.getValueString("selectedCollection", */"GLSL Functions";//);
//        collectionChooser.setSelectedItem(selectedCollection);
//        // in case it was already selected the event is not called and it needs to be made visible manually
//        shaderCollectionNames.get(selectedCollection);
    }
    
    private boolean isLoaded = false;
    private JComboBox collectionChooser;
    private void initComponents() {
        this.setLayout(new LineLayout());
//        this.setLayout(new GridLayout(0, 1));
        collectionChooser = new JComboBox();
        this.add(collectionChooser);
        collectionChooser.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == e.DESELECTED) {
                        shaderCollectionNames.get(e.getItem()).setVisible(false);
                    } else if (e.getStateChange() == e.SELECTED) {
//                        if (isLoaded) settings.setValue("selectedCollection", (String) collectionChooser.getSelectedItem());
                        shaderCollectionNames.get(e.getItem()).setVisible(true);
                    }
                }
        });
    }
    
    public void loadShaderProgramFactories(String[] spFactories) {
        if (allShaderCollections == null) {
            allShaderCollections = new TreeMap<String, ShaderProgramListing>();
            shaderCollectionNames = new TreeMap<String, ShaderProgramListing>();
        }
        Class spCollectionClass = null;
        
        for (String spCollectionClassName : spFactories) {
            try {
                spCollectionClass = Class.forName(spCollectionClassName);
                createListing(spCollectionClassName, (IShaderProgramCollection) spCollectionClass.newInstance());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }
    }

//    private void loadShaderProgramFactories(Settings[] shaderProgramCollections) {
//        if (allShaderCollections == null) {
//            allShaderCollections = new TreeMap<String, ShaderProgramListing>();
//            shaderCollectionNames = new TreeMap<String, ShaderProgramListing>();
//        }
//        for (Settings factory : shaderProgramCollections) {
//            Class spCollectionClass;
//            String jar = factory.getAttributeValue("jar");
//            String spCollectionClassName = factory.getValue();
//            try {
//                if (jar == null || jar.length() == 0) {
//                    spCollectionClass = Class.forName(spCollectionClassName);
//                } else {
//                    spCollectionClass = Editor.findMe(this).getPluginClass(jar, spCollectionClassName);
//                }
//                createListing(spCollectionClassName, (IShaderProgramCollection) spCollectionClass.newInstance());
//            } catch (ClassNotFoundException ex) {
//                ex.printStackTrace();
//            } catch (IllegalAccessException ex) {
//                ex.printStackTrace();
//            } catch (InstantiationException ex) {
//                ex.printStackTrace();
//            } catch (ClassCastException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

    private void createListing(String spCollectionClassName, IShaderProgramCollection shaderProgramCollection) {
        if (allShaderCollections.containsKey(spCollectionClassName)) return;
        
        ShaderProgramListing listing = new ShaderProgramListing(shaderProgramCollection);
        allShaderCollections.put(spCollectionClassName, listing);
        
        // Add as component
        this.add(listing);
        listing.setVisible(false);
        Rectangle bounds = listing.getBounds();
        bounds.width = this.getWidth();
        listing.setBounds(bounds);
        
        // Add to ComboBox
        shaderCollectionNames.put(listing.getName(), listing);
        collectionChooser.addItem(listing.getName());
    }
}