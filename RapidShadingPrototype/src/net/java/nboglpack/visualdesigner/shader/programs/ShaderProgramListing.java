/*
 * ShaderProgramListing.java
 *
 * Created on April 20, 2007, 7:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.util.ArrayList;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.tools.LineLayout;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderProgramListing extends JPanel {
    
    private IShaderProgramCollection collection;
    private ArrayList<ShaderProgramCreationPanel> factories;
    
    /**
     * Creates a new instance of ShaderProgramListing
     */
    public ShaderProgramListing(IShaderProgramCollection collection) {
        this.collection = collection;
        initComponents();
        loadShaderPrograms();
    }

    private void initComponents() {
        this.setLayout(new LineLayout());
        this.setName(this.collection.getCollectionName());
        setBorder(javax.swing.BorderFactory.createTitledBorder(this.collection.getCollectionName()));
    }

    private void loadShaderPrograms() {
        Class[] spFactoryClasses = this.collection.getShaderProgramFactoryClasses();
        if (spFactoryClasses == null) return;
        
        factories = new ArrayList<ShaderProgramCreationPanel>();
        for (Class spFactoryClass : spFactoryClasses) {
            try {
                createSPCreationPanel((IShaderProgramFactory) spFactoryClass.newInstance());
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createSPCreationPanel(IShaderProgramFactory shaderProgramFactory) {
        ShaderProgramCreationPanel panel = new ShaderProgramCreationPanel(shaderProgramFactory);
        factories.add(panel);
        this.add(panel);
    }
    
}
