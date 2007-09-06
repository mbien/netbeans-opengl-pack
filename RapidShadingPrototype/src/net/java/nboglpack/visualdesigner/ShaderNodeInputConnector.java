/*
 * ShaderNodeInputConnector.java
 *
 * Created on June 12, 2007, 6:08 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Graphics;
import java.awt.Point;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;

/**
 *
 * @author Samuel Sperling
 */
public class ShaderNodeInputConnector extends NodeInputConnector {
    
    ShaderProgramInVariable inputVar;
    private Editor editor;
    
    /** Creates a new instance of ShaderNodeConnector */
    public ShaderNodeInputConnector(ShaderProgramInVariable inputVar) {
        super(inputVar.getName());
        this.inputVar = inputVar;
    }
    
    public void paintOnGraph(Graphics g) {
        super.paintOnGraph(g);
        if (getEditor().showVariableNames()) {
            Point thisPoint = getAbsolutePosition();
            int dataType = inputVar.getDataType();
            try {
                dataType = inputVar.resolveDataType();
            } catch (ExportingExeption ex) { }
            String text = DataType.getDataTypeShortName(dataType) + " " + inputVar.getName();

            float textHeight = g.getFontMetrics().getLineMetrics(text, g).getHeight();
            g.drawString(text, thisPoint.x - g.getFontMetrics().stringWidth(text) - this.PREFERED_SIZE.width,
                    thisPoint.y - (int) Math.round((this.getHeight() / 2) - (textHeight / 2)));
        }
    }

    public Editor getEditor() {
        if (editor == null)
            editor = Editor.findMe(this);
        return editor;
    }


    public ShaderProgramInVariable getInputVar() {
        return inputVar;
    }

    // Currently not possible since incomming connector is of type NodeInputConnector
    // TODO: create working isCompatibleHoverConnection function
//    protected boolean isCompatibleHoverConnection(NodeConnector connector) {
//        // On hovering over a connector dataTypes are compared whether their comaptible
//        ShaderProgramInVariable connectorInputVar = ((ShaderNodeInputConnector) connector).getInputVar();
////        ShaderProgramOutVariable outVar = ((ShaderNode) getParentNode()).getOutVariables().get(getName());
//        try {
//            if (DataType.isCompatible(inputVar.resolveDataType(), connectorInputVar.resolveDataType())) return true;
//            if (DataType.isCompatible(connectorInputVar.resolveDataType(), inputVar.resolveDataType())) return true;
//            return false;
//        } catch (ExportingExeption ex) {
//            return false;
//        }
//    }

}
