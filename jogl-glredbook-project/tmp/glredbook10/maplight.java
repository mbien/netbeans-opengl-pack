package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates the use of the GL lighting model. A sphere is drawn
 * using a magenta diffuse reflective and white specular material property. A
 * single light source illuminates the object. This program illustrates lighting
 * in color map mode.
 * 
 * @author Kiet Le
 */
public class maplight
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public maplight()
  {
    super(maplight.class.getSimpleName());
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
    new maplight().run();
  }

  /*
   * Initialize material property, light source, and lighting model.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glut = new GLUT();
    //
    float light_position[] =
    { 1.0f, 1.0f, 1.0f, 0.0f };
    float mat_colormap[] =
    { 16.0f, 48.0f, 79.0f };
    float mat_shininess[] =
    { 10.0f };

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_COLOR_INDEXES, mat_colormap, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);

    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);

    /*
     * jogl does not implement color index mode index color code for (int i = 0;
     * i < 32; i++) { // glut. auxSetOneColor (16 + i, 1.0 * (i/32.0), 0.0, 1.0 *
     * (i/32.0)); // glut.auxSetOneColor (48 + i, 1.0, 1.0 * (i/32.0), 1.0); }
     * gl.glClearIndex(0);
     */
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    glut.glutSolidSphere(1.0f, 20, 20);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) gl.glOrtho(-1.5, 1.5, -1.5 * (float) h / (float) w,
        1.5 * (float) h / (float) w, -10.0, 10.0);
    else gl.glOrtho(-1.5 * (float) w / (float) h, 1.5 * (float) w / (float) h,
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
