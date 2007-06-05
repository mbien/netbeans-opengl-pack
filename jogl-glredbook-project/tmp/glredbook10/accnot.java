package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import com.sun.opengl.util.*;

/**
 * @author Kiet Le (Java port)
 */
public class accnot
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public accnot()
  {
    super(accnot.class.getSimpleName());
    //
    caps = new GLCapabilities();
    caps.setAccumBlueBits(16);
    caps.setAccumGreenBits(16);
    caps.setAccumRedBits(16);
    System.out.println(caps.toString());
    canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    //
    add(canvas);
  }

  public void run()
  {
    setSize(250, 250);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new accnot().run();
  }

  /*
   * Initialize lighting and other values.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glut = new GLUT();
    //
    float mat_ambient[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float mat_specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float light_position[] =
    { 0.0f, 0.0f, 10.0f, 1.0f };
    float lm_ambient[] =
    { 0.2f, 0.2f, 0.2f, 1.0f };

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 50.0f);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);
    gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lm_ambient, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    float torus_diffuse[] =
    { 0.7f, 0.7f, 0.0f, 1.0f };
    float cube_diffuse[] =
    { 0.0f, 0.7f, 0.7f, 1.0f };
    float sphere_diffuse[] =
    { 0.7f, 0.0f, 0.7f, 1.0f };
    float octa_diffuse[] =
    { 0.7f, 0.4f, 0.4f, 1.0f };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    gl.glShadeModel(GL.GL_FLAT);
    gl.glPushMatrix();
    gl.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);

    gl.glPushMatrix();
    gl.glTranslatef(-0.80f, 0.35f, 0.0f);
    gl.glRotatef(100.0f, 1.0f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, torus_diffuse, 0);
    glut.glutSolidTorus(0.275f, 0.85f, 10, 10);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glTranslatef(-0.75f, -0.50f, 0.0f);
    gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, cube_diffuse, 0);
    glut.glutSolidCube(1.5f);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glTranslatef(0.75f, 0.60f, 0.0f);
    gl.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, sphere_diffuse, 0);
    glut.glutSolidSphere(1.0f, 10, 10);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glTranslatef(0.70f, -0.90f, 0.25f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, octa_diffuse, 0);
    glut.glutSolidOctahedron();
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
    if (w <= h) gl.glOrtho(-2.25, 2.25, -2.25 * h / w, 2.25 * h / w, -10.0,
        10.0);
    else gl.glOrtho(-2.25 * w / h, 2.25 * w / h, -2.25, 2.25, -10.0, 10.0);
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
    char ch = key.getKeyChar();
    switch (ch) {
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
