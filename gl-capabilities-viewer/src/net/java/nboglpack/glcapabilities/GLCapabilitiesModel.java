/*
 * GLCapabilitiesModel.java
 * 
 * Created on 25.06.2007, 16:31:39
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nboglpack.glcapabilities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Bien
 */
public class GLCapabilitiesModel {
    
 private String glVersion;
 private String glslVersion;

 private String implVersion;
 private String vendor;
 private String renderer;
 
 private String maxLights;
 private String maxAnisotropy;
 private String maxViewPortSize;
 private String maxTextureSize;
 private String maxTextureUnits;
 private String maxTextureImageUnits;
 private String maxVertexTextureImageUnits;
 private String maxGeometryTextureImageUnits;
 private String maxDrawBuffers;

 
 private ArrayList<String> extensions = new ArrayList<String>();
 private List<Capability> capabilities = new ArrayList<Capability>();


 public GLCapabilitiesModel() {
 }
 
    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }
    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(ArrayList<String> extentions) {
        this.extensions = extentions;
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
        this.implVersion = implementationVersion;
    }

    public void setGLVendor(String glVendor) {
        this.vendor = glVendor;
    }

    public void setGLVersion(String version) {
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
