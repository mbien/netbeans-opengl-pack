/*
 * ISphere.java
 *
 * Created on 12. April 2006, 19:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphic3d.primitives;

import javax.media.opengl.GL;
import net.java.nboglpack.visualdesigner.graphics3d.*;

/**
 * Die Sphere Klasse dient zur Generierung einer Kugel
 * und bietet die Möglichkeit Ihre Eigenschaften dynamisch zu verändern.
 * @author Samuel Sperling
 */
public class Sphere extends Object3D {
    /**
     * Radisu der Kugel
     */
    private float radius;
    /**
     * Anzahl der Segmente der Kugel
     */
    private int segments;
    /**
     * Gibt an, ob die Geometrischen Daten der Sphere bereits
     * mit den aktuellen Parametern berechnet wurden.
     */
    private boolean isCalculated;
  
    /**
     * Erzeugt eine neue Instanz der Klasse Sphere
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public Sphere(GL gl) {
        super(gl);
        createSphere(1.0f, 4);
    }
    
    /**
     * Erzeugt eine neue Instanz der Klasse Sphere
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param radius Radius der Kugel
     * @param segments Anzahl der Segmente der Kugel.
     * Die Segmentanzahl bezieht sich dabei jeweils auf eine Halbkugel
     */
    public Sphere(GL gl, float radius, int segments) {
        super(gl);
        createSphere(radius, segments);
    }
    
    /**
     * Erzeugt eine neue Instanz der Klasse Sphere
     * @param radius Radius der Kugel
     * @param segments Anzahl der Segmente der Kugel.
     * Die Segmentanzahl bezieht sich dabei jeweils auf eine Halbkugel
     */
    private void createSphere(float radius, int segments) {
        this.radius = radius;
        this.segments = segments;
        isCalculated = false;
    }
  
    /**
     * Setzt den Radius der Kugel
     * @param value Radius
     */
    public void setRadius(float value) {
        this.radius = value;
        isCalculated = false;
    }
    
    /**
     * Setzt die Anzahl der Segmente der Kugel
     * @param value Anzahl der Segmente der Kugel.
     * Die Segmentanzahl bezieht sich dabei jeweils auf eine Halbkugel
     */
    public void setSegments(float value) {
        this.segments = (int) value;
        isCalculated = false;
    }
    
    /**
     * Berechnet alle nötigen Daten für die Geometrien.
     * @return Gibt ein Mesh mit allen Faces zurück, die zusammen die Kugel bilden.
     */
    public Face3D[] calcMesh() {
        float angle = 2 * (float) Math.PI / segments;
        float angleH = (float) Math.PI / segments;
        float angleQ = (float) Math.PI / segments * 0.5f;
        float currentAngleY;
        float currentAngleI;
        Face3D[] meshBody;
        Vector3D[][] points = new Vector3D[segments][segments + 1];
        Vector3D pointTop = new Vector3D(0f, radius,  0f);
        Vector3D pointBottom = new Vector3D(0f, radius * -1,  0f);
        
        meshBody = new Face3D[segments * segments];
        
        // Schleife um eine 'Spalte' der Kugelsegmente
        for (int i = 0; i <= segments; i++) {
            currentAngleI = i * angle;
            
            // Oberster und unterster Punkt sind immer gleich.
            if (i < segments) {
                points[i][0] = pointTop;
                points[i][segments] = pointBottom;
            }
            
            // Schleife um eine 'Zeile' der Kugelsegmente pro Spalte
            for (int y = 1; y < segments; y++) {
                
                // Mit Ausname des letzten duchrgangs werden jedes Mal Punkte einer 'slice' der Kugel berechnet.
                currentAngleY = y * angleH;
                if (i < segments) {
                    points[i][y] = new Vector3D(
                              (float) Math.sin(currentAngleY) * (float) Math.cos(currentAngleI) * radius
                            , (float) Math.cos(currentAngleY) * radius
                            , (float) Math.sin(currentAngleY) * (float) Math.sin(currentAngleI) * radius
                            );
                }
                
                // Im ersten Durchlauf werden nur Punkte erzeugt. Keine Flächen
                if (i > 0) {
                    meshBody[(i - 1) * segments + (y - 1)] = new Face3D(
                                new Vector3D[] {
                                    points[i - 1][y - 1],
                                    points[i % segments][y - 1],
                                    points[i % segments][y],
                                    points[i - 1][y]
                                }
                            );
                }
            }
            if (i > 0) {
                // Unterstes Face wird als Dreieck gerendert
                currentAngleY = (float) Math.PI - angleQ;
                meshBody[(i - 1) * segments + (segments - 1)] = new Face3D(
                            new Vector3D[] {
                                points[i - 1][segments - 1],
                                points[i % segments][segments - 1],
                                points[i % segments][segments],
                                points[i - 1][segments]
                            }
                        );
            }
        }
        
        // Jetzt werden noch die Normalen aller vertticies berechnet.
        for (int i = 0; i < segments; i++) {
            for (int j = 0; j < segments; j++) {
                points[i][j].calculateNormal();
            }
        }
        pointTop.calculateNormal();
        pointBottom.calculateNormal();
        
        isCalculated = true;
        return meshBody;
    }
    
    /**
     * Berechnet die Geometrischen Daten der Sphere
     */
    public void calc() {
        if (this.countMeshes() <= 0)
            this.addMesh(new BufferedIndexedMesh(gl));
        this.meshes.get(0).createBuffer(calcMesh(), 4, gl.GL_QUADS);
    }

    /**
     * Rendert den Cube
     */
    public void render() {
        if (!isCalculated) {
            calc();
        }
        super.render();
    }
    
    /**
     * Rendert den Cube
     */
    public void renderNormals() {
        if (!isCalculated) {
            calc();
        }
        super.renderNormals();
    }

    /**
     * 
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void render(GL gl) {
        this.gl = gl;
        render();
    }
}
