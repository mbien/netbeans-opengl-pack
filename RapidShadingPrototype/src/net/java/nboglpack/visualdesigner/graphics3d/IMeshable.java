/*
 * IMeshable.java
 *
 * Created on 13. Mai 2006, 15:28
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

/**
 * Klassen die dieses Interface implementieren,
 * Können ein Mesh in einen buffer speichern und aus diesem wiederum rendern.
 * @author Samuel Sperling
 */
public interface IMeshable extends IRenderable {
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem übergebenen Mesh hinein
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Buffer geschrieben werden sollen.
     * @param drawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    void createBuffer(Face3D[] mesh, int drawMode);
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem übergebenen Mesh hinein
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Buffer geschrieben werden sollen.
     * @param pointsPerFace Anzahl der Verticies pro Fläche.
     * @param drawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    void createBuffer(Face3D[] mesh, int pointsPerFace, int drawMode);
}
