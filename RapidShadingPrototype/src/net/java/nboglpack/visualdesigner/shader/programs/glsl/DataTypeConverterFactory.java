/*
 * DataTypeConverterFactory.java
 *
 * Created on June 7, 2007, 5:09 PM
 */

package net.java.nboglpack.visualdesigner.shader.programs.glsl;

import java.awt.Color;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgramFactory;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 *
 * @author Samuel Sperling
 */
public class DataTypeConverterFactory implements IShaderProgramFactory {
    
    protected static String[] variants = new String[] {"To Components", "From Components", "To vec4", "To vec3", "To Vec2"};
    
    /**
     * Creates a new instance of OperationsFactory
     */
    public DataTypeConverterFactory() {
    }

    public String[] getVariants() {
        return variants;
    }

    public IShaderProgram createShaderProgram() {
        return new DataTypeConverter("To Components");
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new DataTypeConverter(variant);
    }

    public String getName() {
        return "Datatype converter";
    }

    public Color getNodeBackgroundColor() {
        return GLSLFunctions.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class DataTypeConverter extends ShaderNode implements IShaderProgram {
    
    public static final byte DATATYPE_CONVERTER_TO_COMPONENTS = 0;
    public static final byte DATATYPE_CONVERTER_FROM_COMPONENTS = 1;
    public static final byte DATATYPE_CONVERTER_TO_VEC4 = 2;
    public static final byte DATATYPE_CONVERTER_TO_VEC3 = 3;
    public static final byte DATATYPE_CONVERTER_TO_VEC2 = 4;
    
    private byte functionType;
    
    public DataTypeConverter(String variant) {
        super(getFunctionName(getFunctionType(variant)));
        super.setShaderProgram(this);
        setFunctionType(getFunctionType(variant));
        setBackground(GLSLFunctions.nodeBackgroundColor);
        
        setUpVariables();
    }

    private void setUpVariables() {
        int dimensions = 0;
        switch(functionType) {
            case DATATYPE_CONVERTER_TO_COMPONENTS:
                this.addInputVariable(new ShaderProgramInVariable(
                        "input",
                        getName() + " input",
                        DataType.DATA_TYPE_VEC | DataType.DATA_TYPE_IVEC | DataType.DATA_TYPE_BVEC,
                        ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                        ));
                for (int i = 0; i < 4; i++) {
                    this.addOutputVariable(new ShaderProgramOutVariable(
                            DataType.DATA_TYPE_DIMENSION_DESC_COORD[i],
                            getName() + " " + DataType.DATA_TYPE_DIMENSION_DESC_COORD[i],
                            DataType.DATA_TYPE_FLOAT | DataType.DATA_TYPE_INT | DataType.DATA_TYPE_BOOL,
                            ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                            ));
                }
                break;
            case DATATYPE_CONVERTER_FROM_COMPONENTS:
                for (int i = 0; i < 4; i++) {
                    this.addInputVariable(new ShaderProgramInVariable(
                            DataType.DATA_TYPE_DIMENSION_DESC_COORD[i],
                            getName() + " " + DataType.DATA_TYPE_DIMENSION_DESC_COORD[i],
                            DataType.DATA_TYPE_FLOAT | DataType.DATA_TYPE_INT | DataType.DATA_TYPE_BOOL,
                            ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                            ));
                }
                this.addOutputVariable(new ShaderProgramOutVariable(
                        "output",
                        getName() + " Ouput",
                        DataType.DATA_TYPE_VEC | DataType.DATA_TYPE_IVEC | DataType.DATA_TYPE_BVEC,
                        ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                        ));
                break;
            case DATATYPE_CONVERTER_TO_VEC4:
                dimensions++;
            case DATATYPE_CONVERTER_TO_VEC3:
                dimensions++;
            case DATATYPE_CONVERTER_TO_VEC2:
                dimensions += 2;
                
                this.addInputVariable(new ShaderProgramInVariable(
                        "input",
                        getName() + " input",
                        DataType.DATA_TYPE_VEC | DataType.DATA_TYPE_IVEC | DataType.DATA_TYPE_BVEC,
                        ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                        ));
                int targetDataType = DataType.getDataTypeFromDimensions(dimensions) |
                                DataType.DATA_TYPE_FLOAT | DataType.DATA_TYPE_INT | DataType.DATA_TYPE_BOOL;
                ShaderProgramInVariable inVar = new ShaderProgramInVariable(
                        "defaultvalues",
                        getName() + " default values",
                        targetDataType,
                        ShaderProgramInVariable.VALUE_SOURCE_CONST
                        );
                VariableValue vv = new VariableValue();
                vv.setDataType(DataType.DATA_TYPE_FLOAT | DataType.getDataTypeFromDimensions(dimensions));
                for (int i = 0; i < dimensions; i++) vv.setValue(i, 1f);
                inVar.setVariableValue(vv);
                this.addInputVariable(inVar);
                
                // output
                this.addOutputVariable(new ShaderProgramOutVariable(
                        "output",
                        getName() + " Ouput",
                        targetDataType,
                        ShaderProgramOutVariable.VALUE_SOURCE_CONNECTABLE
                        ));
                break;
        }
    }
    
    public void setFunctionType(byte functionType) {
        this.functionType = functionType;
    }

    private static byte getFunctionType(String variant) {
        for (int i = 0; i < DataTypeConverterFactory.variants.length; i++) {
            if (DataTypeConverterFactory.variants[i].equals(variant))
                return (byte) i;
        }
        return 0;
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }
    
    private static String getFunctionName(int variant) {
        return DataTypeConverterFactory.variants[variant];
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
                throws ExportingExeption {
        int dataType;
        String functionBody;
        switch(functionType) {
            case DATATYPE_CONVERTER_TO_COMPONENTS:
                return ValueAssignment.createCodeLineAssignment("<input>." + outputVariable.getElementName(),
                        getSources(outputVariable, exportVisitor));
            case DATATYPE_CONVERTER_FROM_COMPONENTS:
                dataType = outputVariable.resolveDataType();
                functionBody = DataType.getDataTypeShortName(dataType) + "(";
                for (int i = 0; i < DataType.getDimensions(dataType); i++) {
                    functionBody += "<" + DataType.DATA_TYPE_DIMENSION_DESC_COORD[i] + ">" +  ", ";
                }
                return ValueAssignment.createCodeLineAssignment(
                        functionBody.substring(0, functionBody.length() - 2) + ")",
                        getSources(outputVariable, exportVisitor));
            case DATATYPE_CONVERTER_TO_VEC4:
            case DATATYPE_CONVERTER_TO_VEC3:
            case DATATYPE_CONVERTER_TO_VEC2:
                int sourceDataType = inVariables.get(0).resolveDataType();
                int sourceDimensions = DataType.getDimensions(sourceDataType);
                dataType = resolveOutputDataType(outputVariable);
                
                // check whether it's an actual value
                int sourceType = this.getInVariables().get("defaultvalues").getValueSource();
                VariableValue defaultValue = null;
                if (sourceType == ShaderProgramInVariable.VALUE_SOURCE_CONST ||
                        sourceType == ShaderProgramInVariable.VALUE_SOURCE_EDITABLE) {
                    defaultValue = this.getInVariables().get("defaultvalues").getVariableValue();
                }
                
                functionBody = DataType.getDataTypeShortName(dataType) + "(";
                for (int i = 0; i < DataType.getDimensions(dataType); i++) {
                    if(sourceDimensions == 1) {
                        // get value from input
                        functionBody += "<input>";
                    } else if (i < sourceDimensions) {
                        // get value from input
                        functionBody += "<input>." + DataType.DATA_TYPE_DIMENSION_DESC_COORD[i];
                    } else {
                        
                        // Fill in with default value
                        if (defaultValue == null) {
                            functionBody += "<defaultvalues>." + DataType.DATA_TYPE_DIMENSION_DESC_COORD[i];
                        } else {
                            switch(DataType.getDataTypeOnly(sourceType)) {
                                case DataType.DATA_TYPE_BOOL: functionBody += defaultValue.getBooleanString(i);
                                case DataType.DATA_TYPE_INT: functionBody += defaultValue.getIntegerString(i);
                                case DataType.DATA_TYPE_FLOAT: functionBody += defaultValue.getFloatString(i);
                            }
                        }
                    }
                    //TODO: instead of <defaultvalues> use real values if it's a constant 
                    functionBody += ", ";
                }
                return ValueAssignment.createCodeLineAssignment(
                        functionBody.substring(0, functionBody.length() - 2) + ")",
                        getSources(outputVariable, exportVisitor));
        }
        return null;
    }
    
    public int resolveOutputDataType(ShaderProgramOutVariable shaderProgramVariable) throws ExportingExeption {
        int dataType = inVariables.get(0).resolveDataType();
        int dimensions = 0;
        switch(functionType) {
            case DATATYPE_CONVERTER_TO_COMPONENTS:
                dataType = inVariables.get(0).resolveDataType();
                return DataType.getDataTypeOnly(dataType) | DataType.DATA_TYPE_DIMENSION1;
            case DATATYPE_CONVERTER_FROM_COMPONENTS:
                dataType = inVariables.get(0).resolveDataType();
                for (dimensions = 0; dimensions < 4; dimensions++) {
                    if (!inVariables.get(dimensions).hasValue())
                        break;
                }
                switch(dimensions) {
                    case 0: throw new ExportingExeption("OutputDataType unresolvable.\nThere needs to be at least one value set as input at " + this.getName());
                    case 1:
                        dimensions = DataType.DATA_TYPE_DIMENSION1;
                        break;
                    case 2:
                        dimensions = DataType.DATA_TYPE_DIMENSION2;
                        break;
                    case 3:
                        dimensions = DataType.DATA_TYPE_DIMENSION3;
                        break;
                    case 4:
                        dimensions = DataType.DATA_TYPE_DIMENSION4;
                        break;
                }
                return DataType.getDataTypeOnly(dataType) | dimensions;
            case DATATYPE_CONVERTER_TO_VEC4:
                dimensions++;
            case DATATYPE_CONVERTER_TO_VEC3:
                dimensions++;
            case DATATYPE_CONVERTER_TO_VEC2:
                dimensions += 2;
                return DataType.getDataTypeFromDimensions(dimensions) | DataType.getDataTypeOnly(dataType);
        }
        throw new ExportingExeption("OutputDataType unresolvable");
    }
    
    public Class getFactoryClass() {
        return DataTypeConverterFactory.class;
    }
    
    public String getVariant() {
        return getFunctionName(functionType);
    }
    
}