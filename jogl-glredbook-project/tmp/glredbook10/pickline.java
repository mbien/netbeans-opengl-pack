package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * Picking is demonstrated in this program. In rendering mode, three overlapping
 * rectangles are drawn. When the left mouse button is pressed, selection mode
 * is entered with the picking matrix. Rectangles which are drawn under the
 * cursor position are "picked." Pay special attention to the depth value range,
 * which is returned.
 * 
 * @author Kiet Le (Java port)
 */
public class pickline
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private static final int BUFSIZE = 512;
  private Point pickPoint = new Point();

  //
  public pickline()
  {
    super(pickline.class.getSimpleName());
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
    setSize(100, 100);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new pickline().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    drawLine(gl, GL.GL_RENDER);
    pickLine(gl);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluOrtho2D(0.0, (double) w, 0.0, (double) h);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void drawLine(GL gl, int mode)
  {
    if (mode == GL.GL_SELECT) gl.glLoadName(1);
    gl.glBegin(GL.GL_LINES);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glVertex3f(30.0f, 30.0f, 0.0f);
    gl.glVertex3f(50.0f, 60.0f, 0.0f);
    gl.glEnd();

    if (mode == GL.GL_SELECT) gl.glLoadName(2);
    gl.glBegin(GL.GL_LINES);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glVertex3f(50.0f, 60.0f, 0.0f);
    gl.glVertex3f(70.0f, 40.0f, 0.0f);
    gl.glEnd();
  }

  /*
   * pickline() is called when the mouse is pressed. The projection matrix is
   * reloaded to include the picking matrix. The line is "redrawn" during
   * selection mode, and names are sent to the buffer.
   */
  private void pickLine(GL gl)
  {
    int selectBuf[] = new int[BUFSIZE];
    IntBuffer selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
    int hits;
    int viewport[] = new int[4];

    gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

    gl.glSelectBuffer(BUFSIZE, selectBuffer);
    gl.glRenderMode(GL.GL_SELECT);

    gl.glInitNames();
    gl.glPushName(-1);

    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glPushMatrix();
    gl.glLoadIdentity();
    glu.gluPickMatrix((double) pickPoint.x,
        (double) (viewport[3] - pickPoint.y),// 
        5.0, 5.0, viewport, 0);
    glu.gluOrtho2D(0.0, (double) viewport[2], //
        0.0, (double) viewport[3]);
    drawLine(gl, GL.GL_SELECT);
    gl.glPopMatrix();
    gl.glFlush();

    hits = gl.glRenderMode(GL.GL_RENDER);
    selectBuffer.get(selectBuf);
    System.out.println("hits is " + hits);
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
    pickPoint = mouse.getPoint();
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
