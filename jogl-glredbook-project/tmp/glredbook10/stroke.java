package glredbook10;

/**
 * This program demonstrates some characters of a stroke (vector) font. The
 * characters are represented by display lists, which are given numbers which
 * correspond to the ASCII values of the characters. Use of glCallLists() is
 * demonstrated.
 * 
 * @author Kiet Le (Java port)
 */
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

public class stroke
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  //
  private static final int PT = 1;
  private static final int STROKE = 2;
  private static final int END = 3;

  // * C struct. Compare this to charpoint class.
  // typedef struct charpoint
  // {
  // GLfloat x, y;
  // int type;
  // } CP;
  private class charpoint
  {
    public float x;
    public float y;
    public int type;

    public charpoint(float x, float y, int type)
    {
      this.x = x;
      this.y = y;
      this.type = type;
    }
  }

  // * Saved here from original for you to see the difference
  // CP Adata[] =
  // {
  // { 0, 0, PT}, {0, 9, PT}, {1, 10, PT}, {4, 10, PT},
  // {5, 9, PT}, {5, 0, STROKE}, {0, 5, PT}, {5, 5, END}
  // };
  charpoint Adata[] =
  { new charpoint(0, 0, PT), new charpoint(0, 9, PT), //
    new charpoint(1, 10, PT), new charpoint(4, 10, PT),//
    new charpoint(5, 9, PT), new charpoint(5, 0, STROKE),//
    new charpoint(0, 5, PT), new charpoint(5, 5, END) };

  charpoint Edata[] =
  { new charpoint(5, 0, PT), new charpoint(0, 0, PT), //
    new charpoint(0, 10, PT), new charpoint(5, 10, STROKE),//
    new charpoint(0, 5, PT), new charpoint(4, 5, END) };

  charpoint Pdata[] =
  { new charpoint(0, 0, PT), new charpoint(0, 10, PT),//
    new charpoint(4, 10, PT), new charpoint(5, 9, PT),//
    new charpoint(5, 6, PT), new charpoint(4, 5, PT), //
    new charpoint(0, 5, END) };

  charpoint Rdata[] =
  { new charpoint(0, 0, PT), new charpoint(0, 10, PT),//
    new charpoint(4, 10, PT), new charpoint(5, 9, PT),//
    new charpoint(5, 6, PT), new charpoint(4, 5, PT),//
    new charpoint(0, 5, STROKE), new charpoint(3, 5, PT),//
    new charpoint(5, 0, END) };

  charpoint Sdata[] =
  { new charpoint(0, 1, PT), new charpoint(1, 0, PT), //
    new charpoint(4, 0, PT), new charpoint(5, 1, PT),//
    new charpoint(5, 4, PT), new charpoint(4, 5, PT), //
    new charpoint(1, 5, PT), new charpoint(0, 6, PT), //
    new charpoint(0, 9, PT), new charpoint(1, 10, PT),//
    new charpoint(4, 10, PT), new charpoint(5, 9, END) };

  // char *test1 = "A SPARE SERAPE APPEARS AS"; C char ptr to str ...
  // char *test2 = "APES PREPARE RARE PEPPERS";
  private String test1 = "A SPARE SERAPE APPEARS AS";// String object
  private String test2 = "APES PREPARE RARE PEPPERS";

  public stroke()
  {
    super(stroke.class.getSimpleName());
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
    setSize(440, 120);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new stroke().run();
  }

  /* Create a display list for each of 6 characters */
  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    int base;

    gl.glShadeModel(GL.GL_FLAT);

    base = gl.glGenLists(128);
    gl.glListBase(base);
    gl.glNewList(base + 'A', GL.GL_COMPILE);
    drawLetter(gl, Adata);
    gl.glEndList();
    gl.glNewList(base + 'E', GL.GL_COMPILE);
    drawLetter(gl, Edata);
    gl.glEndList();
    gl.glNewList(base + 'P', GL.GL_COMPILE);
    drawLetter(gl, Pdata);
    gl.glEndList();
    gl.glNewList(base + 'R', GL.GL_COMPILE);
    drawLetter(gl, Rdata);
    gl.glEndList();
    gl.glNewList(base + 'S', GL.GL_COMPILE);
    drawLetter(gl, Sdata);
    gl.glEndList();
    gl.glNewList(base + ' ', GL.GL_COMPILE);
    gl.glTranslatef(8.0f, 0.0f, 0.0f);
    gl.glEndList();
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);

    gl.glPushMatrix();
    gl.glScalef(2.0f, 2.0f, 2.0f);
    gl.glTranslatef(10.0f, 30.0f, 0.0f);
    this.printStrokedString(gl, test1);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glScalef(2.0f, 2.0f, 2.0f);
    gl.glTranslatef(10.0f, 13.0f, 0.0f);
    this.printStrokedString(gl, test2);
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
    glu.gluOrtho2D(0, w, 0, h);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  /*
   * interprets the instructions from the array for that letter and renders the
   * letter with line segments.
   */
  private void drawLetter(GL gl, charpoint[] l)
  {
    int i = 0;
    gl.glBegin(GL.GL_LINE_STRIP);
    while (i < l.length)
    {
      switch (l[i].type) {
        case PT:
          gl.glVertex2f(l[i].x, l[i].y);
          break;
        case STROKE:
          gl.glVertex2f(l[i].x, l[i].y);
          gl.glEnd();
          gl.glBegin(GL.GL_LINE_STRIP);
          break;
        case END:
          gl.glVertex2f(l[i].x, l[i].y);
          gl.glEnd();
          gl.glTranslatef(8.0f, 0.0f, 0.0f);
          return;
      }
      i++;// System.out.println(i+" ");
    }
  }

  // private void printStrokedString(char *s)
  private void printStrokedString(GL gl, String s)
  {
    int len = s.length();
    ByteBuffer str = BufferUtil.newByteBuffer(len);
    str.put(s.getBytes());
    str.rewind();
    gl.glCallLists(len, GL.GL_BYTE, str);
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
