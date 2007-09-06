/*
 * FunctionVizScene.java
 *
 * Created on 16. Juni 2006, 10:34
 */

package net.java.nboglpack.visualdesigner.tests;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.media.opengl.GL;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import net.java.nboglpack.visualdesigner.graphics3d.*;
import net.java.nboglpack.visualdesigner.graphic3d.primitives.Plane;
import net.java.nboglpack.visualdesigner.tools.IFunction2Dto3D;
import net.java.nboglpack.visualdesigner.tools.NoiseGenerator;

/**
 * Testet das Objekt Plane mit verschiedenen Mathematischen eigenschaften.
 * @author SSperlin
 */
public class FunctionVizScene extends Canvas3DBase implements IFunction2Dto3D {
    
    private Plane plane;
    private SceneNode root;
    private Point3f rotation = new Point3f();
    private float distance = 3f;
    private int segments = 20;
    private int mode = 0;
    private Material material;
    private VertexShader simpleVertexShader;
    private FragmentShader simpleFragmentShader;
    private ShaderProgram shaderProgram;
    private ArrayList modeNames = new ArrayList();
    private boolean renderNormals = false;
    private Random rnd;
    private ShaderAttribute diffuseColor;
    private ShaderAttribute noiseStrength;
    private ShaderAttribute shaderTime;
    
    /**
     * Creates a new instance of FunctionVizScene
     */
    public FunctionVizScene() {
        this.m_CameraPosition = new Point3f(0f, distance, distance);
        rnd = new Random();
    }

    /**
     * Initialisiert alle Geometrien.
     * @param gl OpenGL Device auf welchem gerendert wird.
     */
    protected void initGeometry(GL gl) {
        plane = new Plane(gl,
                new Point3f(0f, 0f, 0f),
                new Point3f(0f, 0f, 2f),
                new Point3f(2f, 0f, 2f),
                segments
            );
        plane.set2Dto3DFunction(this);
        root = new SceneNode(gl, plane);
        gl.glDisable(gl.GL_CULL_FACE);
        
        material = new Material(gl);
        material.setDiffuse(new Point4f(0f, 0.9f, 0.2f, 1f));
        material.setSpecular(new Point4f(0.5f, 0.8f, 0.6f, 1f));
        material.setShininess(64f);
        material.setFace(gl.GL_FRONT);
        material.apply();

        
        modeNames.add(0, "Squared");
        modeNames.add(1, "Sphere");
        modeNames.add(2, "Waves");
        modeNames.add(3, "Shell");
        modeNames.add(4, "Torus");
        modeNames.add(5, "Fresnel 1");
        modeNames.add(6, "Fresnel 2");
        modeNames.add(7, "Cliffordtorus");
        modeNames.add(8, "Shape 10");
        modeNames.add(9, "Heart");
        modeNames.add(10, "Limpet Torus");
        modeNames.add(11, "Apple");
        
        this.help.add("SPACE: Toggle Polygon Mod");
        this.help.add("UP, DOWN, LEFT, RIGHT: Rotate");
        this.help.add("M: increase shininess");
        this.help.add("N: decrease shininess");
        this.help.add("G: Gouraud-Shading");
        this.help.add("F: Flat-Shading");
        this.help.add("B: Toggle Normal Rendering");
        this.help.add("+ / - : change segment Count");
        this.help.add("Page up / down: Change Model");
        
        System.out.println("Vendor: " + gl.glGetString(gl.GL_VENDOR));
        System.out.println("Renderer: " + gl.glGetString(gl.GL_RENDERER));
        System.out.println("Version: " + gl.glGetString(gl.GL_VERSION));
        System.out.println("Extensions: " + gl.glGetString(gl.GL_EXTENSIONS));
        
        setupShader();
    }
    
    private void setupShader() {
        try {
            simpleVertexShader = new VertexShader(gl, new FileReader("Shader\\Lines.vert"));
            simpleFragmentShader = new FragmentShader(gl, new FileReader("Shader\\Lines.frag"));
            shaderProgram = new ShaderProgram(gl, simpleVertexShader, simpleFragmentShader);
            shaderProgram.process();
            
//            noiseStrength = new ShaderAttribute(gl, "noiseStrength", new float[] {0.5f}, 1);
//            shaderProgram.setAttribute(noiseStrength);
//            
//            diffuseColor = new ShaderAttribute(gl, "diffuseColor", new float[] {rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), 1f}, 4);
//            shaderProgram.setAttribute(diffuseColor);
            
//            TextureMap texturemap = new TextureMap(gl, glu, gl.GL_TEXTURE_2D);
//            texturemap.loadTexture(NoiseGenerator.createNoiseTexture(64, BufferedImage.TYPE_INT_ARGB));
//            shaderProgram.setAttribute(new ShaderAttribute(gl, "noiseMap", texturemap));
            
//            simpleVertexShader = new VertexShader(gl, new FileReader("D:\\JavaProjects\\Bachelor Thesis\\Shader\\Perlin.vert"));
//            simpleFragmentShader = new FragmentShader(gl, new FileReader("D:\\JavaProjects\\Bachelor Thesis\\Shader\\Perlin.frag"));
//            shaderProgram = new ShaderProgram(gl, simpleVertexShader, simpleFragmentShader);
//            shaderProgram.process();
//            
//            shaderTime = new ShaderAttribute(gl, "time", new float[] {0.0f}, 1);
//            shaderProgram.setAttribute(shaderTime);
//            
//            TextureMap permTexture = new TextureMap(gl, glu, gl.GL_TEXTURE_2D);
//            permTexture.loadTexture(NoiseGenerator.createPermTexture());
//            shaderProgram.setAttribute(new ShaderAttribute(gl, "permTexture", permTexture));
//            
//            TextureMap gradTexture = new TextureMap(gl, glu, gl.GL_TEXTURE_2D);
//            gradTexture.loadTexture(NoiseGenerator.createGradTexture());
//            shaderProgram.setAttribute(new ShaderAttribute(gl, "gradTexture", gradTexture));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Zeichnet alle Geometrien.
     * @param gl OpenGL Device auf welchem gerendert wird.
     */
    protected void DrawGeometry(GL gl) {
//        glu.gluLookAt(m_CameraPosition.x, m_CameraPosition.y, m_CameraPosition.z
//            ,m_CameraLookAt.x, m_CameraLookAt.y, m_CameraLookAt.z
//            ,0f, 1f, 0f);
//        shaderTime.floatData = new float[] {(float) (this.runTime / 100.0)};
//        material.apply();
        shaderProgram.apply();
        root.render();
        if (renderNormals)
            root.renderNormals();
    }

    /**
     * Zeichnet den HUD Text
     * @param gl OpenGL Device auf welchem gerendert wird.
     */
    protected void DrawText(GL gl) {
        draw2DString(gl, 230, 10, "Seg: " + plane.getSegments());
        draw2DString(gl, 320, 10, "Model: " + modeNames.get(Math.abs(mode) % modeNames.size()));
    }
    

    /**
     * Wird aufgerufen, wenn Eine Taste gedrückt wurde.
     * @param e Infos über das Objekt, welches das Event ausgelöst hat.
     */
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch(e.getKeyCode()) {
            case KeyEvent.VK_DOWN:              // Drehung um X-Achse erhöhen
                this.rotation.x += 0.1f;
                root.setRotation(this.rotation);
                break;
            case KeyEvent.VK_UP:                // Drehung um X-Achse verringern
                this.rotation.x -= 0.1f;
                root.setRotation(this.rotation);
                break;
            case KeyEvent.VK_RIGHT:             // Drehung um Y-Achse erhöhen
                this.rotation.y += 0.1f;
                root.setRotation(this.rotation);
                break;
            case KeyEvent.VK_LEFT:              // Drehung um Y-Achse verringern
                this.rotation.y -= 0.1f;
                root.setRotation(this.rotation);
                break;
            case KeyEvent.VK_PAGE_UP:
                this.mode++;
                plane.set2Dto3DFunction(this);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                this.mode--;
                plane.set2Dto3DFunction(this);
                break;
            case KeyEvent.VK_PLUS:
                plane.setSegments(plane.getSegments() + 1);
                break;
            case KeyEvent.VK_MINUS:
                if (plane.getSegments() > 0)
                    plane.setSegments(plane.getSegments() - 1);
                break;
            case KeyEvent.VK_Q:
                this.distance -= 0.1f;
                this.m_CameraPosition = new Point3f(0f, distance, distance);
                break;
            case KeyEvent.VK_A:
                this.distance += 0.1f;
                this.m_CameraPosition = new Point3f(0f, distance, distance);
                break;
            case KeyEvent.VK_F:
                material.setIsSmooth(false);
                break;
            case KeyEvent.VK_G:
                material.setIsSmooth(true);
                break;
            case KeyEvent.VK_N:
                material.setShininess(material.getShininess() * 0.75f);
                break;
            case KeyEvent.VK_M:
                material.setShininess(material.getShininess() * 1.25f);
                break;
            case KeyEvent.VK_B:
                this.renderNormals = !this.renderNormals;
                break;
            case KeyEvent.VK_1:
                noiseStrength.floatData = new float[] {0.1f};
                break;
            case KeyEvent.VK_2:
                noiseStrength.floatData = new float[] {0.3f};
                break;
            case KeyEvent.VK_3:
                noiseStrength.floatData = new float[] {0.5f};
                break;
            case KeyEvent.VK_4:
                noiseStrength.floatData = new float[] {0.8f};
                break;
            case KeyEvent.VK_0:
                diffuseColor.floatData = new float[] {rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), 1f};
                break;
        }
    }

    /**
     * Zur Berechnung der Plane-Koordinaten sind hier verschiedene Funktionen
     * implementiert, die je nach Modus ein Mathematische Modell erzeugen.
     * @param u Erster Parameter 
     * @param v Zweiter Parameter
     * @return Mathematische berechnung des 3D Punktes aus den 2 Koordinaten u und v
     */
    public Point3f getValue(float u, float v) {
        double start;
        double end;
        float t;
        switch (Math.abs(mode) % modeNames.size()) {
            case 0:         // Squared
//                return new Point3f (u, v, (float) (Math.pow(u*2, 2) + Math.pow(v*2, 2)) * 0.1f);
                return new Point3f (
                        u - 0.5f,
                        v - 0.5f,
                        (float) (Math.pow((u - 0.5f) * 5, 2) + Math.pow((v - 0.5f) * 5, 2)) * -0.1f
                    );
            case 1:         // Sphere
                float radius = 1f;
                return new Point3f(
                      (float) Math.sin(v * Math.PI * 1f) * (float) Math.cos(u * Math.PI * 2f) * radius
                    , (float) Math.cos(v * Math.PI * 1f) * radius
                    , (float) Math.sin(v * Math.PI * 1f) * (float) Math.sin(u * Math.PI * 2f) * radius
                    );
            case 2:         // Waves
                return new Point3f (
                        u - 0.5f,
                        v - 0.5f,
                        (float) Math.sin(Math.sqrt(Math.pow((u - 0.5f) * 5, 2) + Math.pow((v - 0.5f) * 5, 2)) * 3 * Math.PI) * 0.1f
                    );
            case 3:         // Shell
                t = v; v = u; u = t;
                start = -Math.PI / 4;
                end = 5 * Math.PI / 2;
                v = (float) (v * (end - start) + start);
                
                u = u * (float) Math.PI;
                return new Point3f (
                        (float) (Math.pow(1.2f, v) * (Math.pow(Math.sin(u), 2f) * Math.sin(v))),
                        (float) (Math.pow(1.2f, v) * (Math.pow(Math.sin(u), 2f) * Math.cos(v))),
                        (float) (Math.pow(1.2f, v) * (Math.sin(u) * Math.cos(u)))
                    );
            case 4:         // Torus
                t = v; v = u; u = t;
                u = u * 2f * (float) Math.PI;
                v = v * 2f * (float) Math.PI;
                return new Point3f (
                        (float) ((1 + 0.5 * Math.cos(u)) * Math.cos(v)),
                        (float) ((1 + 0.5 * Math.cos(u)) * Math.sin(v)),
                        (float) (0.5 * Math.sin(u))
                    );
            case 5:         // Fresnel 1
//                t = v; v = u; u = t;
                u = u * 2f * (float) Math.PI;
                
                start = -Math.PI / 2;
                end = Math.PI / 2;
                v = (float) (v * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u)*Math.cos(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))+Math.PI)/3)+0.8)),
                        (float) (Math.sin(u)*Math.cos(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))+Math.PI)/3)+0.8)),
                        (float) (Math.sin(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))+Math.PI)/3)+0.8))
                    );
            case 6:         // Fresnel 2
//                t = v; v = u; u = t;
                u = u * 2f * (float) Math.PI;
                
                start = -Math.PI / 2;
                end = Math.PI / 2;
                v = (float) (v * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u)*Math.cos(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))-Math.PI)/3)+0.8)),
                        (float) (Math.sin(u)*Math.cos(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))-Math.PI)/3)+0.8)),
                        (float) (Math.sin(v)/(-2*Math.sqrt(0.965/3-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u), 4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4)))*Math.cos((Math.acos(-(-0.941/6.+0.374*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))-1.309/6.*((Math.pow(Math.cos(u),6)+Math.pow(Math.sin(u),6))*Math.pow(Math.cos(v),6)+Math.pow(Math.sin(v),6))-1.221*Math.pow(Math.cos(u),2)*Math.pow(Math.cos(v),4)*Math.pow(Math.sin(u),2)*Math.pow(Math.sin(v),2))/Math.pow(Math.sqrt(0.965/3.-0.935/3*((Math.pow(Math.cos(u),4)+Math.pow(Math.sin(u),4))*Math.pow(Math.cos(v),4)+Math.pow(Math.sin(v),4))),3))-Math.PI)/3)+0.8))
                    );
            case 7:         // Cliffordtorus
                t = v; v = u; u = t;
                u = u * 2f * (float) Math.PI;
                
                start = -Math.PI / 2;
                end = Math.PI / 2;
                v = (float) (v * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u+v)/(Math.sqrt(2)+Math.cos(v-u))),
                        (float) (Math.sin(u+v)/(Math.sqrt(2)+Math.cos(v-u))),
                        (float) (Math.sin(v-u)/(Math.sqrt(2)+Math.cos(v-u)))
                    );
            case 8:         // Shape 10
                t = v; v = u; u = t;
                v = v * 2f * (float) Math.PI;
                
                start = -Math.PI / 2;
                end = Math.PI / 2;
                u = (float) (u * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u)*Math.cos(v)*Math.pow((Math.abs(Math.cos(3*u/4)) + Math.abs(Math.sin(3*u/4))),-1)*Math.pow((Math.abs(Math.cos(6*v/4)) + Math.abs(Math.sin(6*v/4))),-1)),
                        (float) (Math.cos(u)*Math.sin(v)*Math.pow((Math.abs(Math.cos(3*u/4)) + Math.abs(Math.sin(3*u/4))),-1)*Math.pow((Math.abs(Math.cos(6*v/4)) + Math.abs(Math.sin(6*v/4))),-1)),
                        (float) (Math.sin(u)*Math.pow((Math.abs(Math.cos(3*u/4)) + Math.abs(Math.sin(3*u/4))),-1))
                    );
            case 9:         // Heart
//                t = v; v = u; u = t;
                v = v * 2f - 1f;
                
                start = -Math.PI;
                end = Math.PI;
                u = (float) (u * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u)*(4*Math.sqrt(1-Math.pow(v,2))*Math.pow(Math.sin(Math.abs(u)),Math.abs(u)))),
                        (float) (Math.sin(u)*(4*Math.sqrt(1-Math.pow(v,2))*Math.pow(Math.sin(Math.abs(u)),Math.abs(u)))),
                        v
                    );
            case 10:         // Limpet Torus
                t = v; v = u; u = t;
                start = -Math.PI;
                end = Math.PI;
                u = (float) (u * (end - start) + start);
                v = (float) (v * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u) / (Math.sqrt(2) + Math.sin(v))),
                        (float) (Math.sin(u) / (Math.sqrt(2) + Math.sin(v))),
                        (float) (1 / (Math.sqrt(2) + Math.cos(v)))
                    );
            case 11:         // 
//                t = v; v = u; u = t;
                u = u * 2f * (float) Math.PI;
                start = -Math.PI;
                end = Math.PI;
                v = (float) (v * (end - start) + start);
                return new Point3f (
                        (float) (Math.cos(u) *(4 + 3.8* Math.cos(v))) * 0.2f,
                        (float) (Math.sin(u) *(4 + 3.8* Math.cos(v))) * 0.2f,
                        (float) ((Math.cos(v) + Math.sin(v) - 1)* (1 + Math.sin(v)) *Math.log(1 - Math.PI *v / 10) + 7.5 *Math.sin(v)) * 0.2f
                    );
        }
        return new Point3f (u, v, 0);
    }
    
}
