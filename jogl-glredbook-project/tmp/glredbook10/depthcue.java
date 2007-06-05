package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program draws a wireframe model, which uses intensity (brightness) to
 * give clues to distance. Fog is used to achieve this effect.
 * 
 * @author Kiet Le (Java conversion)
 */
public class depthcue
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas; 

  //
  public depthcue()
  {
    super(depthcue.class.getSimpleName());
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
    new depthcue().run();
  }

  /*
   * Initialize linear fog for depth cueing.
   */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    float fogColor[] =
    { 0.0f, 0.0f, 0.0f, 1.0f };

    gl.glEnable(GL.GL_FOG);
    gl.glFogi(GL.GL_FOG_MODE, GL.GL_LINEAR);
    gl.glHint(GL.GL_FOG_HINT, GL.GL_NICEST); /* per pixel */
    gl.glFogf(GL.GL_FOG_START, 3.0f);
    gl.glFogf(GL.GL_FOG_END, 5.0f);
    gl.glFogfv(GL.GL_FOG_COLOR, fogColor, 0);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glShadeModel(GL.GL_FLAT);
  }

  /*
   * display() draws an icosahedron.
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    glut.glutWireIcosahedron();
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(45.0, (float) w / (float) h, 3.0, 5.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -4.0f); /* move object into view */
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
