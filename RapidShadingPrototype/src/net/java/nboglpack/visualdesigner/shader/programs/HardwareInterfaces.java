/*
 * HardwareInterfaces.java
 *
 * Created on April 30, 2007, 11:39 AM
 *
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.util.HashMap;
import net.java.nboglpack.visualdesigner.Editor;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.ShaderProgramInVariable;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 *
 * @author Samuel Sperling
 */
public class HardwareInterfaces implements IShaderProgramCollection {
    
    protected static Color nodeBackgroundColor = new Color(50, 50, 50, 255);
    
    private final Class[] availableShaderFactoryNames = new Class[] {
            net.java.nboglpack.visualdesigner.shader.programs.VertexOutputFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.VertexInputFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.FragmentOutputFactory.class,
            net.java.nboglpack.visualdesigner.shader.programs.FragmentInputFactory.class
        
        };
    
    /** Creates a new instance of HardwareInterfaces */
    public HardwareInterfaces() {
    }

    public Class[] getShaderProgramFactoryClasses() {
        return availableShaderFactoryNames;
    }

    public int availableShaders() {
        return availableShaderFactoryNames.length;
    }

    public String getCollectionName() {
        return "Hardware Interfaces";
    }
    
    public static HashMap<String, GlobalVariable> setupVars(
            ShaderNode shaderNode,
            String[] varsNames,
            String[] varsDescriptions,
            int[] varDataTypes,
            int globalType,
            boolean isInput,
            boolean isConnectable
            ) {

        // Add Globals
        HashMap<String, GlobalVariable> globalVariables = new HashMap<String, GlobalVariable>(varsNames.length);
        GlobalVariable globalVar;
        VariableValue variableValue;
        ShaderProgramOutVariable outVar;
        for (int i = 0; i < varsNames.length; i++) {
            // load data
            String varName = varsNames[i];
            String varDesc = varsDescriptions[i];
            int dataType = varDataTypes[i];
            
            // create global var
            globalVar = new GlobalVariable("gl_" + varName, dataType, globalType);
            Editor.globalVariables.add(globalVar);
            globalVariables.put("gl_" + varName, globalVar);
            
            if (isInput) {
                if (isConnectable) {
                    // add var to node
                    shaderNode.addInputVariable(new ShaderProgramInVariable(
                            varName,
                            varDesc,
                            dataType,
                            ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                            ));
                }

                // Add OutputVars to node
                outVar = new ShaderProgramOutVariable(
                        varName,
                        varDesc,
                        dataType,
                        ShaderProgramInVariable.VALUE_SOURCE_GLOBAL
                        );
                shaderNode.addOutputVariable(outVar);

                // Select global as output
                variableValue = new VariableValue();
                variableValue.setShaderVariable(globalVar);
                outVar.setVariableValue(variableValue);
            } else {
                shaderNode.addOutputVariable(new ShaderProgramOutVariable(
                        varName,
                        varDesc,
                        dataType,
                        ShaderProgramInVariable.VALUE_SOURCE_CONNECTABLE
                    ));
            }

        }
        return globalVariables;
    }
}
