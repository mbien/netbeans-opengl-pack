package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;  

/**
 * This program demonstrates a single modeling transformation, glScalef() and a
 * single viewing transformation, gluLookAt(). A wireframe cube is rendered.
 * 
 * @author Kiet Le (Java conversion)
 */
public class cube
  extends JFrame
    implements GLEventListener, KeyListener
{ 
  private GLUT glut;
  private GLCanvas canvas;
  private FPSAnimator animator;

  public cube()
  {
    super(cube.class.getSimpleName());
    //
    canvas = new GLCanvas();
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
    new cube().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL(); 
    glut = new GLUT();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_FLAT);
  }

  /*
   * Clear the screen. Set the current color to white. Draw the wire frame cube.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    GLU glu = new GLU();
    GLUT glut = new GLUT();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glLoadIdentity(); /* clear the matrix */
    /* viewing transformation */
    glu.gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    gl.glScalef(1.0f, 2.0f, 1.0f); /* modeling transformation */
    glut.glutWireCube(1.0f);
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
    gl.glMatrixMode(GL.GL_PROJECTION); /* prepare for and then */
    gl.glLoadIdentity(); /* define the projection */
    gl.glFrustum(-1.0, 1.0, -1.0, 1.0, 1.5, 20.0); /* transformation */
    gl.glMatrixMode(GL.GL_MODELVIEW); /* back to modelview matrix */
    gl.glViewport(0, 0, w, h); /* define the viewport */
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
        animator.stop();
        System.exit(0);
      default:
        break;
    }
  }

  public void keyReleased(KeyEvent arg0)
  {
  }

}
