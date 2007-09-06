/*
 * Cylinder.java
 *
 * Created on 12. April 2006, 19:04
 *
 */

package net.java.nboglpack.visualdesigner.graphic3d.primitives;
import javax.media.opengl.GL;
import net.java.nboglpack.visualdesigner.graphics3d.*;

/**
 * Die Cylinder Klasse dient zur Generierung eines Zylinders
 * und bietet die Möglichkeit seine Eigenschaften dynamisch zu verändern.
 * @author Samuel Sperling
 */
public class Cylinder extends Object3D {
    /**
     * Höhe des Zylinders
     */
    private float height;
    /**
     * Radius des Zylinders
     */
    protected float radiusTop;
    /**
     * Radius des Zylinders
     */
    protected float radiusBottom;
    /**
     * Anzahl der Segmente des Zylinders
     */
    private int segments;
    /**
     * Gibt an, ob die Geometrien des Objektes schon berechnet wurden.
     */
    private boolean isCalculated;

    /**
     * Erzeugt eine neue Instanz der Cylinder Klasse.
     */
    public Cylinder() {
    }
    /**
     * Erzeugt eine neue Instanz der Cylinder Klasse.
     * @param height Höhe des Zylinders
     * @param radius Radius des Zylinders
     * @param segments Anzahl der Segmente des Zylinders
     */
    public Cylinder(float height, float radius, int segments) {
        CreateCylinder(height, radius, radius, segments);
    }
    
    /**
     * Erzeugt eine neue Instanz der Cylinder Klasse.
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param height Höhe des Zylinders
     * @param radius Radius des Zylinders
     * @param segments Anzahl der Segmente des Zylinders
     */
    public Cylinder(GL gl, float height, float radius, int segments) {
        super(gl);
        CreateCylinder(height, radius, radius, segments);
    }

    /**
     * Erzeugt eine neue Instanz der Cylinder Klasse.
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public Cylinder(GL gl) {
        super(gl);
        CreateCylinder(1f, 1f, 1f, 8);
    }

    /**
     * Speichert die Parameter des Zylinders
     * @param radiusBottom Unterer Radius des Zylinders
     * @param radiusTop Oberer Radius des Zylinders
     * @param height Höhe des Zylinders
     * @param segments Anzahl der Segmente des Zylinders
     */
    protected void CreateCylinder(float height, float radiusBottom, float radiusTop, int segments) {
      this.height = height;
      this.radiusTop = radiusTop;
      this.radiusBottom = radiusBottom;
      this.segments = segments;
      isCalculated = false;
    }
    
    /**
     * Setzt die Höhe des Cylinders.
     * @param value neue Höhe
     */
    public void setHeigth(float value) {
      this.height = value;
      isCalculated = false;
    }
    
    /**
     * Setzt den Radius des Cylinders.
     * @param value neuer Radius
     */
    public void setRadius(float value) {
      this.radiusTop = value;
      this.radiusBottom = value;
      isCalculated = false;
    }
    
    /**
     * Setzt die Anzahl der Segmente des Cylinders.
     * @param value anzahl der Segmente
     */
    public void setSegments(float value) {
      this.segments = (int) value;
      isCalculated = false;
    }
    
    /**
     * Berechnet alle nötigen Daten für die Geometrien.
     * @param meshCoverT Mesh mit allen Faces der oberen Deckfläche
     * @param meshCoverB Mesh mit allen Faces der unteren Deckfläche
     * @param meshBody Mesh mit allen Faces der Mantelfläche
     */
    public void calcMesh(Face3D[] meshBody, Face3D[] meshCoverT, Face3D[] meshCoverB) {
        float winkel = (float) Math.PI / segments * 2.0f;
        float nextAngle;
        float height_h = height / 2;
        float height_hn = height / -2;
        Vector3D midPoint = new Vector3D(0.0f, height_hn, 0.0f);
        Vector3D midPointTop = new Vector3D(0.0f, height_h, 0.0f);
        
        /* Für Deckfläche und Außenring werden einzelne Punkte definiert damit
         * die Normalen korrekt berechnet werden können.
         */
        Vector3D[] pointsB = new Vector3D[segments * 2];
        Vector3D[] pointsCT = new Vector3D[segments];
        Vector3D[] pointsCB = new Vector3D[segments];
        
        pointsB[0] = new Vector3D(0.0f, height_hn, radiusBottom);
        pointsCB[0] = pointsB[0].clone();
        pointsB[1] = new Vector3D(0.0f, height_h, radiusTop);
        pointsCT[0] = pointsB[1].clone();
        
        // Schleife um jeden einzelnen Winkel des Cylinders
        for (int i = 1; i < segments; i++) {
            nextAngle = winkel * i;
            
            // Berechnung der Punkte
            /* Punkte müssen für Deck- und Mantelfläche einzeln erzeugt werden,
             * da sie jeweils unterschiedliche Normalen bekommen sollen.
             */
            pointsB[(i * 2)] = new Vector3D((float) Math.sin(nextAngle) * radiusBottom, height_hn,   (float) Math.cos(nextAngle) * radiusBottom);
            pointsCB[i] = pointsB[(i * 2)].clone();
            pointsB[(i * 2) + 1] = new Vector3D((float) Math.sin(nextAngle) * radiusTop, height_h, (float) Math.cos(nextAngle) * radiusTop);
            pointsCT[i] = pointsB[(i * 2) + 1].clone();
            
            // Erzeugen des Face für die Mantelfläche
            meshBody[i - 1] = new Face3D(new Vector3D[] {pointsB[(i * 2)], pointsB[(i * 2) + 1], pointsB[(i * 2) - 1], pointsB[(i * 2) - 2]});
            
            // Erzeugen der Faces für Obere und Untere Deckfläche.
            meshCoverB[i - 1] = new Face3D(new Vector3D[] {midPoint, pointsCB[i], pointsCB[i - 1]});
            meshCoverT[i - 1] = new Face3D(new Vector3D[] {midPointTop, pointsCT[i - 1], pointsCT[i]});
        }
        meshBody[segments - 1] = new Face3D(new Vector3D[] {pointsB[0], pointsB[1], pointsB[(segments * 2) - 1], pointsB[(segments * 2) - 2]});
        meshCoverB[segments - 1] = new Face3D(new Vector3D[] {midPoint, pointsCB[0], pointsCB[segments - 1]});
        meshCoverT[segments - 1] = new Face3D(new Vector3D[] {midPointTop, pointsCT[segments - 1], pointsCT[0]});
        
        // Berechnung aller Normalen pro Vertex
        for (int i = 0; i < pointsB.length; i++) {
            pointsB[i].calculateNormal();
        }
        for (int i = 0; i < pointsCB.length; i++) {
            pointsCB[i].calculateNormal();
            pointsCT[i].calculateNormal();
        }
        midPoint.calculateNormal();
        midPointTop.calculateNormal();
        
        isCalculated = true;
    }
    
    /**
     * Berechnet die Geometrischen Daten des Cylinders
     */
    public void calc() {
        int meshCount = 3;
        IMeshable[] mesh = new IMeshable[meshCount];
        Face3D[][] faceMesh = new Face3D[meshCount][segments];
        if (this.countMeshes() <= 0) {
            for (int i = 0; i < meshCount; i++) {
                mesh[i] = new BufferedIndexedMesh(gl);
                this.addMesh(mesh[i]);
            }
        } else {
            for (int i = 0; i < meshCount; i++) {
                mesh[i] = this.meshes.get(i);
            }
        }
        
        calcMesh(faceMesh[0], faceMesh[2], faceMesh[1]);
        mesh[0].createBuffer(faceMesh[0], gl.GL_QUADS);
        mesh[1].createBuffer(faceMesh[1], gl.GL_TRIANGLES);
        if (this.radiusTop > 0)
            mesh[2].createBuffer(faceMesh[2], gl.GL_TRIANGLES);
    }
    
    /**
     * Rendert den Cylinder
     */
    public void render() {
        if (!isCalculated) {
            calc();
        }
        super.render();
    }
    
    /**
     * Rendert die Normalen des Cylinders
     */
    public void renderNormals() {
        if (!isCalculated) {
            calc();
        }
        
        super.renderNormals();
    }

    /**
     * Rendert den Cylinder
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void render(GL gl) {
        this.gl = gl;
        render();
    }
}
