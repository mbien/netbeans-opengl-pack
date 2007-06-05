package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program draws 5 red teapots, each at a different z distance from the
 * eye, in different types of fog. Pressing the left mouse button chooses
 * between 3 types of fog: exponential, exponential squared, and linear. In this
 * program, there is a fixed density value, as well as fixed start and end
 * values for the linear fog.
 * 
 * @author Kiet Le (Java conversion)
 */
public class fog
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private int fogMode;

  //
  public fog()
  {
    super(fog.class.getSimpleName());
    //
    caps = new GLCapabilities();
    canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    canvas.addMouseListener(this);
    //
    add(canvas);
  }

  public void run()
  {
    setSize(450, 150);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new fog().run();
  }

  /*
   * Initialize z-buffer, projection matrix, light source, and lighting model.
   * Do not specify a material property here.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float position[] =
    { 0.0f, 3.0f, 3.0f, 0.0f };
    float local_view[] =
    { 0.0f };

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
    gl.glLightModelfv(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

    gl.glFrontFace(GL.GL_CW);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_AUTO_NORMAL);
    gl.glEnable(GL.GL_NORMALIZE);
    gl.glEnable(GL.GL_FOG);
    {
      float fogColor[] =
      { 0.5f, 0.5f, 0.5f, 1.0f };

      fogMode = GL.GL_EXP;
      gl.glFogi(GL.GL_FOG_MODE, fogMode);
      gl.glFogfv(GL.GL_FOG_COLOR, fogColor, 0);
      gl.glFogf(GL.GL_FOG_DENSITY, 0.35f);
      gl.glHint(GL.GL_FOG_HINT, GL.GL_DONT_CARE);

      gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }
  }

  /*
   * display() draws 5 teapots at different z positions.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //
    if (fogMode == GL.GL_EXP2)
    {
      gl.glFogf(GL.GL_FOG_START, 1.0f);
      gl.glFogf(GL.GL_FOG_END, 5.0f);
    }
    gl.glFogi(GL.GL_FOG_MODE, fogMode);

    renderRedTeapot(gl, -4.0f, -0.5f, -1.0f);
    renderRedTeapot(gl, -2.0f, -0.5f, -2.0f);
    renderRedTeapot(gl, 0.0f, -0.5f, -3.0f);
    renderRedTeapot(gl, 2.0f, -0.5f, -4.0f);
    renderRedTeapot(gl, 4.0f, -0.5f, -5.0f);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= (h * 3)) gl.glOrtho(-6.0, 6.0, -2.0 * ((float) h * 3) / (float) w,
        2.0 * ((float) h * 3) / (float) w, 0.0, 10.0);
    else gl.glOrtho(-6.0 * (float) w / ((float) h * 3), //
        6.0 * (float) w / ((float) h * 3), -2.0, 2.0, 0.0, 10.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void cycleFog()
  {
    if (fogMode == GL.GL_EXP)
    {
      fogMode = GL.GL_EXP2;
      System.out.println("Fog mode is GL.GL_EXP2");
    }
    else if (fogMode == GL.GL_EXP2)
    {
      fogMode = GL.GL_LINEAR;
      System.out.println("Fog mode is GL.GL_LINEAR\n");
    }
    else if (fogMode == GL.GL_LINEAR)
    {
      fogMode = GL.GL_EXP;
      System.out.println("Fog mode is GL.GL_EXP\n");
    }
  }

  private void renderRedTeapot(GL gl, float x, float y, float z)
  {
    float mat[] = new float[4];

    gl.glPushMatrix();
    gl.glTranslatef(x, y, z);
    mat[0] = 0.1745f;
    mat[1] = 0.01175f;
    mat[2] = 0.01175f;
    mat[3] = 1.0f;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat, 0);
    mat[0] = 0.61424f;
    mat[1] = 0.04136f;
    mat[2] = 0.04136f;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat, 0);
    mat[0] = 0.727811f;
    mat[1] = 0.626959f;
    mat[2] = 0.626959f;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.6f * 128.0f);
    glut.glutSolidTeapot(1.0);
    gl.glPopMatrix();
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

  public void mouseClicked(MouseEvent mouse)
  {
  }

  public void mousePressed(MouseEvent mouse)
  {
    cycleFog();
    canvas.display();
  }

  public void mouseReleased(MouseEvent mouse)
  {
  }

  public void mouseEntered(MouseEvent mouse)
  {
  }

  public void mouseExited(MouseEvent mouse)
  {
  }

}
