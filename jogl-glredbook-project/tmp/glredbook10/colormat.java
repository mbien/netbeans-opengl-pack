package glredbook10;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.ChangedCharSetException;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * After initialization, the program will be in ColorMaterial mode. Pressing the
 * mouse buttons will change the color of the diffuse reflection.
 * 
 * @author Kiet Le (Java conversion)
 */
public class colormat
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  private float diffuseMaterial[] =
  { 0.5f, 0.5f, 0.5f, 1.0f };
  private boolean diffuseColorChanged = false;

  //
  public colormat()
  {
    super(colormat.class.getSimpleName());
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
    new colormat().run();
  }

  /*
   * Initialize values for material property, light source, lighting model, and
   * depth buffer.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float mat_specular[] =
    { 1.0f, 1.0f, 1.0f, 1.0f };
    float light_position[] =
    { 1.0f, 1.0f, 1.0f, 0.0f };

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diffuseMaterial, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 25.0f);
    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);

    gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
    gl.glEnable(GL.GL_COLOR_MATERIAL);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    if (diffuseColorChanged)
    {
      gl.glColor4fv(diffuseMaterial, 0);
      diffuseColorChanged = !diffuseColorChanged;
    }

    glut.glutSolidSphere(1.0f, 10, 10);
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

  private void changeRedDiffuse()
  {
    diffuseMaterial[0] += 0.1;
    if (diffuseMaterial[0] > 1.0) diffuseMaterial[0] = 0.0f;
    // gl.glColor4fv(diffuseMaterial, 0);
  }

  private void changeGreenDiffuse()
  {
    diffuseMaterial[1] += 0.1;
    if (diffuseMaterial[1] > 1.0) diffuseMaterial[1] = 0.0f;
    // gl.glColor4fv(diffuseMaterial, 0);
  }

  private void changeBlueDiffuse()
  {
    diffuseMaterial[2] += 0.1;
    if (diffuseMaterial[2] > 1.0) diffuseMaterial[2] = 0.0f;
    // gl.glColor4fv(diffuseMaterial, 0);
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
    switch (mouse.getButton()) {
      case MouseEvent.BUTTON1:
        changeRedDiffuse();
        diffuseColorChanged = true;
        break;
      case MouseEvent.BUTTON2:
        changeGreenDiffuse();
        diffuseColorChanged = true;
        break;
      case MouseEvent.BUTTON3:
        changeBlueDiffuse();
        diffuseColorChanged = true;
        break;
      default:
        break;
    }
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
