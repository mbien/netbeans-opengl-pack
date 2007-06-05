package glredbook10;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program draws a texture mapped teapot with automatically generated
 * texture coordinates. The texture is rendered as stripes on the teapot.
 * 
 * @author Kiet LE (java port)
 */
public class texgen
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;

  private static final int stripeImageWidth = 32;
  private byte stripeImage[] = new byte[3 * stripeImageWidth];
  private ByteBuffer stripeImageBuf = BufferUtil
      .newByteBuffer(stripeImage.length);
  /* glTexGen stuff: */
  private float sgenparams[] =
  { 1.0f, 1.0f, 1.0f, 0.0f };

  //
  public texgen()
  {
    super(texgen.class.getSimpleName());
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
    setSize(200, 200);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new texgen().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    makeStripeImage();
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
    gl.glTexParameterf(GL.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameterf(GL.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    gl.glTexParameterf(GL.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
    gl.glTexImage1D(GL.GL_TEXTURE_1D, 0, 3, stripeImageWidth, 0, GL.GL_RGB,
        GL.GL_UNSIGNED_BYTE, stripeImageBuf);

    gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
    gl.glTexGenfv(GL.GL_S, GL.GL_OBJECT_PLANE, sgenparams, 0);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glEnable(GL.GL_TEXTURE_GEN_S);
    gl.glEnable(GL.GL_TEXTURE_1D);
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_LIGHT0);
    gl.glEnable(GL.GL_AUTO_NORMAL);
    gl.glEnable(GL.GL_NORMALIZE);
    gl.glFrontFace(GL.GL_CW);
    gl.glCullFace(GL.GL_BACK);
    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 64.0f);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glPushMatrix();
    gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
    glut.glutSolidTeapot(2.0f);
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
    if (w <= h) gl.glOrtho(-3.5, 3.5, -3.5 * (float) h / (float) w,
        3.5 * (float) h / (float) w, -3.5, 3.5);
    else gl.glOrtho(-3.5 * (float) w / (float) h, //
        3.5 * (float) w / (float) h, -3.5, 3.5, -3.5, 3.5);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void makeStripeImage()
  {
    for (int j = 0; j < stripeImageWidth; j++)
    {
      // stripeImage[3 * j] = (j <= 4) ? 255 : 0;
      // stripeImage[3 * j + 1] = (j > 4) ? 255 : 0;
      // stripeImage[3 * j + 2] = 0;
      stripeImageBuf.put(((j <= 4) ? (byte) 255 : (byte) 0));
      stripeImageBuf.put(((j > 4) ? (byte) 255 : (byte) 0));
      stripeImageBuf.put((byte)0);
    }
    stripeImageBuf.rewind();
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
