/*
 * PreviewPanel.java
 *
 * Created on May 7, 2007, 12:06 PM
 *
 */

package net.java.nboglpack.visualdesigner.preview;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import net.java.nboglpack.visualdesigner.graphics3d.FragmentShader;
import net.java.nboglpack.visualdesigner.graphics3d.ShaderProgram;
import net.java.nboglpack.visualdesigner.graphics3d.VertexShader;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.ExternalAttribute;
import net.java.nboglpack.visualdesigner.shader.exporter.GLSLCodeExporter;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExporter;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 * Shows a preview of the current Shader
 *
 * @author Samuel Sperling
 */
public class PreviewPanel extends JPanel {
    
    private JPopupMenu contextmenu;
    private Editor editor;
    private IGLPreviewScene glPreviewScene;
    private GLCapabilities caps;
    private GLJPanel preview;
    private Animator animator;
    private StringWriter vertexShaderSource;
    private StringWriter fragmentShaderSource;
    private Settings settings;
    private boolean isFullscreen;
    private JFrame fullscreenFrame;
    private GLCanvas fullscreenCanvas;
    private Animator fullscreenAnimator;
    private boolean isShaderRecent;
    private JMenuItem mnuToggleRun;
    
    /** Creates a new instance of PreviewPanel */
    public PreviewPanel() {
//        settings = Editor.mainSettings.getChildSettings("Preview");
        initComponents();
        setPreviewScene(new FunctionPreview());
    }
    
    private Editor getEditor() {
        if (editor == null)
            editor = Editor.findMe(this);
        return editor;
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // OpenGL
        caps = new GLCapabilities();
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);
        preview = new GLJPanel(caps);
        preview.setFocusable(true);
        preview.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                preview.requestFocus();
            }
        });
        this.add(preview);
        animator = new FPSAnimator(preview, /*settings.getValueInt("FpsLimit",*/ 30);//));
        preview.addGLEventListener(new GLEventListener() {
            public void display(GLAutoDrawable gLAutoDrawable) {
                refreshShaderProgram(gLAutoDrawable.getGL());
            }
            public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean b, boolean b0) {
                    isShaderRecent = false;
            }
            public void init(GLAutoDrawable gLAutoDrawable) {
                    isShaderRecent = false;
            }
            public void reshape(GLAutoDrawable gLAutoDrawable, int i, int i0, int i1, int i2) {
            }
        });
        
        // Popup Menu
        
        contextmenu = new JPopupMenu();
        preview.setComponentPopupMenu(contextmenu);
        
        JMenuItem mnuRefresh = new JMenuItem("Refresh");
        mnuRefresh.addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                refresh();
            }
        });
        contextmenu.add(mnuRefresh);
        
        
        mnuToggleRun = new JMenuItem("Pause");
        mnuToggleRun.addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                if (animator.isAnimating()) {
                    animator.stop();
                    mnuToggleRun.setText("Run");
                } else {
                    animator.start();
                    mnuToggleRun.setText("Pause");
                }
            }
        });
        contextmenu.add(mnuToggleRun);
        
    }
//    
//    private void setFPS(int fps) {
//        this.fps = fps;
//        ((FPSAnimator) this.animator).fps = fps;
//    }
    
    private void ensureFullscreenFrame() {
        if (fullscreenFrame == null) {
            fullscreenCanvas = new GLCanvas(caps);
            fullscreenAnimator = new Animator(fullscreenCanvas);
            fullscreenCanvas.addGLEventListener(new GLEventListener() {
                public void display(GLAutoDrawable gLAutoDrawable) {
                    refreshShaderProgram(gLAutoDrawable.getGL());
                }
                public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean b, boolean b0) {
                    isShaderRecent = false;
                }
                public void init(GLAutoDrawable gLAutoDrawable) {
                    isShaderRecent = false;
                }
                public void reshape(GLAutoDrawable gLAutoDrawable, int i, int i0, int i1, int i2) {
                }
            });
            fullscreenCanvas.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                        setFullscreen(false);
                }
                public void keyReleased(KeyEvent e) {
                }
                public void keyTyped(KeyEvent e) {
                }
            });

            // Full Screen Window
            fullscreenFrame = new JFrame("Rapid Shading - Fullscreen Preview");
            fullscreenFrame.setUndecorated(true);
            fullscreenFrame.setSize(200, 200);
            fullscreenFrame.add(fullscreenCanvas);
//            fullscreenFrame.addWindowListener(new WindowAdapter() {
//                public void windowClosing(WindowEvent e) {
//                    runExit();
//                }
//            });
            
            // Apply more listeners if supported
            try { fullscreenCanvas.addKeyListener((KeyListener) glPreviewScene);
            } catch (ClassCastException ex) { }

            try { fullscreenCanvas.addMouseListener((MouseListener) glPreviewScene);
            } catch (ClassCastException ex) { }
        }
    }
    
    private VertexShader vertexShader;
    private FragmentShader fragmentShader;
    private ShaderProgram shaderProgram;
    private ArrayList<ExternalAttribute> attributes;
    private void refreshShaderProgram(GL gl) {
        if (!isShaderRecent && vertexShaderSource != null && fragmentShaderSource != null) {
            VertexShader vertexShader = new VertexShader(gl, new String[] {vertexShaderSource.toString()});
            FragmentShader fragmentShader = new FragmentShader(gl, new String[] {fragmentShaderSource.toString()});
            shaderProgram = new ShaderProgram(gl, vertexShader, fragmentShader);
            shaderProgram.process();
            
            for (ExternalAttribute attribute : attributes) {
                shaderProgram.setAttribute(attribute.createShaderAttribute(gl));
            }
            
//            vertexShaderSource = null;
//            fragmentShaderSource = null;
            isShaderRecent = true;
            glPreviewScene.applyShaderProgram(shaderProgram);
        }
    }
    
    /**
     *
     *
     */
    public void refresh() {
        
        // If no scene was loaded yet...
        if (glPreviewScene == null) {
            JOptionPane.showMessageDialog(this, "No Preview Scene loaded yet.", "Refreshing Preview", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        vertexShaderSource = new StringWriter();
        fragmentShaderSource = new StringWriter();
        
        IShaderCodeExporter exporter = new GLSLCodeExporter();
        attributes = null;
        try {
            long time = System.nanoTime();
            attributes = exporter.exportShader(vertexShaderSource, fragmentShaderSource);
            System.out.println("exporting time: " + ((System.nanoTime() - time) / 1000000) + " ms");
        } catch (ExportingExeption ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
//        System.out.println("++++ VERTEX SHADER ++++");
//        System.out.println(vertexShaderSource);
//        System.out.println("++++ FRAGMENT SHADER ++++");
//        System.out.println(fragmentShaderSource);
        
        isShaderRecent = false;
        preview.display();
    }
    
    public IGLPreviewScene getPreviewScene() {
        return glPreviewScene;
    }
    
    public void setPreviewScene(IGLPreviewScene glPreviewScene) {
        if (this.glPreviewScene == glPreviewScene) return;
        if (this.glPreviewScene != null)
            preview.removeGLEventListener(this.glPreviewScene);
        if (fullscreenCanvas != null)
            fullscreenCanvas.removeGLEventListener(this.glPreviewScene);
        
        this.glPreviewScene = glPreviewScene;
        preview.addGLEventListener(glPreviewScene);
        if (!animator.isAnimating()) {
            animator.start();
        }
        
        // Apply more listeners if supported
        try {
            preview.addKeyListener((KeyListener) glPreviewScene);
        } catch (ClassCastException ex) { }
        
        try {
            preview.addMouseListener((MouseListener) glPreviewScene);
        } catch (ClassCastException ex) { }
    }
    
    public void setFullscreen(boolean isFullscreen) {
        if (isFullscreen == this.isFullscreen) return;
        this.isFullscreen = isFullscreen;
        
        GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isFullscreen) {
            animator.stop();
            ensureFullscreenFrame();
            
            fullscreenCanvas.addGLEventListener(glPreviewScene);
            if (dev.isFullScreenSupported()) dev.setFullScreenWindow(fullscreenFrame);
            fullscreenAnimator.start();
            fullscreenFrame.setVisible(true);
            fullscreenCanvas.requestFocus();
        } else {
            try {
                //TODO: Fix this exception problem!!
                if (dev.isFullScreenSupported()) {
                    dev.setFullScreenWindow(null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            fullscreenAnimator.stop();
            fullscreenFrame.setVisible(false);
            this.requestFocus();
            animator.start();
            getEditor().repaint();
        }
        
//        if (isFullscreen) {
//            animator.stop();
//            
//            this.remove(preview);
//            fullscreenFrame.add(preview);
//            fullscreenFrame.setVisible(true);
//            if (dev.isFullScreenSupported()) {
//                dev.setFullScreenWindow(fullscreenFrame);
//            }
////            animator.start();
////            final GLCanvas canvas = new GLCanvas();
////            canvas.addGLEventListener(glPreviewScene);
////            canvas.addGLEventListener(new FullscreenPreview(dev.getFullScreenWindow().getWidth() , dev.getFullScreenWindow().getHeight()));
//        } else {
////            animator.stop();
//            
//            fullscreenFrame.remove(preview);
//            this.add(preview);
//            fullscreenFrame.setVisible(false);
////            animator.start();
//            preview.setSize(preview.getSize());
//        }
    }
    
    public boolean isFullscreen() {
        return this.isFullscreen;
    }
    
    protected void finalize() throws Throwable {
        if (animator.isAnimating())
            animator.stop();
        else if (fullscreenAnimator != null && fullscreenAnimator.isAnimating())
            fullscreenAnimator.stop();
    }
    
}