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
public class pickdepth
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private static final int BUFSIZE = 512;
  private Point pickPoint = new Point();

  //
  public pickdepth()
  {
    super(pickdepth.class.getSimpleName());
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
    new pickdepth().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glShadeModel(GL.GL_FLAT);
    gl.glDepthRange(0.0, 1.0); /* The default z mapping */
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    drawRects(gl, GL.GL_RENDER);
    pickRects(gl);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  /*
   * The three rectangles are drawn. In selection mode, each rectangle is given
   * the same name. Note that each rectangle is drawn with a different z value.
   */
  private void drawRects(GL gl, int mode)
  {
    if (mode == GL.GL_SELECT) gl.glLoadName(1);
    gl.glBegin(GL.GL_QUADS);
    gl.glColor3f(1.0f, 1.0f, 0.0f);
    gl.glVertex3i(2, 0, 0);
    gl.glVertex3i(2, 6, 0);
    gl.glVertex3i(6, 6, 0);
    gl.glVertex3i(6, 0, 0);
    gl.glColor3f(0.0f, 1.0f, 1.0f);
    gl.glVertex3i(3, 2, -1);
    gl.glVertex3i(3, 8, -1);
    gl.glVertex3i(8, 8, -1);
    gl.glVertex3i(8, 2, -1);
    gl.glColor3f(1.0f, 0.0f, 1.0f);
    gl.glVertex3i(0, 2, -2);
    gl.glVertex3i(0, 7, -2);
    gl.glVertex3i(5, 7, -2);
    gl.glVertex3i(5, 2, -2);
    gl.glEnd();
  }

  /*
   * prints out the contents of the selection array.
   */
  private void processHits(int hits, int buffer[])
  {
    int names, ptr = 0;

    System.out.println("hits = " + hits);
    // ptr = (GLuint *) buffer;
    for (int i = 0; i < hits; i++)
    { /* for each hit */
      names = buffer[ptr];
      System.out.println(" number of names for hit = " + names);
      ptr++;
      System.out.println("  z1 is " + buffer[ptr]);
      ptr++;
      System.out.println(" z2 is " + buffer[ptr]);
      ptr++;
      System.out.print("\n   the name is ");
      for (int j = 0; j < names; j++)
      { /* for each name */
        System.out.println("" + buffer[ptr]);
        ptr++;
      }
      System.out.println();
    }
  }

  /*
   * sets up selection mode, name stack, and projection matrix for picking. Then
   * the objects are drawn.
   */
  private void pickRects(GL gl)
  {
    int[] selectBuf = new int[BUFSIZE];
    IntBuffer selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
    int hits;
    int viewport[] = new int[4];
    // int x, y;

    gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

    gl.glSelectBuffer(BUFSIZE, selectBuffer);
    gl.glRenderMode(GL.GL_SELECT);

    gl.glInitNames();
    gl.glPushName(-1);

    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glPushMatrix();
    gl.glLoadIdentity();
    /* create 5x5 pixel picking region near cursor location */
    glu.gluPickMatrix((double) pickPoint.x,
        (double) (viewport[3] - pickPoint.y), //
        5.0, 5.0, viewport, 0);
    gl.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
    drawRects(gl, GL.GL_SELECT);
    gl.glPopMatrix();
    gl.glFlush();

    hits = gl.glRenderMode(GL.GL_RENDER);
    selectBuffer.get(selectBuf);
    processHits(hits, selectBuf);
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
