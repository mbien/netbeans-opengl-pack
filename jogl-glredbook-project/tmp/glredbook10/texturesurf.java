package glredbook10;

import javax.swing.*;
import java.awt.event.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
 

/**
 * This program uses evaluators to generate a curved surface and automatically
 * generated texture coordinates.
 * 
 * @author Kiet Le (Java conversion)
 */

public class texturesurf
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  // as from C version of file
  private static final float ctrlpoints[][][] = new float[][][]
  {
  {
  { -1.5f, -1.5f, 4.0f },
  { -0.5f, -1.5f, 2.0f },
  { 0.5f, -1.5f, -1.0f },
  { 1.5f, -1.5f, 2.0f } },
  {
  { -1.5f, -0.5f, 1.0f },
  { -0.5f, -0.5f, 3.0f },
  { 0.5f, -0.5f, 0.0f },
  { 1.5f, -0.5f, -1.0f } },
  {
  { -1.5f, 0.5f, 4.0f },
  { -0.5f, 0.5f, 0.0f },
  { 0.5f, 0.5f, 3.0f },
  { 1.5f, 0.5f, 4.0f } },
  {
  { -1.5f, 1.5f, -2.0f },
  { -0.5f, 1.5f, -2.0f },
  { 0.5f, 1.5f, 0.0f },
  { 1.5f, 1.5f, -1.0f } } };
  // need float buffer instead of n-dimensional array above
  private FloatBuffer ctrlpointsBuf = BufferUtil
      .newFloatBuffer(ctrlpoints.length * ctrlpoints[0].length
                      * ctrlpoints[0][0].length);
  {// SO copy 4x4x3 array above to float buffer
    for (int i = 0; i < ctrlpoints.length; i++)
      // System.out.print(ctrlpoints.length+ " ");
      for (int j = 0; j < ctrlpoints[0].length; j++)
      {
        // System.out.println(ctrlpoints[0][0].length+" ");
        for (int k = 0; k < ctrlpoints[0][0].length; k++)
        {
          ctrlpointsBuf.put(ctrlpoints[i][j][k]);
          // System.out.print(ctrlpoints[i][j][k] + " ");
        }
        // System.out.println(ctrlpointsBuf.toString());
      }
    // THEN rewind it before use
    ctrlpointsBuf.rewind();
  }
  private float[][][] texpts = new float[][][]
  {
  {
  { 0.0f, 0.0f },
  { 0.0f, 1.0f } },
  {
  { 1.0f, 0.0f },
  { 1.0f, 1.0f } } };
  private FloatBuffer texptsBuf = //
  BufferUtil
      .newFloatBuffer(texpts.length * texpts[0].length * texpts[1].length);
  {
    for (int i = 0; i < texpts.length; i++)
      // System.out.print(ctrlpoints.length+ " ");
      for (int j = 0; j < texpts[0].length; j++)
      {
        // System.out.println(ctrlpoints[0][0].length+" ");
        for (int k = 0; k < texpts[0][0].length; k++)
        {
          texptsBuf.put(texpts[i][j][k]);
          // System.out.print(texpts[i][j][k] + " ");
        }
        // System.out.println(texptsBuf.toString());
      }
    // THEN rewind it before use
    texptsBuf.rewind();

  }
  private static final int imageWidth = 64;
  private static final int imageHeight = 64;
  private static byte image[] = new byte[3 * imageWidth * imageHeight];
  private static ByteBuffer imageBuf = BufferUtil.newByteBuffer(image.length);

  //
  public texturesurf()
  {
    super(texturesurf.class.getSimpleName());
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
    setSize(300, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new texturesurf().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glMap2f(GL.GL_MAP2_VERTEX_3, 0, 1, 3, 4, 0, 1, 12, 4, ctrlpointsBuf);
    gl.glMap2f(GL.GL_MAP2_TEXTURE_COORD_2, 0, 1, 2, 2, 0, 1, 4, 2, texptsBuf);
    gl.glEnable(GL.GL_MAP2_TEXTURE_COORD_2);
    gl.glEnable(GL.GL_MAP2_VERTEX_3);
    gl.glMapGrid2f(20, 0.0f, 1.0f, 20, 0.0f, 1.0f);
    makeImage();
    gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, imageWidth, imageHeight, 0,
        GL.GL_RGB, GL.GL_UNSIGNED_BYTE, imageBuf);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glEvalMesh2(GL.GL_FILL, 0, 20, 0, 20);
    gl.glFlush();

  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) gl.glOrtho(-4.0, 4.0, -4.0 * (float) h / (float) w,
        4.0 * (float) h / (float) w, -4.0, 4.0);
    else gl.glOrtho(-4.0 * (float) w / (float) h, //
        4.0 * (float) w / (float) h, -4.0, 4.0, -4.0, 4.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glRotatef(85.0f, 1.0f, 1.0f, 1.0f);
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void makeImage()
  {
    float ti, tj;

    for (int i = 0; i < imageWidth; i++)
    {
      ti = 2.0f * (float) Math.PI * i / imageWidth;
      for (int j = 0; j < imageHeight; j++)
      {
        tj = 2.0f * (float) Math.PI * j / imageHeight;

        // image[3 * (imageHeight * i + j)] = (byte) (255 * (1.0 + sin(ti)));
        // image[3 * (imageHeight * i + j) + 1] = (byte) (255 * (1.0 + cos(2 *
        // tj)));
        // image[3 * (imageHeight * i + j) + 2] = (byte) (255 * (1.0 + cos(ti +
        // tj)));
        // image[3 * (imageHeight * i + j) + 2] = (byte)0xff;
        imageBuf.put((byte) (127 * (1.0 + Math.sin(ti))));
        imageBuf.put((byte) (127 * (1.0 + Math.cos(2 * tj))));
        imageBuf.put((byte) (127 * (1.0 + Math.cos(ti + tj))));
      }
    }
    imageBuf.rewind();
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
