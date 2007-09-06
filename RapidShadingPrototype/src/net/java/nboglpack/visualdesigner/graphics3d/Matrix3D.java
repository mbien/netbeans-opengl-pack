/*
 * Matrix3D.java
 *
 * Created on 10. Mai 2006, 20:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

/**
 * Die Klasse Matrix3D bietet Funktionen, um Rotationen, Skalierungen
 * und Verschiebungen in einer Matrix zu berechnen und so abzubilden.
 * Diese berechneten Daten können dann für die Multiplikation mit OpenGL
 * Transformationsmatrizen verrechnet werden.
 * @author Samuel Sperling
 */
public class Matrix3D {
    /** Daten der aktuellen Matrize */
    private Matrix4f data;
    
    public static final int DATA_LENGTH = 16;
    
    /** Creates a new instance of Matrix3D */
    public Matrix3D() {
        data = new Matrix4f();
        loadIdentity();
    }
    
    /**
     * Creates a new instance of Matrix3D
     * @param data Matrix auf welcher die berechnungen durchgeführt werden sollen.
     */
    public Matrix3D(Matrix4f data) {
        this.data = data;
    }
    
    /**
     * Setzt die Matrix auf den 'Ursprung' zurück.
     */
    public void loadIdentity() {
        data.setIdentity();
    }
    
    /**
     * Führt eine Rotation um alle Achsen um den Faktor aus welcher für die
     * entsprechenden Achsen im übergenen Punkt angegeben ist.
     * @param rotation Grad der Rotation. X, Y, Z für die jeweilige Achse.
     */
    public void rotate(Point3f rotation) {
        if (rotation.x != 0)
            this.rotateX(rotation.x);
        if (rotation.y != 0)
            this.rotateY(rotation.y);
        if (rotation.z != 0)
            this.rotateZ(rotation.z);
    }
    
    /**
     * Führt eine Rotation um die X-Achse auf der Matrix aus.
     * @param rotation Grad der Rotation
     */
    public void rotateX(float rotation) {
        Matrix4f rotMatrix = new Matrix4f();
        rotMatrix.setIdentity();
        rotMatrix.rotX(rotation);
        data.mul(rotMatrix);
    }
    
    /**
     * Führt eine Rotation um die Y-Achse auf der Matrix aus.
     * @param rotation Grad der Rotation
     */
    public void rotateY(float rotation) {
        Matrix4f rotMatrix = new Matrix4f();
        rotMatrix.setIdentity();
        rotMatrix.rotY(rotation);
        data.mul(rotMatrix);
    }
    
    /**
     * Führt eine Rotation um die Z-Achse auf der Matrix aus.
     * @param rotation Grad der Rotation
     */
    public void rotateZ(float rotation) {
        Matrix4f rotMatrix = new Matrix4f();
        rotMatrix.setIdentity();
        rotMatrix.rotZ(rotation);
        data.mul(rotMatrix);
    }
    
    /**
     * Führt eine Skalierung um den Wert X, Y, Z des übergebenen Punktes aus.
     * @param scale Grad der Skalierung Pro Koordinate X, Y, Z
     */
    public void scale(Point3f scale) {
        Matrix4f scaleMatrix = new Matrix4f();
        scaleMatrix.setIdentity();
        scaleMatrix.m00 = scale.x;
        scaleMatrix.m11 = scale.y;
        scaleMatrix.m22 = scale.z;
        data.mul(scaleMatrix);
    }

    /**
     * Führt eine Bewegung um den Wert X, Y, Z des übergebenen Punktes aus.
     * @param translation Bewegung um den Punkt. Definiert durch X, Y, Z
     */
    public void translate(Point3f translation) {
        Matrix4f transMatrix = new Matrix4f();
        transMatrix.setIdentity();
        transMatrix.m30 = translation.x;
        transMatrix.m31 = translation.y;
        transMatrix.m32 = translation.z;
        data.mul(transMatrix);
    }
    
    /**
     * Gibt die aktuelle Matrix zurück
     * @return aktuelle Matrix
     */
    public Matrix4f getMatrix() {
        return this.data;
    }
    
    /**
     * Gibt die Daten der aktuellen Matrix als Float Array zurück.
     * @return Daten der aktuellen Matrix als Float Array
     */
    public float[] getData() {
        return new float[] {
            data.m00, data.m01, data.m02, data.m03,
            data.m10, data.m11, data.m12, data.m13,
            data.m20, data.m21, data.m22, data.m23,
            data.m30, data.m31, data.m32, data.m33
        };
    }
}
