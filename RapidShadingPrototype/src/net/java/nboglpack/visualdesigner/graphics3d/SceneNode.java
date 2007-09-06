/*
 * SzeneNode.java
 *
 * Created on 10. Mai 2006, 20:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

/**
 * Eine Instanz dieser Klasse repräsentiert einen Knoten in einem
 * Szenengraphen.
 * Diese Knoten kann weitere Knoten beinhalten sowie ein IRenerable Mesh.
 * @author Samuel Sperling
 */
public class SceneNode implements IRenderable {
    
    /** Skalierungswerte */
    private Point3f scale;
    
    /** Rotationswerte */
    private Point3f rotation;
    
    /** Pivot Punkt */
    private Point3f pivot;
    
    /** Verschiebungswerte */
    private Point3f translation;
    
    /** GL Interface auf das gerendert wird. */
    private GL gl;
    
    /**
     * Komplette Transformationsmatrix, die alle einzelnen Transformationen
     * vereinigt.
     */
    private Matrix3D transform;
    
    /**
     * Gibt an, ob die Matrix mit den aktuellen Transformationen schon
     * berechnet wurde.
     */
    private boolean  calculated = false;
    
    /** Objekt was diesem Knoten angehört und IRenderable ist. */
    private IRenderable mesh;
    
    /** Alle Unterknoten des dieses Knotens. */
    private ArrayList<SceneNode> children;
    
    /** VaterKnoten dieses SceneNodes */
    private SceneNode parent;
    
    /**
     * Erzeugt eine Instanz der Klasse SzeneNode
     * @param gl OpenGL Device auf dem gerendert werden soll
     */
    public SceneNode(GL gl) {
        initSzeneNode(gl);
    }

    /**
     * Erzeugt eine Instanz der Klasse SzeneNode
     * @param gl OpenGL Device auf dem gerendert werden soll
     * @param mesh Objekt, das Kind dieses Szenenknotens sein soll.
     */
    public SceneNode(GL gl, IRenderable mesh) {
        this.initSzeneNode(gl);
        this.setMesh(mesh);
    }
    
    /**
     * Setzt die Defaultwerte für Skalierung, Rotation und Translation
     */
    private void initSzeneNode(GL gl) {
        this.gl = gl;
        this.pivot = new Point3f(0f, 0f, 0f);
        this.scale = new Point3f(1f, 1f, 1f);
        this.rotation = new Point3f(0f, 0f, 0f);
        this.translation = new Point3f(0f, 0f, 0f);
    }
    
    /**
     * Weißt diesem Node ein Mesh zu.
     * @param mesh Mesh, welches diesem Objekt zugeordnet werden soll.
     */
    public void setMesh(IRenderable mesh) {
        this.mesh = mesh;
    }
    
    /**
     * Gibt das diesem Node zugeordneten Mesh zurück.
     * @return Mesh welches diesem Node angehört.
     */
    public IRenderable getMesh() {
        return this.mesh;
    }
    
    /**
     * Fügt dem Szenen-Knoten ein Kind an
     * @param child Objekt, das Kind dieses Szenenknotens sein soll.
     */
    public void addChild(SceneNode child) {
        if (this.children == null)
            this.children = new ArrayList();
        this.children.add(child);
        child.setParent(this);
    }
    
    /**
     * Gibt einen Kindknoten zurück
     * @param index Index des Kindes
     * @return Kindknoten in angegebenem Index
     */
    public SceneNode getChild(int index) {
        if (this.children == null || index >= this.children.size()) {
            return null;
        }
        return this.children.get(index);
    }
    
    /**
     * Gibt den Vaterknoten dieses Szenen-Knotens
     * @return Vaterknoten dieses Szenen-Knotens
     */
    public SceneNode getParent() {
        return this.parent;
    }

    /**
     * Setzt den Vaterknoten dieses Szenen-Knotens
     * @param parent Vaterknoten dieses Szenen-Knotens
     */
    public void setParent(SceneNode parent) {
        this.parent = parent;
    }
    
    /**
     * Führt eine Skalierung um den Wert X, Y, Z des übergebenen Punktes aus.
     * @param scale Grad der Skalierung Pro Koordinate X, Y, Z
     */
    public void setScale(Point3f scale) {
        this.scale = scale;
        calculated = false;
    }
    
    /**
     * Gibt den Wert der Skalierung dieses Knotens zurück.
     * @return Grad der Skalierung Pro Koordinate X, Y, Z
     */
    public Point3f getScale() {
        /* Muss geklont werden damit außerhalb keine änderungen gemacht werden
         * von dem die SceneNode nichts mitbekommt.
         */
        Point3f clone = new Point3f(this.scale.x, this.scale.y, this.scale.z);
        return clone;
    }
    
    /**
     * Führt eine Rotation um alle Achsen um den Faktor aus welcher für die
     * entsprechenden Achsen im übergenen Punkt angegeben ist.
     * @param rotation Grad der Rotation. X, Y, Z für die jeweilige Achse.
     */
    public void setRotation(Point3f rotation) {
        this.rotation = rotation;
        calculated = false;
    }
    
    /**
     * Gibt den Wert der Rotation dieses Knotens zurück.
     * @return Grad der Rotation um die Achsen X, Y, Z
     */
    public Point3f getRotation() {
        /* Muss geklont werden damit außerhalb keine änderungen gemacht werden
         * von dem die SceneNode nichts mitbekommt.
         */
        Point3f clone = new Point3f(this.rotation.x, this.rotation.y, this.rotation.z);
        return clone;
    }
    
    /**
     * Setzt die Position des PivotPoints (Schwerpunkt) des Objektes fest.
     * @param pivot PivotPoint. Definiert durch X, Y, Z
     */
    public void setPivotPoint(Point3f pivot) {
        this.pivot = pivot;
        calculated = false;
    }
    
    /**
     * Setzt die Position des PivotPoints (Schwerpunkt) des Objektes fest.
     * @return PivotPoint. Definiert durch X, Y, Z
     */
    public Point3f getPivotPoint() {
        return this.pivot;
    }
    
    /**
     * Führt eine Bewegung um den Wert X, Y, Z des übergebenen Punktes aus.
     * @param translation Bewegung um den Punkt. Definiert durch X, Y, Z
     */
    public void setTranslation(Point3f translation) {
        this.translation = translation;
        calculated = false;
    }
    
    /**
     * Gibt den Wert der Bewegung dieses Knotens zurück.
     * @return Grad der Bewegung um X, Y, Z
     */
    public Point3f getTranslation() {
        /* Muss geklont werden damit außerhalb keine änderungen gemacht werden
         * von dem die SceneNode nichts mitbekommt.
         */
        Point3f clone = new Point3f(this.translation.x, this.translation.y, this.translation.z);
        return clone;
    }
    
    /**
     * Gibt die Transformationsmatrix dieses Knotens zurück.
     * Dabei werden alle Matrizen übergeordneter Objekte miteingerechnet.
     * @return Absolute Transformationsmatrix dieses Knotens
     */
    public Matrix4f getTransformationAbsolut() {
        if (!calculated) {
            calc();
        }
        Matrix4f transform = null;
        if (this.parent != null) {
            transform = (Matrix4f) this.transform.getMatrix().clone();
            transform.mul(this.parent.getTransformationAbsolut());
//            transform = this.parent.getTransformationAbsolut();
//            transform.mul(this.transform.getMatrix());
        } else {
            transform = (Matrix4f) this.transform.getMatrix().clone();
        }
        return transform;
    }
    
    /**
     * Gibt die Transformationsmatrix dieses Knotens zurück.
     * @return Transformationsmatrix dieses Knotens
     */
    public Matrix4f getTransformation() {
        if (!calculated) {
            calc();
        }
        return this.transform.getMatrix();
    }
    
    /**
     * Berechnet die Transformations-Matrix
     */
    private void calc() {
        Point3f p;
        transform = new Matrix3D();
        p = new Point3f(this.pivot.x * -1f, this.pivot.y * -1f, this.pivot.z * -1f);
        transform.translate(p);
        transform.scale(this.scale);
        transform.rotate(this.rotation);
        p = (Point3f) this.translation.clone();
        p.add(this.pivot);
        transform.translate(p);
        calculated = true;
    }

    /**
     * Rendert alle Kinder dieses Knotens
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void render(GL gl) {    
        this.gl = gl;
        render();
    }
    
    /**
     * Rendert alle Kinder dieses Knotens
     */
    public void render() {
        // evt. muss zunächst die Transformationsmatrix berechnet werden.
        if (!calculated) {
            calc();
        }

        /* Mit push / pop wird die aktuelle matrix gesichert.
         * Sodass sie nach beendigung dieser Methode wieder die gleiche ist
         * wie bei Beginn.
         */
        gl.glPushMatrix();
        gl.glMultMatrixf(transform.getData(), 0);
//        gl.glTranslatef(this.translation.x, this.translation.y, this.translation.z);
//        gl.glTranslatef(this.pivot.x, this.pivot.y, this.pivot.z);
//        gl.glRotatef(rotation.z / (float) Math.PI * 180, 0f, 0f, 1f);
//        gl.glRotatef(rotation.y / (float) Math.PI * 180, 0f, 1f, 0f);
//        gl.glRotatef(rotation.x / (float) Math.PI * 180, 1f, 0f, 0f);
//        gl.glScalef(this.scale.x, this.scale.y, this.scale.z);
//        gl.glTranslatef(-this.pivot.x, -this.pivot.y, -this.pivot.z);
        
        // Render Mesh
        if (this.mesh != null) {
            this.mesh.render();
        }
        
        // Render SubNodes
        if (this.children != null && this.children.size() > 0) {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).render();
            }
        }
        gl.glPopMatrix();
    }

    /**
     * Rendert die Normalen aller Kinder dieses Knotens
     */
    public void renderNormals() {
        // evt. muss zunächst die Transformationsmatrix berechnet werden.
        if (!calculated) {
            calc();
        }

        /* Mit push / pop wird die aktuelle matrix gesichert.
         * Sodass sie nach beendigung dieser Methode wieder die gleiche ist
         * wie bei Beginn.
         */
        gl.glPushMatrix();
        gl.glMultMatrixf(transform.getData(), 0);
        
        // Render Mesh
        if (this.mesh != null) {
            this.mesh.renderNormals();
        }
        
        // Render SubNodes
        if (this.children != null && this.children.size() > 0) {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).renderNormals();
            }
        }
        gl.glPopMatrix();
    }
    
    /**
     * Rendert nur das angegebene Objekt.
     * Berücksichtigt dabei jedoch seine Position im Graphen.
     */
    public void renderStandAlone() {
        gl.glPushMatrix();
        Matrix4f m4f = getTransformationAbsolut();
        gl.glMultMatrixf((new Matrix3D(m4f)).getData(), Matrix3D.DATA_LENGTH);
        if (this.mesh != null) {
            this.mesh.render();
        }
        gl.glPopMatrix();
    }
}
