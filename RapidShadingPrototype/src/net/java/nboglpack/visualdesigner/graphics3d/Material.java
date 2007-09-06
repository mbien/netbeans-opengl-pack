/*
 * Material.java
 *
 * Created on 31. Mai 2006, 16:51
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import javax.media.opengl.GL;
import javax.vecmath.Point3i;
import javax.vecmath.Point4f;

/**
 * Erzeugt eine neue Instanz der Klasse Material
 * @author Samuel Sperling
 */
public class Material {
    
    
    private float[] ambient = new float [] {0.2f, 0.2f, 0.2f, 1f};
    private float[] diffuse = new float [] {0.8f, 0.8f, 0.8f, 1f};
    private float[] specular = new float [] {0f, 0f, 0f, 1f};
    private float shininess = 128f;
    private float[] emission = new float [] {0f, 0f, 0f, 1f};
    private int[] colorIndexes = new int [] {0, 1, 1};
    private float[] color = new float[] {1f, 1f, 1f, 1f};
    private boolean isSmooth = true;
    private GL gl;
    private int face = gl.GL_FRONT;
    
    /**
     * Erzeugt eine neue Instanz der Klasse Material
     * @param gl OpenGL Device auf dem Gerendert wird.
     */
    public Material(GL gl) {
        this.gl = gl;
    }
    
    /**
     * Wendet alle Eigenschaften des Materials auf das bekannte OpenGL Device an.
     */
    public void apply() {
        if (isSmooth)
            gl.glShadeModel(gl.GL_SMOOTH);
        else
            gl.glShadeModel(gl.GL_FLAT);
        
        gl.glColor4fv(color, 0);
        gl.glMaterialfv(this.face, gl.GL_AMBIENT, this.ambient, 0);
        gl.glMaterialfv(this.face, gl.GL_DIFFUSE, this.diffuse, 0);
        gl.glMaterialfv(this.face, gl.GL_SPECULAR, this.specular, 0);
        gl.glMaterialf(this.face, gl.GL_SHININESS, this.shininess);
        gl.glMaterialfv(this.face, gl.GL_EMISSION, this.emission, 0);
        gl.glMaterialiv(this.face, gl.GL_COLOR_INDEXES, this.colorIndexes, 0);
    }
    
    private float[] point4f2fa(Point4f point) {
        return new float[] {point.x, point.y, point.z, point.w};
    }
    
    private int[] point3i2ia(Point3i point) {
        return new int[] {point.x, point.y, point.z};
    }
    
    private Point3i ia2point3i(int[] point) {
        return new Point3i(point[0], point[1], point[2]);
    }
    
    private Point4f fa2point4f(float[] point) {
        return new Point4f(point[0], point[1], point[2], point[3]);
    }

    /**
     * Getter for property ambient.
     * @return Value of property ambient.
     */
    public Point4f getAmbient() {
        return fa2point4f(this.ambient);
    }

    /**
     * Setter for property ambient.
     * @param ambient New value of property ambient.
     */
    public void setAmbient(Point4f ambient) {
        this.ambient = point4f2fa(ambient);
    }

    /**
     * Getter for property diffuse.
     * @return Value of property diffuse.
     */
    public Point4f getDiffuse() {
        return fa2point4f(this.diffuse);
    }

    /**
     * Setter for property diffuse.
     * @param diffuse New value of property diffuse.
     */
    public void setDiffuse(Point4f diffuse) {
        this.diffuse = point4f2fa(diffuse);
    }

    /**
     * Getter for property specular.
     * @return Value of property specular.
     */
    public Point4f getSpecular() {
        return fa2point4f(this.specular);
    }

    /**
     * Setter for property specular.
     * @param specular New value of property specular.
     */
    public void setSpecular(Point4f specular) {
        this.specular = point4f2fa(specular);
    }

    /**
     * Getter for property shininess.
     * @return Value of property shininess.
     */
    public float getShininess() {
        return this.shininess;
    }

    /**
     * Setter for property shininess.
     * @param shininess New value of property shininess.
     */
    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    /**
     * Getter for property emission.
     * @return Value of property emission.
     */
    public Point4f getEmission() {
        return fa2point4f(this.emission);
    }

    /**
     * Setter for property emission.
     * @param emission New value of property emission.
     */
    public void setEmission(Point4f emission) {
        this.emission = point4f2fa(emission);
    }

    /**
     * Getter for property colorIndexes.
     * @return Value of property colorIndexes.
     */
    public Point3i getColorIndexes() {
        return ia2point3i(this.colorIndexes);
    }

    /**
     * Setter for property colorIndexes.
     * @param colorIndexes New value of property colorIndexes.
     */
    public void setColorIndexes(Point3i colorIndexes) {
        this.colorIndexes = point3i2ia(colorIndexes);
    }

    /**
     * Getter for property color.
     * @return Value of property color.
     */
    public Point4f getColor() {
        return fa2point4f(this.color);
    }

    /**
     * Setter for property color.
     * @param color New value of property color.
     */
    public void setColor(Point4f color) {
        this.color = point4f2fa(color);
    }

    /**
     * Getter for property isSmooth.
     * @return Value of property isSmooth.
     */
    public boolean isSmooth() {
        return this.isSmooth;
    }

    /**
     * Setter for property isSmooth.
     * @param isSmooth New value of property isSmooth.
     */
    public void setIsSmooth(boolean isSmooth) {
        this.isSmooth = isSmooth;
    }

    /**
     * Getter for property face.
     * @return Value of property face.
     */
    public int getFace() {
        return this.face;
    }

    /**
     * Setter for property face.
     * @param face New value of property face.
     */
    public void setFace(int face) {
        if (face == gl.GL_FRONT || face == gl.GL_BACK || face == gl.GL_FRONT_AND_BACK)
            this.face = face;
        else
            throw new IllegalArgumentException("face was not of type GL_FRONT, GL_BACK, GL_FRONT_AND_BACK");
    }
    
}
