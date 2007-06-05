package glredbook10;

import java.awt.event.*;
import javax.swing.*; 
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

/**
 * This program demonstrates how to make and execute a display list. Note that
 * attributes, such as current color and matrix, are changed.
 * 
 * @author Kiet Le (Java conversion)
 */
public class list
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLCanvas canvas;
  private GLCapabilities caps;
  private int listName;

  public list()
  {
    super(list.class.getSimpleName());
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
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 50);
    setLocationRelativeTo(null);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new list().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    listName = gl.glGenLists(1);
    gl.glNewList(listName, GL.GL_COMPILE);
    gl.glColor3f(1f, 0f, 0f);
    gl.glBegin(GL.GL_TRIANGLES);
    gl.glVertex2f(0f, 0f);
    gl.glVertex2f(1f, 0f);
    gl.glVertex2f(0f, 1f);
    gl.glEnd();
    gl.glTranslatef(1.5f, 0f, 0f);
    gl.glEndList();
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(0f, 1f, 0f);// has not affect
    for (int i = 0; i < 10; i++)
      gl.glCallList(listName);
    drawLine(gl); // * is this line green? NO! where is the line drawn?
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    GLU glu = new GLU();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) glu.gluOrtho2D(0.0, 2.0, -0.5 * (float) h / (float) w,
        1.5 * (float) h / (float) w);
    else glu.gluOrtho2D(0.0, 2.0 * (float) w / (float) h, -0.5, 1.5);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void drawLine(GL gl)
  {
    gl.glBegin(GL.GL_LINES);
    gl.glVertex2f(0f, 0.5f);
    gl.glVertex2f(15f, .05f);
    gl.glEnd();
  }

  public void keyTyped(KeyEvent arg0)
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

  public void keyReleased(KeyEvent arg0)
  {
  }
}
