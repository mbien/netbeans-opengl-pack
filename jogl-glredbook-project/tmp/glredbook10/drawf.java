package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * Draws the bitmapped letter F on the screen (several times). This demonstrates
 * use of the glBitmap() call.
 * 
 * @author Kiet Le (Java conversion)
 */
public class drawf
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLCapabilities caps;
  private GLCanvas canvas;
  private FPSAnimator animator;
  private byte rasters[] = new byte[]
  { (byte) 0xc0, (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0xc0,
    (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0xc0, (byte) 0x00,
    (byte) 0xff, (byte) 0x00, (byte) 0xff, (byte) 0x00, (byte) 0xc0,
    (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0xc0, (byte) 0x00,
    (byte) 0xff, (byte) 0xc0, (byte) 0xff, (byte) 0xc0 };

  //
  public drawf()
  {
    super(drawf.class.getSimpleName());
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
    new drawf().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    gl.glRasterPos2f(20.5f, 20.5f);
    gl.glBitmap(10, 12, 0.0f, 0.0f, 12.0f, 0.0f, rasters, 0);
    gl.glBitmap(10, 12, 0.0f, 0.0f, 12.0f, 0.0f, rasters, 0);
    gl.glBitmap(10, 12, 0.0f, 0.0f, 12.0f, 0.0f, rasters, 0);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrtho(0, w, 0, h, -1.0, 1.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  public void keyTyped(KeyEvent key)
  {
    // TODO Auto-generated method stub
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
    // TODO Auto-generated method stub
  }

}
