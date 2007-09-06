/*
 * vector3D.java
 *
 * Created on 28. April 2006, 12:55
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import net.java.nboglpack.visualdesigner.tools.ArraySet;

/**
 * Diese Klasse repräsentiert einen Vertex.
 * Er beinhaltet Informationen über seine Koordinaten, seinen Normalenvektor,
 * Seine Texturkoordinaten, sowie seine Farbe.
 * Der Vector3D kennt seine verbundenen Flächen und bietet Möglichkeiten daraus
 * seine Normale selbst zu berechnen.
 * Er kann sich außerdem in einen Buffer schreiben.
 * @author Samuel Sperling
 */
public class Vector3D {
    
    /**
     * Dimension in welcher Der Vector arbeitet.
     */
    private static final int DIMENSION = 3;
    /**
     * Alle Faces, die diesen Vertex nutzen. Dienen zur normalenberechnung
     */
    private ArraySet Faces;
    /**
     * Vertex
     */
    public Vector3f vertex;
    /**
     * Normale
     */
    public Vector3f normal;
    /**
     * Textur Koordinaten
     */
    public Vector2f texture;
    /**
     * Farbe
     */
    public long color;
    /**
     * Index des Vertex. Wird i.d.R. über getIndex(int) gesetzt.
     */
    private int index = -1;
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     */
    public Vector3D() {
        this.vertex = new Vector3f(0, 0, 0);
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param vx X-Koordinate des Vertex
     * @param vy Y-Koordinate des Vertex
     * @param vz Z-Koordinate des Vertex
     */
    public Vector3D(float vx, float vy, float vz) {
        this.vertex = new Vector3f(vx, vy, vz);
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param vertex Vertex
     */
    public Vector3D(Vector3f vertex) {
        this.vertex = vertex;
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param vertex Vertex
     * @param normal Normale
     */
    public Vector3D(Vector3f vertex, Vector3f normal) {
        this.vertex = vertex;
        this.normal = normal;
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param vertex Vertex
     * @param normal Normale
     * @param texture Textur Koordinaten
     */
    public Vector3D(Vector3f vertex, Vector3f normal, Vector2f texture) {
        this.vertex = vertex;
        this.normal = normal;
        this.texture = texture;
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param vertex Vertex
     * @param texture Textur Koordinaten
     */
    public Vector3D(Vector3f vertex, Vector2f texture) {
        this.vertex = vertex;
        this.texture = texture;
    }
    
    /**
     * Erzeugt eine neue Instanz des Vector3D
     * @param color Farbe
     * @param vertex Vertex
     * @param normal Normale
     */
    public Vector3D(Vector3f vertex, Vector3f normal, long color) {
        this.vertex = vertex;
        this.normal = normal;
        this.color = color;
    }
    
    /**
     * Liefert den Index des Vertex.
     * Falls dem Vertex noch kein Index zugeteilt wurde, wird diese automatisch gesetzt.
     * @param currentIndex Derzeit Höchster Index im Mesh. Hierraus wird der Eigene Index berechnet,
     * Falls diesem Vertex noch kein Index zugeteilt ist.
     * @return Index des Vertex
     */
    public int getIndex(int currentIndex) {
        if (this.index < 0) {
            this.index = currentIndex + 1;
        }
        return this.index;
    }
    
    /**
     * Liefert den Index des Vertex.
     * @return Index des Vertex. Falls noch keiner vergeben wurde, wird -1 zurück gegeben.
     */
    public int getIndex() {
        return this.index;
    }
    
    /**
     * Klont dieses Objekt
     * @return Klon dieses Objektes
     */
    public Vector3D clone() {
        Vector3f vertex2 = null;
        Vector3f normal2 = null;
        Vector2f texture2 = null;
        if (this.vertex != null)
            vertex2 = new Vector3f(this.vertex);
        if (this.normal != null)
            normal2 = new Vector3f(this.normal);
        if (this.texture != null)
            texture2 = new Vector2f(this.texture);
        
        Vector3D vec2 = new Vector3D(vertex2, normal2, texture2);
        vec2.color = this.color;
        return vec2;
    }

    /**
     * Erzeugt für diesen Vertex einen Verweis auf das übergebene Face.
     * Dieses wird dann in die Normalenberechnung dieses Vertex mit einbezogen.
     * @param face Face, welches diesem Vertex zugeordnet werden soll.
     */
    public void addFace(Face3D face) {
        if (Faces == null) Faces = new ArraySet();
        Faces.add(face);
    }
    
    /**
     * Berechnet die Normale dieses Vertex.
     * Zur korrekten Berechnung, müssen dem Objekt vorher alle Flächen übergeben
     * worden sein, die diesen Vertex nutzen.
     */
    public void calculateNormal() {
        if (Faces != null) {
            Vector3f resultNormal = new Vector3f();
            Face3D currentFace;
            for (int i = 0; i < Faces.items.length; i++) {
                currentFace = (Face3D) Faces.items[i];
                resultNormal.add(currentFace.getNormal());
                currentFace.showNormals(false);
            }
            resultNormal.normalize();
            this.normal = resultNormal;
        }
    }
    
    /**
     * Erzeugt eine Textuelle Repräsentation dieses Vertex.
     * @return Textuelle Repräsentation dieses Vertex
     */
    public String toString() {
        return vertex.x + ", " + vertex.y + ", " + vertex.z;
    }
    
    /**
     * Schreibt die Daten dieses Vertex in den übergeben Puffer.
     * Die Pufferung erfolgt indiziert.
     * @param vertices Buffer der Verticies
     * @param normals Buffer der Normalen
     * @param indices Buffer der Indicies
     * @param pos Position im Buffer
     */
    public void bufferIndexed(FloatBuffer vertices, FloatBuffer normals, IntBuffer indices, int pos) {
        int position = index * DIMENSION;
        vertices.put(position    , vertex.x);
        vertices.put(position + 1, vertex.y);
        vertices.put(position + 2, vertex.z);
        normals.put(position     , normal.x);
        normals.put(position + 1 , normal.y);
        normals.put(position + 2 , normal.z);
        indices.put(pos      , index);
    }
    
    /**
     * Schreibt die Daten dieses Vertex in den übergeben Puffer.
     * @param vertices Buffer der Verticies
     * @param normals Buffer der Normalen
     * @param pos Position im Buffer
     */
    public void buffer(FloatBuffer vertices, FloatBuffer normals, int pos) {
        vertices.put(pos    , vertex.x);
        vertices.put(pos + 1, vertex.y);
        vertices.put(pos + 2, vertex.z);
        normals.put(pos     , normal.x);
        normals.put(pos + 1 , normal.y);
        normals.put(pos + 2 , normal.z);
    }
    
    /**
     * Rendert das Vertex
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void render(GL gl) {
        if (this.normal != null)
            gl.glNormal3f(normal.x, normal.y, normal.z);
        gl.glVertex3f(vertex.x, vertex.y, vertex.z);
    }
    
    /**
     * Rendert das Vertex
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void renderNormals(GL gl) {
        if (normal !=  null) {
            gl.glVertex3f(vertex.x, vertex.y, vertex.z);
            gl.glVertex3f(vertex.x + normal.x * 0.3f, vertex.y + normal.y * 0.3f, vertex.z + normal.z * 0.3f);
        }
    }
}
