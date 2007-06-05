package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program calculates the fovy (field of view angle in the y direction), by
 * using trigonometry, given the size of an object and its size.
 * 
 * @author Kiet Le (Java port)
 */
public class aim
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public aim()
  {
    super(aim.class.getSimpleName());
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
    new aim().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glShadeModel(GL.GL_FLAT);
  }

  /*
   * Clear the screen. Set the current color to white. Draw the wire frame cube
   * and sphere.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);

    gl.glLoadIdentity();
    /* glTranslatef() as viewing transformation */
    gl.glTranslatef(0.0f, 0.0f, -5.0f);
    glut.glutWireCube(2.0f);
    glut.glutWireSphere(1.0f, 10, 10);
    gl.glFlush();
  }

  /*
   * Called when the window is first opened and whenever the window is
   * reconfigured (moved or resized).
   */
  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    double theta;

    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    theta = calculateAngle(2.0, 5.0);
    glu.gluPerspective(theta, (float) w / (float) h, 1.0, 20.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  /*
   * atan2 () is a system math routine which calculates the arctangent of an
   * angle, given length of the opposite and adjacent sides of a right triangle.
   * atan2 () is not an OpenGL routine.
   */
  private double calculateAngle(double size, double distance)
  {
    double radtheta, degtheta;

    radtheta = 2.0 * Math.atan2(size / 2.0, distance);
    degtheta = (180.0 * radtheta) / Math.PI;
    System.out.println("degtheta is " + degtheta);
    return ((double) degtheta);
  }

  public void keyTyped(KeyEvent key)
  {
    // TODO Auto-generated method stub
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
