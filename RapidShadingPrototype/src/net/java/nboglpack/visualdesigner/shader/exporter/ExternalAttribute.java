/*
 * ExternalAttribute.java
 *
 * Created on 1. Juli 2007, 16:41
 *
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import javax.media.opengl.GL;
import net.java.nboglpack.visualdesigner.graphics3d.ShaderAttribute;

/**
 * Classes deriving from this class are attributes to a shader like the uniform
 * variables in GLSL
 *
 * @author Samuel Sperling
 */
public abstract class ExternalAttribute {
    
    protected String name;
    protected int dataType;
    protected ExternalAssignment externalAssignment;
    
    /**
     * Creates a new instance of ExternalAttribute
     */
    public ExternalAttribute(String name, int dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public void setRepresentative(ExternalAssignment externalAssignment) {
        this.externalAssignment = externalAssignment;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public abstract ShaderAttribute createShaderAttribute(GL gl); //{
//        // Exporter needs a factory
//        return null;
//    }
    
}
