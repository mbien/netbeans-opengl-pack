package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

public class light
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  /**
   * This program demonstrates the use of the OpenGL lighting model. A sphere is
   * drawn using a grey material characteristic. A single light source
   * illuminates the object.
   * 
   * @author Kiet Le (Java conversion)
   */
  public light()
  {
    super(light.class.getSimpleName());
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
    new light().run();
  }

  /*
   * Initialize material property, light source, lighting model, and depth
   * buffer.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glut = new GLUT();
    //
    float mat_specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float mat_shininess[] =
    { 50.0f };
    float light_position[] =
    { 1.0f, 1.0f, 1.0f, 0.0f };
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_SMOOTH);
    //
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);
    //
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    glut.glutSolidSphere(1.0, 20, 20);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) // 
    gl.glOrtho(-1.5, 1.5,//
        -1.5 * (float) h / (float) w,//
        1.5 * (float) h / (float) w,//
        -10.0, 10.0);
    else gl.glOrtho(-1.5 * (float) w / (float) h, //
        1.5 * (float) w / (float) h, //
        -1.5, 1.5, -10.0, 10.0);
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
