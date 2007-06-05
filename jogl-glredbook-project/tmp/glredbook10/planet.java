package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program shows how to composite modeling transformations to draw
 * translated and rotated models. Interaction: pressing the d and y keys (day
 * and year) alters the rotation of the planet around the sun.
 * 
 * @author Kiet Le (Java conversion)
 */
public class planet
  extends JFrame
    implements GLEventListener, KeyListener

{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private int year = 0, day = 0;

  //
  public planet()
  {
    super(planet.class.getSimpleName());
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
    new planet().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);

    /* draw sun */
    gl.glPushMatrix();
    glut.glutWireSphere(1.0, 20, 16);
    /* draw smaller planet */
    gl.glRotatef((float) year, 0.0f, 1.0f, 0.0f);
    gl.glTranslatef(2.0f, 0.0f, 0.0f);
    gl.glRotatef((float) day, 0.0f, 1.0f, 0.0f);
    glut.glutWireSphere(0.2, 10, 10);
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
    glu.gluPerspective(60.0, (float) w / (float) h, 1.0, 20.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -5.0f);
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
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_A:
        yearSubtract();
        break;
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_D:
        yearAdd();
        break;
      case KeyEvent.VK_UP:
      case KeyEvent.VK_W:
        dayAdd();
        break;
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_S:
        daySubtract();
        break;

      default:
        break;
    }
    canvas.display();
  }

  public void keyReleased(KeyEvent key)
  {
  }

  private void dayAdd()
  {
    day = (day + 10) % 360;
  }

  private void daySubtract()
  {
    day = (day - 10) % 360;
  }

  private void yearAdd()
  {
    year = (year + 5) % 360;
  }

  private void yearSubtract()
  {
    year = (year - 5) % 360;
  }

}
