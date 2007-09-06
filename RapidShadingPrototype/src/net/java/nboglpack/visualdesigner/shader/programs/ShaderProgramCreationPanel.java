/*
 * ShaderProgramCreationPanel.java
 *
 * Created on April 24, 2007, 3:12 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderProgramCreationPanel extends JPanel {
    
    private IShaderProgramFactory shaderProgramFactory;
    private JComboBox cBoxVariants;
    private JLabel lblName;
    private JButton butNew;
    private Settings styleSettings;
    private Color backgroundColor = new Color(100, 100, 100, 255);
    private Color foregroundColor = new Color(255, 255, 255, 255);
    private Color shadowColor = new Color(0, 0, 0, 50);
    private Editor editor;
    
    /** Creates a new instance of ShaderProgramCreationPanel */
    public ShaderProgramCreationPanel(IShaderProgramFactory shaderProgramFactory) {
        this.shaderProgramFactory = shaderProgramFactory;
        loadSettings();
        initComponents();
    }

    private void loadSettings() {
//        styleSettings = Editor.mainSettings.getChildSettingsByPath("Nodes/Style");
        
//        if (shaderProgramFactory.getNodeBackgroundColor() == null)
//            backgroundColor = styleSettings.getValueColor("BackgroundColor", backgroundColor);
//        else
//            backgroundColor = shaderProgramFactory.getNodeBackgroundColor();
//        foregroundColor = styleSettings.getValueColor("ForegroundColor", foregroundColor);
//        shadowColor = styleSettings.getValueColor("ShadowColor", shadowColor);
    }

    private void initComponents() {
        
        setBackground(backgroundColor);
//        setLayout(null);
        setOpaque(false);
        
        lblName = new JLabel();
        lblName.setText(this.shaderProgramFactory.getName());
        lblName.setForeground(foregroundColor);
        add(lblName);
        
        String[] variants = this.shaderProgramFactory.getVariants();
        if (variants != null) {
            cBoxVariants = new JComboBox(variants);
            add(cBoxVariants);
        }
        
        butNew = new JButton("NEW");
        butNew.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                createDragableNode();
            }
            public void mouseReleased(MouseEvent e) {
            }
        });
        add(butNew);
    }
    
    private Editor getEditor() {
        if (this.editor == null)
            this.editor = Editor.findMe(this);
        return this.editor;
    }
    
    private void createDragableNode() {
        if (getEditor() == null) return;
        
        if (cBoxVariants == null)
            getEditor().createDragableNode(this.shaderProgramFactory.createShaderProgram());
        else
            getEditor().createDragableNode(this.shaderProgramFactory.createShaderProgram((String) cBoxVariants.getSelectedItem()));
    }
    
    public void paint(Graphics g) {
        Rectangle bounds = this.getBounds();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(this.getBackground());
        g.fillRoundRect(0, 0, bounds.width, bounds.height, 8, 8);
        
        super.paint(g);
    }
    
}
