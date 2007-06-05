package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates the same scene as dof.c, but without use of the
 * accumulation buffer, so everything is in focus.
 * 
 * @author Kiet Le (Java conversion)
 */
public class dofnot
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;

  //
  public dofnot()
  {
    super(dofnot.class.getSimpleName());
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
    setSize(400, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new dofnot().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float ambient[] =
    { 0.0f, 0.0f, 0.0f, 1.0f };
    float diffuse[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float position[] =
    { 0.0f, 3.0f, 3.0f, 0.0f };

    float lmodel_ambient[] =
    { 0.2f, 0.2f, 0.2f, 1.0f };
    float local_view[] =
    { 0.0f };

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);

    gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse, 0);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);

    gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
    gl.glLightModelfv(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

    gl.glFrontFace(GL.GL_CW);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_AUTO_NORMAL);
    gl.glEnable(GL.GL_NORMALIZE);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /*
   * display() draws 5 teapots into the accumulation buffer several times; each
   * time with a jittered perspective. The focal point is at z = 5.0, so the
   * gold teapot will stay in focus. The amount of jitter is adjusted by the
   * magnitude of the accPerspective() jitter; in this example, 0.33. In this
   * example, the teapots are drawn 8 times. See jitter.h
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    gl.glPushMatrix();
    /* ruby, gold, silver, emerald, and cyan teapots */
    renderTeapot(gl, -1.1f, -0.5f, -4.5f, 0.1745f, 0.01175f, 0.01175f,
        0.61424f, 0.04136f, 0.04136f, 0.727811f, 0.626959f, 0.626959f, 0.6f);
    renderTeapot(gl, -0.5f, -0.5f, -5.0f, 0.24725f, 0.1995f, 0.0745f, 0.75164f,
        0.60648f, 0.22648f, 0.628281f, 0.555802f, 0.366065f, 0.4f);
    renderTeapot(gl, 0.2f, -0.5f, -5.5f, 0.19225f, 0.19225f, 0.19225f,
        0.50754f, 0.50754f, 0.50754f, 0.508273f, 0.508273f, 0.508273f, 0.4f);
    renderTeapot(gl, 1.0f, -0.5f, -6.0f, 0.0215f, 0.1745f, 0.0215f, 0.07568f,
        0.61424f, 0.07568f, 0.633f, 0.727811f, 0.633f, 0.6f);
    renderTeapot(gl, 1.8f, -0.5f, -6.5f, 0.0f, 0.1f, 0.06f, 0.0f, 0.5098039f,
        0.50980392f, 0.50196078f, 0.50196078f, 0.50196078f, .25f);

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
    glu.gluPerspective(45.0, (float) w / (float) h, 1.0, 15.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void renderTeapot(GL gl, float x, float y, float z, float ambr,
      float ambg, float ambb, float difr, float difg, float difb, float specr,
      float specg, float specb, float shine)
  {
    float mat[] = new float[4];

    gl.glPushMatrix();
    gl.glTranslatef(x, y, z);
    mat[0] = ambr;
    mat[1] = ambg;
    mat[2] = ambb;
    mat[3] = 1.0f;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat, 0);
    mat[0] = difr;
    mat[1] = difg;
    mat[2] = difb;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat, 0);
    mat[0] = specr;
    mat[1] = specg;
    mat[2] = specb;
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, shine * 128.0f);
    glut.glutSolidTeapot(0.5);
    gl.glPopMatrix();
  }

  public void keyTyped(KeyEvent key)
  {
    // TODO Auto-generated method stub
  }

  public void keyPressed(KeyEvent key)
  {
    if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
    {
      new Thread()
      {
        public void run()
        {
          animator.stop();
        }
      }.start();
      System.exit(0);
    }
    else switch (key.getKeyChar()) {
      case ' ':
        System.out.println("space pressed.");
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
