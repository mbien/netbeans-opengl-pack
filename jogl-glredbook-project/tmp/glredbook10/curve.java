package glredbook10;

/*
 * #include <GL/gl.h> #include <GL/glu.h> #include <stdlib.h> #include "aux.h"
 * GLUnurbsObj *theNurb; void myinit(void) { glShadeModel (GL_FLAT); theNurb =
 * gluNewNurbsRenderer(); gluNurbsProperty (theNurb, GLU_SAMPLING_TOLERANCE,
 * 10.0); } This routine draws a B-spline curve. Try a different knot sequence
 * for a Bezier curve. For example, GLfloat knots[8] = {0.0, 1.0, 2.0, 3.0, 4.0,
 * 5.0, 6.0, 7.0}; void display(void) { GLfloat ctlpoints[4][3] = {{-.75, -.75,
 * 0.0}, {-.5, .75, 0.0}, {.5, .75, 0.0}, {.75, -.75, 0.0}}; GLfloat knots[8] =
 * {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0}; glClear(GL_COLOR_BUFFER_BIT);
 * glColor3f (1.0, 1.0, 1.0); gluBeginCurve(theNurb); gluNurbsCurve(theNurb, 8,
 * knots, 3, &ctlpoints[0][0], 4, GL_MAP1_VERTEX_3); gluEndCurve(theNurb);
 * glFlush(); } void myReshape(int w, int h) { glViewport(0, 0, w, h);
 * glMatrixMode(GL_PROJECTION); glLoadIdentity(); if (w <= h) gluOrtho2D (-1.0,
 * 1.0, -1.0 * (GLfloat) h/(GLfloat) w, 1.0 * (GLfloat) h/(GLfloat) w); else
 * gluOrtho2D (-1.0 * (GLfloat) w/(GLfloat) h, 1.0 * (GLfloat) w/(GLfloat) h,
 * -1.0, 1.0); glMatrixMode(GL_MODELVIEW); glLoadIdentity(); } Main Loop int
 * main(int argc, char** argv) { auxInitDisplayMode (AUX_SINGLE | AUX_RGB);
 * auxInitPosition (0, 0, 500, 500); auxInitWindow (argv[0]); myinit();
 * auxReshapeFunc (myReshape); auxMainLoop(display); }
 */
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*; 

/**
 * This program uses the Utility Library NURBS routines to draw a
 * one-dimensional NURBS curve. <br>
 * <br>
 * 
 * @TODO port when nurbs are available in java. this is a stub.
 * @author Kiet Le (java port)
 */
public class curve
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCapabilities caps;
  private GLCanvas canvas;

  // private GLUnurbsObj theNurb;//nurb not available in jogl

  //
  public curve()
  {
    super(curve.class.getSimpleName());
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
    setSize(512, 256);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new curve().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();
    glut = new GLUT();
    //
    gl.glShadeModel(GL.GL_FLAT);
    // theNurb = gluNewNurbsRenderer();
    // gluNurbsProperty(theNurb, GLU_SAMPLING_TOLERANCE, 10.0);
  }

  /*
   * This routine draws a B-spline curve. Try a different knot sequence for a
   * Bezier curve. For example, GLfloat knots[8] = {0.0, 1.0, 2.0, 3.0, 4.0,
   * 5.0, 6.0, 7.0};
   */
  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    //
    double ctlpoints[][] =
    {
    { -.75, -.75, 0.0 },
    { -.5, .75, 0.0 },
    { .5, .75, 0.0 },
    { .75, -.75, 0.0 } };

    double knots[] =
    { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0 };

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glColor3f(1.0f, 1.0f, 1.0f);
    /*
     * glu.gluBeginCurve(theNurb); glu.gluNurbsCurve(theNurb, 8, knots,// 3,//
     * ctlpointsBuf,// 4,// GL.GL_MAP1_VERTEX_3); glu.gluEndCurve(theNurb);
     */gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();
    //
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) glu.gluOrtho2D(-1.0, 1.0, -1.0 * (float) h / (float) w,
        1.0 * (float) h / (float) w);
    else glu.gluOrtho2D(-1.0 * (float) w / (float) h, //
        1.0 * (float) w / (float) h, -1.0, 1.0);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
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
