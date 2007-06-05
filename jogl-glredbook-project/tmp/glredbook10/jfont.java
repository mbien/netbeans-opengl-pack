package glredbook10;

/**
 * Draws some text in using GlyphVector.<br>
 * This example is my replacement for xfont.c.
 * 
 * @author Kiet Le
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;

import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

public class jfont
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private int base;

  //
  public jfont()
  {
    super(jfont.class.getSimpleName());
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
    new jfont().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //

    gl.glShadeModel(GL.GL_FLAT);
    gl.glEnable(gl.GL_POLYGON_SMOOTH);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glPushMatrix();
    gl.glTranslatef(getWidth() / 2 - getWidth() / 4, //
        getHeight() / 2, 0);
    // GL has lower left origin compare java's upper left
    gl.glScalef(5, -5, 0);

    gl.glColor3f(0, 0, 1);
    drawString(gl, "OpenGL", true);

    gl.glTranslatef(-20, -20, 0);
    gl.glScalef(0.75f, 1, 0);
    gl.glColor3f(1, 0, 0);
    drawString(gl, "javax.media.opengl", false);

    gl.glTranslatef(20, 40, 0);
    drawString(gl, "Ka-EL", false);
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
    gl.glOrtho(0, (float) w, 0, (float) h, -1.0, 1.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void drawString(GL gl, String s, boolean drawBounds)
  {
    Font font = getFont();
    System.out.println(font.toString());
    Graphics2D g2 = (Graphics2D) getGraphics();
    FontMetrics fontInfo = g2.getFontMetrics(font);
    GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), s);
    Shape shape = gv.getOutline();
    // System.out.println(gv.toString());
    PathIterator itor = shape.getPathIterator(null, 0.01f);// very fine grain
    int it = 0;
    float seg[] = new float[6];
    if (drawBounds) drawGlyphBounds(gl, shape.getBounds());
    gl.glBegin(GL.GL_LINE_LOOP);
    while (!itor.isDone())
    {
      System.out.println(++it + " " + seg[0] + " " + seg[1]);
      itor.currentSegment(seg);
      gl.glVertex2f(seg[0], seg[1]);
      itor.next();
      gl.glColor3d(Math.random(), Math.random(), Math.random());
    }
    gl.glEnd();
  }

  private void drawGlyphBounds(GL gl, Rectangle r)
  {
    gl.glBegin(GL.GL_LINE_LOOP);
    gl.glVertex2f(r.x, r.y);
    gl.glVertex2f(r.x + r.width, r.y);
    gl.glVertex2f(r.x + r.width, r.height + r.y);
    gl.glVertex2f(r.x, r.y + r.height);
    gl.glEnd();
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
