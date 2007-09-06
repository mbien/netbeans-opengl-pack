/*
 * BufferedIndexedMesh.java
 *
 * Created on 11. Mai 2006, 23:58
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import com.sun.opengl.util.BufferUtil;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GLException;

/**
 * Diese Klasse steht für informationen eines Mesh welche im Speicher der Grafikharder liegen.
 * Dabei werden die Verticies indiziert, um Speicher zu sparen und die Performance zu erhöhen.
 * Die Klasse bietet Möglichkeiten zur Generierung dieses Buffers und zum Rendern.
 * @author Samuel Sperling
 */
public class BufferedIndexedMesh implements IMeshable {
    /**
     * X, Y, Z
     */
    private static final int DIMENSION = 3;
    /**
     * IDs des Buffers im Grafikspeicher {vertex, normal, index}
     */
    private int[] arbIDs;
    /**
     * Anzahl aller Vertices. inkl. Doppelnutznug
     */
    private int bufferLength;
    /**
     * Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    private GL gl;
    /**
     * Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    private int drawMode;
    /**
     * True, falls die Hardware den Buffer unterstützt
     */
    private boolean hasHardwaresupport = true;
    /**
     * Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repräsentiert werden soll.
     * Diese Daten werden nur gehalten, falls die Grafikharware keinen Buffer unterstützt.
     */
    private Face3D[] mesh;
    
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public BufferedIndexedMesh(GL gl) {
        this.gl = gl;
        arbIDs = new int[] {-1, -1, -1};
    }
    
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repräsentiert werden soll.
     * @param DrawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     * Durch angabe des Typs wird automatisch entschieden, wieviele Punkte für eine Fläche genutzt werden.
     */
    public BufferedIndexedMesh(GL gl, Face3D[] mesh, int DrawMode) {
        this.gl = gl;
        arbIDs = new int[] {-1, -1, -1};
        createBuffer(mesh, DrawMode);
    }
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repräsentiert werden soll.
     * @param pointsPerFace Anzahl der Verticies pro Fläche.
     * @param DrawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    public BufferedIndexedMesh(GL gl, Face3D[] mesh, int pointsPerFace, int DrawMode) {
        this.gl = gl;
        arbIDs = new int[] {-1, -1, -1};
        createBuffer(mesh, pointsPerFace, DrawMode);
    }
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem übergebenen Mesh in den Speicher
     * der Grafikhardware.
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repräsentiert werden soll.
     * @param DrawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    public void createBuffer(Face3D[] mesh, int DrawMode) {
        int pointsPerFace;
        switch(DrawMode) {
            case GL.GL_POINTS:
                pointsPerFace = 1;
                break;
            case GL.GL_LINES:
                pointsPerFace = 2;
                break;
            case GL.GL_TRIANGLES:
                pointsPerFace = 3;
                break;
            case GL.GL_QUADS:
                pointsPerFace = 4;
                break;
            default:
                // TODO: autodetect
                pointsPerFace = 5;
        }
        createBuffer(mesh, pointsPerFace, DrawMode);
    }
    
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem übergebenen Mesh in den Speicher
     * der Grafikhardware.
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repräsentiert werden soll.
     * @param pointsPerFace Anzahl der Verticies pro Fläche.
     * @param DrawMode Typ der Fläche (GL_QUAD, GL_TRIANGLE, ...)
     */
    public void createBuffer(Face3D[] mesh, int pointsPerFace, int DrawMode) {
        this.drawMode = DrawMode;
        try {
            
            // aktiviert falls der Buffer 'künstlich' deaktiviert werden soll.
//            if (true) throw new GLException();

            // Alte Buffer löschen
            if (arbIDs[0] != -1)
                gl.glDeleteBuffersARB(3, arbIDs, 0);

            // TODO: if (pointsPerFace == 0) count...
            bufferLength = mesh.length * pointsPerFace;

            // Höchsten Index finden.
            Vector3D[] vector;
            int maxIndex = 0;
            int currentIndex;
            for (int i = 0; i < mesh.length; i++) {
                vector = mesh[i].getCoords();
                for (int v = 0; v < pointsPerFace; v++) {
                    currentIndex = vector[v].getIndex(maxIndex);
                    maxIndex = Math.max(maxIndex, currentIndex);
                }
            }

            // Buffer generieren.
            FloatBuffer vertices = BufferUtil.newFloatBuffer((maxIndex + 1) * DIMENSION);
            FloatBuffer normals = BufferUtil.newFloatBuffer((maxIndex + 1) * DIMENSION);
            IntBuffer indices = BufferUtil.newIntBuffer(bufferLength);
            
            // Daten in die Buffer schreiben
            int z = 0;
            for (int i = 0; i < mesh.length; i++) {
                vector = mesh[i].getCoords();
                for (int v = 0; v < pointsPerFace; v++) {
                    vector[v].bufferIndexed(vertices, normals, indices, z++);
                }
            }

            // Buffer generieren
            gl.glGenBuffersARB(this.arbIDs.length, this.arbIDs, 0);

            // Vertex Buffer binden (aktivieren)
            gl.glBindBufferARB( GL.GL_ARRAY_BUFFER_ARB, this.arbIDs[0] );
            gl.glBufferDataARB( GL.GL_ARRAY_BUFFER_ARB, (maxIndex + 1) * DIMENSION * BufferUtil.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW_ARB);

            // Normal Buffer binden (aktivieren)
            gl.glBindBufferARB( GL.GL_ARRAY_BUFFER_ARB, this.arbIDs[1] );
            gl.glBufferDataARB( GL.GL_ARRAY_BUFFER_ARB, (maxIndex + 1) * DIMENSION * BufferUtil.SIZEOF_FLOAT, normals, GL.GL_STATIC_DRAW_ARB);

            // Index Buffer binden (aktivieren)
            gl.glBindBufferARB( GL.GL_ARRAY_BUFFER_ARB, this.arbIDs[2] );
            gl.glBufferDataARB( GL.GL_ARRAY_BUFFER_ARB, bufferLength * BufferUtil.SIZEOF_INT, indices, GL.GL_STATIC_DRAW_ARB);
        } catch (GLException e) {
            
            /* Falls die Hardware die Buffernutzung nicht unterstützt,
             * werden die Mesh-Informationen roh gespeichert
             */
            hasHardwaresupport = false;
        }
        this.mesh = mesh;
    }
    
    /**
     * Rendert den Buffer. Dabei wird das OpenGL Device genutzt welches dem Konstruktor
     * übergeben wurde.
     */
    public void render() {
        if (hasHardwaresupport) {
            gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL.GL_INDEX_ARRAY);

            // Vertex Buffer binden (aktivieren)
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, arbIDs[0]);
            gl.glVertexPointer(DIMENSION, GL.GL_FLOAT, 0, 0);

            // Normal Buffer binden (aktivieren)
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, arbIDs[1]);
            gl.glNormalPointer(GL.GL_FLOAT, 0, 0);

            // Index Buffer binden (aktivieren)
            gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, arbIDs[2]);
            gl.glIndexPointer(GL.GL_INT, 0, (Buffer) null);

            // Buffered Vertices malen
            gl.glDrawElements(this.drawMode, this.bufferLength, GL.GL_UNSIGNED_INT, 0);

            gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL.GL_INDEX_ARRAY);
        } else {
            
            /* Falls die Hardware die Buffernutzung nicht unterstützt,
             * werden die Faces einzeln gerendert.
             */
            gl.glBegin(this.drawMode);
            for (int i = 0; i < this.mesh.length; i++) {
                this.mesh[i].render(gl);
            }
            gl.glEnd();
        }
    }

    /**
     * Rendert die Normalen des 3DObjektes aus dem Buffer.
     * Dabei wird das OpenGL Device genutzt welches dem Konstruktor übergeben wurde.
     */
    public void renderNormals() {
        gl.glDisable(gl.GL_LIGHTING);
        gl.glColor3f(1f, 1f, 1f);
        gl.glBegin(gl.GL_LINES);
        for(int i = 0; i < this.mesh.length; i++) {
            this.mesh[i].renderNormals(gl);
        }
        gl.glEnd();
    }
}
