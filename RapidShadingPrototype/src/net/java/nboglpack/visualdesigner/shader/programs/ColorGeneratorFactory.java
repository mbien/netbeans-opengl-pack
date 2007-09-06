/*
 * ColorGeneratorFactory.java
 *
 * Created on June 13, 2007, 6:38 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.PersistanceException;
import net.java.nboglpack.visualdesigner.ProjectPersistor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class ColorGeneratorFactory implements IShaderProgramFactory {
    
    /**
     * Creates a new instance of ColorGeneratorFactory
     */
    public ColorGeneratorFactory() {
    }
    
    public String[] getVariants() {
        return null;
    }
    
    public IShaderProgram createShaderProgram() {
        return new ColorGenerator();
    }
    
    public IShaderProgram createShaderProgram(String variant) {
        return new ColorGenerator();
    }
    
    public String getName() {
        return "Color Generator";
    }
    
    public Color getNodeBackgroundColor() {
        return SimpleGeneratorsCollection.nodeBackgroundColor;
    }
}

/**
 *
 * @author Samuel Sperling
 */
class ColorGenerator extends ShaderNode implements IShaderProgram {
    
    private JColorChooser colorChooser;
    
    public ColorGenerator() {
        super("Color Generator");
        super.setShaderProgram(this);
        setBackground(SimpleGeneratorsCollection.nodeBackgroundColor);
        
        setUpVariables();
    }
    
    protected void initComponents() {
        super.initComponents();        

        colorPlate = new JPanel(null);
        colorPlate.setSize(32, 32);
        colorPlate.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        colorPlate.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() < 2) {
                    e.setSource(getShaderNode());
                    processMouseEvent(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2)
                    openColorDialog();
                else {
                    e.setSource(getShaderNode());
                    processMouseEvent(e);
                }
                //TODO: pass event on to the parent
            }
        });
        getNodeContent().setLayout(new BorderLayout()); //new FlowLayout(FlowLayout.TRAILING)
        getNodeContent().add(colorPlate);
        getNodeContent().setPreferredSize(colorPlate.getSize());
        setChosenColor(Color.BLACK);
        adjustPreferedSize();
    }
    
    private JPanel colorPlate;
    private Color chosenColor;

    public void setChosenColor(Color chosenColor) {
        this.chosenColor = chosenColor;
        colorPlate.setBackground(chosenColor);
    }

    public Color getChosenColor() {
        return chosenColor;
    }
    
    private void openColorDialog() {
        if (colorChooser == null)
            colorChooser = new JColorChooser();
        Color newColor = colorChooser.showDialog(this, "Choose Color", getChosenColor());
        if (newColor != null)
            setChosenColor(newColor);
    }
    
    private void setUpVariables() {
        
        this.addOutputVariable(new ShaderProgramOutVariable(
                "color",
                "Color",
                DataType.DATA_TYPE_VEC3,
                ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                ));
    }
    
    public ShaderNode getShaderNode() {
        return this;
    }
    
    public JPanel getProperiesPanel() {
        return null;
    }
    
    /** {@inheritDoc} */
    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
    throws ExportingExeption {
        String colorCode = "";
        colorCode = "vec3(" + (getChosenColor().getRed() / 255f) + ", ";
        colorCode += (getChosenColor().getGreen() / 255f) + ", ";
        colorCode += (getChosenColor().getBlue() / 255f) + ")";
        return ValueAssignment.createCodeLineAssignment(colorCode, null);
    }
    public Class getFactoryClass() {
        return ColorGeneratorFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        super.saveState(saveVisitor);
//        saveVisitor.save("color", this.chosenColor);
    }
    
    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        super.loadState(loadVisitor);
//        setChosenColor(loadVisitor.loadColor("color"));
    }
}
