/*
 * Light.java
 *
 * Created on 30. Mai 2006, 22:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import javax.media.opengl.GL;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

/**
 * Erzeugt eine neue Instanz der Klasse Light
 * @author Samuel Sperling
 */
public class Light {
    
    private float[] ambient = new float [] {0f, 0f, 0f, 1f};
    private float[] diffuse = new float [] {1f, 1f, 1f, 1f};
    private float[] specular = new float [] {1f, 1f, 1f, 1f};
    private float[] position = new float [] {0f, 0f, 1f, 0f};
    private float[] spotDirection = new float [] {0f, 0f, -1f};
    private float[] spotAim = new float [] {0f, 0f, 1f};
    private float spotExponent = 0f;
    private float spotCutOff = 180f;
    
    /*
     * Attenuation factor = (1 / (kc + (kl*d) + (kq*d^2)))
     * d = distance
     */
    
    /** kc */
    private float attenuationConstant = 1f;
    
    /** kl */
    private float attenuationLinear = 0f;
    
    /** kq */
    private float attenuationQuadratic = 0f;
    
    private boolean isActivated = true;
    private GL gl;
    private int lightIndex;
    private int lightEnumVal;
    
    /**
     * Erzeugt eine neue Instanz der Klasse Light
     * @param gl Referenz auf das OpenGl Device
     * @param lightIndex index des Lichtes in OpenGL
     */
    public Light(GL gl, int lightIndex) {
        this.gl = gl;
        this.lightIndex = lightIndex;
        this.lightEnumVal = gl.GL_LIGHT0 + lightIndex;
    }
    
    /**
     * Aktiviert das Licht und setzt alle Eigenschaften.
     */
    public void apply() {
        if (isActivated) {
            gl.glEnable(lightEnumVal);
            gl.glLightfv(lightEnumVal, gl.GL_AMBIENT, ambient, 0);
            gl.glLightfv(lightEnumVal, gl.GL_DIFFUSE, diffuse, 0);
            gl.glLightfv(lightEnumVal, gl.GL_SPECULAR, specular, 0);
            gl.glLightfv(lightEnumVal, gl.GL_POSITION, position, 0);
            gl.glLightfv(lightEnumVal, gl.GL_SPOT_DIRECTION, spotDirection, 0);
            gl.glLightf(lightEnumVal, gl.GL_SPOT_EXPONENT, spotExponent);
            gl.glLightf(lightEnumVal, gl.GL_SPOT_CUTOFF, spotCutOff);
            gl.glLightf(lightEnumVal, gl.GL_CONSTANT_ATTENUATION, attenuationConstant);
            gl.glLightf(lightEnumVal, gl.GL_LINEAR_ATTENUATION, attenuationLinear);
            gl.glLightf(lightEnumVal, gl.GL_QUADRATIC_ATTENUATION, attenuationQuadratic);
        } else {
            gl.glDisable(lightEnumVal);
        }
    }
    
    private float[] point4f2fa(Point4f point) {
        return new float[] {point.x, point.y, point.z, point.w};
    }
    
    private float[] point3f2fa(Point3f point) {
        return new float[] {point.x, point.y, point.z};
    }
    
    private Point3f fa2point3f(float[] point) {
        return new Point3f(point[0], point[1], point[2]);
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
     * Getter for property position.
     * @return Value of property position.
     */
    public Point4f getPosition() {
        return fa2point4f(this.position);
    }

    /**
     * Setter for property position.
     * @param position New value of property position.
     */
    public void setPosition(Point4f position) {
        this.position = point4f2fa(position);
    }

    /**
     * Getter for property spotDirection.
     * @return Value of property spotDirection.
     */
    public Point3f getSpotDirection() {
        return fa2point3f(this.spotDirection);
    }

    /**
     * Setter for property spotDirection.
     * @param spotDirection New value of property spotDirection.
     */
    public void setSpotDirection(Point3f spotDirection) {
        this.spotDirection = point3f2fa(spotDirection);
    }

    /**
     * Getter for property spotExponent.
     * @return Value of property spotExponent.
     */
    public float getSpotExponent() {
        return this.spotExponent;
    }

    /**
     * Setter for property spotExponent.
     * @param spotExponent New value of property spotExponent.
     */
    public void setSpotExponent(float spotExponent) {
        this.spotExponent = spotExponent;
    }

    /**
     * Getter for property spotCutOff.
     * @return Value of property spotCutOff.
     */
    public float getSpotCutOff() {
        return this.spotCutOff;
    }

    /**
     * Setter for property spotCutOff.
     * @param spotCutOff New value of property spotCutOff.
     */
    public void setSpotCutOff(float spotCutOff) {
        this.spotCutOff = spotCutOff;
    }

    /**
     * Getter for property attenuationConstant.
     * @return Value of property attenuationConstant.
     */
    public float getAttenuationConstant() {
        return this.attenuationConstant;
    }

    /**
     * Setter for property attenuationConstant.
     * @param attenuationConstant New value of property attenuationConstant.
     */
    public void setAttenuationConstant(float attenuationConstant) {
        this.attenuationConstant = attenuationConstant;
    }

    /**
     * Getter for property attenuationLinear.
     * @return Value of property attenuationLinear.
     */
    public float getAttenuationLinear() {
        return this.attenuationLinear;
    }

    /**
     * Setter for property attenuationLinear.
     * @param attenuationLinear New value of property attenuationLinear.
     */
    public void setAttenuationLinear(float attenuationLinear) {
        this.attenuationLinear = attenuationLinear;
    }

    /**
     * Getter for property attenuationQuadratic.
     * @return Value of property attenuationQuadratic.
     */
    public float getAttenuationQuadratic() {
        return this.attenuationQuadratic;
    }

    /**
     * Setter for property attenuationQuadratic.
     * @param attenuationQuadratic New value of property attenuationQuadratic.
     */
    public void setAttenuationQuadratic(float attenuationQuadratic) {
        this.attenuationQuadratic = attenuationQuadratic;
    }

    /**
     * Getter for property isActivated.
     * @return Value of property isActivated.
     */
    public boolean isActivated() {
        return this.isActivated;
    }

    /**
     * Setter for property isActivated.
     * @param isActivated New value of property isActivated.
     */
    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    /**
     * Getter for property spotAim.
     * @return Value of property spotAim.
     */
    public Point3f getSpotAim() {
        return fa2point3f(this.spotAim);
    }

    /**
     * Setter for property spotAim.
     * @param spotAim New value of property spotAim.
     */
    public void setSpotAim(Point3f spotAim) {
        Point3f tmp = (Point3f) spotAim.clone();
        tmp.sub(new Point3f(this.position[0], this.position[1], this.position[2]));
        this.spotDirection = point3f2fa(tmp);
        this.spotAim = point3f2fa(spotAim);
    }
    
}
