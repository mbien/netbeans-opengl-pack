/*
 * Plane.java
 *
 * Created on 15. Juni 2006, 22:34
 */

package net.java.nboglpack.visualdesigner.graphic3d.primitives;

import javax.media.opengl.GL;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import net.java.nboglpack.visualdesigner.graphics3d.*;
import net.java.nboglpack.visualdesigner.tools.IFunction2Dto3D;

/**
 * Repräsentiert eine Planare Fläche.
 * Wobei die Anzahl der Segmente bestimmt werden kann.
 * @author Samuel Sperling
 */
public class Plane extends Object3D{
    private Point3f p1;
    private Point3f p2;
    private Point3f p3;
    private IFunction2Dto3D func;
    
    /**
     * Anzahl der Segmente des Zylinders
     */
    private int segments;
    /**
     * Gibt an, ob die Geometrien des Objektes schon berechnet wurden.
     */
    private boolean isCalculated;
    
    /**
     * Erzeugt eine neue Instanz der Plane Klasse
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param p1 Punkt 1 der Fläche. Punkte müssen gegen den Uhrzeigersinn angegeben werden.
     * @param p2 Punkt 2 der Fläche. Punkte müssen gegen den Uhrzeigersinn angegeben werden.
     * @param p3 Punkt 3 der Fläche. Punkte müssen gegen den Uhrzeigersinn angegeben werden.
     * @param segments Anzahl der Segmente
     */
    public Plane(GL gl, Point3f p1, Point3f p2, Point3f p3, int segments) {
        super(gl);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.segments = segments;
    }
    
    /**
     * Wenn hier eine Objekt übergeben wird, welches die Schnittstelle
     * IFunction2Dto3D unterstützt, wird dies Benutzt um die position jedes
     * einzelnen Punktes auf der Plane zu berechnen.
     * Der Funktion werden Werte zwischen 0 und 1 übergeben.
     * @param func Objekt welches die Funktion zur Berechnung der 3D Koordinate
     *             bereit stellt.
     */
    public void set2Dto3DFunction(IFunction2Dto3D func) {
        this.func = func;
        this.isCalculated = false;
    }
    
    /**
     * Berechnet alle nötigen Daten für die Geometrien.
     * @return Gibt ein Mesh mit allen Faces zurück, die zusammen die Fläche bilden.
     */
    public Face3D[] calcMesh() {
        Vector3f VecU = new Vector3f((Point3f) this.p2.clone());
        Vector3f VecV = new Vector3f((Point3f) this.p3.clone());
        Vector3f VecW;
        Vector3f tmpU;
        Vector3f tmpV;
        Vector3f tmpW;
        float segments = (float) this.segments;
        float segDist = 1f / segments;
        float uf = 0f;
        float vf = 0f;
        VecU.sub(this.p1);
        VecV.sub(this.p2);
        Vector3D[][] points = new Vector3D[this.segments + 1][this.segments + 1];
        
                
        // Wert per Callbak func bestimmen
        if (func != null) {
            Point3f newPos;
            Vector3f tmp;
            VecW = new Vector3f();
            VecW.cross(VecU, VecV);
            VecW.scale(2f / (VecU.length() + VecV.length()));
            for (int u = 0; u <= this.segments; u++) {
                for (int v = 0; v <= this.segments; v++) {
                    tmp = new Vector3f(0f, 0f, 0f);
                    newPos = func.getValue(((float) u / segments), (float) (v / segments));
                    
                    tmpU = (Vector3f) VecU.clone();
                    tmpU.scale(newPos.x);
                    tmp.add(tmpU);
                    
                    tmpV = (Vector3f) VecV.clone();
                    tmpV.scale(newPos.y);
                    tmp.add(tmpV);
                    
                    tmpW = (Vector3f) VecW.clone();
                    tmpW.scale(newPos.z);
                    tmp.add(tmpW);
                    
                    // Ortsvektor addieren
                    tmp.add(this.p1);
                    points[u][v] = new Vector3D(tmp);
                }
            }
        } else {
            for (int u = 0; u <= this.segments; u++) {
                tmpU = (Vector3f) VecU.clone();
                tmpU.scale(uf);
                vf = 0f;
                for (int v = 0; v <= this.segments; v++) {
                    tmpV = (Vector3f) VecV.clone();
                    tmpV.scale(vf);
                    tmpV.add(tmpU);
                    tmpV.add(this.p1);
                    points[u][v] = new Vector3D(tmpV);
                    vf += segDist;
                }
                uf += segDist;
            }
        }
        
        Face3D[] mesh = new Face3D[this.segments * this.segments];
        for (int u = 0; u < this.segments; u++) {
            for (int v = 0; v < this.segments; v++) {
                mesh[u * this.segments + v] = new Face3D(new Vector3D[] {
                        points[u][v],
                        points[u + 1][v],
                        points[u + 1][v + 1],
                        points[u][v + 1]
                    }
                );
            }
        }
        for (int u = 0; u <= this.segments; u++) {
            for (int v = 0; v <= this.segments; v++) {
                points[u][v].calculateNormal();
            }
        }
        this.isCalculated = true;
        return mesh;
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
     * Rendert die Fläche
     */
    public void render() {
        if (!isCalculated) {
            calc();
        }
        super.render();
    }

    /**
     * rendert die Normalen der Fläche
     */
    public void renderNormals() {
        if (!isCalculated) {
            calc();
        }
        super.renderNormals();
    }

    /**
     * Getter for property point1.
     * @return Value of property point1.
     */
    public Point3f getPoint1() {
        return this.p1;
    }

    /**
     * Setter for property point1.
     * @param point1 New value of property point1.
     */
    public void setPoint1(Point3f point1) {
        this.p1 = point1;
        this.isCalculated = false;
    }

    /**
     * Getter for property point2.
     * @return Value of property point2.
     */
    public Point3f getPoint2() {
        return this.p2;
    }

    /**
     * Setter for property point2.
     * @param point2 New value of property point2.
     */
    public void setPoint2(Point3f point2) {
        this.p2 = point2;
        this.isCalculated = false;
    }

    /**
     * Getter for property point2.
     * @return Value of property point3.
     */
    public Point3f getPoint3() {
        return this.p3;
    }

    /**
     * Setter for property point2.
     * @param point3 New value of property point2.
     */
    public void setPoint3(Point3f point3) {
        this.p3 = point3;
        this.isCalculated = false;
    }

    /**
     * Getter for property segments.
     * @return Value of property segments.
     */
    public int getSegments() {
        return this.segments;
    }

    /**
     * Setter for property segments.
     * @param segments New value of property segments.
     */
    public void setSegments(int segments) {
        this.segments = segments;
        this.isCalculated = false;
    }
}
