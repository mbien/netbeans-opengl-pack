/*
 * NoiseGenerator.java
 *
 * Created on April 3, 2007, 9:59 AM
 */

package net.java.nboglpack.visualdesigner.tools;

import edu.cornell.lassp.houle.RngPack.RanMT;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Class to create a noisy bitmap
 *
 * @author Samuel Sperling
 */
public class NoiseGenerator {
    
    private static int perm[] = new int[] {151,160,137,91,90,15,
    131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
    190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
    88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
    77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
    102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
    135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
    5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
    223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
    129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
    251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
    49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
    138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180};
    
    private static int[][] grad3 = new int[][]
        {{0,1,1},{0,1,-1},{0,-1,1},{0,-1,-1},
        {1,0,1},{1,0,-1},{-1,0,1},{-1,0,-1},
        {1,1,0},{1,-1,0},{-1,1,0},{-1,-1,0}, // 12 cube edges
        {1,0,-1},{-1,0,-1},{0,-1,1},{0,1,1}}; // 4 more to make 16
    
    private static int[][] grad4 = new int[][]
        {{0,1,1,1}, {0,1,1,-1}, {0,1,-1,1}, {0,1,-1,-1}, // 32 tesseract edges
        {0,-1,1,1}, {0,-1,1,-1}, {0,-1,-1,1}, {0,-1,-1,-1},
        {1,0,1,1}, {1,0,1,-1}, {1,0,-1,1}, {1,0,-1,-1},
        {-1,0,1,1}, {-1,0,1,-1}, {-1,0,-1,1}, {-1,0,-1,-1},
        {1,1,0,1}, {1,1,0,-1}, {1,-1,0,1}, {1,-1,0,-1},
        {-1,1,0,1}, {-1,1,0,-1}, {-1,-1,0,1}, {-1,-1,0,-1},
        {1,1,1,0}, {1,1,-1,0}, {1,-1,1,0}, {1,-1,-1,0},
        {-1,1,1,0}, {-1,1,-1,0}, {-1,-1,1,0}, {-1,-1,-1,0}};

    public static final int DEFAUL_IMAGE_DIMENSION = 128;
    
    /** Creates a new instance of NoiseGenerator */
    public static BufferedImage createNoiseTexture() throws IOException {
        return createNoiseTexture(DEFAUL_IMAGE_DIMENSION, BufferedImage.TYPE_INT_ARGB);
    }
    public static BufferedImage createNoiseTexture(int dimension, int imageType) throws IOException {
        BufferedImage bi = new BufferedImage( dimension, dimension, imageType);
//        Random r = new Random();
        RanMT r = new RanMT(System.nanoTime());
        int[] data = new int[dimension * dimension];
        long max = 1l << 32l;
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) ((long) (r.raw() * max) - Integer.MAX_VALUE);
        }
        bi.setRGB(0, 0, dimension, dimension, data, 0, dimension);
        return bi;
    }
    
    /**
     *
     * param imageType  Such as BufferedImage.TYPE_INT_ARGB
     */
    public static void writeNoiseTexture(File file, String formatName, int dimension, int imageType) throws IOException {
        ImageIO.write(createNoiseTexture(dimension, imageType), formatName, file);
    }    
    
    public static void writeNoiseTexture(File file, String formatName) throws IOException {
        ImageIO.write(createNoiseTexture(), formatName, file);
    }
    
    public static void main(String[] args) {
        try {
            long time = System.currentTimeMillis();
            writeNoiseTexture(new File("noise32.png"), "png", 128, BufferedImage.TYPE_INT_ARGB);
            System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        try {
//            long time = System.currentTimeMillis();
//            ImageIO.write(createPermTexture(), "png", new File("perm.png"));
//            ImageIO.write(createGradTexture(), "png", new File("grad.png"));
//            System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
    
    /**
     * initPermTexture(GLuint *texID) - create and load a 2D texture for
     * a combined index permutation and gradient lookup table.
     * This texture is used for 2D and 3D noise, both classic and simplex.
     */
    public static BufferedImage createPermTexture() {
        int dimension = perm.length;
        BufferedImage bi = new BufferedImage( dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        int[] data = new int[dimension*dimension];
        int offset;
        int value;
        for(int i = 0; i<dimension; i++) {
            for(int j = 0; j<dimension; j++) {
                offset = (i*dimension+j);
                value = perm[(j+perm[i]) & 0xFF];
                data[offset] =  (grad3[value & 0x0F][0] * 64 + 64) |
                                (grad3[value & 0x0F][1] * 64 + 64) << 8 |
                                (grad3[value & 0x0F][2] * 64 + 64) << 16 |
                                value << 24;
//                data[offset] = (byte) (grad3[value & 0x0F][0] * 64 + 64);   // Gradient x
//                data[offset+1] = (byte) (grad3[value & 0x0F][1] * 64 + 64); // Gradient y
//                data[offset+2] = (byte) (grad3[value & 0x0F][2] * 64 + 64); // Gradient z
//                data[offset+3] = (byte) value;                     // Permuted index
            }
        }
        bi.setRGB(0, 0, dimension, dimension, data, 0, dimension);
        return bi;
    }
    public static BufferedImage createGradTexture() {
        int dimension = perm.length;
        BufferedImage bi = new BufferedImage( dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        
        int[] data = new int[dimension*dimension];
        int offset;
        int value;
        for(int i = 0; i<dimension; i++) {
            for(int j = 0; j<dimension; j++) {
                offset = (i*dimension+j);
                value = perm[(j+perm[i]) & 0xFF];
                data[offset] =  (grad4[value & 0x1F][0] * 64 + 64) |
                                (grad4[value & 0x1F][1] * 64 + 64) << 8 |
                                (grad4[value & 0x1F][2] * 64 + 64) << 16 |
                                (grad4[value & 0x1F][3] * 64 + 64) << 24;

            }
        }
        bi.setRGB(0, 0, dimension, dimension, data, 0, dimension);
        return bi;
    }
}
