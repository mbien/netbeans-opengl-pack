/*
 * Sampler.java
 *
 * Created on 18. Juni 2007, 23:24
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import net.java.nboglpack.visualdesigner.graphics3d.ShaderAttribute;
import net.java.nboglpack.visualdesigner.graphics3d.TextureMap;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class Sampler extends ExternalAttribute {
    
    protected static GLU glu = new GLU();
    private BufferedImage bufferedImage;
    private int samplerType;
    
    /** Creates a new instance of Sampler */
    public Sampler(String name, int samplerType, BufferedImage bufferedImage) {
        super(name, samplerType);
        this.bufferedImage = bufferedImage;
        switch(samplerType) {
            case DataType.DATA_TYPE_SAMPLER1D:
                this.samplerType = GL.GL_TEXTURE_1D;
                break;
            case DataType.DATA_TYPE_SAMPLER2D:
                this.samplerType = GL.GL_TEXTURE_2D;
                break;
            case DataType.DATA_TYPE_SAMPLER3D:
                this.samplerType = GL.GL_TEXTURE_3D;
                break;
            case DataType.DATA_TYPE_SAMPLERCUBE:
                this.samplerType = GL.GL_TEXTURE_CUBE_MAP;
                break;
            default:
                throw new RuntimeException("Sapler Type " + DataType.getDataTypeName(samplerType) + " is not supported yet.");
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
    
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
    
    public ShaderAttribute createShaderAttribute(GL gl) {
        TextureMap permTexture = new TextureMap(gl, glu, this.samplerType);
        try {
            permTexture.loadTexture(this.bufferedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return new ShaderAttribute(gl, this.name, permTexture);
    }    

/*
    public boolean operatesInFragmentShader() {
        return true;
    }

    public boolean operatesInVertexShader() {
        return true;
    }

    public boolean hasSameElement(UniqueCodeElement uniqueCodeElement) {
        return false;
    }

    public String exportCode(ShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return getUniqueName();
    }

    public boolean requiresSources(ShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return false;
    }

    public IExportable[] getSources(ShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return null;
    }

    public String getElementName() throws ExportingExeption {
        return name;
    }

    public int resolveDataType() throws ExportingExeption {
        return dataType;
    }
 */

}
