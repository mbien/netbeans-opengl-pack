/*
 * VariableValue.java
 *
 * Created on May 15, 2007, 12:53 PM
 */

package net.java.nboglpack.visualdesigner.shader.variables;

import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.IPersistable;
import net.java.nboglpack.visualdesigner.PersistanceException;
import net.java.nboglpack.visualdesigner.ProjectPersistor;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;

/**
 * Represents a value that is assigned to a variable
 *
 * @author Samuel Sperling
 */
public class VariableValue implements IExportable, IPersistable {
    
    protected Object value;
    protected int dataType;
    protected int dataTypeOnly;
    protected int dimension;
    protected boolean isReferencing = false;
    
    /** Creates a new instance of VariableValue */
    public VariableValue() {
    }
    
    public void setDataType(int dataType) {
        isReferencing = false;
        if (this.dataType == dataType) return;
        dataTypeChanged(dataType);
        this.dataType = dataType;
        this.dataTypeOnly = DataType.getDataTypeOnly(dataType);
        this.dimension = DataType.getDimensions(dataType);
    }
    
    public int getDataType() {
        if (isReferencing)
            return ((ShaderVariable) value).getDataType();
        else
            return dataType;
    }
    
    /**
     * Handles everything that needs to be done, when the dataType was changed.
     */
    private void dataTypeChanged(int dataType) {
        int dimension = DataType.getDimensions(dataType);
        switch(DataType.getDataTypeOnly(dataType)) {
            case DataType.DATA_TYPE_BOOL:
                value = copyData(new Boolean[dimension]);
                break;
            case DataType.DATA_TYPE_INT:
                value = copyData(new Integer[dimension]);
                break;
            case DataType.DATA_TYPE_FLOAT:
                value = copyData(new Float[dimension]);
                break;
            case DataType.DATA_TYPE_MAT:
                value = copyData(new Float[dimension][dimension]);
                break;
            case DataType.DATA_TYPE_SAMPLER:
            case DataType.DATA_TYPE_SAMPLER_SHADOW:
            case DataType.DATA_TYPE_SAMPLERCUBE:
                //TODO: Sampler
                break;
        }
    }
    
    public void setChoosenDataType(int dataType) {
        dataTypeChanged(dataType);
    }
    
    private Boolean[] copyData(Boolean[] data) {
        // TODO: Catch Cast Exception
        for(int i = 0; i < data.length; i++)
            data[i] = getBooleanValue(i);
        return data;
    }
    
    private Integer[] copyData(Integer[] data) {
        for(int i = 0; i < data.length; i++)
            data[i] = getIntegerValue(i);
        return data;
    }
    
    private Float[] copyData(Float[] data) {
        for(int i = 0; i < data.length; i++)
            data[i] = getFloatValue(i);
        return data;
    }
    
    private Float[][] copyData(Float[][] data) {
        for(int i = 0; i < data.length; i++)
            data[i] = getMatrixValue(i);
        return data;
    }

    public Boolean getBooleanValue(int i) {
        if (i < dimension && (this.value instanceof Boolean[]))
            return ((Boolean[]) this.value)[i];
        return false;
    }

    public Integer getIntegerValue(int i) {
        if (i < dimension && (this.value instanceof Integer[]))
            return ((Integer[]) this.value)[i];
        return null;
    }

    public Float getFloatValue(int i) {
        if (i < dimension && (this.value instanceof Float[]))
            return ((Float[]) this.value)[i];
        return null;
    }

    public Float[] getMatrixValue(int i) {
        if (i < dimension && (this.value instanceof Float[][]))
            return ((Float[][]) this.value)[i];
        return null;
    }

    public Float getMatrixValue(int i, int j) {
        if (i < dimension && j < dimension && (this.value instanceof Float[][]))
            return ((Float[][]) this.value)[i][j];
        return null;
    }
    
    public ShaderVariable getShaderVariable() {
        return (ShaderVariable) this.value;
    }

    public String getBooleanString(int i) {
        Boolean v = getBooleanValue(i);
        return v == null ? "" : v.toString();
    }

    public String getIntegerString(int i) {
        Integer v = getIntegerValue(i);
        return v == null ? "" : v.toString();
    }

    public String getFloatString(int i) {
        Float v = getFloatValue(i);
        return v == null ? "" : v.toString();
    }

    public String getMatrixString(int i) {
        Float[] v = getMatrixValue(i);
        if (v == null) return "";
        
        String value = "";
        if (dimension > 1) value += "{";
        for (int j = 0; j < dimension; j++) {
            value += v[j];
            if (j != dimension - 1) value += ", ";
        }
        if (dimension > 1) value += "}";
        return value;
    }
    
    /**
     * Sets the value of this variable
     * @param i  index of dimension where value should be set.
     * @param value  Value set is to be set.
     */
    public void setValue(int i, Boolean value) {
        if (i < this.dimension)
            ((Boolean[]) this.value)[i] = value;
    }
    /**
     * Sets the value of this variable
     * @param i  index of dimension where value should be set.
     * @param value  Value set is to be set.
     */
    public void setValue(int i, Integer value) {
        if (i < this.dimension)
            ((Integer[]) this.value)[i] = value;
    }
    /**
     * Sets the value of this variable
     * @param i  index of dimension where value should be set.
     * @param value  Value set is to be set.
     */
    public void setValue(int i, Float value) {
        if (i < this.dimension)
            ((Float[]) this.value)[i] = value;
    }
    /**
     * Sets the value of this variable
     * @param i  index of dimension where value should be set.
     * @param value  Value set is to be set.
     */
    public void setValue(int i, Float[] value) {
        if (i < this.dimension)
            ((Float[][]) this.value)[i] = value;
    }
    /**
     * Sets the value of this variable
     * @param value  Value set is to be set.
     */
    public void setShaderVariable(ShaderVariable value) {
        isReferencing = true;
        this.value = value;
    }
    
    public String toString() {
        if (this.value instanceof ShaderVariable)
            return ((ShaderVariable) this.value).getName();
        
        //TODO: make string conversion dependend on class type of this.value
        
        String value = "";
        if ((dataTypeOnly &
                (DataType.DATA_TYPE_SAMPLER |
                DataType.DATA_TYPE_SAMPLER_SHADOW |
                DataType.DATA_TYPE_SAMPLERCUBE))
                > 0) {
            // Sampler
            //TODO: create Sampler value
            value = DataType.getDataTypeName(dataTypeOnly);
        } else {
            if (dimension > 1) value = "{";
            for (int i = 0; i < dimension; i++) {
                switch (dataTypeOnly) {
                    case DataType.DATA_TYPE_BOOL:
                        value += getBooleanString(i);
                        break;
                    case DataType.DATA_TYPE_INT:
                        value += getIntegerString(i);
                        break;
                    case DataType.DATA_TYPE_FLOAT:
                        value += getFloatString(i);
                        break;
                    case DataType.DATA_TYPE_MAT:
                        value += getMatrixString(i);
                        break;
                }
                if (i != dimension - 1) value += ", ";
            }
            if (dimension > 1) value += "}";
        }
        return value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isReferencing() {
        return isReferencing;
    }

    public String exportCode(IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return exportVisitor.autoExport((VariableValue) this.value);
    }

    public boolean requiresSources(IShaderCodeExportVisitor exportVisitor) {
        return false;
    }

    public IExportable[] getSources(IShaderCodeExportVisitor exportVisitor) {
        return null;
    }

    public String getElementName() {
        return DataType.getDataTypeShortName(this.getDataType());
    }

    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        saveVisitor.save("isreferencing", isReferencing);
//        if (isReferencing) {
//            saveVisitor.save("refrerencename", ((ShaderVariable) this.value).getName());
//        } else {
//            saveVisitor.save("datatype", dataType);
//            if ((dataTypeOnly &
//                    (DataType.DATA_TYPE_SAMPLER |
//                    DataType.DATA_TYPE_SAMPLER_SHADOW |
//                    DataType.DATA_TYPE_SAMPLERCUBE))
//                    > 0) {
//                // Sampler
//                //TODO: save Sampler value
//            } else {
//                switch (dataTypeOnly) {
//                    case DataType.DATA_TYPE_BOOL:
//                        saveVisitor.save("value", ((Boolean[]) this.value));
//                        break;
//                    case DataType.DATA_TYPE_INT:
//                        saveVisitor.save("value", ((Integer[]) this.value));
//                        break;
//                    case DataType.DATA_TYPE_FLOAT:
//                        saveVisitor.save("value", ((Float[]) this.value));
//                        break;
//                    case DataType.DATA_TYPE_MAT:
//                        saveVisitor.save("value", ((Float[][]) this.value));
//                        break;
//                }
//            }
//        }
    }

    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        this.isReferencing = loadVisitor.loadBoolean("isreferencing");
//        if (isReferencing) {
//            String name = loadVisitor.loadString("refrerencename");
//            this.value = Editor.globalVariables.get(name);
//        } else {
//            setDataType(loadVisitor.loadInteger("datatype"));
//            if ((dataTypeOnly &
//                    (DataType.DATA_TYPE_SAMPLER |
//                    DataType.DATA_TYPE_SAMPLER_SHADOW |
//                    DataType.DATA_TYPE_SAMPLERCUBE))
//                    > 0) {
//                // Sampler
//                //TODO: load Sampler value
//            } else {
//                switch (dataTypeOnly) {
//                    case DataType.DATA_TYPE_BOOL:
//                        this.value = loadVisitor.loadBooleanArray("value");
//                        break;
//                    case DataType.DATA_TYPE_INT:
//                        this.value = loadVisitor.loadIntegerArray("value");
//                        break;
//                    case DataType.DATA_TYPE_FLOAT:
//                        this.value = loadVisitor.loadFloatArray("value");
//                        break;
//                    case DataType.DATA_TYPE_MAT:
//                        this.value = loadVisitor.loadFloatArray2("value");
//                        break;
//                }
//            }
//        }
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public int resolveDataType() throws ExportingExeption {
        if (!DataType.isDataTypeExplicit(this.getDataType()))
            throw new ExportingExeption("DataType of Variable Value is not expocit :" + DataType.getDataTypeName(this.getDataType()));
        return this.getDataType();
    }

}
