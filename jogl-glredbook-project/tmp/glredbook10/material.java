  package glredbook10;

import java.awt.event.*;
import javax.swing.*;
import com.sun.opengl.util.*; 
import javax.media.opengl.*;

/**
 * This program demonstrates the use of the GL lighting model. Several objects
 * are drawn using different material characteristics. A single light source
 * illuminates the objects.
 * 
 * @author Kiet Le
 */
public class material
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLCanvas canvas;
  private GLCapabilities caps;

  public material()
  {
    super(material.class.getSimpleName());
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
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(512, 512);
    setLocationRelativeTo(null);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new material().run();
  }

  /*
   * Initialize z-buffer, projection matrix, light source, and lighting model.
   * Do not specify a material property here.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    float ambient[] =
    { 0.0f, 0.0f, 0.0f, 1.0f };
    float diffuse[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float position[] =
    { 0.0f, 3.0f, 2.0f, 0.0f };
    float lmodel_ambient[] =
    { 0.4f, 0.4f, 0.4f, 1.0f };
    float local_view[] =
    { 0.0f };

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
    gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
    gl.glLightModelfv(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);

    gl.glClearColor(0.0f, 0.1f, 0.1f, 0.0f);
  }

  /*
   * Draw twelve spheres in 3 rows with 4 columns. The spheres in the first row
   * have materials with no ambient reflection. The second row has materials
   * with significant ambient reflection. The third row has materials with
   * colored ambient reflection. The first column has materials with blue,
   * diffuse reflection only. The second column has blue diffuse reflection, as
   * well as specular reflection with a low shininess exponent. The third column
   * has blue diffuse reflection, as well as specular reflection with a high
   * shininess exponent (a more concentrated highlight). The fourth column has
   * materials which also include an emissive component. glTranslatef() is used
   * to move spheres to their appropriate locations.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    GLUT glut = new GLUT();
    //
    float no_mat[] =
    { 0.0f, 0.0f, 0.0f, 1.0f };
    float mat_ambient[] =
    { 0.7f, 0.7f, 0.7f, 1.0f };
    float mat_ambient_color[] =
    { 0.8f, 0.8f, 0.2f, 1.0f };
    float mat_diffuse[] =
    { 0.1f, 0.5f, 0.8f, 1.0f };
    float mat_specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float no_shininess[] =
    { 0.0f };
    float low_shininess[] =
    { 5.0f };
    float high_shininess[] =
    { 100.0f };
    float mat_emission[] =
    { 0.3f, 0.2f, 0.2f, 0.0f };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    /*
     * draw sphere in first row, first column diffuse reflection only; no
     * ambient or specular
     */
    gl.glPushMatrix();
    gl.glTranslatef(-3.75f, 3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in first row, second column diffuse and specular reflection;
     * low shininess; no ambient
     */
    gl.glPushMatrix();
    gl.glTranslatef(-1.25f, 3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, low_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in first row, third column diffuse and specular reflection;
     * high shininess; no ambient
     */
    gl.glPushMatrix();
    gl.glTranslatef(1.25f, 3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, high_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in first row, fourth column diffuse reflection; emission; no
     * ambient or specular reflection
     */
    gl.glPushMatrix();
    gl.glTranslatef(3.75f, 3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in second row, first column ambient and diffuse reflection;
     * no specular
     */
    gl.glPushMatrix();
    gl.glTranslatef(-3.75f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in second row, second column ambient, diffuse and specular
     * reflection; low shininess
     */
    gl.glPushMatrix();
    gl.glTranslatef(-1.25f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, low_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in second row, third column ambient, diffuse and specular
     * reflection; high shininess
     */
    gl.glPushMatrix();
    gl.glTranslatef(1.25f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, high_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in second row, fourth column ambient and diffuse reflection;
     * emission; no specular
     */
    gl.glPushMatrix();
    gl.glTranslatef(3.75f, 0.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in third row, first column colored ambient and diffuse
     * reflection; no specular
     */
    gl.glPushMatrix();
    gl.glTranslatef(-3.75f, -3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient_color, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in third row, second column colored ambient, diffuse and
     * specular reflection; low shininess
     */
    gl.glPushMatrix();
    gl.glTranslatef(-1.25f, -3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient_color, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, low_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in third row, third column colored ambient, diffuse and
     * specular reflection; high shininess
     */
    gl.glPushMatrix();
    gl.glTranslatef(1.25f, -3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient_color, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, high_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, no_mat, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    /*
     * draw sphere in third row, fourth column colored ambient and diffuse
     * reflection; emission; no specular
     */
    gl.glPushMatrix();
    gl.glTranslatef(3.75f, -3.0f, 0.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient_color, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, no_mat, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, no_shininess, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glPopMatrix();
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= (h * 2)) //
    gl.glOrtho(-6.0, 6.0, -3.0 * ((float) h * 2) / (float) w, //
        3.0 * ((float) h * 2) / (float) w, -10.0, 10.0);
    else gl.glOrtho(-6.0 * (float) w / ((float) h * 2), //
        6.0 * (float) w / ((float) h * 2), -3.0, 3.0, -10.0, 10.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  public void keyTyped(KeyEvent arg0)
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

  public void keyReleased(KeyEvent arg0)
  {
  }
}
