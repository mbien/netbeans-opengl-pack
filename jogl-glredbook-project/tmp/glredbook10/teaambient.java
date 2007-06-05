package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program renders three lighted, shaded teapots, with different ambient
 * values.
 * 
 * @author Kiet Le (Java port)
 */
public class teaambient
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public teaambient()
  {
    super(teaambient.class.getSimpleName());
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
    setSize(512, 256);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new teaambient().run();
  }

  /*
   * Initialize light source and lighting model.
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
    { 1.0f, 0.0f, 0.0f, 0.0f };
    float global_ambient[] =
    { 0.75f, 0.75f, 0.75f, 1.0f };

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

    gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, global_ambient, 0);

    gl.glFrontFace(GL.GL_CW);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_AUTO_NORMAL);
    gl.glEnable(GL.GL_NORMALIZE);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    float low_ambient[] =
    { 0.1f, 0.1f, 0.1f, 1.0f };
    float more_ambient[] =
    { 0.4f, 0.4f, 0.4f, 1.0f };
    float most_ambient[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    /* material has small ambient reflection */
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, low_ambient, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 40.0f);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 2.0f, 0.0f);
    glut.glutSolidTeapot(1.0);
    gl.glPopMatrix();

    /* material has moderate ambient reflection */
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, more_ambient, 0);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 0.0f, 0.0f);
    glut.glutSolidTeapot(1.0);
    gl.glPopMatrix();

    /* material has large ambient reflection */
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, most_ambient, 0);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, -2.0f, 0.0f);
    glut.glutSolidTeapot(1.0);
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
    if (w <= h) gl.glOrtho(-4.0, 4.0, -4.0 * (float) h / (float) w,
        4.0 * (float) h / (float) w, -10.0, 10.0);
    else gl.glOrtho(-4.0 * (float) w / (float) h, 4.0 * (float) w / (float) h,
        -4.0, 4.0, -10.0, 10.0);
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
