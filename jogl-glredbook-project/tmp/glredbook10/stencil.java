package glredbook10;

import javax.swing.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates use of the stencil buffer for masking
 * nonrectangular regions. Whenever the window is redrawn, a value of 1 is drawn
 * into a diamond-shaped region in the stencil buffer. Elsewhere in the stencil
 * buffer, the value is 0. Then a blue sphere is drawn where the stencil value
 * is 1, and yellow torii are drawn where the stencil value is not 1.
 * 
 * @author Kiet Le (java port)
 */
public class stencil
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLCapabilities caps;
  private GLCanvas canvas;
  private GLU glu;
  private GLUT glut;

  private static final int YELLOWMAT = 1;
  private static final int BLUEMAT = 2;

  public stencil()
  {
    super(stencil.class.getSimpleName());
    // Be certain to request stencil bits.
    caps = new GLCapabilities();
    caps.setStencilBits(8);
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
    new stencil().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float yellow_diffuse[] = new float[]
    { 0.7f, 0.7f, 0.0f, 1.0f };
    float yellow_specular[] = new float[]
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float blue_diffuse[] = new float[]
    { 0.1f, 0.1f, 0.7f, 1.0f };
    float blue_specular[] = new float[]
    { 0.1f, 1.0f, 1.0f, 1.0f };
    float position_one[] = new float[]
    { 1.0f, 1.0f, 1.0f, 0.0f };
    //
    gl.glNewList(YELLOWMAT, GL.GL_COMPILE);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, yellow_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, yellow_specular, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 64.0f);
    gl.glEndList();

    gl.glNewList(BLUEMAT, GL.GL_COMPILE);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, blue_diffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, blue_specular, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 45.0f);
    gl.glEndList();

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position_one, 0);

    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);

    gl.glClearStencil(0x0);
    gl.glEnable(GL.GL_STENCIL_TEST);

  }

  /*
   * Draw a sphere in a diamond-shaped section in the middle of a window with 2
   * torii.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    /* draw blue sphere where the stencil is 1 */
    gl.glStencilFunc(GL.GL_EQUAL, 0x1, 0x1);
    gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
    gl.glCallList(BLUEMAT);
    glut.glutSolidSphere(0.5, 20, 20);

    /* draw the tori where the stencil is not 1 */
    gl.glStencilFunc(GL.GL_NOTEQUAL, 0x1, 0x1);
    gl.glPushMatrix();
    gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
    gl.glCallList(YELLOWMAT);
    glut.glutSolidTorus(0.275, 0.85, 20, 20);
    gl.glPushMatrix();
    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    glut.glutSolidTorus(0.275, 0.85, 20, 20);
    gl.glPopMatrix();
    gl.glPopMatrix();
  }

  /*
   * Whenever the window is reshaped, redefine the coordinate system and redraw
   * the stencil area.
   */
  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glClear(GL.GL_STENCIL_BUFFER_BIT);
    /* create a diamond shaped stencil area */
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrtho(-3.0, 3.0, -3.0, 3.0, -1.0, 1.0);
    // if (w <= h) glu.gluOrtho2D(-3.0, 3.0, -3.0 * (float) h / (float) w,
    // 3.0 * (float) h / (float) w);
    // else glu.gluOrtho2D(-3.0 * (float) w / (float) h, //
    // 3.0 * (float) w / (float) h, -3.0, 3.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();

    gl.glStencilFunc(GL.GL_ALWAYS, 0x1, 0x1);
    gl.glStencilOp(GL.GL_REPLACE, GL.GL_REPLACE, GL.GL_REPLACE);
    gl.glBegin(GL.GL_QUADS);
    gl.glVertex2f(-1.0f, 0.0f);
    gl.glVertex2f(0.0f, 1.0f);
    gl.glVertex2f(1.0f, 0.0f);
    gl.glVertex2f(0.0f, -1.0f);
    gl.glEnd();

    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(45.0, (float) w / (float) h, 3.0, 7.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -5.0f);

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
