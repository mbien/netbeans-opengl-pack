/*
 * ShaderNodeOutputConnector.java
 *
 * Created on June 12, 2007, 6:24 PM
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
public class ShaderNodeOutputConnector extends NodeOutputConnector  {
    
    ShaderProgramOutVariable outputVar;
    
    /** Creates a new instance of ShaderNodeConnector */
    public ShaderNodeOutputConnector(ShaderProgramOutVariable outVar) {
        super(outVar.getName());
        this.outputVar = outVar;
    }
    private Editor editor;

    public ShaderProgramOutVariable getOutputVar() {
        return outputVar;
    }

    public void paintOnGraph(Graphics g) {
        super.paintOnGraph(g);
        if (getEditor().showVariableNames()) {
            Point thisPoint = getAbsolutePosition();
            int dataType = outputVar.getDataType();
            try {
                dataType = outputVar.resolveDataType();
            } catch (ExportingExeption ex) { }
            String text = DataType.getDataTypeShortName(dataType) + " " + outputVar.getName();

            float textHeight = g.getFontMetrics().getLineMetrics(text, g).getHeight();
            g.drawString(text, thisPoint.x + this.PREFERED_SIZE.width,
                    thisPoint.y - (int) Math.round((this.getHeight() / 2f) - (textHeight / 2f)));
        }
    }


    public Editor getEditor() {
        if (editor == null)
            editor = Editor.findMe(this);
        return editor;
    }
}
