package net.java.nboglpack.glcapabilities.demo;

import com.sun.opengl.util.Animator;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;


/**
 * Gears.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */

public class JOGLGearsDemo implements GLEventListener {

    private float view_rotx = 20.0f;
    private float view_roty = 30.0f;
    private float view_rotz = 0.0f;
    
    private int gear1;
    private int gear2;
    private int gear3;
    
    private float angle = 0.0f;

    private final Animator animator;

    public JOGLGearsDemo(GLAutoDrawable drawable) {
        drawable.addGLEventListener(this);
        animator = new Animator(drawable);
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL2 gl = (GL2) drawable.getGL();

        //System.err.println("INIT GL IS: " + gl.getClass().getName());

        //System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

        gl.setSwapInterval(1);

        float[] pos = {5.0f, 5.0f, 10.0f, 0.0f};
        float[] red = {0.8f, 0.1f, 0.0f, 1.0f};
        float[] green = {0.0f, 0.8f, 0.2f, 1.0f};
        float[] blue = {0.2f, 0.2f, 1.0f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        /* make the gears */
        gear1 = gl.glGenLists(1);
        gl.glNewList(gear1, GL2.GL_COMPILE);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, red, 0);
        gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
        gl.glEndList();

        gear2 = gl.glGenLists(1);
        gl.glNewList(gear2, GL2.GL_COMPILE);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, green, 0);
        gear(gl, 0.5f, 2.0f, 2.0f, 10, 0.7f);
        gl.glEndList();

        gear3 = gl.glGenLists(1);
        gl.glNewList(gear3, GL2.GL_COMPILE);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, blue, 0);
        gear(gl, 1.3f, 2.0f, 0.5f, 10, 0.7f);
        gl.glEndList();

        gl.glEnable(GL2.GL_NORMALIZE);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = (GL2) drawable.getGL();

        float h = (float) height / (float) width;

        gl.glMatrixMode(GL2.GL_PROJECTION);

        //System.err.println("GL_VENDOR: " + gl.glGetString(GL2.GL_VENDOR));
        //System.err.println("GL_RENDERER: " + gl.glGetString(GL2.GL_RENDERER));
        //System.err.println("GL_VERSION: " + gl.glGetString(GL2.GL_VERSION));
        gl.glLoadIdentity();
        gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -40.0f);
    }

    public void display(GLAutoDrawable drawable) {
        
        angle += 0.5f;

        GL2 gl = (GL2) drawable.getGL();
        if ((drawable instanceof GLJPanel) && !((GLJPanel) drawable).isOpaque() && ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
            gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        } else {
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        }

        gl.glPushMatrix();
        
            gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

            gl.glPushMatrix();
                gl.glTranslatef(-2.7f, -2.0f, 0.0f);
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                gl.glCallList(gear1);
            gl.glPopMatrix();

            gl.glPushMatrix();
                gl.glTranslatef(3.4f, -2.0f, 0.0f);
                gl.glRotatef(-2.0f * angle - 9.0f, 0.0f, 0.0f, 1.0f);
                gl.glCallList(gear2);
            gl.glPopMatrix();

            gl.glPushMatrix();
                gl.glTranslatef(-3.4f, 4.2f, 0.0f);
                gl.glRotatef(-2.0f * angle - 25.0f, 0.0f, 0.0f, 1.0f);
                gl.glCallList(gear3);
            gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void dispose(GLAutoDrawable arg0) {
    }

    public static void gear(GL2 gl, float inner_radius, float outer_radius, float width, int teeth, float tooth_depth) {
        int i;
        float r0;
        float r1;
        float r2;
        float angle;
        float da;
        float u;
        float v;
        float len;

        r0 = inner_radius;
        r1 = outer_radius - tooth_depth / 2.0f;
        r2 = outer_radius + tooth_depth / 2.0f;

        da = 2.0f * (float) Math.PI / teeth / 4.0f;

        gl.glShadeModel(GL2.GL_FLAT);

        gl.glNormal3f(0.0f, 0.0f, 1.0f);

        /* draw front face */
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (i = 0; i <= teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), width * 0.5f);
            if (i < teeth) {
                gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), width * 0.5f);
                gl.glVertex3f(r1 * (float) Math.cos(angle + 3.0f * da), r1 * (float) Math.sin(angle + 3.0f * da), width * 0.5f);
            }
        }
        gl.glEnd();

        /* draw front sides of teeth */
        gl.glBegin(GL2.GL_QUADS);
        for (i = 0; i < teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + da), r2 * (float) Math.sin(angle + da), width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + 2.0f * da), r2 * (float) Math.sin(angle + 2.0f * da), width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle + 3.0f * da), r1 * (float) Math.sin(angle + 3.0f * da), width * 0.5f);
        }
        gl.glEnd();

        /* draw back face */
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (i = 0; i <= teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), -width * 0.5f);
            gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), -width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle + 3 * da), r1 * (float) Math.sin(angle + 3 * da), -width * 0.5f);
            gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), -width * 0.5f);
        }
        gl.glEnd();

        /* draw back sides of teeth */
        gl.glBegin(GL2.GL_QUADS);
        for (i = 0; i < teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glVertex3f(r1 * (float) Math.cos(angle + 3 * da), r1 * (float) Math.sin(angle + 3 * da), -width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + 2 * da), r2 * (float) Math.sin(angle + 2 * da), -width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + da), r2 * (float) Math.sin(angle + da), -width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), -width * 0.5f);
        }
        gl.glEnd();

        /* draw outward faces of teeth */
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (i = 0; i < teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle), r1 * (float) Math.sin(angle), -width * 0.5f);
            u = r2 * (float) Math.cos(angle + da) - r1 * (float) Math.cos(angle);
            v = r2 * (float) Math.sin(angle + da) - r1 * (float) Math.sin(angle);
            len = (float) Math.sqrt(u * u + v * v);
            u /= len;
            v /= len;
            gl.glNormal3f(v, -u, 0.0f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + da), r2 * (float) Math.sin(angle + da), width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + da), r2 * (float) Math.sin(angle + da), -width * 0.5f);
            gl.glNormal3f((float) Math.cos(angle), (float) Math.sin(angle), 0.0f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + 2 * da), r2 * (float) Math.sin(angle + 2 * da), width * 0.5f);
            gl.glVertex3f(r2 * (float) Math.cos(angle + 2 * da), r2 * (float) Math.sin(angle + 2 * da), -width * 0.5f);
            u = r1 * (float) Math.cos(angle + 3 * da) - r2 * (float) Math.cos(angle + 2 * da);
            v = r1 * (float) Math.sin(angle + 3 * da) - r2 * (float) Math.sin(angle + 2 * da);
            gl.glNormal3f(v, -u, 0.0f);
            gl.glVertex3f(r1 * (float) Math.cos(angle + 3 * da), r1 * (float) Math.sin(angle + 3 * da), width * 0.5f);
            gl.glVertex3f(r1 * (float) Math.cos(angle + 3 * da), r1 * (float) Math.sin(angle + 3 * da), -width * 0.5f);
            gl.glNormal3f((float) Math.cos(angle), (float) Math.sin(angle), 0.0f);
        }
        gl.glVertex3f(r1 * (float) Math.cos(0), r1 * (float) Math.sin(0), width * 0.5f);
        gl.glVertex3f(r1 * (float) Math.cos(0), r1 * (float) Math.sin(0), -width * 0.5f);
        gl.glEnd();

        gl.glShadeModel(GL2.GL_SMOOTH);

        /* draw inside radius cylinder */
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (i = 0; i <= teeth; i++) {
            angle = i * 2.0f * (float) Math.PI / teeth;
            gl.glNormal3f(-(float) Math.cos(angle), -(float) Math.sin(angle), 0.0f);
            gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), -width * 0.5f);
            gl.glVertex3f(r0 * (float) Math.cos(angle), r0 * (float) Math.sin(angle), width * 0.5f);
        }
        gl.glEnd();
    }

    public void start() {
        animator.start();
    }

    public void stop() {
        animator.stop();
    }
    
    public boolean isRunning() {
        return animator.isAnimating();
    }

}
