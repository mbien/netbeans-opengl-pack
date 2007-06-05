package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program demonstrates using mipmaps for texture maps. To overtly show the
 * effect of mipmaps, each mipmap reduction level has a solidly colored,
 * contrasting texture image. Thus, the quadrilateral which is drawn is drawn
 * with several different colors.
 * 
 * @author Kiet Le
 */
public class mipmap
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private static final int color = 3;
  // private byte mipmapImage32[][][] = new byte[32][32][color];
  // private byte mipmapImage16[][][] = new byte[16][16][color];
  // private byte mipmapImage8[][][] = new byte[8][8][color];
  // private byte mipmapImage4[][][] = new byte[4][4][color];
  // private byte mipmapImage2[][][] = new byte[2][2][color];
  private byte mipmapImage1[][][] = new byte[1][1][color];
  private ByteBuffer mipmapImage32Buf = BufferUtil
      .newByteBuffer(32 * 32 * color);
  private ByteBuffer mipmapImage16Buf = BufferUtil
      .newByteBuffer(16 * 16 * color);
  private ByteBuffer mipmapImage8Buf = BufferUtil.newByteBuffer(8 * 8 * color);
  private ByteBuffer mipmapImage4Buf = BufferUtil.newByteBuffer(4 * 4 * color);
  private ByteBuffer mipmapImage2Buf = BufferUtil.newByteBuffer(2 * 2 * color);
  private ByteBuffer mipmapImage1Buf = BufferUtil.newByteBuffer(1 * 1 * color);

  //
  public mipmap()
  {
    super(mipmap.class.getSimpleName());
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
    new mipmap().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    //
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glShadeModel(GL.GL_FLAT);

    gl.glTranslatef(0.0f, 0.0f, -3.6f);

    makeImages();

    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, 32, 32, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage32Buf);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 1, 3, 16, 16, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage16Buf);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 2, 3, 8, 8, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage8Buf);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 3, 3, 4, 4, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage4Buf);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 4, 3, 2, 2, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage2Buf);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 5, 3, 1, 1, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
        mipmapImage1Buf);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
        GL.GL_NEAREST_MIPMAP_NEAREST);
    gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
    gl.glEnable(GL.GL_TEXTURE_2D);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex3f(-2.0f, -1.0f, 0.0f);
    gl.glTexCoord2f(0.0f, 8.0f);
    gl.glVertex3f(-2.0f, 1.0f, 0.0f);
    gl.glTexCoord2f(8.0f, 8.0f);
    gl.glVertex3f(2000.0f, 1.0f, -6000.0f);
    gl.glTexCoord2f(8.0f, 0.0f);
    gl.glVertex3f(2000.0f, -1.0f, -6000.0f);
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
    glu.gluPerspective(60.0, 1.0 * (float) w / (float) h, 1.0, 30000.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  /*
   * 3D arrays are never used by gl command TexImage2D. it instead use byte
   * buffer.
   */
  private void makeImages()
  {
    int i, j;

    for (i = 0; i < 32; i++)
    {
      for (j = 0; j < 32; j++)
      {
        // mipmapImage32[i][j][0] = (byte) 255;
        // mipmapImage32[i][j][1] = (byte) 255;
        // mipmapImage32[i][j][2] = (byte) 0;
        //
        mipmapImage32Buf.put((byte) 255);
        mipmapImage32Buf.put((byte) 255);
        mipmapImage32Buf.put((byte) 0);
      }
    }
    for (i = 0; i < 16; i++)
    {
      for (j = 0; j < 16; j++)
      {
        // mipmapImage16[i][j][0] = (byte) 255;
        // mipmapImage16[i][j][1] = (byte) 0;
        // mipmapImage16[i][j][2] = (byte) 255;
        //
        mipmapImage16Buf.put((byte) 255);
        mipmapImage16Buf.put((byte) 0);
        mipmapImage16Buf.put((byte) 255);
      }
    }
    for (i = 0; i < 8; i++)
    {
      for (j = 0; j < 8; j++)
      {
        // mipmapImage8[i][j][0] = (byte) 255;
        // mipmapImage8[i][j][1] = (byte) 0;
        // mipmapImage8[i][j][2] = (byte) 0;
        //
        mipmapImage8Buf.put((byte) 255);
        mipmapImage8Buf.put((byte) 0);
        mipmapImage8Buf.put((byte) 0);
      }
    }
    for (i = 0; i < 4; i++)
    {
      for (j = 0; j < 4; j++)
      {
        // mipmapImage4[i][j][0] = (byte) 0;
        // mipmapImage4[i][j][1] = (byte) 255;
        // mipmapImage4[i][j][2] = (byte) 0;
        //
        mipmapImage4Buf.put((byte) 0);
        mipmapImage4Buf.put((byte) 255);
        mipmapImage4Buf.put((byte) 0);
      }
    }
    for (i = 0; i < 2; i++)
    {
      for (j = 0; j < 2; j++)
      {
        // mipmapImage2[i][j][0] = (byte) 0;
        // mipmapImage2[i][j][1] = (byte) 0;
        // mipmapImage2[i][j][2] = (byte) 255;
        //
        mipmapImage2Buf.put((byte) 0);
        mipmapImage2Buf.put((byte) 0);
        mipmapImage2Buf.put((byte) 255);
      }
    }
    // mipmapImage1[0][0][0] = (byte) 255;
    // mipmapImage1[0][0][1] = (byte) 255;
    // mipmapImage1[0][0][2] = (byte) 255;
    //
    mipmapImage1Buf.put((byte) 255);
    mipmapImage1Buf.put((byte) 255);
    mipmapImage1Buf.put((byte) 255);
    // rewind all
    mipmapImage32Buf.rewind();
    mipmapImage16Buf.rewind();
    mipmapImage8Buf.rewind();
    mipmapImage4Buf.rewind();
    mipmapImage2Buf.rewind();
    mipmapImage1Buf.rewind();
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
