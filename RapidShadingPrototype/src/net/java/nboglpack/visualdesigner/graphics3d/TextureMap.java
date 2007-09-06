/*
 * TextureMap.java
 *
 * Created on April 3, 2007, 10:42 AM
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Samuel Sperling
 */
public class TextureMap {
    
    private GL gl;
    private GLU glu;
    private int target;
    private int textureID;
    private int height;
    private int width;
    private BufferedImage bufferedImage;
    private static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);
    private static ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    private int srcPixelFormat = 0;
    private int dstPixelFormat;
    private int minFilter = GL.GL_LINEAR;
    private int magFilter = GL.GL_LINEAR;
    private boolean wrap = true;
    private boolean mipmapped = true;
    private ByteBuffer textureBuffer;
    
    /**
     * Creates a new instance of TextureMap
     * param type  One of GL_TEXTURE_2D, gl.GL_TEXTURE_3D...
     */
    public TextureMap(GL gl, GLU glu, int target) {
        this.gl = gl;
        this.glu = glu;
        this.target = target;
        
        // create the texture ID for this texture 
        textureID = createTextureID(); 
 
        // bind this texture 
        gl.glBindTexture(target, textureID);
    }
    public void loadTexture(String resourceName) throws IOException 
    {
        loadTexture(loadImage(resourceName));
    }
    
    public void loadTexture(BufferedImage bufferedImage) throws IOException 
    {
        this.bufferedImage = bufferedImage; 
        
        // Getting the real Width/Height of the Texture in the Memory
        this.width = get2Fold(bufferedImage.getWidth()); 
        this.height = get2Fold(bufferedImage.getHeight());
        
        // don't need it?
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL.GL_RGBA;
            dstPixelFormat = GL.GL_RGBA8;
        } else {
            srcPixelFormat = GL.GL_RGB;
            dstPixelFormat = GL.GL_RGB8;
        }
        
        // convert that image into a byte buffer of texture data 
        textureBuffer = convertImageData(bufferedImage); 

    } 
    
    /**
     * Should be called by the shaderProgram since that's the managing unit
     * @param texture one of GL_TEXTURE0, GL_TEXTURE1, ...
     */
    public void process(int texture) {
        gl.glActiveTexture(texture);
        int wrapMode = wrap ? GL.GL_REPEAT : GL.GL_CLAMP; 

        if (target == GL.GL_TEXTURE_2D) 
        { 
              gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, wrapMode); 
              gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, wrapMode); 
              gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter); 
              gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
        }
 
        // create either a series of mipmaps of a single texture image based on what's loaded 
        if (mipmapped) 
        { 
            glu.gluBuild2DMipmaps(target, 
                                    dstPixelFormat, 
                                    width, 
                                    height, 
                                    srcPixelFormat, 
                                    GL.GL_UNSIGNED_BYTE, 
                                    textureBuffer); 
        } 
        else 
        { 
            gl.glTexImage2D(target,
                            0, 
                            dstPixelFormat, 
                            width, 
                            height, 
                            0, 
                            srcPixelFormat, 
                            GL.GL_UNSIGNED_BYTE, 
                            textureBuffer ); 
        }
    }
    private ByteBuffer convertImageData(BufferedImage bufferedImage) throws IOException 
    { 
        ByteBuffer imageBuffer = null;
        WritableRaster raster;
        BufferedImage texImage;
        
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }
        
        Graphics g = texImage.getGraphics();
        g.drawImage(bufferedImage,0,0,null);
        
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length);
        imageBuffer.position(0);

        return imageBuffer; 
    }
    
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold)
            ret *= 2;               //TODO ret <<= 1;
        return ret;
    }
    
    private void actiateMe() {
        gl.glActiveTexture(gl.GL_TEXTURE0);//this.textureID);
    }
    
    /**
     * Set various parameters (wrapping, filtering, etc.) of the texture object
     */
    public void setParameter(int param, int value) {
        actiateMe();
        gl.glTexParameteri(this.target, param, value);
    }
    
    /**
     * Set various parameters (wrapping, filtering, etc.) of the texture object
     */
    public void setParameter(int param, float value) {
        actiateMe();
        gl.glTexParameterf(this.target, param, value);
    }
    
    private int createTextureID() {
        int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }
    private BufferedImage loadImage(String filename) throws IOException 
    { 
      File file = new File(filename);
        
        if (!file.isFile()) {
            throw new IOException("Cannot find: " + filename);
        }
        
        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(
           new FileInputStream(file))
        ); 
 
        return bufferedImage;
    }

    /**
     * Getter for property minFilter.
     * @return Value of property minFilter.
     */
    public int getMinFilter() {
        return this.minFilter;
    }

    /**
     * Setter for property minFilter.
     * @param minFilter New value of property minFilter.
     *                  should be set to GL_LINEAR, GL_NEAREST,
     *                  GL_NEAREST_MIPMAP_LINEAR,  GL_NEAREST_MIPMAP_NEAREST...
     */
    public void setMinFilter(int minFilter) {
        this.minFilter = minFilter;
    }

    /**
     * Getter for property magFilter.
     * @return Value of property magFilter.
     */
    public int getMagFilter() {
        return this.magFilter;
    }

    /**
     * Setter for property magFilter.
     * @param magFilter New value of property magFilter.
     *                  should be set to GL.GL_LINEAR or GL.GL_NEAREST
     */
    public void setMagFilter(int magFilter) {
        this.magFilter = magFilter;
    }

    public boolean isMipmapped() {
        return mipmapped;
    }

    public void setMipmapped(boolean mipmapped) {
        this.mipmapped = mipmapped;
    }

}
