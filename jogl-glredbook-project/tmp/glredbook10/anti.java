package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program draws antialiased lines in RGBA mode.
 * 
 * @author Kiet Le (Java conversion)
 */
public class anti
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;

  //
  public anti()
  {
    super(anti.class.getSimpleName());
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
    setSize(400, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new anti().run();
  }

  /*
   * Initialize antialiasing for RGBA mode, including alpha blending, hint, and
   * line width. Print out implementation specific info on line width
   * granularity and width.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float values[] = new float[2];
    gl.glGetFloatv(GL.GL_LINE_WIDTH_GRANULARITY, values, 0);
    System.out.println("GL.GL_LINE_WIDTH_GRANULARITY value is " + values[0]);

    gl.glGetFloatv(GL.GL_LINE_WIDTH_RANGE, values, 0);
    System.out.println("GL.GL_LINE_WIDTH_RANGE values are " //
                       + values[0] + ", " + values[1]);

    gl.glEnable(GL.GL_LINE_SMOOTH);
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
    gl.glLineWidth(1.5f);

    gl.glShadeModel(GL.GL_FLAT);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  /*
   * display() draws an icosahedron with a large alpha value, 1.0.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    glut.glutWireIcosahedron();
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(45.0, (float) w / (float) h, 3.0, 5.0);

    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -4.0f); /* move object into view */
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
      case ' ':
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
