package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates the use of the quadrics Utility Library routines to
 * draw circles and arcs.
 * 
 * @author Kiet Le (Java conversion)
 */
public class disk
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  private GLUquadric quadObj;

  //
  public disk()
  {
    super(disk.class.getSimpleName());
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
    setSize(512, 512);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new disk().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    quadObj = glu.gluNewQuadric();
    gl.glShadeModel(GL.GL_FLAT);
  }

  /*
   * Clear the screen. For each triangle, set the current color and modify the
   * modelview matrix.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);

    gl.glPushMatrix();
    glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glTranslatef(10.0f, 10.0f, 0.0f);
    glu.gluDisk(quadObj, 0.0, 5.0, 10, 2);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glColor3f(1.0f, 1.0f, 0.0f);
    gl.glTranslatef(20.0f, 20.0f, 0.0f);
    glu.gluPartialDisk(quadObj, 0.0, 5.0, 10, 3, 30.0, 120.0);
    gl.glPopMatrix();

    gl.glPushMatrix();
    glu.gluQuadricDrawStyle(quadObj, GLU.GLU_SILHOUETTE);
    gl.glColor3f(0.0f, 1.0f, 1.0f);
    gl.glTranslatef(30.0f, 30.0f, 0.0f);
    glu.gluPartialDisk(quadObj, 0.0, 5.0, 10, 3, 135.0, 270.0);
    gl.glPopMatrix();

    gl.glPushMatrix();
    glu.gluQuadricDrawStyle(quadObj, GLU.GLU_LINE);
    gl.glColor3f(1.0f, 0.0f, 1.0f);
    gl.glTranslatef(40.0f, 40.0f, 0.0f);
    glu.gluDisk(quadObj, 2.0, 5.0, 10, 10);
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
    if (w <= h) gl.glOrtho(0.0, 50.0, 0.0, 50.0 * (float) h / (float) w, -1.0,
        1.0);
    else gl.glOrtho(0.0, 50.0 * (float) w / (float) h, 0.0, 50.0, -1.0, 1.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
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
    switch (key.getKeyChar()) {
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
