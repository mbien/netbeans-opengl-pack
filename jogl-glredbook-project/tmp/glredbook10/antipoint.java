package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

/**
 * The program draws antialiased points, in RGBA mode.
 * 
 * @author Kiet Le (Java conversion)
 */
public class antipoint
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public antipoint()
  {
    super(antipoint.class.getSimpleName());
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
    setSize(100, 100);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new antipoint().run();
  }

  /*
   * Initialize point anti-aliasing for RGBA mode, including alpha blending,
   * hint, and point size. These points are 3.0 pixels big.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glEnable(GL.GL_POINT_SMOOTH);
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_DONT_CARE);
    gl.glPointSize(3.0f);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /*
   * display() draws several points.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    int i;

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    gl.glBegin(GL.GL_POINTS);
    for (i = 1; i < 10; i++)
    {
      gl.glVertex2f((float) i * 10.0f, (float) i * 10.0f);
    }
    gl.glEnd();
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w < h) gl.glOrtho(0.0, 100.0, 0.0, 100.0 * (float) h / (float) w, -1.0,
        1.0);
    else gl.glOrtho(0.0, 100.0 * (float) w / (float) h, 0.0, 100.0, -1.0, 1.0);
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
