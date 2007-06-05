package glredbook10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 
import java.nio.*;

/**
 * Use of multiple names and picking are demonstrated. A 3x3 grid of squares is
 * drawn. When the left mouse button is pressed, all squares under the cursor
 * position have their color changed.
 * 
 * @author Kiet Le (java port)
 */
public class picksquare
  extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLCapabilities caps;
  private GLCanvas canvas;
  private GLU glu;
  //
  private int board[][] = new int[3][3]; /* amount of color for each square */
  private static final int BUFSIZE = 512;
  private Point pickPoint;

  public picksquare()
  {
    super(picksquare.class.getSimpleName());
    //
    canvas = new GLCanvas();
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    canvas.addMouseListener(this);
    //
    add(canvas);
  }

  public void run()
  {
    setSize(512, 256);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new picksquare().run();
  }

  /* Clear color value for every square on the board */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    int i, j;
    for (i = 0; i < 3; i++)
      for (j = 0; j < 3; j++)
        board[i][j] = 0;
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    drawSquares(gl, GL.GL_RENDER);
    if (pickPoint != null) pickSquares(gl);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluOrtho2D(0.0, 3.0, 0.0, 3.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();

  }

  public void displayChanged(GLAutoDrawable drawable,//
      boolean modeChanged, boolean deviceChanged)
  {
  }

  /*
   * The nine squares are drawn. In selection mode, each square is given two
   * names: one for the row and the other for the column on the grid. The color
   * of each square is determined by its position on the grid, and the value in
   * the board[][] array.
   */
  private void drawSquares(GL gl, int mode)
  {
    int i, j;
    for (i = 0; i < 3; i++)
    {
      if (mode == GL.GL_SELECT) gl.glLoadName(i);
      for (j = 0; j < 3; j++)
      {
        if (mode == GL.GL_SELECT) gl.glPushName(j);
        gl.glColor3f((float) i / 3.0f, (float) j / 3.0f,
            (float) board[i][j] / 3.0f);
        gl.glRecti(i, j, i + 1, j + 1);
        if (mode == GL.GL_SELECT) gl.glPopName();
      }
    }
  }

  /*
   * processHits prints out the contents of the selection array.
   */
  private void processHits(int hits, int buffer[])
  {
    int i, j;
    int ii = 0, jj = 0, names, ptr = 0;

    System.out.println("hits =  " + hits);
    // ptr = (GLuint *) buffer;
    for (i = 0; i < hits; i++)
    { /* for each hit */
      names = buffer[ptr];
      System.out.println(" number of names for this hit = " + names);
      ptr++;
      System.out.println("  z1 is  " + (float) buffer[ptr] / 0x7fffffff);
      ptr++;
      System.out.println(" z2 is " + (float) buffer[ptr] / 0x7fffffff);
      ptr++;
      System.out.println("   names are ");
      for (j = 0; j < names; j++)
      { /* for each name */
        System.out.println("" + buffer[ptr]);
        if (j == 0) /* set row and column */
        ii = buffer[ptr];
        else if (j == 1) jj = buffer[ptr];
        ptr++;
      }
      System.out.println("\n");
      board[ii][jj] = (board[ii][jj] + 1) % 3;
    }
  }

  /*
   * pickSquares() sets up selection mode, name stack, and projection matrix for
   * picking. Then the objects are drawn.
   */
  // private void pickSquares(GL gl, int button, int state, int x, int y)
  private void pickSquares(GL gl)
  {
    int selectBuf[] = new int[BUFSIZE];
    IntBuffer selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
    int hits;
    int viewport[] = new int[4];

    // if (button != GLUT_LEFT_BUTTON || state != GLUT_DOWN) return;

    gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

    gl.glSelectBuffer(BUFSIZE, selectBuffer);
    gl.glRenderMode(GL.GL_SELECT);

    gl.glInitNames();
    gl.glPushName(0);

    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glPushMatrix();
    gl.glLoadIdentity();
    /* create 5x5 pixel picking region near cursor location */
    glu.gluPickMatrix((double) pickPoint.x,
        (double) (viewport[3] - pickPoint.y),// 
        5.0, 5.0, viewport, 0);
    glu.gluOrtho2D(0.0, 3.0, 0.0, 3.0);
    drawSquares(gl, GL.GL_SELECT);

    gl.glMatrixMode(GL.GL_PROJECTION);
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

  public void mouseClicked(MouseEvent arg0)
  {
  }

  public void mousePressed(MouseEvent mouse)
  {
    if (mouse.getButton() == MouseEvent.BUTTON1) //
    {
      pickPoint = mouse.getPoint();
      canvas.display();
    }
  }

  public void mouseReleased(MouseEvent arg0)
  {
  }

  public void mouseEntered(MouseEvent arg0)
  {
  }

  public void mouseExited(MouseEvent arg0)
  {
  }

}
