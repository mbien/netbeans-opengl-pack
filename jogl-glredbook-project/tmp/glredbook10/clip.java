package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates arbitrary clipping planes.
 * 
 * @author Kiet Le (Java conversion)
 */
public class clip
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  //
  public clip()
  {
    super(clip.class.getSimpleName());
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
    new clip().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glClearColor(0, 0, 0, 0);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //  
    double eqn[] =
    { 0.0, 1.0, 0.0, 0.0 };
    double eqn2[] =
    { 1.0, 0.0, 0.0, 0.0 };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);

    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 0.0f, -5.0f);

    /* clip lower half -- y < 0 */
    gl.glClipPlane(GL.GL_CLIP_PLANE0, eqn, 0);
    gl.glEnable(GL.GL_CLIP_PLANE0);
    /* clip left half -- x < 0 */
    gl.glClipPlane(GL.GL_CLIP_PLANE1, eqn2, 0);
    gl.glEnable(GL.GL_CLIP_PLANE1);

    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    glut.glutWireSphere(1.0, 20, 16);
    gl.glPopMatrix();

    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(60.0, (float) w / (float) h, 1.0, 20.0);
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
