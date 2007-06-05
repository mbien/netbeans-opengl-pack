package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates the use of OpenGL modeling transformations. Four
 * triangles are drawn, each with a different transformation.
 * 
 * @author Kiet Le (Java port)
 */
public class model
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;

  //
  public model()
  {
    super(model.class.getSimpleName());
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
    new model().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClearColor(0f, 0f, 0f, 0f);
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
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1f, 1f, 1f);
    //
    gl.glLoadIdentity();
    drawTriangle(gl);
    //
    gl.glEnable(GL.GL_LINE_STIPPLE);
    gl.glLineStipple(1, (short) 0xf0f0);
    gl.glLoadIdentity();
    gl.glTranslatef(-20f, 0f, 0f);
    drawTriangle(gl);
    gl.glLineStipple(1, (short) 0xF00F);
    gl.glLoadIdentity();
    gl.glScalef(1.5f, 0.5f, 1.0f);
    drawTriangle(gl);
    gl.glLineStipple(1, (short) 0x8888);
    gl.glLoadIdentity();
    gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
    drawTriangle(gl);
    gl.glDisable(GL.GL_LINE_STIPPLE);
    //
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) gl.glOrtho(-50.0, 50.0, -50.0 * (float) h / (float) w,
        50.0 * (float) h / (float) w, -1.0, 1.0);
    else gl.glOrtho(-50.0 * (float) w / (float) h,
        50.0 * (float) w / (float) h, -50.0, 50.0, -1.0, 1.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void drawTriangle(GL gl)
  {
    gl.glBegin(GL.GL_LINE_LOOP);
    gl.glVertex2f(0.0f, 25.0f);
    gl.glVertex2f(25.0f, -25.0f);
    gl.glVertex2f(-25.0f, -25.0f);
    gl.glEnd();
  }

  public void keyTyped(KeyEvent key)
  {
    // TODO Auto-generated method stub
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
    // TODO Auto-generated method stub
  }

}
