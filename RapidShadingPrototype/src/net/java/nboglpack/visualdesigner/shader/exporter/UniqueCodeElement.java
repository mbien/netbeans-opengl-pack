/*
 * UniqueCodeElement.java
 *
 * Created on May 25, 2007, 4:26 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariable;

/**
 * Instances of this class represent an element of the fragment oder vertex
 * shader code. Their name is unique within their context.
 * This class is directly attached to a object that implements the ICodeElement
 * interface, but is supposed to hold it|s information just for the time of the
 * export.
 *
 * @author Samuel Sperling
 */
public abstract class UniqueCodeElement implements IExportable {
    
    public static final int CONTEXT_VERTEX_SHADER = 1;
    public static final int CONTEXT_FRAGMENT_SHADER = 2;
    public static final int CONTEXT_VERTEX_OR_FRAGMENT_SHADER = CONTEXT_VERTEX_SHADER | CONTEXT_FRAGMENT_SHADER;
    
    private String uniqueName;
    private UniqueCodeElementCollection holder;
    private boolean availableInVertexShader;
    private boolean availableInFragmentShader;
    private boolean isUniform;
    
    /**
     * Creates a new instance of UniqueCodeElement
     */
    public UniqueCodeElement() {
        availableInVertexShader = operatesInVertexShader();
        availableInFragmentShader = operatesInFragmentShader();
    }
    
    public abstract boolean operatesInFragmentShader();
    
    public abstract boolean operatesInVertexShader();

    public UniqueCodeElementCollection getHolder() {
        return holder;
    }

    public void setHolder(UniqueCodeElementCollection holder) {
        this.holder = holder;
    }
    
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void setAvailableInFragmentShader() {
        this.availableInFragmentShader = true;
    }

    public void setAvailableInVertexShader() {
        this.availableInVertexShader = true;
    }

    public boolean isAvailableInVertexShader() {
        return availableInVertexShader;
    }

    public boolean isAvailableInFragmentShader() {
        return availableInFragmentShader;
    }

    public void setUniform(boolean isUniform) {
        this.isUniform = isUniform;
    }
    
    public boolean isUniform() {
        return isUniform;
    }


    /**
     * Activates the availability of this element in either the vertex or the
     * fragment shader.
     * In no case the availability is set to false.
     * @param availableInFragmentShader
     *          true  - same as calling setAvailableInFragmentShader()
     *          false - same as calling setAvailableInVertexShader()
     */
    public void setShaderAvailablitily(boolean availableInFragmentShader) {
        if (availableInFragmentShader)
            setAvailableInFragmentShader();
        else
            setAvailableInVertexShader();
    }
    
    public abstract boolean hasSameElement(UniqueCodeElement uniqueCodeElement);
    
//    boolean hasSameElement(ICodeElement element) {
//        return getCodeElement() == element;
//    }
    
//
//    public int hashCode() {
//        return this.codeElement.hashCode();
//    }
//
//    public boolean equals(Object obj) {
//        return this.codeElement.equals(obj);
//    }
}
