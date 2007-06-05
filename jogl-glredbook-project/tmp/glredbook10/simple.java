package glredbook10;
/**
 * This program draws a white rectangle on a black background.
 * @author Kiet Le (Java port)
 */
import javax.media.opengl.*;
 
import javax.swing.*;

public class simple
{
  public static void main(String[] args)
  {
    /*
     * remove/commet out to be able to resize window smaller
     */
    JFrame.setDefaultLookAndFeelDecorated(true);
    // name of class as title
    JFrame jframe = new JFrame(simple.class.getSimpleName());
    jframe.setSize(500, 500);
    jframe.setLocationRelativeTo(null); // center of screen

    GLCanvas canvas = new GLCanvas();
    //GLJPanel jcanvas = new GLJPanel();
    // anonymous object of GLEventListener interface
//    jcanvas.addGLEventListener(new GLEventListener()
    canvas.addGLEventListener(new GLEventListener()
    {
      public void init(GLAutoDrawable drawable)
      {
        // TODO Auto-generated method stub
      }

      public void display(GLAutoDrawable drawable)
      {
        GL gl = drawable.getGL();
        //
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2f(-0.5f, -0.5f);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glVertex2f(0.5f, 0.5f);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glEnd();
        gl.glFlush();
      }

      public void reshape(GLAutoDrawable drawable, int x, int y, int width,
          int height)
      {
        // TODO Auto-generated method stub
      }

      public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
          boolean deviceChanged)
      {
        // TODO Auto-generated method stub
      }
    });

    jframe.add(canvas);// put the canvas into a JFrame window
    //jframe.add(jcanvas);
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setVisible(true); // show window
  }
}
