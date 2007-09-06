//////////////////////////////////////////////////////////////////////////
//
//  $Id: Demo_TextureMapping.java,v 1.4 2005/11/28 08:56:46 tsky Exp $
//
//  $Source: /usr/local/cvsroot/geVE/src/geVE/demos/Demo_TextureMapping.java,v $
//  $Author: tsky $
//  $Date: 2005/11/28 08:56:46 $
//  $Revision: 1.4 $
//  $State: Exp $
//
//
//  $Log: Demo_TextureMapping.java,v $
//  Revision 1.4  2005/11/28 08:56:46  tsky
//  change some TextureMapping things
//
//  Revision 1.3  2005/11/22 20:10:30  tsky
//  now LGPL
//
//
///////////////////////////////////////////////////////////////////////////
// READ COPYING - LGPL - GNU LESSER GENERAL PUBLIC LICENSE
///////////////////////////////////////////////////////////////////////////
package net.java.nboglpack.visualdesigner.tests;

import com.sun.opengl.util.Animator;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point4f;
import net.java.nboglpack.visualdesigner.graphics3d.*;
import net.java.nboglpack.visualdesigner.graphic3d.primitives.Sphere;

/**
 *
 *
 * @author tsky
 */
public class test1 implements GLEventListener {
    // Light 0
    static public float m_LightPos0[]={0.0f, 6.0f, 5.0f, 0};
    // Farbe des Licht0 R=1 G=1 B=0 alpha=1
    static public float m_LightAmbient0[]={0, 0, 0, 1};
    static public float m_LightDiffuse0[]={1, 1, 1, 1};
    static public float m_LightSpecular0[]={1, 1, 1, 1};
    // Default Material
    static public float m_MaterialAmbient[]={0.2f, 0.2f, 0.2f, 1};
    static public float m_MaterialDiffuse[]={0.8f, 0.6f, 0.2f, 1};
    static public float m_MaterialSpecular[]={0.2f, 0.2f, 0.2f, 1};
    
    private int[] internalTexture;
    private ArrayList<IRenderable> mesh = new ArrayList();
   private static final GLU glu = new GLU();
    private Material material;
    
    /**
     * Creates a new instance of test1
     */
    public test1() {
        
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Main program; do basic GLUT setup, but leave most of the work
    // to the display() function.
    
    public static void main(String[] argv) {
        Frame frame = new Frame("OpenGL with JOGL basic");
        GLCanvas canvas = new GLCanvas();
//        GLCanvas canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
        canvas.addGLEventListener(new test1());
        
        // Use debug pipeline
        canvas.setGL(new DebugGL(canvas.getGL()));
        
        frame.add(canvas);
        frame.setSize(512, 512);
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
        // and all the rest happens in the display function...
    }
    
   public void init1(GLAutoDrawable gLDrawable) {
       final GL gl = gLDrawable.getGL();
       gl.glShadeModel(GL.GL_SMOOTH);
       gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
       gl.glClearDepth(1.0f);
       gl.glEnable(GL.GL_DEPTH_TEST);
       gl.glDepthFunc(GL.GL_LEQUAL);
       gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
//       gLDrawable.addKeyListener(this);
   }
    public void init(GLAutoDrawable drawable) {
        GLContext context = drawable.createContext(null);
        GL gl = context.getGL();
        GLU glu = new GLU();
        
        // Licht 0 wird angeschaltet
        gl.glEnable(gl.GL_LIGHT0);
        // Position wird gesetzt
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, m_LightPos0, 0);
        // Die Farbe des Lichts wird in allen bereichen auf
        // denselben Farbwert gesetzt
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, m_LightAmbient0, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, m_LightDiffuse0, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, m_LightSpecular0, 0);
        
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_POLYGON_SMOOTH);
        gl.glEnable(gl.GL_NORMALIZE);
        gl.glEnable(gl.GL_CULL_FACE);
        
        material = new Material(gl);
        material.setDiffuse(new Point4f(0f, 0.0f, 0.9f, 1f));
        material.setSpecular(new Point4f(0.5f, 0.8f, 0.6f, 1f));
        material.setShininess(64f);
        material.setFace(gl.GL_FRONT);
        material.apply();

//        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, m_MaterialAmbient, 3);
//        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, m_MaterialDiffuse, 3);
//        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, m_MaterialSpecular, 3);
//        gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 64);
        
        gl.glShadeModel(gl.GL_SMOOTH);
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glDepthFunc(gl.GL_LEQUAL);
        gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);
        gl.glPolygonMode(gl.GL_FRONT, gl.GL_FILL);
        
       
        //mesh.add(new Cube(gl, 0.2f, 3.0f, 3.0f));
        //mesh.add(new Cylinder(gl, 0.5f, 1.5f, 128));
        mesh.add(new Sphere(gl, 1.2f, 64));
        printError(gl, glu);
        System.out.println("Vendor: " + gl.glGetString(gl.GL_VENDOR));
        System.out.println("Renderer: " + gl.glGetString(gl.GL_RENDERER));
        System.out.println("Version: " + gl.glGetString(gl.GL_VERSION));
        System.out.println("Extensions: " + gl.glGetString(gl.GL_EXTENSIONS));
    }
    
    private static float curTime = 0;
    
    // display callback function
    public void display2(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        
        // The usual OpenGL stuff to clear the screen and set up viewing.
        gl.glClearColor(.25f, .25f, .25f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(65.0f, 1.0f, .1f, 100);
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 3, 3,  0, 0, 0,  0, 1, 0);
        gl.glShadeModel(gl.GL_SMOOTH);
        
        // Make the object rotate a bit each time the display function
        // is called
        gl.glRotatef(curTime/1, (float) Math.sin(curTime/75), (float) Math.sin(curTime/100), (float) Math.cos(curTime/50));
        gl.glRotatef(curTime/10, 1.0f, 0f, 0f);
        
        // And go ahead and draw the scene geometry
        DrawGeometry(gl);
        
        printError(gl, glu);
        curTime++;
    }
    
   private float rotateT = 0.0f;
   public void display(GLAutoDrawable gLDrawable) {
       final GL gl = gLDrawable.getGL();
        material.apply();
       gl.glClear(GL.GL_COLOR_BUFFER_BIT);
       gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
       gl.glLoadIdentity();
       gl.glTranslatef(0.0f, 0.0f, -5.0f);

       gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
       gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
       gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
       gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);

       gl.glBegin(GL.GL_TRIANGLES);

       // Front
       gl.glColor3f(0.0f, 1.0f, 1.0f); gl.glVertex3f(0.0f, 1.0f, 0.0f);
       gl.glColor3f(0.0f, 0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
       gl.glColor3f(0.0f, 0.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, 1.0f);

       // Right Side Facing Front
       gl.glColor3f(0.0f, 1.0f, 1.0f); gl.glVertex3f(0.0f, 1.0f, 0.0f);
       gl.glColor3f(0.0f, 0.0f, 1.0f); gl.glVertex3f(1.0f, -1.0f, 1.0f);
       gl.glColor3f(0.0f, 0.0f, 0.0f); gl.glVertex3f(0.0f, -1.0f, -1.0f);

       // Left Side Facing Front
       gl.glColor3f(0.0f, 1.0f, 1.0f); gl.glVertex3f(0.0f, 1.0f, 0.0f);
       gl.glColor3f(0.0f, 0.0f, 1.0f); gl.glVertex3f(0.0f, -1.0f, -1.0f);
       gl.glColor3f(0.0f, 0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);

       // Bottom
       gl.glColor3f(0.0f, 0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
       gl.glColor3f(0.1f, 0.1f, 0.1f); gl.glVertex3f(1.0f, -1.0f, 1.0f);
       gl.glColor3f(0.2f, 0.2f, 0.2f); gl.glVertex3f(0.0f, -1.0f, -1.0f);

       gl.glEnd();

       rotateT += 0.2f;
   }
   
    private void printError(GL gl, GLU glu) {
        int error = gl.glGetError();
        if (error != gl.GL_NO_ERROR) System.out.println(glu.gluErrorString(error));
    }
    
    /**
     *
     */
    void DrawGeometry(GL gl) {
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(1.0f, 0.5f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.5f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glEnd();
                
//        for (int i = 0; i < this.mesh.size(); i++) {
//            //((Sphere) this.mesh.get(i)).render(gl, Math.min(((int) (curTime / 20)) % 40, 32));
//            this.mesh.get(i).render();
//        }
    }
    
    /**
     *
     */
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        // nothing
    }
    
    /**
     *
     */
   public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
       final GL gl = gLDrawable.getGL();
       if(height <= 0) {
           height = 1;
       }
       final float h = (float)width / (float)height;
       gl.glMatrixMode(GL.GL_PROJECTION);
       gl.glLoadIdentity();
       glu.gluPerspective(50.0f, h, 1.0, 1000.0);
       gl.glMatrixMode(GL.GL_MODELVIEW);
       gl.glLoadIdentity();
   }

}
