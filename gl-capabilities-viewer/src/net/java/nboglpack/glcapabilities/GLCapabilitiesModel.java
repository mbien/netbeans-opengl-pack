/*
 * GLCapabilitiesModel.java
 * 
 * Created on 25.06.2007, 16:31:39
 * 
 */

package net.java.nboglpack.glcapabilities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GLCapabilities;

/**
 *
 * @author Michael Bien
 */
public class GLCapabilitiesModel {
 
 private final static String NULL = "";
    
 private String glVersion = NULL;
 private String glslVersion = NULL;

 private String implVersion = NULL;
 private String vendor = NULL;
 private String renderer = NULL;
 
 private String maxLights = NULL;
 private String maxAnisotropy = NULL;
 private String maxViewPortSize = NULL;
 private String maxTextureSize = NULL;
 private String maxTextureUnits = NULL;
 private String maxTextureImageUnits = NULL;
 private String maxVertexTextureImageUnits = NULL;
 private String maxGeometryTextureImageUnits = NULL;
 private String maxDrawBuffers = NULL;
 private String maxSampleBuffers = NULL;

 private final ArrayList<String> extensions = new ArrayList<String>();
 private final List<Capability> capabilities = new ArrayList<Capability>();
 private final ArrayList<GLCapabilities> displayModes = new ArrayList<GLCapabilities>();

// private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

 public GLCapabilitiesModel() {
 }
 
    public String getMaxSampleBuffers() {
        return maxSampleBuffers;
    }

    public void setMaxSampleBuffers(String maxSampleBuffers) {
        this.maxSampleBuffers = maxSampleBuffers;
    }
    
    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public ArrayList<GLCapabilities> getDisplayModes() {
        return displayModes;
    }

    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public String getGlslVersion() {
        return glslVersion;
    }

    public void setGlslVersion(String glslVersion) {
        this.glslVersion = glslVersion;
    }
    
    public String getGLSLVersion() {
        return glslVersion;
    }

    public String getGlVersion() {
        return glVersion;
    }

    public String getImplVersion() {
        return implVersion;
    }

    public String getVendor() {
        return vendor;
    }
    
    public String getRenderer() {
        return renderer;
    }

    public void setGLImplVersion(String implementationVersion) {
//        pcs.firePropertyChange("implVersion", implVersion, implementationVersion);
        this.implVersion = implementationVersion;
    }

    public void setGLVendor(String glVendor) {
//        pcs.firePropertyChange("vendor", vendor, glVendor);
        this.vendor = glVendor;
    }

    public void setGLVersion(String version) {
//        pcs.firePropertyChange("glVersion", glVersion, version);
        this.glVersion = version;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public void setGLSLVersion(String glslVersion) {
        this.glslVersion = glslVersion;
    }
    
    public void setMaxAnisotropy(String maxAnisotropy) {
        this.maxAnisotropy = maxAnisotropy;
    }

    public void setMaxLights(String lights) {
        this.maxLights = lights;
    }

    public void setMaxTextureUnits(String units) {
        this.maxTextureUnits = units;
    }

    public void setMaxViewportSize(String size) {
        this.maxViewPortSize = size;
    }
    
    public String getMaxAnisotropy() {
        return maxAnisotropy;
    }

    public String getMaxLights() {
        return maxLights;
    }

    public String getMaxTextureUnits() {
        return maxTextureUnits;
    }

    public String getMaxViewPortSize() {
        return maxViewPortSize;
    }
    
    public String getMaxTextureSize() {
        return maxTextureSize;
    }

    public void setMaxTextureSize(String maxTextureSize) {
        this.maxTextureSize = maxTextureSize;
    }
    
    public String getMaxDrawBuffers() {
        return maxDrawBuffers;
    }

    public void setMaxDrawBuffers(String maxDrawBuffers) {
        this.maxDrawBuffers = maxDrawBuffers;
    }
    
    public String getMaxTextureImageUnits() {
        return maxTextureImageUnits;
    }

    public void setMaxTextureImageUnits(String maxTextureImageUnits) {
        this.maxTextureImageUnits = maxTextureImageUnits;
    }

    public String getMaxVertexTextureImageUnits() {
        return maxVertexTextureImageUnits;
    }

    public void setMaxVertexTextureImageUnits(String maxVertexTextureImageUnits) {
        this.maxVertexTextureImageUnits = maxVertexTextureImageUnits;
    }
    
    public String getMaxGeometryTextureImageUnits() {
        return maxGeometryTextureImageUnits;
    }

    public void setMaxGeometryTextureImageUnits(String maxGeometryTextureImageUnits) {
        this.maxGeometryTextureImageUnits = maxGeometryTextureImageUnits;
    }
    
    
//    public void addPropertyChangeListener(String property, PropertyChangeListener pcl) {
//        pcs.addPropertyChangeListener(property, pcl);
//    }
//    
//    public void addPropertyChangeListener(PropertyChangeListener pcl) {
//        pcs.addPropertyChangeListener(pcl);
//    }
//    
//    public void removePropertyChangeListener(PropertyChangeListener pcl) {
//        pcs.removePropertyChangeListener(pcl);
//    }
//    
//    public void removePropertyChangeListener(String property, PropertyChangeListener pcl) {
//        pcs.removePropertyChangeListener(property, pcl);
//    }
    
 public static class Capability {

    public String name;
    public String value;

    public Capability() {
    }
    
    public Capability(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

 }


}
