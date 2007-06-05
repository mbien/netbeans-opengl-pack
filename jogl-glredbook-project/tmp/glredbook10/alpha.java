package glredbook10;

import javax.swing.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*; 

/**
 * This program draws several overlapping filled polygons to demonstrate the
 * effect order has on alpha blending results. Use the 't' key to toggle the
 * order of drawing polygons.
 * 
 * @author: Kiet Le (Java conversion)
 */
public class alpha
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLCanvas canvas;

  public alpha()
  {
    super(alpha.class.getSimpleName());
    //
    canvas = new GLCanvas();
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    //
    add(canvas);
  }

  public void run()
  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 500);
    setLocationRelativeTo(null);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  /*
   * Main Loop Open window with initial window size, title bar, RGBA display
   * mode, and handle input events.
   */
  public static void main(String[] args)
  {
    new alpha().run();
  }

  /*
   * Initialize alpha blending function.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glShadeModel(GL.GL_FLAT);
    gl.glClearColor(0, 0, 0, 0);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
    gl.glRectf(0.0f, 0.0f, 0.5f, 1.0f);

    gl.glColor4f(0.0f, 1.0f, 1.0f, 0.75f);
    gl.glRectf(0.0f, 0.0f, 1.f, 0.5f);
    /* draw colored polygons in reverse order in upper right */
    gl.glColor4f(0.f, 1.0f, 1.0f, 0.75f);
    gl.glRectf(0.5f, 0.5f, 1.0f, 1.0f);

    gl.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
    gl.glRectf(0.5f, 0.5f, 1.0f, 1.0f);

    gl.glEnd();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    GLU glu = new GLU();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) glu.gluOrtho2D(0.0, 1.0, 0.0, 1.0 * h / w);
    else glu.gluOrtho2D(0.0, 1.0 * w / h, 0.0, 1.0);
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
