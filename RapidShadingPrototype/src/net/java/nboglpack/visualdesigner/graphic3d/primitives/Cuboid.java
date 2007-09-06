/*
 * Cube.java
 *
 * Created on 10. April 2006, 14:23
 */

package net.java.nboglpack.visualdesigner.graphic3d.primitives;
import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import net.java.nboglpack.visualdesigner.graphics3d.*;

/**
 * Die Cuboid Klasse dient zur Generierung eines Quaders
 * und bietet die Möglichkeit seine Eigenschaften dynamisch zu verändern.
 * @author Samuel Sperling
 */
public class Cuboid extends Object3D {
    /**
     * Breite des Cuboid
     */
    protected float width;
    /**
     * Höhe des Cuboid
     */
    protected float height;
    /**
     * Tiefe des Cuboid
     */
    protected float depth;
    /**
     * Gibt an, ob die Geometrie schon berechnet ist.
     */
    protected boolean isCalculated = false;
    
    /**
     * Erzeugt eine neue Instanz der Cuboid Klasse.
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public Cuboid(GL gl) {
        super(gl);
        createCube(1.0f, 1.0f, 1.0f);
    }
    
    /**
     * Erzeugt eine neue Instanz der Cuboid Klasse.
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param height Höhe des Quaders
     * @param width Breite des Quaders
     * @param depth Tiefe des Quaders
     */
    public Cuboid(GL gl, float height, float width, float depth) {
        super(gl);
        createCube(height, width, depth);
    }
    
    /**
     * Erzeugt eine neue Instanz der Cuboid Klasse.
     * @param height Höhe des Quaders
     * @param width Breite des Quaders
     * @param depth Tiefe des Quaders
     */
    public Cuboid(float height, float width, float depth) {
        createCube(height, width, depth);
    }
    
    /**
     * Speichert die Parameter des Cuboid.
     * @param height Höhe des Quaders
     * @param width Breite des Quaders
     * @param depth Tiefe des Quaders
     */
    private void createCube(float height, float width, float depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;
        isCalculated = false;
    }
    
    /**
     * Setzt die Höhe, Breit und Tiefe auf einen Wert
     * @param value Größe für Höhe, Breit und Tiefe des Cubus
     */
    public void setSize(float value) {
        this.width = value;
        this.height = value;
        this.depth = value;
        isCalculated = false;
    }

    /**
     * Setzt anhand zweier Punktdefinitionen die Größe des Cubus
     * @param p1 Eckpunkt 1
     * @param p2 Eckpunkt 2
     */
    public void set(Vector3f p1, Vector3f p2) {
        Vector3f sub = new Vector3f(p2);
        sub.sub(p1);
        this.width = sub.x;
        this.height = sub.y;
        this.depth = sub.z;
        isCalculated = false;
    }

    /**
     * Setzt die Breite des Cube
     * @param value Breite
     */
    public void setWidth(float value) {
        this.width = value;
        isCalculated = false;
    }

    /**
     * Setzt die Höhe des Cube
     * @param value Höhe
     */
    public void setHeigth(float value) {
        this.height = value;
        isCalculated = false;
    }

    /**
     * Setzt die Tiefe des Cube
     * @param value Tiefe
     */
    public void setDepth(float value) {
        this.depth = value;
        isCalculated = false;
    }
    
    /**
     * Berechnet alle nötigen Geometrien zur Darstellung des Cube
     * @return Liefert ein Array mit den berechneten Faces des Quaders.
     */
    public Face3D[] calcMesh() {
        Face3D[] meshBody = new Face3D[6];
        
        // Konstanten berechnen
        float widthH = this.width / 2.0f;
        float widthHN = this.width / -2.0f;
        float heightH = this.height / 2.0f;
        float heightHN = this.height / -2.0f;
        float depthH = this.depth / 2.0f;
        float depthHN = this.depth / -2.0f;
        
        // Punkte 'berechnen
        Vector3D[] points = new Vector3D[8];
        points[0] = new Vector3D(widthHN, heightHN, depthH);
        points[1] = new Vector3D(widthHN, heightH,  depthH);
        points[2] = new Vector3D(widthH,  heightH,  depthH);
        points[3] = new Vector3D(widthH,  heightHN, depthH);
        points[4] = new Vector3D(widthHN, heightHN, depthHN);
        points[5] = new Vector3D(widthHN, heightH,  depthHN);
        points[6] = new Vector3D(widthH,  heightH,  depthHN);
        points[7] = new Vector3D(widthH,  heightHN, depthHN);

        // Fläche Oben
        meshBody[0] = new Face3D(new Vector3D[] {
                    points[1].clone(),
                    points[2].clone(),
                    points[6].clone(),
                    points[5].clone()
                });
        // Fläche Unten
        meshBody[1] = new Face3D(new Vector3D[] {
                    points[0].clone(),
                    points[4].clone(),
                    points[7].clone(),
                    points[3].clone()
                });
        // Fläche Links
        meshBody[2] = new Face3D(new Vector3D[] {
                    points[0].clone(),
                    points[1].clone(),
                    points[5].clone(),
                    points[4].clone()
                });
        // Fläche Rechts
        meshBody[3] = new Face3D(new Vector3D[] {
                    points[3].clone(),
                    points[7].clone(),
                    points[6].clone(),
                    points[2].clone()
                });
        // Fläche Vorne
        meshBody[4] = new Face3D(new Vector3D[] {
                    points[0].clone(),
                    points[3].clone(),
                    points[2].clone(),
                    points[1].clone()
                });
        // Fläche Hinten
        meshBody[5] = new Face3D(new Vector3D[] {
                    points[4].clone(),
                    points[5].clone(),
                    points[6].clone(),
                    points[7].clone()
                });
        
        // Normalen berechnen lassen.
        Vector3D[] currentCoords;
        for (int i = 0; i < meshBody.length; i++) {
            currentCoords = meshBody[i].getCoords();
            for (int j = 0; j < currentCoords.length; j++) {
                currentCoords[j].calculateNormal();
            }
        }
        isCalculated = true;
        return meshBody;
    }
    
    /**
     * Leitet die Berechnung des Quaders ein.
     */
    public void calc() {
        if (this.countMeshes() <= 0)
            this.addMesh(new BufferedIndexedMesh(gl));
        this.meshes.get(0).createBuffer(calcMesh(), 4, gl.GL_QUADS);
    }
    
    /**
     * Rendert den Cuboid
     */
    public void render() {
        if (!isCalculated) {
            calc();
        }
        super.render();
    }
    
    /**
     * Rendert die Normalen des Cuboid
     */
    public void renderNormals() {
        if (!isCalculated) {
            calc();
        }
        super.renderNormals();
    }

    /**
     * Rendert den Cuboid
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public void render(GL gl) {
        if (!isCalculated) {
            calc();
        }
        this.gl = gl;
        render();
    }
}
