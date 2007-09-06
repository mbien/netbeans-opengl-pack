/*
 * Object3D.java
 *
 * Created on 13. Mai 2006, 15:21
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;

/**
 * Object3D dient als Oberklasse für jede Art von renderbaren 3D Objekten.
 * Ein Objekt3D kann auch eigenständig fungieren, indem ihm meshes übergeben werden.
 * @author Samuel Sperling
 */
public class Object3D implements IRenderable{
    /**
     * Alle meshgruppen dieses Objektes. Optimal wäre ein mesh pro DrawType
     */
    protected ArrayList<IMeshable> meshes;
    /**
     * Referenz auf das OpenGL Device auf welchem gerendert wird.
     */
    protected GL gl;
    
    /**
     * Materialdefinition dieses Objektes.
     */
    protected Material material;

    /**
     * Erzeugt eine neue Instanz von Object3D
     */
    public Object3D() {
    }

    /**
     * Erzeugt eine neue Instanz von Object3D
     * @param gl Referenz auf das OpenGL Device auf welchem gerendert wird.
     */
    public Object3D(GL gl) {
        this.gl = gl;
    }
    
    /**
     * Setzt das OpenGL Device
     * @param gl Referenz auf das OpenGL Device auf welchem gerendert wird.
     */
    public void setGL(GL gl) {
        this.gl = gl;
    }
    
    /**
     * Liefert das OpenGL Device
     * @return Referenz auf das OpenGL Device auf welchem gerendert wird.
     */
    public GL getGL() {
        return this.gl;
    }
    
    /**
     * Fügt diesem Objekt ein mesh an.
     * @param newMesh Mesh, welches in diesem Objekt gerendert werden soll.
     */
    public void addMesh(IMeshable newMesh) {
        if (meshes == null) meshes = new ArrayList();
        meshes.add(newMesh);
    }
    
    /**
     * überschreibt in diesem Objekt ein mesh.
     * @param index Index des Mesh welches überschrieben werden soll.
     * @param newMesh Meshdaten die gespeichert werden sollen
     */
    public void setMesh(int index, IMeshable newMesh) {
        meshes.set(index, newMesh);
    }
    
    /**
     * Gibt die Anzahl der Meshes zurück, die dem Objekt zugeordnet wurden.
     * @return Anzahl der Meshes, die dem Objekt zugeordnet wurden.
     */
    public int countMeshes() {
        if (this.meshes == null) return 0;
        return this.meshes.size();
    }
    
    /**
     * Rendert das Objekt
     */
    public void render() {
        if (this.material != null) this.material.apply();
        Iterator<IMeshable> it = this.meshes.iterator();
        while(it.hasNext()) {
            it.next().render();
        }
    }

    /**
     * Render die Normalen des Objekts
     */
    public void renderNormals() {
        Iterator<IMeshable> it = this.meshes.iterator();
        while(it.hasNext()) {
            it.next().renderNormals();
        }
    }    

    /**
     * Getter for property material.
     * @return Value of property material.
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Setter for property material.
     * @param material New value of property material.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
