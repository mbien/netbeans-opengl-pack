/*
 * Simple3DFrame.java
 *
 * Created on 13. Mai 2006, 23:24
 *
 */

package net.java.nboglpack.visualdesigner.tests;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import net.java.nboglpack.visualdesigner.graphics3d.*;
import net.java.nboglpack.visualdesigner.tests.FunctionVizScene;

/**
 * Erzeugt ein ganz simples Fenster zur Darstellung von OpenGL Szenen
 * @author Samuel Sperling
 */
public class Simple3DFrame extends Frame {
    
    /**
     * Erzeugt ein ganz simples Fenster zur Darstellung von OpenGL Szenen
     * @param name Übergibt einen Parameter an die OpenGL Szene
     */
    public Simple3DFrame(String name) {
        super(name);
    }
    
    /**
     * Erzeugt ein Simple3DFrame
     * @param argv Commandline Parameter
     */
    public static void main(String[] argv) {
//        System.loadLibrary("jogl");
        
        Canvas3DBase app = new FunctionVizScene();
        Simple3DFrame frame = new Simple3DFrame("OpenGL - Samuel Sperling");
//        GLDrawable glDrawable = GLDrawableFactory.getFactory().createExternalGLDrawable();
        GLCapabilities caps = new GLCapabilities();
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);
        GLCanvas canvas = new GLCanvas(caps); //GLDrawableFactory.getFactory() .createGLCanvas(new GLCapabilities());
        canvas.addGLEventListener(app);
        canvas.addKeyListener(app);
        
        GL gl = canvas.getGL();
        
        // Use debug pipeline
        //canvas.setGL(new DebugGL(gl));
        
        frame.add(canvas);
        frame.setSize(768, 768);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                animator.stop();
                System.out.println("Exiting");
                System.exit(0);
            }
        });
        frame.setVisible(true);
        animator.start();
    }
    
}
