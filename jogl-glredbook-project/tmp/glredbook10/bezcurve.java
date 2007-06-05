package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program uses evaluators to draw a Bezier curve.
 * 
 * @author Kiet Le (Java conversion)
 */
public class bezcurve
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  private float ctrlpoints[][] = new float[][]
  {
  { -4.0f, -4.0f, 0.0f },
  { -2.0f, 4.0f, 0.0f },
  { 2.0f, -4.0f, 0.0f },
  { 4.0f, 4.0f, 0.0f } };
  private FloatBuffer ctrlpointBuf = //
  FloatBuffer.allocate(ctrlpoints[0].length * ctrlpoints.length);

  //
  public bezcurve()
  {
    super(bezcurve.class.getSimpleName());
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
    new bezcurve().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    // need to convert 2d array to buffer type
    for (int i = 0; i < ctrlpoints.length; i++)
    {
      for (int j = 0; j < 3; j++)
      {
        ctrlpointBuf.put(ctrlpoints[i][j]);
      }
    }
    ctrlpointBuf.rewind();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_FLAT);
    gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, ctrlpointBuf);
    gl.glEnable(GL.GL_MAP1_VERTEX_3);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glBegin(GL.GL_LINE_STRIP);
    for (int i = 0; i <= 30; i++)
    {
      gl.glEvalCoord1f((float) i / (float) 30.0);
    }
    gl.glEnd();
    /* The following code displays the control points as dots. */
    gl.glPointSize(5.0f);
    gl.glColor3f(1.0f, 1.0f, 0.0f);
    gl.glBegin(GL.GL_POINTS);
    for (int i = 0; i < 4; i++)
    {
      gl.glVertex3fv(ctrlpointBuf);
      ctrlpointBuf.position(i * 3);
    }
    gl.glEnd();
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
    gl.glOrtho(-5.0, 5.0, -5.0 * (float) h / (float) w, //
        5.0 * (float) h / (float) w, -5.0, 5.0);
    else gl.glOrtho(-5.0 * (float) w / (float) h, //
        5.0 * (float) w / (float) h,//
        -5.0, 5.0, -5.0, 5.0);
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
