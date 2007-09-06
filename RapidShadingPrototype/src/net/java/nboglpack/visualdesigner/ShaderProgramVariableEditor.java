/*
 * ShaderProgramVariableEditor.java
 *
 * Created on May 15, 2007, 6:05 PM
 */

package net.java.nboglpack.visualdesigner;

import javax.swing.JComboBox;
import net.java.nboglpack.visualdesigner.shader.variables.ShaderVariableEditor;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;

/**
 * Let's the user edit variables depending on the source defined by the
 * ShaderProgramVariable
 *
 * @author Samuel Sperling
 */
public class ShaderProgramVariableEditor extends ShaderVariableEditor {
    
    /** Creates a new instance of ShaderProgramVariableEditor */
    public ShaderProgramVariableEditor() {
    }
    
    protected void createControls() {
        if (isGlobal()) {
            createGlobalSelectionControl();
        } else {
            super.createControls();
        }
    }
    
    private boolean isGlobal() {
        ShaderProgramVariable value = (ShaderProgramVariable) this.value;
        return (this.value instanceof ShaderProgramInVariable &&
                (value.getValueSource() == ((ShaderProgramInVariable) value).VALUE_SOURCE_GLOBAL)) ||
                (this.value instanceof ShaderProgramOutVariable &&
                (value.getValueSource() == ((ShaderProgramOutVariable) value).VALUE_SOURCE_GLOBAL));
    }
    
    private JComboBox globalVars;
    private void createGlobalSelectionControl() {
        this.removeAll();
        globalVars = new JComboBox(Editor.globalVariables.getAllNames(this.value.getDataType()));
        // Select previously selected item
        if (this.value.getVariableValue() != null && this.value.getVariableValue().getShaderVariable() != null) {
            globalVars.setSelectedItem(this.value.getVariableValue().getShaderVariable().getName());
        }
        this.add(globalVars);
    }
    
    public Object getCellEditorValue() {
        if (isGlobal()) {
            VariableValue newValue = getVariableValue();
            newValue.setShaderVariable(Editor.globalVariables.get((String) globalVars.getSelectedItem()));
            return newValue;
        } else {
            return super.getCellEditorValue();
        }
    }
}
