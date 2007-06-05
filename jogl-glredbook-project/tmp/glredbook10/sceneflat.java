package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program draws lighted objects with flat shading.
 * 
 * @author Kiet Le (Java conversion)
 */
public class sceneflat
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public sceneflat()
  {
    super(sceneflat.class.getSimpleName());
    //
    caps = new GLCapabilities();
    canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    //
    add(canvas);
  }

  public void run()
  {
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new sceneflat().run();
  }

  /*
   * Initialize light source and shading model (GL.GL_FLAT).
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float light_ambient[] =
    { 0.0f, 0.0f, 0.0f, 1.0f };
    float light_diffuse[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float light_specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    /* light_position is NOT default value */
    float light_position[] =
    { 1.0f, 1.0f, 1.0f, 0.0f };

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    gl.glPushMatrix();
    gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);

    gl.glPushMatrix();
    gl.glTranslatef(-0.75f, 0.5f, 0.0f);
    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    glut.glutSolidTorus(0.275, 0.85, 20, 20);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glTranslatef(-0.75f, -0.5f, 0.0f);
    gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
    glut.glutSolidCone(1.0, 2.0, 20, 20);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glTranslatef(0.75f, 0.0f, -1.0f);
    glut.glutSolidSphere(1.0, 20, 20);
    gl.glPopMatrix();

    gl.glPopMatrix();
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) gl.glOrtho(-2.5, 2.5, -2.5 * (float) h / (float) w,
        2.5 * (float) h / (float) w, -10.0, 10.0);
    else gl.glOrtho(-2.5 * (float) w / (float) h, 2.5 * (float) w / (float) h,
        -2.5, 2.5, -10.0, 10.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  public void keyTyped(KeyEvent key)
  {
  }

  public void keyPressed(KeyEvent key)
  {
    switch (key.getKeyCode()) {
      case KeyEvent.VK_ESCAPE:
        System.exit(0);
        break;

      default:
        break;
    }
  }

  public void keyReleased(KeyEvent key)
  {
  }

}
