/*
 * PrimitivesDisplay.java
 *
 * Created on 14. April 2006, 15:48
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;
import com.sun.opengl.util.GLUT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

/**
 * @author Samuel Sperling
 */
public abstract class Canvas3DBase implements GLEventListener, KeyListener {
    /**
     * alle Lichter der Szene
     */
    protected Light[] lights = new Light[8];
    
    /**
     * Default Material für die Ambiente Beleuchtung.
     */
    protected float m_MaterialAmbient[]={0.2f, 0.2f, 0.2f, 1};
    /**
     * Default Material für die Diffuse Beleuchtung.
     */
    protected float m_MaterialDiffuse[]={0.7f, 0.7f, 0.7f, 1};
    /**
     * Default Material für die Specular Beleuchtung.
     */
    protected float m_MaterialSpecular[]={0.2f, 0.2f, 0.2f, 1};
    
    /**
     * Position der Kamera
     */
    protected Point3f m_CameraPosition = new Point3f(0f, 0f, 2f);
    
    /**
     * Punkt auf den die Kamera schaut
     */
    protected Point3f m_CameraLookAt = new Point3f(0f, 0f, 0f);
    
    /**
     * Bilder Pro Sekunde, die vom Prigramm generiert werden.
     */
    public double fps;

    /**
     * Zeit an der die Applikation gestartet ist.
     */
    protected long startTime;
    /**
     * Vergangene Zeit seit dem Start der Applikation
     */
    protected double runTime;
    /**
     * Zeit des letzten Zeichenvorgangs von OpenGL
     */
    protected double lastDraw;
    /**
     * GL
     */
    protected GL gl;
    /**
     * GLU
     */
    protected GLU glu;
    /**
     * GLUT
     */
    protected GLUT glut;
    /**
     * Breite des Fensters
     */
    protected int width = 512;
    /**
     * Höhe des Fensters
     */
    protected int height = 512;
    /**
     * Verhältnis von Höhe zu Breite
     */
    protected float aspectRatio;
    /**
     * True wenn die Hilfe im HUD engezeigt werden soll.
     */
    protected boolean showHelp = false;
    /**
     * True, wenn die Beleuchtung der Modelle aktiv sein soll.
     */
    protected boolean hasLighting = true;
    /**
     * ANgabe ob Flächen, Linien oder Punkte gezeichnet werden sollen.
     */
    protected int polygonMod = GL.GL_FILL;// 6914;     // gl.GL_FILL
    /**
     * Format de FPS Anzeige
     */
    protected DecimalFormat fpsFormat = new DecimalFormat( "0000.00" );
    /**
     * Liste alles Einträge die in der Hilfe angezeigt werden sollen.
     */
    protected ArrayList<String> help = new ArrayList();
    /**
     * Hintergrundfarbe
     */
    protected Point4f backgroundColor = new Point4f(0.25f, 0.25f, 0.25f, 1f);
    /**
     * Default Vordergrund-Farbe
     */
    protected float[] defaultForegroundColor = new float[] {0.25f, 0.25f, 0.25f, 1f};

    private GLContext context;
    
    /**
     * Erzeugt eine neue Instanz der PrimitivesDrawer Klasse
     */
    public Canvas3DBase() {
    }

    public void setGl(GL gl) {
        this.gl = gl;
    }
    
    /**
     * Wird beim Initialisieren der 'OpenGL_Session' aufgerufen und dient zu
     * Initalisierung aller Relevanten Daten.
     * @param drawable Canvas auf dem Gerendert wird.
     */
    public void init(GLAutoDrawable gLAutoDrawable) {
        this.context = gLAutoDrawable.getContext();// .createContext(null);
        this.gl = gLAutoDrawable.getGL();
        this.glu = new GLU(); //context.getGLU();
        this.glut = new GLUT();
        
        this.lights[0] = new Light(gl, 0);
        lights[0].setPosition(new Point4f(0.0f, 6.0f, 5.0f, 0));
        lights[0].setAmbient(new Point4f(0.1f, 0.1f, 0.1f, 0.0f));
        lights[0].setDiffuse(new Point4f(1f, 1f, 1f, 1f));
        lights[0].setSpecular(new Point4f(1f, 1f, 1f, 1f));
        lights[0].apply();
        
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, m_MaterialAmbient, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, m_MaterialDiffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, m_MaterialSpecular, 0);
        gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 64);
        
        // Diverse Einstellungen...
        gl.glEnable(gl.GL_POLYGON_SMOOTH);
        gl.glEnable(gl.GL_CULL_FACE);
        gl.glEnable(gl.GL_DEPTH_TEST);
        
        gl.glShadeModel(gl.GL_SMOOTH);
        gl.glDepthFunc(gl.GL_LEQUAL);
        gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);
        
        // 3D Content Erzeugen
        initGeometry(gl);
        startTime = System.nanoTime();
        
        aspectRatio = 1.0f;
    }
    
    /**
     * Hier sollten alle Geometrien Geladen und arrangiert werden.
     * @param gl OpenGL Device auf dem gerendert werden soll.
     */
    abstract protected void initGeometry(GL gl);
    
    /**
     * Wird bei jedem Renderzyklus aufgerufen.
     * @param drawable Canvas auf dem Gerendert wird.
     */
    public void display(GLAutoDrawable gLAutoDrawable) {
        // Berechnung der FPS
        lastDraw = runTime;
        runTime = (System.nanoTime() - startTime) / 1000000000.0d;
        this.fps = 1 / (runTime - lastDraw);
        
        GL gl = gLAutoDrawable.getGL();// context.getGL();
        GLU glu = new GLU(); //drawable.getGLU();
        
        // Draw eigenschaften setzen.
        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, this.polygonMod);
        
        // Alles Aufräumen
        gl.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        // Orthogonaler View für 2d Text
        gl.glDisable(gl.GL_LIGHTING);
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, this.width, 0, this.height);
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Vordergrundfarbe setzen
        gl.glColor4fv(defaultForegroundColor, 0);
        
        // Text rendern
        gl.glUseProgramObjectARB(0); // Default shading;
        DrawText(gl);
        DrawHelp(gl);
        
        // 3D view für Models
        if (this.hasLighting)
            gl.glEnable(gl.GL_LIGHTING);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(65.0f, aspectRatio, .1f, 100);
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(m_CameraPosition.x, m_CameraPosition.y, m_CameraPosition.z
                        ,m_CameraLookAt.x, m_CameraLookAt.y, m_CameraLookAt.z
                        ,0f, 1f, 0f);
        
        // Szenen Geometrie rendern
        DrawGeometry(gl);
    }
    
    /**
     * Rendert alle 3D-Elemente der Szene
     * @param gl OpenGL Device auf dem gerendert werden soll.
     */
    abstract protected void DrawGeometry(GL gl);
    
    /**
     * Rendert alle Textelemente der Szene
     * @param gl OpenGL Device auf dem gerendert werden soll.
     */
    abstract protected void DrawText(GL gl);
    
    private void DrawHelp(GL gl) {
        draw2DString(gl, 10, 10, "F1: Help   "
                        + "FPS: "
                        + fpsFormat.format(this.fps)
                    );
        
        if (this.showHelp && !this.help.isEmpty()) {
            int l = 2;
            int LineHeight = 12;
            for (int i = 0; i < this.help.size(); i++) {
                draw2DString(gl, 10, l++ * LineHeight, this.help.get(i));
            }
        }
    }
    
    /**
     * Schreibt einen 2D Text in das Fenster
     * Da die glRasterPosXX nicht von jeder Hardware augeführt wird.
     * Kann die Anzeige nicht garantiert werden.
     * @param gl OpenGL Device auf dem gerendert werden soll.
     * @param x X Koordinate an dem der Text gerendert werden soll. (links = 0)
     * @param y Y Koordinate an dem der Text gerendert werden soll. (unten = 0)
     * @param string Text, der gerendert werden soll.
     * @param color Farbe, mit der der Text gerendert werden soll.
     */   
    protected void draw2DString(GL gl, float x, float y, String string, float[] color) {
        gl.glRasterPos2f(x, y);
        gl.glColor3fv(color, 0);
        int len = string.length();
        for (int i=0; i < len; i++) {
            glut.glutBitmapCharacter(glut.BITMAP_8_BY_13, string.charAt(i));
        }
    }
    
    /**
     * Schreibt einen 2D Text in das Fenster
     * Da die glRasterPosXX nicht von jeder Hardware augeführt wird.
     * Kann die Anzeige nicht garantiert werden.
     * @param gl OpenGL Device auf dem gerendert werden soll.
     * @param x X Koordinate an dem der Text gerendert werden soll. (links = 0)
     * @param y Y Koordinate an dem der Text gerendert werden soll. (unten = 0)
     * @param string Text, der gerendert werden soll.
     */
    protected void draw2DString(GL gl, float x, float y, String string) {
        draw2DString(gl, x, y, string, new float[] {1f, 1f, 1f});
    }
    
    /**
     * Wird aufgerufen, wenn sich das Display geändert hat. Oder das Display neu geladen wurde.
     * @param drawable Canvas auf dem das Display zeichnet
     * @param modeChanged Art der Änderung
     * @param deviceChanged Device, welches sich geändert hat.
     */
    public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean modeChanged, boolean deviceChanged) {
        // nothing
    }
    
    /**
     * Wird aufgerufen, wenn siche die Maße des Canvas geändert haben.
     * @param drawable Canvas auf dem das Display zeichnet
     * @param x Neue X-Position des Display
     * @param y Neue Y-Position des Display
     * @param width Neue Breite des Display
     * @param height Neue Höhe des Display
     */
    public void reshape(GLAutoDrawable gLAutoDrawable, int x, int y, int width, int height) {
        this.aspectRatio = (float) width / (float) height;
        this.width = width;
        this.height = height;
    }

    /**
     * Wird aufgerufen, wenn Eine Taste gedrückt wurde.
     * @param e Infos über das Objekt, welches das Event ausgelöst hat.
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Wird aufgerufen, wenn Eine Taste gedrückt wurde.
     * @param e Infos über das Objekt, welches das Event ausgelöst hat.
     */
    public void keyPressed(KeyEvent e) {
        boolean updateGeometry = false;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:             // Polygon Modus wechseln
                this.polygonMod++;
                if (this.polygonMod > GL.GL_FILL)
                    this.polygonMod = GL.GL_POINT;
            break;
            case KeyEvent.VK_ENTER:             // Licht Ein/Aus
                this.hasLighting = !this.hasLighting;
            break;
            case KeyEvent.VK_F1:                // Hilfe Ein/Aus
                this.showHelp = !this.showHelp;
            break;
        }
    }

    /**
     * Wird aufgerufen, wenn Eine gedrückt Taste öpsgelseen wurde.
     * @param e Infos über das Objekt, welches das Event ausgelöst hat.
     */
    public void keyReleased(KeyEvent e) {
    }
}
