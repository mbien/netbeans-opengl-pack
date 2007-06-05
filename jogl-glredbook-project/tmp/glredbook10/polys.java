package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates polygon stippling.
 * 
 * @author Kiet Le (Java conversin)
 */
public class polys
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  private byte fly[] =
  { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x80,
    (byte) 0x01, (byte) 0xC0, (byte) 0x06, (byte) 0xC0, (byte) 0x03,
    (byte) 0x60, (byte) 0x04, (byte) 0x60, (byte) 0x06, (byte) 0x20,
    (byte) 0x04, (byte) 0x30, (byte) 0x0C, (byte) 0x20, (byte) 0x04,
    (byte) 0x18, (byte) 0x18, (byte) 0x20, (byte) 0x04, (byte) 0x0C,
    (byte) 0x30, (byte) 0x20, (byte) 0x04, (byte) 0x06, (byte) 0x60,
    (byte) 0x20, (byte) 0x44, (byte) 0x03, (byte) 0xC0, (byte) 0x22,
    (byte) 0x44, (byte) 0x01, (byte) 0x80, (byte) 0x22, (byte) 0x44,
    (byte) 0x01, (byte) 0x80, (byte) 0x22, (byte) 0x44, (byte) 0x01,
    (byte) 0x80, (byte) 0x22, (byte) 0x44, (byte) 0x01, (byte) 0x80,
    (byte) 0x22, (byte) 0x44, (byte) 0x01, (byte) 0x80, (byte) 0x22,
    (byte) 0x44, (byte) 0x01, (byte) 0x80, (byte) 0x22, (byte) 0x66,
    (byte) 0x01, (byte) 0x80, (byte) 0x66, (byte) 0x33, (byte) 0x01,
    (byte) 0x80, (byte) 0xCC, (byte) 0x19, (byte) 0x81, (byte) 0x81,
    (byte) 0x98, (byte) 0x0C, (byte) 0xC1, (byte) 0x83, (byte) 0x30,
    (byte) 0x07, (byte) 0xe1, (byte) 0x87, (byte) 0xe0, (byte) 0x03,
    (byte) 0x3f, (byte) 0xfc, (byte) 0xc0, (byte) 0x03, (byte) 0x31,
    (byte) 0x8c, (byte) 0xc0, (byte) 0x03, (byte) 0x33, (byte) 0xcc,
    (byte) 0xc0, (byte) 0x06, (byte) 0x64, (byte) 0x26, (byte) 0x60,
    (byte) 0x0c, (byte) 0xcc, (byte) 0x33, (byte) 0x30, (byte) 0x18,
    (byte) 0xcc, (byte) 0x33, (byte) 0x18, (byte) 0x10, (byte) 0xc4,
    (byte) 0x23, (byte) 0x08, (byte) 0x10, (byte) 0x63, (byte) 0xC6,
    (byte) 0x08, (byte) 0x10, (byte) 0x30, (byte) 0x0c, (byte) 0x08,
    (byte) 0x10, (byte) 0x18, (byte) 0x18, (byte) 0x08, (byte) 0x10,
    (byte) 0x00, (byte) 0x00, (byte) 0x08 };

  private byte halftone[] =
  { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xAA,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
    (byte) 0xAA, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55,
    (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x55,
    (byte) 0x55, (byte) 0x55, (byte) 0x55 };

  //
  public polys()
  {
    super(polys.class.getSimpleName());
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
    setSize(350, 150);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new polys().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);

    /* draw one solid, unstippled rectangle, */
    /* then two stippled rectangles */
    gl.glRectf(25.0f, 25.0f, 125.0f, 125.0f);
    gl.glEnable(GL.GL_POLYGON_STIPPLE);
    gl.glPolygonStipple(fly, 0);
    gl.glRectf(125.0f, 25.0f, 225.0f, 125.0f);
    gl.glPolygonStipple(halftone, 0);
    gl.glRectf(225.0f, 25.0f, 325.0f, 125.0f);
    gl.glDisable(GL.GL_POLYGON_STIPPLE);

    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluOrtho2D(0.0, (double) w, 0.0, (double) h);
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
