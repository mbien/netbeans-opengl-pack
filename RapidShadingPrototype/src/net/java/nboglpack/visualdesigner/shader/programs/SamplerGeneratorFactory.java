/*
 * SamplerGeneratorFactory.java
 *
 * Created on 18. Juni 2007, 12:56
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.Sampler;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class SamplerGeneratorFactory implements IShaderProgramFactory {
    
    /**
     * Creates a new instance of SamplerGeneratorFactory
     */
    public SamplerGeneratorFactory() {
    }
    
    public String[] getVariants() {
        return null;
    }
    
    public IShaderProgram createShaderProgram() {
        return new SamplerGenerator();
    }
    
    public IShaderProgram createShaderProgram(String variant) {
        return new SamplerGenerator();
    }
    
    public String getName() {
        return "Sampler Generator";
    }
    
    public Color getNodeBackgroundColor() {
        return SimpleGeneratorsCollection.nodeBackgroundColor;
    }
}

/**
 *
 * @author Samuel Sperling
 */
class SamplerGenerator extends ShaderNode implements IShaderProgram {
    
    private JFileChooser fileChooser;
    private JPanel imagePlate;
    private Image chosenImage;
    private BufferedImage bufferedImage;
    private static Dimension defaultImagePreviewSize = new Dimension(128, 128);
    
    public SamplerGenerator() {
        super("Sampler Generator");
        super.setShaderProgram(this);
        setBackground(SimpleGeneratorsCollection.nodeBackgroundColor);
        
        setUpVariables();
    }
    
    protected void initComponents() {
        super.initComponents();
        
        imagePlate = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                if (chosenImage == null) return;
                g.drawImage(chosenImage, 0, 0, (int) defaultImagePreviewSize.getWidth(), (int) defaultImagePreviewSize.getHeight(), this);
            }
        };
        imagePlate.setBackground(Color.BLACK);
        imagePlate.setSize(defaultImagePreviewSize);
        imagePlate.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        imagePlate.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() < 2) {
                    e.setSource(getShaderNode());
                    processMouseEvent(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openImageDialog();
                } else {
                    e.setSource(getShaderNode());
                    processMouseEvent(e);
                }
                //TODO: pass event on to the parent
            }
        });
        getNodeContent().setLayout(null); //new FlowLayout(FlowLayout.TRAILING)
        getNodeContent().add(imagePlate);
        getNodeContent().setPreferredSize(imagePlate.getSize());
        adjustPreferedSize();
    }
    
    public void setChosenImage(Image chosenImage) {
        this.chosenImage = chosenImage;
        bufferedImage = null;
//        imagePlate.setImg(chosenImage);
        this.repaint();
    }
    
    public Image getChosenImage() {
        return chosenImage;
    }
    
    private void openImageDialog() {
        if (fileChooser == null)
            fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Image image = null;
            try {
                image = Toolkit.getDefaultToolkit().createImage(fileChooser.getSelectedFile().getAbsolutePath());
//                image = Toolkit.getDefaultToolkit().getImage(fileChooser.getSelectedFile().getAbsolutePath());
                if (image != null)
                    setChosenImage(image.getScaledInstance(defaultImagePreviewSize.width, defaultImagePreviewSize.height, image.SCALE_SMOOTH));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading Image\r\n" + ex.getMessage(), "Loading sampler", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    
    private void setUpVariables() {
        
        this.addOutputVariable(new ShaderProgramOutVariable(
                "sampler",
                "sampler",
                DataType.DATA_TYPE_SAMPLER2D,
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
        Sampler sampler = new Sampler("image", DataType.DATA_TYPE_SAMPLER2D, getBufferedImage());
        return ValueAssignment.createExternalAssignment(sampler);
    }
    public Class getFactoryClass() {
        return SamplerGeneratorFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
    
    private BufferedImage getBufferedImage() throws ExportingExeption {
        if (bufferedImage == null) {
            if (chosenImage == null)
                throw new ExportingExeption("No Image loaded for node " + this.getName());
            bufferedImage = toBufferedImage(this.chosenImage);
        }
        return bufferedImage;
    }
    
    /**
     * This method returns a buffered image with the contents of an image
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        
        // Determine if the image has transparent pixels; for this method's
        boolean hasAlpha = hasAlpha(image);
        
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        return bimage;
    }
    
    // This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
        
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
    
}