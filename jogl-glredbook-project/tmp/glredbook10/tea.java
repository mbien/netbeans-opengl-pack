package glredbook10;
 
import java.awt.event.*;
import javax.swing.*;
 

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;  

/**
 * This program demonstrates two-sided lighting and compares it with one-sided
 * lighting. Three teapots are drawn, with a clipping plane to expose the
 * interior of the objects.
 * 
 * @author Kiet Le (java port)
 */
public class tea
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas; 

  //
  public tea()
  {
    super(tea.class.getSimpleName());
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
    new tea().run();
  }

  /*
   * Initialize light source.
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
    double eqn[] =
    { 1.0, 0.0, -1.0, 1.0 };
    // float two_side_on[] = { 1f };//never used by original example
    // float two_side_off[] = { 0 };
    float mat_diffuse[] =
    { 0.8f, 0.8f, 0.8f, 1.0f };
    float back_diffuse[] =
    { 0.8f, 0.2f, 0.8f, 1.0f };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    gl.glPushMatrix();
    gl.glClipPlane(GL.GL_CLIP_PLANE0, eqn, 0); /* slice objects */
    gl.glEnable(GL.GL_CLIP_PLANE0);

    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 2.0f, 0.0f);
    glut.glutSolidTeapot(1.0); /* one-sided lighting */
    gl.glPopMatrix();

    /* two-sided lighting, but same material */
    gl.glLightModelf(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 0.0f, 0.0f);
    glut.glutSolidTeapot(1.0);
    gl.glPopMatrix();

    /* two-sided lighting, two different materials */
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
    gl.glMaterialfv(GL.GL_BACK, GL.GL_DIFFUSE, back_diffuse, 0);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, -2.0f, 0.0f);
    glut.glutSolidTeapot(1.0);
    gl.glPopMatrix();

    gl.glLightModelf(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_FALSE);
    gl.glDisable(GL.GL_CLIP_PLANE0);
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
