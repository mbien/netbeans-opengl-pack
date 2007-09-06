/*
 * Face3D.java
 *
 * Created on 28. April 2006, 13:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;
import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

/**
 * Die Face3D Klasse Repräsentiert eine Fläche im 3-Dimensionalen Raum, welche
 * durch eine Anzahl von Punkten definiert ist.
 * Abhängig von der Anzahl der Punkten entscheided Sie, um welche art von Fläche
 * es sich habdelt. Point, Line, Triangle, Quad, Polygon
 * Außerdem bietet die Klassen Funktionen Normalenberechnung.
 * @author Samuel Sperling
 */
public class Face3D {
    
    /**
     * Array, mit den Koordinaten der Fläche
     */
    private Vector3D[] coords;
    /**
     * Normalen vector der Fläche
     */
    private Vector3f normal;
    /**
     * Position der Normale für die Anzeige
     */
    private Vector3f normalPos;
    /**
     * FaceType = DrawMode; GL_POINTS, GL_LINES, GL_TRIANGLES, GL_QUADS, GL_POLYGON
     */
    private int faceType;
    /**
     * true, falls die normale dieses Face bereits berechnet wurden.
     */
    private boolean normalsCalculated = false;
    /**
     * Angabe ob die Normalen des Face angezeigt werden sollen. (true)
     */
    private boolean showNormals = true;
    
    /**
     * Erzeugt eine neue Instanz der Face3D Klasse
     * @param coords Array mit Koordinaten des Fläche. Die Koordinaten müssten gegen den Uhrzeigersinn
     * übergeben werden.
     * Abhängig von der Anzahl der Punkten wird entscheided, um welche Art von Fläche
     * es sich habdelt. Point, Line, Triangle, Quad, Polygon
     */
    public Face3D(Vector3D[] coords) {
        this.coords = coords;
        switch(this.coords.length) {
            case 1:
                this.faceType = GL.GL_POINTS;
                break;
            case 2:
                this.faceType = GL.GL_LINES;
                break;
            case 3:
                this.faceType = GL.GL_TRIANGLES;
                break;
            case 4:
                this.faceType = GL.GL_QUADS;
                break;
            default:
                this.faceType = GL.GL_POLYGON;
        }
        
        // Jedem Vertex wird diese Fläche zugeordnet, damit das Vertex seine Normalen berechnen kann.
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i].addFace(this);
        }
    }
    
    /**
     * Gibt an, ob das Face seine eingenen Normalen Darstellen soll (true)
     * oder ob nur die Normalen der Verticies (false)
     * @param showNormals Angabe ob die Normalen des Face angezeigt werden sollen. (true)
     */
    public void showNormals(boolean showNormals) {
        this.showNormals = showNormals;
    }
    
    /**
     * Berechnet die Normale der Fläche anhand aller Eckpunkte.
     */
    public void calcNormal() {
        int l = this.coords.length;
        int i = 1;
        
        // TODO: Das Finden zweier unabhängigen vektoren ist in gewissen Fällen noch umstimmig
        
        // V1
        Vector3f v1 = (Vector3f) this.coords[i++ % l].vertex.clone();
        // Falls 2 Punkte an der selben Stelle liegen...
        if (this.coords[0].vertex.equals(v1))
            v1 = (Vector3f) this.coords[i++ % l].vertex.clone();
        v1.sub(this.coords[0].vertex);
        
        // V2
        Vector3f v2 = (Vector3f) this.coords[i++ % l].vertex.clone();
        // Falls 2 Punkte an der selben Stelle liegen... wird der nächste genommen
//        if (v2.equals(this.coords[1].vertex))
//            v2 = (Vector3f) this.coords[i++ % l].vertex.clone();
        v2.sub(this.coords[0].vertex);
        
        // Resulting Normal
        normal = new Vector3f();
        normal.cross(v1, v2);
        normal.normalize();
        
        // Position der Normalen auf dem Face berechnen (nur für die Anzeige)
        switch(this.faceType) {
            case GL.GL_POINTS:
                this.normalPos = new Vector3f(this.coords[0].vertex);
                break;
            case GL.GL_LINES:
                this.normalPos = new Vector3f(this.coords[0].vertex);
                this.normalPos.sub(this.coords[1].vertex);
                this.normalPos.scale(0.5f);
                this.normalPos.add(this.coords[1].vertex);
                break;
            case GL.GL_TRIANGLES:
                this.normalPos = new Vector3f(this.coords[0].vertex);
                this.normalPos.sub(this.coords[1].vertex);
                this.normalPos.scale(0.5f);
                this.normalPos.add(this.coords[1].vertex);
                Vector3f tmp = new Vector3f(this.coords[0].vertex);
                tmp.sub(this.coords[2].vertex);
                tmp.scale(0.5f);
                tmp.add(this.coords[2].vertex);
                tmp.sub(this.normalPos);
                tmp.scale(0.5f);
                tmp.add(this.normalPos);
                this.normalPos = tmp;
                break;
            case GL.GL_QUADS:
            default:
                this.normalPos = new Vector3f(this.coords[0].vertex);
                this.normalPos.sub(this.coords[2].vertex);
                this.normalPos.scale(0.5f);
                this.normalPos.add(this.coords[2].vertex);
        }
        
        normalsCalculated = true;
    }
    
    /**
     * Gibt die Normale der Fläche zurück.
     * sie berechnet sich automatisch aus den Daten Ihrer geometrie.
     *
     * @return Normlaenvektor dieser Fläche.
     */
    public Vector3f getNormal() {
        if (!normalsCalculated) calcNormal();
        return this.normal;
    }
    
    /**
     * Gibt ein Array, mit den Koordinaten der Fläche zurück, die die Fläche beschreiben.
     * @return Array, mit den Koordinaten der Fläche
     */
    public Vector3D[] getCoords() {
        return this.coords;
    }
    
    /**
     * Rendert das Vertex
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void render(GL gl) {
        if (this.normal != null)
            gl.glNormal3f(normal.x, normal.y, normal.z);
        
        for (int i = 0; i < coords.length; i++) {
            coords[i].render(gl);
        }
    }
    
    /**
     * Rendert das Vertex
     * @param gl OpenGL Device in welches gerendert werden soll.
     */
    public void renderNormals(GL gl) {
        if (normal != null && showNormals) {
            gl.glVertex3f(this.normalPos.x, this.normalPos.y, this.normalPos.z);
            gl.glVertex3f(this.normalPos.x + normal.x * 0.3f, this.normalPos.y + normal.y * 0.3f, this.normalPos.z + normal.z * 0.3f);
        }
        
        for (int i = 0; i < coords.length; i++) {
            coords[i].renderNormals(gl);
        }
    }
}
