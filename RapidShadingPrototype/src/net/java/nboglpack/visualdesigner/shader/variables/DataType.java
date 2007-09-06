package net.java.nboglpack.visualdesigner.shader.variables;

import java.awt.Component;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

public class DataType
{
    // <editor-fold defaultstate="collapsed" desc=" static content ">
    /* General data types indicies */
    public static final int DATA_TYPE_BOOL_INDEX = 0;
    public static final int DATA_TYPE_INT_INDEX = 1;
    public static final int DATA_TYPE_FLOAT_INDEX = 2;
    public static final int DATA_TYPE_MAT_INDEX = 3;
    public static final int DATA_TYPE_SAMPLER_INDEX = 4;
    public static final int DATA_TYPE_SAMPLER_SHADOW_INDEX = 5;
    public static final int DATA_TYPE_SAMPLERCUBE_INDEX = 6;
    
    /* General data types */
    public static final int DATA_TYPE_VOID = 0;
    public static final int DATA_TYPE_BOOL = 1 << DATA_TYPE_BOOL_INDEX;
    public static final int DATA_TYPE_INT = 1 << DATA_TYPE_INT_INDEX;
    public static final int DATA_TYPE_FLOAT = 1 << DATA_TYPE_FLOAT_INDEX;
    public static final int DATA_TYPE_MAT = 1 << DATA_TYPE_MAT_INDEX;
    public static final int DATA_TYPE_SAMPLER = 1 << DATA_TYPE_SAMPLER_INDEX;
    public static final int DATA_TYPE_SAMPLER_SHADOW = 1 << DATA_TYPE_SAMPLER_SHADOW_INDEX;
    public static final int DATA_TYPE_SAMPLERCUBE = 1 << DATA_TYPE_SAMPLERCUBE_INDEX;

    public static final int DATA_TYPE_LOWEST_EXPLICIT_BIT = DATA_TYPE_BOOL_INDEX;
    public static final int DATA_TYPE_HIGHEST_EXPLICIT_BIT = DATA_TYPE_SAMPLERCUBE_INDEX;
    
    /* Data types Dimensions */
    public static final int DATA_TYPE_DIMENSION1 = 1 << 10;
    public static final int DATA_TYPE_DIMENSION2 = 1 << 11;
    public static final int DATA_TYPE_DIMENSION3 = 1 << 12;
    public static final int DATA_TYPE_DIMENSION4 = 1 << 13;

    public static final int DATA_TYPE_LOWEST_DIMENSION_BIT = 10;
    public static final int DATA_TYPE_HIGHEST_DIMENSION_BIT = 13;


    /* Existing generic data types  */
    public static final int DATA_TYPE_BVEC1 = DATA_TYPE_BOOL | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_BVEC2 = DATA_TYPE_BOOL | DATA_TYPE_DIMENSION2;
    public static final int DATA_TYPE_BVEC3 = DATA_TYPE_BOOL | DATA_TYPE_DIMENSION3;
    public static final int DATA_TYPE_BVEC4 = DATA_TYPE_BOOL | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_IVEC1 = DATA_TYPE_INT | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_IVEC2 = DATA_TYPE_INT | DATA_TYPE_DIMENSION2;
    public static final int DATA_TYPE_IVEC3 = DATA_TYPE_INT | DATA_TYPE_DIMENSION3;
    public static final int DATA_TYPE_IVEC4 = DATA_TYPE_INT | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_VEC1 = DATA_TYPE_FLOAT | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_VEC2 = DATA_TYPE_FLOAT | DATA_TYPE_DIMENSION2;
    public static final int DATA_TYPE_VEC3 = DATA_TYPE_FLOAT | DATA_TYPE_DIMENSION3;
    public static final int DATA_TYPE_VEC4 = DATA_TYPE_FLOAT | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_MAT1 = DATA_TYPE_MAT | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_MAT2 = DATA_TYPE_MAT | DATA_TYPE_DIMENSION2;
    public static final int DATA_TYPE_MAT3 = DATA_TYPE_MAT | DATA_TYPE_DIMENSION3;
    public static final int DATA_TYPE_MAT4 = DATA_TYPE_MAT | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_SAMPLER1D = DATA_TYPE_SAMPLER | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_SAMPLER2D = DATA_TYPE_SAMPLER | DATA_TYPE_DIMENSION2;
    public static final int DATA_TYPE_SAMPLER3D = DATA_TYPE_SAMPLER | DATA_TYPE_DIMENSION3;
    public static final int DATA_TYPE_SAMPLER1D_SHADOW = DATA_TYPE_SAMPLER_SHADOW | DATA_TYPE_DIMENSION1;
    public static final int DATA_TYPE_SAMPLER2D_SHADOW = DATA_TYPE_SAMPLER_SHADOW | DATA_TYPE_DIMENSION2;

    /* Common datatype variations for inbuild functions */
    public static final int DATA_TYPE_VEC = DATA_TYPE_FLOAT | DATA_TYPE_DIMENSION2 | DATA_TYPE_DIMENSION3 | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_IVEC = DATA_TYPE_INT | DATA_TYPE_DIMENSION2 | DATA_TYPE_DIMENSION3 | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_BVEC = DATA_TYPE_BOOL | DATA_TYPE_DIMENSION2 | DATA_TYPE_DIMENSION3 | DATA_TYPE_DIMENSION4;
    public static final int DATA_TYPE_GENTYPE = DATA_TYPE_VEC | DATA_TYPE_DIMENSION1;
    
    /* Dimension Desciptions */
    public static final String[] DATA_TYPE_DIMENSION_DESC_COORD = new String[] {"x", "y", "z", "w"};
    public static final String[] DATA_TYPE_DIMENSION_DESC_COLOR = new String[] {"r", "g", "b", "a"};
    public static final String[] DATA_TYPE_DIMENSION_DESC_TEXTURE = new String[] {"s", "t", "p", "q"};
    
    public static String getDataTypeName(int dataType) {
        switch(dataType) {
            case DATA_TYPE_VOID:
                return "Void";
            case DATA_TYPE_BOOL:
                return "Boolean";
            case DATA_TYPE_INT:
                return "Integer";
            case DATA_TYPE_FLOAT:
                return "Float";
            case DATA_TYPE_MAT:
                return "Matrix";
            case DATA_TYPE_SAMPLER:
                return "Sampler";
            case DATA_TYPE_SAMPLER_SHADOW:
                return "Shadow Sampler";
            case DATA_TYPE_DIMENSION1:
                return "One Dimentional";
            case DATA_TYPE_DIMENSION2:
                return "Two Dimentional";
            case DATA_TYPE_DIMENSION3:
                return "Three Dimentional";
            case DATA_TYPE_DIMENSION4:
                return "Four Dimentional";
            case DATA_TYPE_SAMPLERCUBE:
                return "Cube Sampler";
            case DATA_TYPE_BVEC1:
                return "One Dimensional Boolean Vector";
            case DATA_TYPE_BVEC2:
                return "Two Dimensional Boolean Vector";
            case DATA_TYPE_BVEC3:
                return "Three Dimensional Boolean Vector";
            case DATA_TYPE_BVEC4:
                return "Four Dimensional Boolean Vector";
            case DATA_TYPE_IVEC1:
                return "One Dimensional Integer Vector";
            case DATA_TYPE_IVEC2:
                return "Two Dimensional Integer Vector";
            case DATA_TYPE_IVEC3:
                return "Three Dimensional Integer Vector";
            case DATA_TYPE_IVEC4:
                return "Four Dimensional Integer Vector";
            case DATA_TYPE_VEC1:
                return "One Dimensional Float Vector";
            case DATA_TYPE_VEC2:
                return "Two Dimensional Float Vector";
            case DATA_TYPE_VEC3:
                return "Three Dimensional Float Vector";
            case DATA_TYPE_VEC4:
                return "Four Dimensional Float Vector";
            case DATA_TYPE_MAT1:
                return "One Dimensional Float Matrix";
            case DATA_TYPE_MAT2:
                return "Two Dimensional Float Matrix";
            case DATA_TYPE_MAT3:
                return "Three Dimensional Float Matrix";
            case DATA_TYPE_MAT4:
                return "Four Dimensional Float Matrix";
            case DATA_TYPE_SAMPLER1D:
                return "One Dimensional Sampler";
            case DATA_TYPE_SAMPLER2D:
                return "Two Dimensional Sampler";
            case DATA_TYPE_SAMPLER3D:
                return "Three Dimensional Sampler";
            case DATA_TYPE_SAMPLER1D_SHADOW:
                return "One Dimensional Shadow Sampler";
            case DATA_TYPE_SAMPLER2D_SHADOW:
                return "Two Dimensional Shadow Sampler";
            case DATA_TYPE_VEC:
                return "Two to Four Dimensional Float Vector";
            case DATA_TYPE_IVEC:
                return "Two to Four Dimensional Integer Vector";
            case DATA_TYPE_BVEC:
                return "Two to Four Dimensional Boolean Vector";
            case DATA_TYPE_GENTYPE:
                return "One to Four Dimensional Float Vector";
            case DATA_TYPE_FLOAT | DATA_TYPE_INT | DATA_TYPE_BOOL:
                return "Float, Integer or Boolean";
            case DATA_TYPE_VEC | DATA_TYPE_IVEC | DATA_TYPE_BVEC:
                return "One to Four Dimensional Float, Integer or Boolean Vector";
            case DATA_TYPE_FLOAT | DATA_TYPE_INT | DATA_TYPE_BOOL | DATA_TYPE_DIMENSION2:
                return "Two Dimensional Float, Integer or Boolean Vector";
            case DATA_TYPE_FLOAT | DATA_TYPE_INT | DATA_TYPE_BOOL | DATA_TYPE_DIMENSION3:
                return "Three Dimensional Float, Integer or Boolean Vector";
            case DATA_TYPE_FLOAT | DATA_TYPE_INT | DATA_TYPE_BOOL | DATA_TYPE_DIMENSION4:
                return "Four Dimensional Float, Integer or Boolean Vector";
            case DATA_TYPE_SAMPLER | DATA_TYPE_SAMPLER_SHADOW | DATA_TYPE_SAMPLERCUBE | DATA_TYPE_DIMENSION1 | DATA_TYPE_DIMENSION2 | DATA_TYPE_DIMENSION3:
                return "One to Three dimensional Sampler of any Type";
        }
        return "Undefined";
    }
    
    public static String getDataTypeShortName(int dataType) {
        switch(dataType) {
            case DATA_TYPE_VOID:
                return "void";
            case DATA_TYPE_BOOL:
                return "bool";
            case DATA_TYPE_INT:
                return "int";
            case DATA_TYPE_FLOAT:
                return "float";
            case DATA_TYPE_MAT:
                return "mat";
            case DATA_TYPE_SAMPLER:
                return "sampler";
            case DATA_TYPE_SAMPLER_SHADOW:
                return "samplerShadow";
            case DATA_TYPE_DIMENSION1:
                return "dim1";
            case DATA_TYPE_DIMENSION2:
                return "dim2";
            case DATA_TYPE_DIMENSION3:
                return "dim3";
            case DATA_TYPE_DIMENSION4:
                return "dim4";
            case DATA_TYPE_SAMPLERCUBE:
                return "samplerCube";
            case DATA_TYPE_BVEC1:
                return "bool";
            case DATA_TYPE_BVEC2:
                return "bvec2";
            case DATA_TYPE_BVEC3:
                return "bvec3";
            case DATA_TYPE_BVEC4:
                return "bvec4";
            case DATA_TYPE_IVEC1:
                return "int";
            case DATA_TYPE_IVEC2:
                return "ivec2";
            case DATA_TYPE_IVEC3:
                return "ivec3";
            case DATA_TYPE_IVEC4:
                return "ivec4";
            case DATA_TYPE_VEC1:
                return "float";
            case DATA_TYPE_VEC2:
                return "vec2";
            case DATA_TYPE_VEC3:
                return "vec3";
            case DATA_TYPE_VEC4:
                return "vec4";
            case DATA_TYPE_MAT1:
                return "float";
            case DATA_TYPE_MAT2:
                return "mat2";
            case DATA_TYPE_MAT3:
                return "mat3";
            case DATA_TYPE_MAT4:
                return "mat4";
            case DATA_TYPE_SAMPLER1D:
                return "sampler1D";
            case DATA_TYPE_SAMPLER2D:
                return "sampler2D";
            case DATA_TYPE_SAMPLER3D:
                return "sampler3D";
            case DATA_TYPE_SAMPLER1D_SHADOW:
                return "sampler1DShadow";
            case DATA_TYPE_SAMPLER2D_SHADOW:
                return "sampler2DShadow";
            case DATA_TYPE_VEC:
                return "vec";
            case DATA_TYPE_IVEC:
                return "ivec";
            case DATA_TYPE_BVEC:
                return "bvec";
            case DATA_TYPE_GENTYPE:
                return "gentype";
        }
        return "Undefined";
    }
    
    /**
     * Determines whether the given dataType and Dimension is generic or explicit
     * @param dataType to 'analyze'
     * @return True if dataType is explicit
     *         False if dataType is generic
     */
    public static boolean isDataTypeExplicit(int dataType) {
        return !(isDataTypeGeneric(dataType) || isDimensionGeneric(dataType));
    }
    
    /**
     * Determines whether the given dataType is generic or explicit
     * @param dataType to 'analyze'
     * @return True if dataType is generic
     *         False if dataType is explicit
     */
    public static boolean isDataTypeGeneric(int dataType) {
        // check whether it's just one type int, bool, ...
        return (countSetBits(dataType, 0, DATA_TYPE_HIGHEST_EXPLICIT_BIT) > 1);
    }
    
    /**
     * Determines whether the given dataType Dimension is generic or explicit
     * @param dataType to 'analyze'
     * @return True if dataType Dimension is generic
     *         False if dataType Dimension is explicit
     */
    public static boolean isDimensionGeneric(int dataType) {
        // check whether it has more than one dimension
        return (countSetBits(dataType, DATA_TYPE_HIGHEST_EXPLICIT_BIT + 1, DATA_TYPE_HIGHEST_DIMENSION_BIT) > 1);
    }
    
    /**
     * Counts the number of set bits in an int number
     * e.g. 0011101 would return 4
     *      1000001 would return 2
     * @param value Number where set bits will be counted
     * @param bitStart startBit where counting starts
     * @param bitEnd endBit where counting stops
     */
    public static byte countSetBits(int value, int bitStart, int bitEnd) {
        byte count = 0;
//        bitEnd = Math.max(bitEnd, 32);
        for (int i = bitStart; i <= bitEnd; i++)
            if ((value & (1 << i)) > 0)
                count++;
        return count;
    }
    
    /**
     * Gets dataType describing part only
     */
    public static int getDataTypeOnly(int dataType) {
        return ((1 << (DataType.DATA_TYPE_HIGHEST_EXPLICIT_BIT + 1)) - 1)
                & dataType;
    }
    
    /**
     * Gets dimension describing part only
     */
    public static int getDimensionOnly(int dataType) {
        return (0xFFFFFFFF ^ 
                ((1 << (DataType.DATA_TYPE_HIGHEST_EXPLICIT_BIT + 1)) - 1))
                & dataType;
    }
    
    public static Component getEditingComponent(int dataType) {
        JTextField textField = new JTextField();
        
        return textField;
    }
    
    /**
     * Returns the numer of dimensions of the given dataType.
     * @return the numer of dimensions of the given dataType.
     *         returns 0 if dataType dimensions are generic
     */
    public static int getDimensions(int dataType) {
        switch (getDimensionOnly(dataType)) {
            case DataType.DATA_TYPE_DIMENSION1: return 1;
            case DataType.DATA_TYPE_DIMENSION2: return 2;
            case DataType.DATA_TYPE_DIMENSION3: return 3;
            case DataType.DATA_TYPE_DIMENSION4: return 4;
        }
        return 0;
    }
    
    /**
     * Returns the dataType representing the given dimension
     * @return the dataType representing the given dimension
     *          returns 0 if dimension is not a valid dataType
     */
    public static int getDataTypeFromDimensions(int i) {
        switch (i) {
            case 1: return DataType.DATA_TYPE_DIMENSION1;
            case 2: return DataType.DATA_TYPE_DIMENSION2;
            case 3: return DataType.DATA_TYPE_DIMENSION3;
            case 4: return DataType.DATA_TYPE_DIMENSION4;
        }
        return 0;
    }
    
    /**
     * Returns the index of dataType
     * @return the index of dataType
     */
    public static int getDataTypeIndex(int dataType) {
        switch (getDataTypeOnly(dataType)) {
            case DataType.DATA_TYPE_BOOL: return DATA_TYPE_BOOL_INDEX;
            case DataType.DATA_TYPE_INT: return DATA_TYPE_INT_INDEX;
            case DataType.DATA_TYPE_FLOAT: return DATA_TYPE_FLOAT_INDEX;
            case DataType.DATA_TYPE_MAT: return DATA_TYPE_MAT_INDEX;
            case DataType.DATA_TYPE_SAMPLER: return DATA_TYPE_SAMPLER_INDEX;
            case DataType.DATA_TYPE_SAMPLER_SHADOW: return DATA_TYPE_SAMPLER_SHADOW_INDEX;
            case DataType.DATA_TYPE_SAMPLERCUBE: return DATA_TYPE_SAMPLERCUBE_INDEX;
        }
        return 0;
    }
    
    /**
     * Checks whether a generic dataType supports a specific dataType
     *
     * @param genericDataType Genric dataType
     * @param specificDataType this dataType is checked it's compatibility to
     *          the genericDataType
     * @return true if the specificDataType is compatible to the genericDataType
     */
    public static boolean isCompatible(int genericDataType, int specificDataType) {
        return (((0xFFFFFFFF ^ genericDataType) & specificDataType) == 0);
    }
    
    // </editor-fold>

}
