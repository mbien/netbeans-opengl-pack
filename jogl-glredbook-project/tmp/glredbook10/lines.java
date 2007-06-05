package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates geometric primitives and their attributes.
 * 
 * @author Kiet Le (Java conversion)
 */
public class lines
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public lines()
  {
    super(lines.class.getSimpleName());
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
    setSize(400, 150);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new lines().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    int i;

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);

    /* select white for all lines */
    gl.glColor3f(1.0f, 1.0f, 1.0f);

    /* in 1st row, 3 lines, each with a different stipple */
    gl.glEnable(GL.GL_LINE_STIPPLE);

    gl.glLineStipple(1, (short) 0x0101); /* dotted */
    drawOneLine(gl, 50.0f, 125.0f, 150.0f, 125.0f);
    gl.glLineStipple(1, (short) 0x00FF); /* dashed */
    drawOneLine(gl, 150.0f, 125.0f, 250.0f, 125.0f);
    gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
    drawOneLine(gl, 250.0f, 125.0f, 350.0f, 125.0f);

    /* in 2nd row, 3 wide lines, each with different stipple */
    gl.glLineWidth(5.0f);
    gl.glLineStipple(1, (short) 0x0101); /* dotted */
    drawOneLine(gl, 50.0f, 100.0f, 150.0f, 100.f);
    gl.glLineStipple(1, (short) 0x00FF); /* dashed */
    drawOneLine(gl, 150.0f, 100.0f, 250.0f, 100.0f);
    gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
    drawOneLine(gl, 250.0f, 100.0f, 350.0f, 100.0f);
    gl.glLineWidth(1.0f);

    /* in 3rd row, 6 lines, with dash/dot/dash stipple */
    /* as part of a single connected line strip */
    gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
    gl.glBegin(GL.GL_LINE_STRIP);
    for (i = 0; i < 7; i++)
      gl.glVertex2f(50.0f + ((float) i * 50.0f), 75.0f);
    gl.glEnd();

    /* in 4th row, 6 independent lines with same stipple */
    for (i = 0; i < 6; i++)
    {
      drawOneLine(gl, 50.0f + ((float) i * 50.0f), 50.0f,
          50.0f + ((float) (i + 1) * 50.0f), 50.0f);
    }

    /* in 5th row, 1 line, with dash/dot/dash stipple */
    /* and a stipple repeat factor of 5 */
    gl.glLineStipple(5, (short) 0x1C47); /* dash/dot/dash */
    drawOneLine(gl, 50.0f, 25.0f, 350.0f, 25.0f);

    gl.glDisable(GL.GL_LINE_STIPPLE);
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

  private void drawOneLine(GL gl, float x1, float y1, float x2, float y2)
  {
    gl.glBegin(GL.GL_LINES);

    gl.glVertex2f((x1), (y1));
    gl.glVertex2f((x2), (y2));
    gl.glEnd();
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
