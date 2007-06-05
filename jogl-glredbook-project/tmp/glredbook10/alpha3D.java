package glredbook10;

import java.awt.event.*;

import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates how to intermix opaque and alpha blended polygons
 * in the same scene, by using glDepthMask. Pressing the left mouse button
 * toggles the eye position.
 * 
 * @author Kiet Le (Java conversion)
 */
public class alpha3D
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private boolean eyePosition = false;

  //
  public alpha3D()
  {
    super(alpha3D.class.getSimpleName());
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
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new alpha3D().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float mat_ambient[] =
    { 0.0f, 0.0f, 0.0f, 0.15f };
    float mat_specular[] =
    { 1.0f, 1.0f, 1.0f, 0.15f };
    float mat_shininess[] =
    { 15.0f };

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    float position[] =
    { 0.0f, 0.0f, 1.0f, 1.0f };
    float mat_torus[] =
    { 0.75f, 0.75f, 0.0f, 1.0f };
    float mat_cylinder[] =
    { 0.0f, 0.75f, 0.75f, 0.15f };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
    gl.glPushMatrix();
    if (eyePosition) glu.gluLookAt(0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    else glu.gluLookAt(0.0, 0.0, -9.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    gl.glPushMatrix();
    gl.glTranslatef(0.0f, 0.0f, 1.0f);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_torus, 0);
    glut.glutSolidTorus(0.275, 0.85, 10, 10);
    gl.glPopMatrix();

    gl.glEnable(GL.GL_BLEND);
    gl.glDepthMask(false);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_cylinder, 0);
    gl.glTranslatef(0.0f, 0.0f, -1.0f);
    glut.glutSolidCube(2.0f);// (1.0, 2.0);
    gl.glDepthMask(true);
    gl.glDisable(GL.GL_BLEND);
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
    glu.gluPerspective(30.0, (float) w / (float) h, 1.0, 20.0);
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

  public void mouseClicked(MouseEvent mouse)
  {
  }

  public void mousePressed(MouseEvent mouse)
  {
    if (mouse.getButton() == MouseEvent.BUTTON1) //
    eyePosition = !eyePosition;
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
